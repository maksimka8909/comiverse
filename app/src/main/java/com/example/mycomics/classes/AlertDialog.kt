package com.example.mycomics.classes

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.mycomics.activities.MainActivity

class AlertDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = getArguments()?.getString("message")
        var positiveButton = getArguments()?.getString("buttonText")
        val action = getArguments()?.getString("action")
        if (positiveButton== null){
            positiveButton = "OK, иду исправлять"
        }
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Важное сообщение!")
                .setMessage(message)
                .setPositiveButton(positiveButton) {
                    dialog, id ->
                    if(action==null)
                    {
                        dialog.cancel()
                    }
                    else{
                        if (action=="authPage")
                        {
                            val authPage = Intent(activity, MainActivity::class.java)
                            startActivity(authPage)
                        }
                    }

                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}