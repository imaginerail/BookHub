package com.aneeq.bookhub.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aneeq.bookhub.R
import com.aneeq.bookhub.activity.DescriptionActivity
import com.aneeq.bookhub.model.Book
import com.squareup.picasso.Picasso
import java.security.AccessControlContext


class DashBoardRecyclerAdapter(val context: Context,val itemList:ArrayList<Book>):RecyclerView.Adapter<DashBoardRecyclerAdapter.DasBoardViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DasBoardViewHolder {
        val view=  LayoutInflater.from(parent.context).inflate(R.layout.recycler_dashboard_single_row, parent, false)
        return DasBoardViewHolder(view)

    }

    override fun getItemCount(): Int {
       return itemList.size
        }

    override fun onBindViewHolder(holder: DasBoardViewHolder, position: Int) {
   val book=itemList[position]
        holder.txtBookName.text=book.bookName
        holder.txtBookAuthor.text=book.bookAuthor
        holder.txtBookPrice.text=book.bookPrice
        holder.txtBookRating.text=book.bookRating
        //holder.imgBookImage.setImageResource(book.bookImage)
        Picasso.get().load(book.bookImage).error(R.drawable.book_app_icon_web).into(holder.imgBookImage)

        holder.llContent.setOnClickListener {
           val intent= Intent(context,DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)



        }

    }

    class DasBoardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtBookName: TextView = view.findViewById(R.id.txtBookName)
        val txtBookAuthor: TextView = view.findViewById(R.id.txtBookAuthor)
        val txtBookPrice: TextView = view.findViewById(R.id.txtBookPrice)
        val txtBookRating: TextView = view.findViewById(R.id.txtBookRating)
        val imgBookImage: ImageView = view.findViewById(R.id.imgBookImage)
        val llContent:LinearLayout=view.findViewById(R.id.llContent)
    }
}


