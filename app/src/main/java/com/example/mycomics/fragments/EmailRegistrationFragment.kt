package com.example.mycomics.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.FileUtils
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.mycomics.ApiInterface
import com.example.mycomics.R
import com.example.mycomics.classes.AlertDialog
import com.example.mycomics.classes.UserModel
import com.example.mycomics.models.EmailCode
import com.example.mycomics.models.Message
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest


class EmailRegistrationFragment : Fragment() {
    var codeUser =""
    private val userModel: UserModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email_registration, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnNext = view.findViewById<Button>(R.id.btnAccept)
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        val btnSendCode = view.findViewById<Button>(R.id.btnSendCode)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val tvCode = view.findViewById<TextView>(R.id.tvCode)
        val etCode = view.findViewById<EditText>(R.id.etCode)
        btnSendCode.setOnClickListener {
            val progressBar = requireActivity().findViewById<ProgressBar>(R.id.progressBar)
            if(etEmail.text.trim().toString() != "" ) {
                if (btnNext.visibility != View.INVISIBLE) {
                    if (btnSendCode.text.toString() != "Отправить код") {
                        val myAlertDialog = AlertDialog()
                        val manager = requireActivity().supportFragmentManager
                        val transaction = manager.beginTransaction()
                        var args = Bundle()
                        args.putString(
                            "message",
                            "Похоже что наш почтальон еще не вернулся к тебе обратно в телефон, необходимо подождать"
                        )
                        args.putString("buttonText", "Хорошо, я подожду")
                        myAlertDialog.setArguments(args)
                        myAlertDialog.show(transaction, "dialog")
                    } else {
                        progressBar.setVisibility(ProgressBar.VISIBLE)
                        val apiInterface = ApiInterface.create()
                        apiInterface.checkEmail(etEmail.text.toString()).enqueue(object : retrofit2.Callback<EmailCode>{
                            override fun onFailure(call: Call<EmailCode>, t: Throwable) {
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
                                call: Call<EmailCode>,
                                response: Response<EmailCode>
                            ) {
                                progressBar.setVisibility(ProgressBar.INVISIBLE)
                                if(response.body()!!.key != "ERROR") {
                                    btnSendCode.setBackground(btnSendCode.context.getDrawable(R.drawable.round_shape_button_blocked))
                                    var seconds = 30
                                    val timer = object : CountDownTimer(30000, 1000) {
                                        override fun onTick(millisUntilFinished: Long) {
                                            btnSendCode.setText("Отправить повторно через $seconds")
                                            seconds = seconds - 1

                                        }

                                        override fun onFinish() {
                                            btnSendCode.setBackground(btnSendCode.context.getDrawable(R.drawable.round_shape_button))
                                            btnSendCode.setText("Отправить код")
                                            btnSendCode.setEnabled(true);
                                        }
                                    }.start()
                                    codeUser = response.body()!!.key
                                    progressBar.setVisibility(ProgressBar.INVISIBLE)
                                    val myAlertDialog = AlertDialog()
                                    val manager = requireActivity().supportFragmentManager
                                    val transaction = manager.beginTransaction()
                                    var args = Bundle()
                                    args.putString(
                                        "message",
                                        "Наш почтальон отправился доставлять тебе секретный код"
                                    )
                                    args.putString("buttonText", "ОК, буду ждать")
                                    myAlertDialog.setArguments(args)
                                    myAlertDialog.show(transaction, "dialog")
                                }
                                else
                                {
                                    val myAlertDialog = AlertDialog()
                                    val manager = requireActivity().supportFragmentManager
                                    val transaction = manager.beginTransaction()
                                    var args = Bundle()
                                    args.putString(
                                        "message",
                                        "Пользователь с такой почтой уже существует"
                                    )
                                    args.putString("buttonText", "ОК, впишу другую")
                                    myAlertDialog.setArguments(args)
                                    myAlertDialog.show(transaction, "dialog")
                                }
                            }
                        })
                        btnSendCode.setBackground(btnSendCode.context.getDrawable(R.drawable.round_shape_button_blocked))
                        var seconds = 30
                        val timer = object : CountDownTimer(30000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                btnSendCode.setText("Отправить повторно через $seconds")
                                seconds = seconds - 1

                            }

                            override fun onFinish() {
                                btnSendCode.setBackground(btnSendCode.context.getDrawable(R.drawable.round_shape_button))
                                btnSendCode.setText("Отправить код")
                                btnSendCode.setEnabled(true);
                            }
                        }.start()
                    }
                } else {
                    progressBar.setVisibility(ProgressBar.VISIBLE)


                    val apiInterface = ApiInterface.create()
                    apiInterface.checkEmail(etEmail.text.toString()).enqueue(object : retrofit2.Callback<EmailCode>{
                        override fun onFailure(call: Call<EmailCode>, t: Throwable) {
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
                            call: Call<EmailCode>,
                            response: Response<EmailCode>
                        )
                        {
                            progressBar.setVisibility(ProgressBar.INVISIBLE)
                            if(response.body()!!.key != "ERROR") {
                                btnSendCode.setBackground(btnSendCode.context.getDrawable(R.drawable.round_shape_button_blocked))
                                var seconds = 30
                                val timer = object : CountDownTimer(30000, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                        btnSendCode.setText("Отправить повторно через $seconds")
                                        seconds = seconds - 1

                                    }

                                    override fun onFinish() {
                                        btnSendCode.setBackground(btnSendCode.context.getDrawable(R.drawable.round_shape_button))
                                        btnSendCode.setText("Отправить код")
                                        btnSendCode.setEnabled(true);
                                    }
                                }.start()
                                btnNext.setVisibility(View.VISIBLE)
                                btnNext.setEnabled(true)
                                tvCode.setVisibility(View.VISIBLE)
                                etCode.setVisibility(View.VISIBLE)
                                etCode.setEnabled(true)
                                codeUser = response.body()!!.key
                                val myAlertDialog = AlertDialog()
                                val manager = requireActivity().supportFragmentManager
                                val transaction = manager.beginTransaction()
                                var args = Bundle()
                                args.putString(
                                    "message",
                                    "Наш почтальон отправился доставлять тебе секретный код"
                                )
                                args.putString("buttonText", "ОК, буду ждать")
                                myAlertDialog.setArguments(args)
                                myAlertDialog.show(transaction, "dialog")
                            }
                            else
                            {
                                val myAlertDialog = AlertDialog()
                                val manager = requireActivity().supportFragmentManager
                                val transaction = manager.beginTransaction()
                                var args = Bundle()
                                args.putString(
                                    "message",
                                    "Пользователь с такой почтой уже существует"
                                )
                                args.putString("buttonText", "ОК, впишу другую")
                                myAlertDialog.setArguments(args)
                                myAlertDialog.show(transaction, "dialog")
                            }
                        }
                    })

                }
            }
            else{
                val myAlertDialog = AlertDialog()
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString(
                    "message",
                    "Наш почтальон не видит почтового адреса, пожалуйста, заполни поле почты для отправки"
                )
                myAlertDialog.setArguments(args)
                myAlertDialog.show(transaction, "dialog")
            }
        }
        btnNext.setOnClickListener {
            val etCode = view.findViewById<EditText>(R.id.etCode)
            val progressBar = requireActivity().findViewById<ProgressBar>(R.id.progressBar)
            progressBar.setVisibility(ProgressBar.VISIBLE)
            if (etCode.text.trim().toString()!="") {
                Log.i("DATA","RESPONSE "+ codeUser)
                Log.i("DATA","USER " + (etCode.text.trim().toString().md5()).uppercase())
                if(codeUser == etCode.text.trim().toString().md5().uppercase()) {
                    var login : String = ""
                    var name : String = ""
                    var password : String = ""
                    var email : String = ""
                    userModel.email.value = etEmail.text.toString()
                    userModel.name.observe(activity as LifecycleOwner) {
                        name = it
                    }
                    userModel.login.observe(activity as LifecycleOwner) {
                        login = it
                    }
                    userModel.password.observe(activity as LifecycleOwner) {
                        password = it
                    }
                    userModel.email.observe(activity as LifecycleOwner) {
                        email = it
                    }
                    userModel.avatar.observe(activity as LifecycleOwner) { uri->
                        if(uri == null){
                            val apiInterface = ApiInterface.create()
                            apiInterface.regUser(null, name, login, password, email)
                                .enqueue(object : retrofit2.Callback<Message> {
                                    override fun onFailure(call: Call<Message>, t: Throwable) {
                                        progressBar.setVisibility(ProgressBar.INVISIBLE)
                                        val myAlertDialog = AlertDialog()
                                        val manager = requireActivity().supportFragmentManager
                                        val transaction = manager.beginTransaction()
                                        var args = Bundle()
                                        args.putString(
                                            "message",
                                            "Что-то пошло не так повторите попытку или обратитесь в службу поддержки"
                                        )
                                        myAlertDialog.setArguments(args)
                                        myAlertDialog.show(transaction, "dialog")
                                        Log.i("LOL", t.message.toString())
                                    }

                                    override fun onResponse(
                                        call: Call<Message>,
                                        response: Response<Message>
                                    ) {
                                        progressBar.setVisibility(ProgressBar.INVISIBLE)
                                        val myAlertDialog = AlertDialog()
                                        val manager = requireActivity().supportFragmentManager
                                        val transaction = manager.beginTransaction()
                                        var args = Bundle()
                                        args.putString(
                                            "message",
                                            response.message()
                                        )
                                        args.putString("buttonText", "Уже иду")
                                        args.putString("action", "authPage")
                                        myAlertDialog.setArguments(args)
                                        myAlertDialog.show(transaction, "dialog")
                                    }
                                })
                        }
                        else {
                            lifecycleScope.launch {
                                val stream =
                                    requireActivity().contentResolver?.openInputStream(uri) ?: return@launch
                                val file: File = File(uri.path)
                                val requestFile =
                                    RequestBody.create(MediaType.parse("image/*"), stream.readBytes())
                                val body = MultipartBody.Part.createFormData(
                                    "profile_picture",
                                    file.name,
                                    requestFile
                                )
                                val apiInterface = ApiInterface.create()
                                apiInterface.regUser(body, name, login, password, email)
                                    .enqueue(object : retrofit2.Callback<Message> {
                                        override fun onFailure(call: Call<Message>, t: Throwable) {
                                            progressBar.setVisibility(ProgressBar.INVISIBLE)
                                            val myAlertDialog = AlertDialog()
                                            val manager = requireActivity().supportFragmentManager
                                            val transaction = manager.beginTransaction()
                                            var args = Bundle()
                                            args.putString(
                                                "message",
                                                "Что-то пошло не так повторите попытку или обратитесь в службу поддержки"
                                            )
                                            myAlertDialog.setArguments(args)
                                            myAlertDialog.show(transaction, "dialog")
                                            Log.i("LOL", t.message.toString())
                                        }

                                        override fun onResponse(
                                            call: Call<Message>,
                                            response: Response<Message>
                                        ) {
                                            progressBar.setVisibility(ProgressBar.INVISIBLE)
                                            val myAlertDialog = AlertDialog()
                                            val manager = requireActivity().supportFragmentManager
                                            val transaction = manager.beginTransaction()
                                            var args = Bundle()
                                            args.putString(
                                                "message",
                                                "Поздравляем с успешно пройденной регистрацией, теперь смело можешь идти авторизовываться"
                                            )
                                            args.putString("buttonText", "Уже иду")
                                            args.putString("action", "authPage")
                                            myAlertDialog.setArguments(args)
                                            myAlertDialog.show(transaction, "dialog")
                                        }
                                    })
                            }
                        }




                        }
                    }
                else{
                    val myAlertDialog = AlertDialog()
                    val manager = requireActivity().supportFragmentManager
                    val transaction = manager.beginTransaction()
                    var args = Bundle()
                    args.putString(
                        "message",
                        "Секретный код неверный попробуй ещё раз"
                    )
                    myAlertDialog.setArguments(args)
                    myAlertDialog.show(transaction, "dialog")
                }

            }
            else{
                val myAlertDialog = AlertDialog()
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString(
                    "message",
                    "Ты забыл ввести секртный код"
                )
                myAlertDialog.setArguments(args)
                myAlertDialog.show(transaction, "dialog")
            }
        }
        btnBack.setOnClickListener {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragments_holder, LoginAndPasswordRegistrationFragment.newInstance())
                .commit()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = EmailRegistrationFragment()
    }
    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }
}