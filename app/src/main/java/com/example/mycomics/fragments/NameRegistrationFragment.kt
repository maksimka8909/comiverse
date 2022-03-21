package com.example.mycomics.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.mycomics.activities.MainActivity
import com.example.mycomics.classes.AlertDialog
import com.example.mycomics.R
import com.example.mycomics.classes.UserModel


class NameRegistrationFragment : Fragment() {

    private val userModel: UserModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_name_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnNext = view.findViewById<Button>(R.id.btnNext)
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        val etName = view.findViewById<EditText>(R.id.etName)
        btnNext.setOnClickListener {
            if (etName.text.trim().toString() == "")
            {
                val myAlertDialog = AlertDialog()
                val manager = requireActivity().supportFragmentManager
                val transaction = manager.beginTransaction()
                var args = Bundle()
                args.putString("message","Упс, кажется ты забыл указать своё имя, не мог бы ты это исправить?")
                myAlertDialog.setArguments(args)
                myAlertDialog.show(transaction,"dialog")
            }
            else {
                userModel.name.value = etName.text.toString()
                val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.fragments_holder, PhotoRegistrationFragment.newInstance())
                    .commit()
            }
        }
        btnBack.setOnClickListener {
            val authPage = Intent(activity, MainActivity::class.java)
            startActivity(authPage)
        }

    }
    companion object {
        @JvmStatic
        fun newInstance() = NameRegistrationFragment()
    }
}