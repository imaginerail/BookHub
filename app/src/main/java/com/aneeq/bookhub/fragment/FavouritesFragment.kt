package com.aneeq.bookhub.fragment

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.aneeq.bookhub.R
import com.aneeq.bookhub.adapter.DashBoardRecyclerAdapter
import com.aneeq.bookhub.adapter.FavouritesRecyclerAdapter
import com.aneeq.bookhub.database.BookDatabase
import com.aneeq.bookhub.database.BookEntity
import com.aneeq.bookhub.model.Book

/**
 * A simple [Fragment] subclass.
 */
class FavouritesFragment : Fragment() {
    lateinit var recyclerFavourite: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var recyclerAdapter: FavouritesRecyclerAdapter
    var dbBookList = listOf<BookEntity>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         val view= inflater.inflate(R.layout.fragment_favourites, container, false)

//initialization
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility=View.VISIBLE
        recyclerFavourite = view.findViewById(R.id.recyclerFavourite)
        layoutManager = GridLayoutManager(activity as Context,2)
        dbBookList=RetrieveFavourites(activity as Context).execute().get()

        //attach adapter to recycler
        if(activity!=null){
            progressLayout.visibility=View.GONE
            recyclerAdapter = FavouritesRecyclerAdapter(activity as Context, dbBookList)
            recyclerFavourite.adapter = recyclerAdapter
            recyclerFavourite.layoutManager = layoutManager
        }


        return view
    }
//retrieving the favourites booklist from database using asynctaskclass

    class RetrieveFavourites(val context: Context):AsyncTask<Void,Void,List<BookEntity>>(){
        override fun doInBackground(vararg p0: Void?): List<BookEntity> {
            val db= Room.databaseBuilder(context, BookDatabase::class.java,"book-db").build()
            return db.bookDao().getAllBooks()
        }

    }
}
