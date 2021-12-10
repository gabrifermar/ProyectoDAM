package com.gabrifermar.proyectodam.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.gabrifermar.proyectodam.R
import com.gabrifermar.proyectodam.databinding.ActivityNewNewsBinding
import com.gabrifermar.proyectodam.view.fragment.DatePickerFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.sql.Timestamp
import java.util.*

class NewNews : AppCompatActivity() {

    private lateinit var binding: ActivityNewNewsBinding
    private lateinit var getImage: ActivityResultLauncher<String>
    private var imageuri: Uri? = null
    private lateinit var timestamp: Timestamp
    private var cal = Calendar.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //variables
        getImage = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            imageuri = it
            binding.myImageView.setImageURI(it)
            binding.myImageViewText.visibility = View.INVISIBLE
        }

        //listeners
        startListeners()

        val millis = cal.timeInMillis
        timestamp = Timestamp(millis)
    }

    private fun startListeners() {
        binding.myImageView.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.adminNewnewsBtnAdd.setOnClickListener {
            if (imageuri == null || binding.adminNewnewsEtTitle.text.isNullOrEmpty() ||
                binding.adminNewnewsEtDescription.text.isNullOrEmpty() ||
                binding.adminNewnewsTvDate.text.isNullOrEmpty()
            ) {
                Toast.makeText(this, R.string.emptyfield, Toast.LENGTH_SHORT).show()
            } else {
                uploadImage()
                writeDatabase()
            }
        }

        binding.adminNewnewsBtnCalendar.setOnClickListener {
            val datePicker =
                DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }
            datePicker.show(supportFragmentManager, "datePicker")
        }
    }

    fun onDateSelected(day: Int, month: Int, year: Int) {
        binding.adminNewnewsTvDate.text = this.getString(R.string.date, day, month, year)
        cal.set(year, month - 1, day, 0, 0, 0)
    }

    private fun writeDatabase() {
        //variables
        val db = Firebase.firestore
        val millis = cal.timeInMillis
        timestamp = Timestamp(millis)

        val new = hashMapOf(
            "title" to binding.adminNewnewsEtTitle.text.toString(),
            "description" to binding.adminNewnewsEtDescription.text.toString(),
            "link" to binding.adminNewnewsEtLink.text.toString(),
            "date" to timestamp
        )

        db.collection("news").document(binding.adminNewnewsEtTitle.text.toString())
            .get().addOnSuccessListener {
                if (it.exists()) {
                    Toast.makeText(this, R.string.newexist, Toast.LENGTH_SHORT).show()
                } else {
                    db.collection("news").document(binding.adminNewnewsEtTitle.text.toString())
                        .set(new)
                    Toast.makeText(this, R.string.writesuccess, Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun uploadImage() {
        //variable
        val filename = binding.adminNewnewsEtTitle.text.toString()
        val storageReference = FirebaseStorage.getInstance().getReference("news/$filename")

        storageReference.putFile(imageuri!!).addOnSuccessListener {
        }
    }
}