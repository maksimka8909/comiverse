package com.example.mycomics.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycomics.ApiInterface
import com.example.mycomics.R
import com.example.mycomics.adapters.ComicsRecyclerAdapter
import com.example.mycomics.adapters.IssueRecyclerAdapter
import com.example.mycomics.classes.AlertDialog
import com.example.mycomics.models.Comics
import com.example.mycomics.models.Issue
import com.example.mycomics.models.Score
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response


class NavComicsDetailFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nav_comics_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvName = requireActivity().findViewById<TextView>(R.id.tvName)
        val tvAuthor = requireActivity().findViewById<TextView>(R.id.tvAuthor)
        val tvEditor = requireActivity().findViewById<TextView>(R.id.tvEditor)
        val tvDate = requireActivity().findViewById<TextView>(R.id.tvDate)
        val imgCover = requireActivity().findViewById<ImageView>(R.id.coverImage)
        val tvDescription = requireActivity().findViewById<TextView>(R.id.tvDescription)
        val tvScore = requireActivity().findViewById<TextView>(R.id.tvMark)
        var btnTrack = requireActivity().findViewById<ImageButton>(R.id.btnTrack)
        tvDescription.text = arguments?.getString("descriptionComics")
        tvName.text = "${arguments?.getString("nameComics")}"
        tvAuthor.text = "${tvAuthor.text}${ arguments?.getString("authorComics")}"
        tvEditor.text = "${tvEditor.text}${arguments?.getString("editorComics")}"
        tvDate.text = "${tvDate.text}${arguments?.getString("dateComics")!!.substring(0,10)}"
        Picasso.get().load(arguments?.getString("coverComics")).into(imgCover)
        val apiInterface = ApiInterface.create()
        apiInterface.getScore(requireArguments().getInt("idComics")).enqueue(object : retrofit2.Callback<Score>{
            override fun onFailure(call: Call<Score>, t: Throwable) {
                val myAlertDialog = AlertDialog()
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString("message","Что-то пошло не так повторите попытку или обратитесь в службу поддержки")
                myAlertDialog.arguments = args
                myAlertDialog.show(transaction,"dialog")
                Log.i("LOL",t.message.toString())
            }

            override fun onResponse(call: Call<Score>, response: Response<Score>) {
                if (response.body() == null) {
                    tvScore.text = "Оценка: 0 из 5"
                }
                else{
                    tvScore.text = "Оценка: ${response.body()!!.score} из 5"
                }
            }
        })
        apiInterface.getIssues(arguments?.getInt("idComics")!!).enqueue(object : retrofit2.Callback<List<Issue>>{
            override fun onFailure(call: Call<List<Issue>>, t: Throwable) {
                val myAlertDialog = AlertDialog()
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString("message","Что-то пошло не так повторите попытку или обратитесь в службу поддержки")
                myAlertDialog.arguments = args
                myAlertDialog.show(transaction,"dialog")
                Log.i("LOL",t.message.toString())
            }

            override fun onResponse(call: Call<List<Issue>>, response: Response<List<Issue>>) {
                val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.recyclerViewIssue)
                recyclerView.layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL,true)
                var adapter = IssueRecyclerAdapter(response.body().orEmpty())
                recyclerView.adapter = adapter
                adapter.setOnItemClickListener(
                    object : IssueRecyclerAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val bundle = Bundle()
                            bundle.putInt("idIssue", response.body()!![position].id)
                            val transaction: FragmentTransaction =
                                fragmentManager!!.beginTransaction()
                            val newFragment = NavIssueFragment()
                            newFragment.arguments = bundle
                            transaction.replace(R.id.navFragment, newFragment)
                            transaction.addToBackStack(null)
                            transaction.commit()
                        }
                    })

            }
        })
        btnTrack.setOnClickListener {
            if(btnTrack.drawable == resources.getDrawable(R.drawable.dislike_heart)){
                btnTrack.background = resources.getDrawable(R.drawable.liked_heart)
            }
            else{
                btnTrack.background = resources.getDrawable(R.drawable.dislike_heart)
            }
        }
    }
    companion object {

        @JvmStatic
        fun newInstance() = NavComicsDetailFragment()

    }
}