package com.example.mycomics.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.example.mycomics.ApiInterface
import com.example.mycomics.R
import com.example.mycomics.classes.AlertDialog
import com.example.mycomics.dataclasses.UserAuthData
import com.example.mycomics.models.Message
import com.example.mycomics.models.User
import retrofit2.Call
import retrofit2.Response
import java.math.BigInteger
import java.security.MessageDigest
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnReg = findViewById<Button>(R.id.btnReg)
        val btnAuth = findViewById<Button>(R.id.btnEnter)
        val etLogin = findViewById<EditText>(R.id.etLogin)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        btnReg.setOnClickListener {
            val regPage = Intent(this, RegistrationActivity::class.java)
            startActivity(regPage)
        }
        btnAuth.setOnClickListener {
            if(etLogin.text.trim().toString() == "" || etPassword.text.trim().toString() == "")
            {
                val myAlertDialog = AlertDialog()
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString(
                    "message",
                    "Не все поля заполнены"
                )
                myAlertDialog.setArguments(args)
                myAlertDialog.show(transaction, "dialog")
            }
            else{
                progressBar.setVisibility(ProgressBar.VISIBLE)
                val userData = UserAuthData(etLogin.text.toString(),etPassword.text.toString().md5())
                val apiInterface = ApiInterface.create()
                apiInterface.authUser(userData).enqueue(object : retrofit2.Callback<User>{
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        progressBar.setVisibility(ProgressBar.INVISIBLE)
                        val myAlertDialog = AlertDialog()
                        val manager = supportFragmentManager
                        val transaction = manager.beginTransaction()
                        var args = Bundle()
                        args.putString("message","Что-то пошло не так повторите попытку или обратитесь в службу поддержки")
                        myAlertDialog.setArguments(args)
                        myAlertDialog.show(transaction,"dialog")
                        Log.i("LOL",t.message.toString())
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        progressBar.setVisibility(ProgressBar.INVISIBLE)
                        if(response.body() !=null) {
                            val date =  response.body()?.lastLog
                            if(date?.substring(0,10)== "0001-01-01") {
                                val startPage =
                                    Intent(this@MainActivity, StartPageActivity::class.java)
                                startPage.putExtra("userId",response.body()!!.id)
                                startActivity(startPage)
                            }
                            else{
                                val lobby =
                                    Intent(this@MainActivity, LobbyActivity::class.java)
                                lobby.putExtra("userId",response.body()!!.id)
                                startActivity(lobby)
                            }
                        }
                        else{
                            val myAlertDialog = AlertDialog()
                            val manager = supportFragmentManager
                            val transaction = manager.beginTransaction()
                            var args = Bundle()
                            args.putString("message","Такого пользователя не существует")
                            myAlertDialog.setArguments(args)
                            myAlertDialog.show(transaction,"dialog")
                        }
                    }
                })
            }
        }
    }
    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }
}