package com.example.mycomics.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycomics.R
import com.example.mycomics.models.Comics
import com.example.mycomics.models.Genre
import com.squareup.picasso.Picasso

class ComicsRecyclerAdapter(private val comics: List<Comics>) : RecyclerView.Adapter<ComicsRecyclerAdapter.MyViewHolder>() {

    private lateinit var mListener: ComicsRecyclerAdapter.onItemClickListener

    interface onItemClickListener{

        fun onItemClick(position: Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    class MyViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val image: ImageView? =  itemView.findViewById<ImageView>(R.id.imgCover)
        val comicsName: TextView? = itemView.findViewById<TextView>(R.id.tvComicsName)
        init {

            itemView.setOnClickListener {


                listener.onItemClick(adapterPosition)

            }


        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ComicsRecyclerAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.comics_recyclerview_item, parent, false)
        return ComicsRecyclerAdapter.MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ComicsRecyclerAdapter.MyViewHolder, position: Int) {
        Picasso.get().load(comics[position].cover).into(holder.image)
        holder.comicsName!!.id = comics[position].id
        holder.comicsName!!.text = comics[position].name
    }

    override fun getItemCount(): Int {
        return comics.size
    }
}