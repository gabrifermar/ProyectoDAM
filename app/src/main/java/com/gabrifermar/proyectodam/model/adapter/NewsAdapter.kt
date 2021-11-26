package com.gabrifermar.proyectodam.model.adapter

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gabrifermar.proyectodam.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.item_news.view.*

class NewsAdapter(
    private val title: MutableList<String>,
    private val description: MutableList<String>,
    private val date: MutableList<String>,
    private val url: MutableList<String>
) : RecyclerView.Adapter<NewsAdapter.NewsHolder>() {

    class NewsHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun show(title: String, description: String, date: String) {
            view.news_item_title.text = title
            view.news_item_description.text = description
            view.news_item_date.text = date

            Firebase.storage.reference.child("news/$title")
                .getBytes(2048 * 2048)
                .addOnSuccessListener { bytes ->
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    view.news_item_image.setImageBitmap(bitmap)
                }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return NewsHolder(
            layoutInflater.inflate(
                R.layout.item_news, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        holder.show(title[position], description[position], date[position])

        holder.view.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url[position]))
            holder.view.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return title.size
    }
}