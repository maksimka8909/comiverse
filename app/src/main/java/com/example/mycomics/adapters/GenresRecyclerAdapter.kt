package com.example.mycomics.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mycomics.R
import com.example.mycomics.fragments.GenresFragment
import com.example.mycomics.models.Genre
import com.google.gson.Gson

class GenresRecyclerAdapter(private val genres: List<Genre>) :RecyclerView.Adapter<GenresRecyclerAdapter.MyViewHolder>() {
    val selectedGenres:MutableList<Int> = mutableListOf()


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val btn = itemView.findViewById<Button>(R.id.btnGenre)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.genre_recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sharedPref: SharedPreferences = holder.itemView.context.getSharedPreferences("sharPref", Context.MODE_PRIVATE)
        holder.btn.text = genres[position].name
        holder.btn.id = genres[position].id
        holder.btn.setOnClickListener {
            if(holder.btn.backgroundTintList ==holder.itemView.context.resources.getColorStateList(R.color.black))
            {
                holder.btn.backgroundTintList = holder.itemView.context.resources.getColorStateList(R.color.main)
                selectedGenres.add(genres[position].id)
                Log.i("LOL","$selectedGenres")
            }
            else
            {
                holder.btn.backgroundTintList = holder.itemView.context.resources.getColorStateList(R.color.black)
                selectedGenres.remove(genres[position].id)
                Log.i("LOL","$selectedGenres")
            }
        }
        val serializedGenres = Gson().toJson(selectedGenres)
        sharedPref.edit().putString("selectedGenres", serializedGenres).apply()
    }

    override fun getItemCount(): Int {
        return genres.size
    }

}