package com.example.kontakt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.kontakt.databinding.ActivitySmsBinding
import com.github.florent37.runtimepermission.kotlin.askPermission

class SmsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySmsBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.txtNameSms.text = intent.getStringExtra("name")
        binding.txtNumberSms.text = intent.getStringExtra("sms")

//        binding.apply {
//            btnSend.setOnClickListener {
//                askPermission(android.Manifest.permission.SEND_SMS) {
//                    //all permissions already granted or just granted
//                    val numbers = intent.getStringExtra("sms")
//                    val matn = edtMatn.text.toString()
//                    var obj = SmsManager.getDefault()
//
//                    obj.sendTextMessage(
//                        numbers,
//                        null, matn,
//                        null, null
//                    )
//
//                }.onDeclined { e ->
//                    if (e.hasDenied()) {
//                        AlertDialog.Builder(this@SmsActivity)
//                            .setMessage("Ruxsat bermasangiz ilova ishlay olmaydi ruxsat bering...")
//                            .setPositiveButton("yes") { dialog, which ->
//                                e.askAgain()
//                            } //ask again
//                            .setNegativeButton("no") { dialog, which ->
//                                dialog.dismiss()
//                            }
//                            .show()
//                    }
//
//                    if (e.hasForeverDenied()) {
//                        //the list of forever denied permissions, user has check 'never ask again'
//
//                        // you need to open setting manually if you really need it
//                        e.goToSettings()
//                    }
//                }
//            }
//        }

    }
}