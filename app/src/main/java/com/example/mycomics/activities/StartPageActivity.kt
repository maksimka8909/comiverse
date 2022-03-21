package com.example.mycomics.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mycomics.R
import com.example.mycomics.fragments.GenresFragment
import com.example.mycomics.fragments.NameRegistrationFragment

class StartPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_page)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragments_holder, GenresFragment.newInstance())
            .commit()
    }
}