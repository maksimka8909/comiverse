package com.example.mycomics.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.mycomics.ApiInterface
import com.example.mycomics.classes.AlertDialog
import com.example.mycomics.models.Message
import com.example.mycomics.R
import com.example.mycomics.classes.UserModel
import retrofit2.Call
import retrofit2.Response


class LoginAndPasswordRegistrationFragment : Fragment() {
    private val userModel: UserModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_and_password_registration, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnNext = view.findViewById<Button>(R.id.btnNext)
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        val etLogin = view.findViewById<EditText>(R.id.etLogin)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val etPasswordRepeat = view.findViewById<EditText>(R.id.etPasswordRepeat)
        btnNext.setOnClickListener {
            if (etLogin.text.trim().toString()=="" ||
                    etPassword.text.trim().toString()=="" ||
                        etPasswordRepeat.text.trim().toString()=="")
            {
                val myAlertDialog = AlertDialog()
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString("message","Упс, кажется ты забыл заполнить какое-то из полей, не мог бы ты это исправить?")
                myAlertDialog.setArguments(args)
                myAlertDialog.show(transaction,"dialog")
            }
            else {
                if(etPassword.text.toString()!=etPasswordRepeat.text.toString())
                {
                    val myAlertDialog = AlertDialog()
                    val manager = requireActivity().supportFragmentManager
                    val transaction = manager.beginTransaction()
                    var args = Bundle()
                    args.putString("message","Упс, пароли не совпадают, не мог бы ты перепроверить их?")
                    myAlertDialog.setArguments(args)
                    myAlertDialog.show(transaction,"dialog")
                }
                else
                {
                    val progressBar = requireActivity().findViewById<ProgressBar>(R.id.progressBar)
                    progressBar.setVisibility(ProgressBar.VISIBLE)
                    val apiInterface = ApiInterface.create()
                    apiInterface.checkLogin(etLogin.text.toString()).enqueue(object : retrofit2.Callback<Message>{
                        override fun onFailure(call: Call<Message>, t: Throwable) {
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
                            call: Call<Message>,
                            response: Response<Message>
                        ) {
                            if(response.body()!!.message == "ERROR"){
                                userModel.login.value = etLogin.text.toString()
                                userModel.password.value = etPassword.text.toString()
                                progressBar.setVisibility(ProgressBar.INVISIBLE)
                                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                                fragmentManager.beginTransaction()
                                .replace(R.id.fragments_holder, EmailRegistrationFragment.newInstance())
                                .commit()
                            }
                            else{
                                progressBar.setVisibility(ProgressBar.INVISIBLE)
                                val myAlertDialog = AlertDialog()
                                val manager = requireActivity().supportFragmentManager
                                val transaction = manager.beginTransaction()
                                var args = Bundle()
                                args.putString("message","Из отдела кадров на соообщили, что пользователь с таким именем уже существует")
                                myAlertDialog.setArguments(args)
                                myAlertDialog.show(transaction,"dialog")
                            }
                        }
                    })

                }

            }
        }
        btnBack.setOnClickListener {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragments_holder, PhotoRegistrationFragment.newInstance())
                .commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginAndPasswordRegistrationFragment()
    }
}