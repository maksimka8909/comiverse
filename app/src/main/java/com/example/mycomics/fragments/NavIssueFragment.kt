package com.example.mycomics.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycomics.ApiInterface
import com.example.mycomics.R
import com.example.mycomics.adapters.IssueRecyclerAdapter
import com.example.mycomics.classes.AlertDialog
import com.example.mycomics.models.Issue
import com.github.chrisbanes.photoview.PhotoView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response


class NavIssueFragment : Fragment() {
    var counter = 0
    var images: MutableList<String> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nav_issue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val btnNext = requireActivity().findViewById<Button>(R.id.btnNextPage)
        val btnBack = requireActivity().findViewById<Button>(R.id.btnPreviousPage)
        val pvPage = requireActivity().findViewById<PhotoView>(R.id.page)
        val countPage = requireActivity().findViewById<TextView>(R.id.tvCountPage)
        val apiInterface = ApiInterface.create()
        apiInterface.getIssueImages(arguments?.getInt("idIssue")!!)
            .enqueue(object : retrofit2.Callback<List<String>>{
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                val myAlertDialog = AlertDialog()
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString("message","Что-то пошло не так повторите попытку или обратитесь в службу поддержки")
                myAlertDialog.arguments = args
                myAlertDialog.show(transaction,"dialog")
                Log.i("LOL",t.message.toString())
            }

            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                countPage.text = "1/${response.body()!!.size}"
                Picasso.get().load(response.body()!![counter]).into(pvPage)
                for (i in response.body()!!){
                    images.add(i)
                }
                Log.i("LOL","${images}")
            }
        })
        pvPage.setOnClickListener {
            if(images==null)
            {
                val myAlertDialog = AlertDialog()
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString("message","Что-то пошло не так повторите попытку или обратитесь в службу поддержки")
                myAlertDialog.arguments = args
                myAlertDialog.show(transaction,"dialog")
            }
            else {
                if (counter != images.size) {
                    counter = counter + 1
                    Picasso.get().load(images[counter]).into(pvPage)
                    countPage.text = "${counter + 1}/${images.size}"
                }
            }
        }
        btnNext.setOnClickListener {
            if(images==null)
            {
                val myAlertDialog = AlertDialog()
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString("message","Что-то пошло не так повторите попытку или обратитесь в службу поддержки")
                myAlertDialog.arguments = args
                myAlertDialog.show(transaction,"dialog")
            }
            else {
                if (counter != images.size) {
                    counter = counter + 1
                    Picasso.get().load(images[counter]).into(pvPage)
                    countPage.text = "${counter + 1}/${images.size}"
                }
            }

        }

        btnBack.setOnClickListener {
            if(images==null)
            {
                val myAlertDialog = AlertDialog()
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString("message","Что-то пошло не так повторите попытку или обратитесь в службу поддержки")
                myAlertDialog.arguments = args
                myAlertDialog.show(transaction,"dialog")
            }
            else
            {
                if (counter != 0) {
                    counter = counter - 1
                    Picasso.get().load(images[counter]).into(pvPage)
                    countPage.text = "${counter + 1}/${images.size}"
                }
            }

        }

    }
    companion object {
        @JvmStatic
        fun newInstance() = NavIssueFragment()
    }
}