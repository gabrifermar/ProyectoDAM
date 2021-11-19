package com.gabrifermar.proyectodam.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.gabrifermar.proyectodam.R
import kotlinx.android.synthetic.main.item_test.view.*

class FlightTestAdapter(
    private val context: Context,
    private val statement: List<String>,
    private val ans1: List<String>,
    private val ans2: List<String>,
    private val ans3: List<String>,
    private val ans4: List<String>
) : RecyclerView.Adapter<FlightTestAdapter.FlightTestHolder>() {

    class FlightTestHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun show(
            context: Context,
            statement: String,
            ans1: String,
            ans2: String,
            ans3: String,
            ans4: String
        ) {
            view.item_test_statement.text = statement
            view.item_test_ans1.text = ans1
            view.item_test_ans2.text = ans2
            view.item_test_ans3.text = ans3
            view.item_test_ans4.text = ans4
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightTestHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FlightTestHolder(layoutInflater.inflate(R.layout.item_test, parent, false))
    }

    override fun onBindViewHolder(holder: FlightTestHolder, position: Int) {
        holder.show(
            context,
            statement[position],
            ans1[position],
            ans2[position],
            ans3[position],
            ans4[position]
        )

        holder.view.item_test_rg_ans.setOnCheckedChangeListener { radioGroup, i ->
            val sharedPreferences = context.getSharedPreferences("C172Test", Context.MODE_PRIVATE)
            sharedPreferences.edit()
                .putString(
                    holder.view.item_test_statement.text.toString(),
                    radioGroup.findViewById<RadioButton>(i).text.toString()
                ).apply()
        }


    }

    override fun getItemCount(): Int {
        return statement.size
    }

}