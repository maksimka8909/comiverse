package com.example.mycomics.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.mycomics.R
import com.example.mycomics.classes.UserModel


class PhotoRegistrationFragment : Fragment() {
    private val userModel: UserModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userModel.avatar.value = null
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_photo_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnNext = view.findViewById<Button>(R.id.btnNext)
        val btnBack = view.findViewById<Button>(R.id.btnBack)
        val btnImage = view.findViewById<Button>(R.id.btnChooseImage)
        btnNext.setOnClickListener {

            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragments_holder, LoginAndPasswordRegistrationFragment.newInstance())
                .commit()
        }
        btnBack.setOnClickListener {
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.fragments_holder, NameRegistrationFragment.newInstance())
                .commit()
        }
        btnImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode==Activity.RESULT_OK && requestCode==1){
            userModel.avatar.value = data?.data
            val btnBack = view?.findViewById<Button>(R.id.btnBack)
            val image = view?.findViewById<ImageView>(R.id.imageAvatar)
            image?.setBackgroundColor(Color.BLACK)
            image?.setImageURI(data?.data)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = PhotoRegistrationFragment()
    }
}