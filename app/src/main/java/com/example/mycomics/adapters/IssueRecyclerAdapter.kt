package com.example.mycomics.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycomics.R
import com.example.mycomics.models.Comics
import com.example.mycomics.models.Issue
import com.squareup.picasso.Picasso

class IssueRecyclerAdapter(private val issues: List<Issue>) : RecyclerView.Adapter<IssueRecyclerAdapter.MyViewHolder>() {

    private lateinit var mListener: IssueRecyclerAdapter.onItemClickListener

    interface onItemClickListener{

        fun onNumberClick(position: Int)
        fun onDateClick(position: Int)
        fun onNameClick(position: Int)
        fun onDownloadClick(position: Int)

    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class MyViewHolder(itemView: View,listener: IssueRecyclerAdapter.onItemClickListener) : RecyclerView.ViewHolder(itemView){

        val number = itemView.findViewById<TextView>(R.id.tvNumber)
        val name = itemView.findViewById<TextView>(R.id.tvIssueName)
        val date = itemView.findViewById<TextView>(R.id.tvDate)
        val download = itemView.findViewById<Button>(R.id.btnDownload)
        init {

            number.setOnClickListener {


                listener.onNumberClick(adapterPosition)


            }
            date.setOnClickListener {


                listener.onDateClick(adapterPosition)


            }
            name.setOnClickListener {


                listener.onNameClick(adapterPosition)


            }
            download.setOnClickListener {


                listener.onDownloadClick(adapterPosition)


            }


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IssueRecyclerAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.issue_recyclerview_item, parent, false)
        return IssueRecyclerAdapter.MyViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: IssueRecyclerAdapter.MyViewHolder, position: Int) {
        holder.number.text = (position+1).toString()
        holder.name.text = issues[position].name
        holder.number.id =issues[position].id
        holder.date.text = issues[position].date
        holder.name.hint = issues[position].pathDownload
    }

    override fun getItemCount(): Int {
        return issues.size
    }
}