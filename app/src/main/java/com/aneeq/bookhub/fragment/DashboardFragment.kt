package com.aneeq.bookhub.fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.bookhub.R
import com.aneeq.bookhub.adapter.DashBoardRecyclerAdapter
import com.aneeq.bookhub.model.Book
import com.aneeq.bookhub.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

class DashboardFragment : Fragment() {
lateinit var recycleDashboard:RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    val bookInfoList = arrayListOf<Book>()
    //creating comparator
    var ratingComparator=Comparator<Book> { book1, book2 ->
        if(book1.bookRating.compareTo(book2.bookRating, ignoreCase = true)==0){
            book1.bookName.compareTo(book2.bookName, ignoreCase = true)
    } //if ratings are same,then sort with name
        else{
        book1.bookRating.compareTo(book2.bookRating, ignoreCase = true)
    }}
    lateinit var recyclerAdapter:DashBoardRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
       // show the menu sort
       setHasOptionsMenu(true)


        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility=View.VISIBLE
        recycleDashboard = view.findViewById(R.id.recycleDashboard)
        layoutManager = LinearLayoutManager(activity)

//invoking volley and our url
        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().checkConnection(activity as Context)) {

//creating a json object
        val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

            try {
                progressLayout.visibility=View.GONE

                val success = it.getBoolean("success")
                if (success) {

                    //parsing the JSOn objects key/value pair

                    val data = it.getJSONArray("data")
                    for (i in 0 until data.length()) {
                        val bookJsonObject = data.getJSONObject(i)
                        val bookObject = Book(
                            bookJsonObject.getString("book_id"),
                            bookJsonObject.getString("name"),
                            bookJsonObject.getString("author"),
                            bookJsonObject.getString("rating"),
                            bookJsonObject.getString("price"),
                            bookJsonObject.getString("image") //server sends the link for image,coz image will load slowly
                        )
                        bookInfoList.add(bookObject)
                        //attach dashboard fragment to adapter(bridge between data and view)
                        recyclerAdapter =
                            DashBoardRecyclerAdapter(activity as Context, bookInfoList)
                        recycleDashboard.adapter = recyclerAdapter
                        recycleDashboard.layoutManager = layoutManager

                    }
                } else {
                    Toast.makeText(activity as Context, "Some Error Occurred", Toast.LENGTH_LONG)
                        .show()
                }
            }
            catch (e:JSONException){
                Toast.makeText(activity as Context, "Some UnExpected Error Occurred", Toast.LENGTH_LONG)
                    .show()
            }


            },
                Response.ErrorListener {
                    //println("Error is $it")
                    if(activity!=null){
                    Toast.makeText(activity as Context, "Volley Error Occurred", Toast.LENGTH_LONG)
                        .show()
                }}) {
//headers are required for type and unique token
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "ca571f94be4133"
                    return headers
                }
            }

        queue.add(jsonObjectRequest)
    }
        else {
            //creating a dialogue box if there is no internet connection

            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Failure")
            dialog.setMessage("Internet Connection NOT Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
           val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }
        return view
    }
       //sorting menu creation
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)
    }
       // sorting menu functionality
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item?.itemId
      if(id==R.id.action_sort){
    Collections.sort(bookInfoList,ratingComparator)
          bookInfoList.reverse()
      }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
       }
}
