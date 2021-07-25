package com.aneeq.bookhub.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.bookhub.R
import com.aneeq.bookhub.database.BookDao
import com.aneeq.bookhub.database.BookDatabase
import com.aneeq.bookhub.database.BookEntity
import com.aneeq.bookhub.util.ConnectionManager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_description.view.*
import org.json.JSONObject
import java.lang.Exception

class DescriptionActivity : AppCompatActivity() {
    lateinit var txtBookName: TextView
    lateinit var txtBookAuthor: TextView
    lateinit var txtBookPrice: TextView
    lateinit var txtBookRating: TextView
    lateinit var imgBookImage: ImageView
    lateinit var txtRealDescription: TextView
    lateinit var btnAddToFav: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var toolbar: Toolbar


    var bookid: String? = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Book Details"
        progressLayout = findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE
        txtBookName = findViewById(R.id.txtBookName)
        txtBookAuthor = findViewById(R.id.txtBookAuthor)
        txtBookPrice = findViewById(R.id.txtBookPrice)
        txtBookRating = findViewById(R.id.txtBookRating)
        imgBookImage = findViewById(R.id.imgBookImage)
        txtRealDescription = findViewById(R.id.txtRealDescription)
        btnAddToFav = findViewById(R.id.btnAddToFav)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE

        if (intent != null) {
            bookid = intent.getStringExtra("book_id")
        } else {
            finish()

            Toast.makeText(this@DescriptionActivity, "Some Error Occurred", Toast.LENGTH_LONG)
                .show()
        }
        if (bookid == "100") {
            finish()

            Toast.makeText(this@DescriptionActivity, "Some Error Occurred", Toast.LENGTH_LONG)
                .show()
        }
        //////////////////////////////////////////////////////////////////////////////////////
        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v1/book/get_book/"

        val jsonParams = JSONObject()
        jsonParams.put("book_id", bookid)

        if (ConnectionManager().checkConnection(this@DescriptionActivity)) {
            val jsonRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {
                        val success = it.getBoolean("success")
                        if (success) {
                            val bookJsonObject = it.getJSONObject("book_data")
                            progressLayout.visibility = View.GONE
                            val bookImageUrl=bookJsonObject.getString("image")
                            Picasso.get().load(bookJsonObject.getString("image"))
                                .error(R.drawable.book_app_icon_web).into(imgBookImage)

                            txtBookName.text = bookJsonObject.getString("name")
                            txtBookAuthor.text = bookJsonObject.getString("author")
                            txtBookPrice.text = bookJsonObject.getString("price")
                            txtBookRating.text = bookJsonObject.getString("rating")
                            txtRealDescription.text = bookJsonObject.getString("description")

                            //creating object for book entity



                            val bookEntity=BookEntity(
                                bookid?.toInt()as Int,
                                txtBookName.text.toString(),
                                txtBookAuthor.text.toString(),
                                txtBookRating.text.toString(),
                                txtBookPrice.text.toString(),
                                txtRealDescription.text.toString(),
                                bookImageUrl
                                )
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                            val checkFav=DBAsyncTask(applicationContext,bookEntity,1).execute()
                            val isFav=checkFav.get()
                            if(isFav){
                                btnAddToFav.text="Remove From Favourites"
                                val favColor=ContextCompat.getColor(applicationContext,R.color.colorFavourite)
                                btnAddToFav.setBackgroundColor(favColor)
                            }
                            else{
                                btnAddToFav.text="Add to Favourites"
                                val noFavColor=ContextCompat.getColor(applicationContext,R.color.colorPrimary)
                                btnAddToFav.setBackgroundColor(noFavColor)
                            }

                            btnAddToFav.setOnClickListener {
                                if(!DBAsyncTask(applicationContext,bookEntity,1).execute().get()){
                                    val async=DBAsyncTask(applicationContext,bookEntity,2).execute()
                            val result=async.get()
                                    if(result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book Added To Favourites",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                        btnAddToFav.text = "Remove From Favourites"
                                        val favColor = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.colorFavourite
                                        )
                                        btnAddToFav.setBackgroundColor(favColor)
                                    } else{
                                            Toast.makeText(
                                                this@DescriptionActivity,
                                                "Some Error Occurred",
                                                Toast.LENGTH_LONG
                                            )
                                                .show()
                                        }
                                    }
                                else{
                                    val async=DBAsyncTask(applicationContext,bookEntity,3).execute()
                                    val result=async.get()
                                    if(result) {
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Book Removed from Favourites",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                        btnAddToFav.text = "Add to Favourites"
                                        val noFavColor = ContextCompat.getColor(
                                            applicationContext,
                                            R.color.colorPrimary
                                        )
                                        btnAddToFav.setBackgroundColor(noFavColor)
                                    } else{
                                        Toast.makeText(
                                            this@DescriptionActivity,
                                            "Some Error Occurred",
                                            Toast.LENGTH_LONG
                                        )
                                            .show()
                                    }
                                }

                                }

///////////////////////////////////////////////////////

                        } else {
                            Toast.makeText(
                                this@DescriptionActivity,
                                "Some Error Occurred",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Some Error Occurred",
                            Toast.LENGTH_LONG
                        )
                            .show()

                    }
                },


                    Response.ErrorListener {
                        Toast.makeText(
                            this@DescriptionActivity,
                            "Volley Error Occurred",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "ca571f94be4133"
                        return headers


                    }

                }
            queue.add(jsonRequest)
        }
        else{

            val dialog = AlertDialog.Builder(this@DescriptionActivity)
            dialog.setTitle("Failure")
            dialog.setMessage("Internet Connection NOT Found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent= Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this@DescriptionActivity)
            }
            dialog.create()
            dialog.show()
        }
    }
    //this class is created for favourites fragment. Instead of Volley, AsyncTask will do the honors.
    class DBAsyncTask(val context: Context,val bookEntity: BookEntity,val mode: Int):AsyncTask<Void,Void,Boolean>(){
        /* mode1=check the db if the book is fav or not
        mode2=save the book as fav
        mode3=remove the  book from fav
         */
        val db= Room.databaseBuilder(context,BookDatabase::class.java,"book-db").build()

        override fun doInBackground(vararg p0: Void?): Boolean {
            when(mode){
                1->{
                    val book:BookEntity? = db.bookDao().getBookById(bookEntity.bookId.toString())
                    db.close()
                    return book!=null
                }
                2->{
                    db.bookDao().insertBook(bookEntity)
                    db.close()
                    return true
                }
                3->{
                    db.bookDao().deleteBook(bookEntity)
                    db.close()
                    return true
                }
            }

           return false
        }

    }

}
