package com.example.mycomics.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycomics.ApiInterface
import com.example.mycomics.R
import com.example.mycomics.activities.LobbyActivity
import com.example.mycomics.activities.MainActivity
import com.example.mycomics.activities.StartPageActivity
import com.example.mycomics.adapters.GenresRecyclerAdapter
import com.example.mycomics.classes.AlertDialog
import com.example.mycomics.models.Genre
import com.example.mycomics.models.User
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response


class GenresFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_genres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val progressBar = requireActivity().findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = ProgressBar.VISIBLE
        val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        val retrofit = ApiInterface.create()
        val genres = retrofit.getGenres().enqueue(object : retrofit2.Callback<List<Genre>>{
            override fun onFailure(call: Call<List<Genre>>, t: Throwable) {
                progressBar.visibility = ProgressBar.INVISIBLE
                val myAlertDialog = AlertDialog()
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString("message","Что-то пошло не так повторите попытку или обратитесь в службу поддержки")
                myAlertDialog.arguments = args
                myAlertDialog.show(transaction,"dialog")
                Log.i("LOL",t.message.toString())
            }

            override fun onResponse(call: Call<List<Genre>>, response: Response<List<Genre>>) {
                progressBar.visibility = ProgressBar.INVISIBLE
                recyclerView.adapter = GenresRecyclerAdapter(response.body().orEmpty())
            }
        })
        val btnBack = requireActivity().findViewById<Button>(R.id.btnBack)
        val btnNext = requireActivity().findViewById<Button>(R.id.btnNext)
        btnNext.setOnClickListener {
            progressBar.setVisibility(ProgressBar.VISIBLE)
            val apiInterface = ApiInterface.create()
            val idUser =requireActivity().intent.getIntExtra("userId",0)
            apiInterface.logUpdate(idUser).enqueue(object : retrofit2.Callback<com.example.mycomics.models.Message>{
                override fun onFailure(call: Call<com.example.mycomics.models.Message>, t: Throwable) {
                    progressBar.setVisibility(ProgressBar.INVISIBLE)
                    val myAlertDialog = AlertDialog()
                    val manager = requireActivity().supportFragmentManager
                    val transaction = manager.beginTransaction()
                    var args = Bundle()
                    args.putString("message","Что-то пошло не так повторите попытку или обратитесь в службу поддержки")
                    myAlertDialog.setArguments(args)
                    myAlertDialog.show(transaction,"dialog")
                    Log.i("LOL",t.message.toString())
                }

                override fun onResponse(
                    call: Call<com.example.mycomics.models.Message>,
                    response: Response<com.example.mycomics.models.Message>
                ) {
                    progressBar.setVisibility(ProgressBar.INVISIBLE)

                    val lobby = Intent(activity, LobbyActivity::class.java)
                    lobby.putExtra("userId", requireActivity().intent.getIntExtra("userId",0))
                    startActivity(lobby)
                }
            })

        }
        btnBack.setOnClickListener {
            val authPage = Intent(activity, MainActivity::class.java)
            startActivity(authPage)
        }

    }
    companion object {
        @JvmStatic
        fun newInstance() = GenresFragment()
    }
}