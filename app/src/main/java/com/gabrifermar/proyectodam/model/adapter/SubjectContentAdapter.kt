package com.gabrifermar.proyectodam.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.gabrifermar.proyectodam.R
import kotlinx.android.synthetic.main.item_subject_content.view.*

class SubjectContentAdapter(
    private val content: List<String>
) : RecyclerView.Adapter<SubjectContentAdapter.SubjectContentHolder>() {

    class SubjectContentHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun show(name: String) {
            view.item_subjectcontent_title.text = name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectContentHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return SubjectContentHolder(
            layoutInflater.inflate(R.layout.item_subject_content, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SubjectContentHolder, position: Int) {
        holder.show(content[position])
    }

    override fun getItemCount(): Int {
        return content.size
    }
}