package com.gabrifermar.proyectodam.view.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gabrifermar.proyectodam.databinding.FragmentNewsBinding
import com.gabrifermar.proyectodam.model.adapter.NewsAdapter
import com.gabrifermar.proyectodam.viewmodel.GalleryViewModel
import com.google.android.gms.ads.AdRequest
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class NewsFragment : Fragment() {

    private lateinit var galleryViewModel: GalleryViewModel
    private lateinit var adapter: NewsAdapter
    private var _binding: FragmentNewsBinding? = null
    private var titles = mutableListOf<String>()
    private var descriptions = mutableListOf<String>()
    private var dates = mutableListOf<String>()
    private var images = mutableListOf<Bitmap>()
    private var urls = mutableListOf<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        galleryViewModel = ViewModelProvider(this)[GalleryViewModel::class.java]

        //recycler
        initRecycler()

        //start recycler
        startRecycler()

        //adds
        val adRequest = AdRequest.Builder().build()
        binding.add.loadAd(adRequest)

    }

    private fun startRecycler() {
        //variables
        val db = Firebase.firestore
        var index = 0
        var date: Timestamp
        var millisec: Long
        var netDate: java.util.Date
        val format = SimpleDateFormat("dd/MM/yyyy")
        titles.clear()
        descriptions.clear()
        dates.clear()
        images.clear()
        urls.clear()

        db.collection("news").orderBy("date", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { document ->
                document.forEach {
                    val title = it.getString("title")!!
                    titles.add(title)
                    descriptions.add(it.getString("description")!!)
                    urls.add(it.getString("link")!!)

                    //get timestamp
                    date = it["date"] as Timestamp
                    millisec = date.seconds * 1000 + date.nanoseconds / 1000000
                    netDate = Date(millisec)
                    dates.add(format.format(netDate).toString())

                    //update recyclerview
                    adapter.notifyItemChanged(index)
                    index++
                }
            }

    }

    private fun initRecycler() {
        binding.newsRvNews.layoutManager = LinearLayoutManager(activity)
        adapter = NewsAdapter(titles, descriptions, dates, urls)
        binding.newsRvNews.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}