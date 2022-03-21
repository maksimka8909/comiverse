package com.example.mycomics.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.mycomics.fragments.NameRegistrationFragment
import com.example.mycomics.R
import com.example.mycomics.classes.UserModel
import com.example.mycomics.dataclasses.DataUser

class RegistrationActivity : AppCompatActivity() {
    private val userModel: UserModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragments_holder, NameRegistrationFragment.newInstance())
            .commit()
        userModel.name.observe(this) {

        }
        userModel.email.observe(this) {

        }
        userModel.login.observe(this) {

        }
        userModel.password.observe(this) {

        }
        userModel.avatar.observe(this) {

        }
    }
}