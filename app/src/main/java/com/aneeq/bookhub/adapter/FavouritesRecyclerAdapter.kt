package com.aneeq.bookhub.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.bookhub.R
import com.aneeq.bookhub.activity.DescriptionActivity
import com.aneeq.bookhub.database.BookEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_favourites_single_row.view.*

class FavouritesRecyclerAdapter(val context: Context,val bookList: List<BookEntity>):RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouriteViewHolder>() {

    class FavouriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtBookName: TextView = view.findViewById(R.id.txtFavBookTitle)
        var txtBookAuthor: TextView = view.findViewById(R.id.txtFavBookAuthor)
        var txtBookPrice: TextView = view.findViewById(R.id.txtFavBookPrice)
        var txtBookRating: TextView = view.findViewById(R.id.txtFavBookRating)
        var imgBookImage: ImageView = view.findViewById(R.id.imgFavBookImage)
        val llContent: LinearLayout = view.findViewById(R.id.llFavContent)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_favourites_single_row, parent, false)
        return FavouritesRecyclerAdapter.FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {

        val book = bookList[position]
        holder.txtBookName.text = book.bookName
        holder.txtBookAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtBookRating.text = book.bookRating
        Picasso.get().load(book.bookImage).error(R.drawable.book_app_icon_web)
            .into(holder.imgBookImage)

        /* holder.llContent.setOnClickListener {
            val intent= Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)
        }*/

    }
}