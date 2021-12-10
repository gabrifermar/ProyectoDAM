package com.gabrifermar.proyectodam.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrifermar.proyectodam.databinding.ActivitySubjectBinding
import com.gabrifermar.proyectodam.model.adapter.SubjectContentAdapter
import com.gabrifermar.proyectodam.viewmodel.SubjectContentViewModel

class Subject : AppCompatActivity() {

    private lateinit var binding: ActivitySubjectBinding
    private lateinit var viewModel: SubjectContentViewModel
    private lateinit var adapter: SubjectContentAdapter
    private lateinit var ref: String
    private var contentList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubjectBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[SubjectContentViewModel::class.java]
        setContentView(binding.root)

        //getExtras
        ref = intent.extras!!.getString("ref")!!

        //recycler
        initRecycler()

        //setup viewmodel
        setupViewModel()

        //activity label
        supportActionBar!!.title = intent.extras!!.getString("title")

        //backarrow
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    private fun initRecycler() {
        binding.subjectRvContent.layoutManager = LinearLayoutManager(this)
        adapter = SubjectContentAdapter(contentList)
        binding.subjectRvContent.adapter = adapter
    }

    private fun setupViewModel() {
        //content
        var index = 0
        viewModel.getContentTitle(ref)
        viewModel.contentTitle.observe(this, { titlelist ->
            titlelist.forEach {
                contentList.add(it)
                adapter.notifyItemInserted(index)
                index++
                Log.e("error", it)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}