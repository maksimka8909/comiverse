package com.example.mycomics.classes

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class UserModel : ViewModel() {
    val name: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    val login: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    val email: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    val password: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    val avatar: MutableLiveData<Uri> by lazy{
        MutableLiveData<Uri>()
    }
}