package com.example.mycomics.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.mycomics.ApiInterface
import com.example.mycomics.R
import com.example.mycomics.activities.LobbyActivity
import com.example.mycomics.activities.StartPageActivity
import com.example.mycomics.classes.AlertDialog
import com.example.mycomics.models.User
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response


class NavUserFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nav_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var avatar = requireActivity().findViewById<ImageView>(R.id.imgAvatar)
        var login = requireActivity().findViewById<TextView>(R.id.tvLogin)
        var name = requireActivity().findViewById<TextView>(R.id.tvName)
        var email = requireActivity().findViewById<TextView>(R.id.tvEmail)
        var lastLog = requireActivity().findViewById<TextView>(R.id.tvLastLog)
        val apiInterface = ApiInterface.create()
        val progressBar =  requireActivity().findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = ProgressBar.VISIBLE
        apiInterface.getUser(requireActivity().intent.getIntExtra("userId",0)).enqueue(object : retrofit2.Callback<User>{
            override fun onFailure(call: Call<User>, t: Throwable) {
                progressBar.visibility = ProgressBar.INVISIBLE
                val myAlertDialog = AlertDialog()
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString("message","Что-то пошло не так повторите попытку или обратитесь в службу поддержки")
                myAlertDialog.setArguments(args)
                myAlertDialog.show(transaction,"dialog")
                Log.i("LOL",t.message.toString())
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                progressBar.visibility = ProgressBar.INVISIBLE
                if(response.body() !=null) {
                    Picasso.get().load("https://makachuka.xyz" + response.body()!!.photo).into(avatar)
                    login.text = "${response.body()!!.login}"
                    name.text = "${name.text}${response.body()!!.name}"
                    email.text ="${email.text}${response.body()!!.email}"
                    lastLog.text = "${lastLog.text}${response.body()!!.lastLog.substring(0,10)} ${response.body()!!.lastLog.substring(11,19)}"
                }
                else{
                    val myAlertDialog = AlertDialog()
                    val manager = requireActivity().supportFragmentManager
                    val transaction = manager.beginTransaction()
                    var args = Bundle()
                    args.putString("message","Ошибка получения данных, перезагрузите страницу")
                    myAlertDialog.setArguments(args)
                    myAlertDialog.show(transaction,"dialog")
                }
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = NavUserFragment()
    }
}