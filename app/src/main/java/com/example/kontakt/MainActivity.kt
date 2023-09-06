package com.example.kontakt

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.AdapterKontakt.AdapterKontakt
import com.example.contactapp.models.Kontakt
import com.example.kontakt.databinding.ActivityMainBinding
import com.example.kontakt.databinding.DialogContaktBinding
import com.example.lesson51sql.DataBase.DbHelper
import com.github.florent37.runtimepermission.kotlin.askPermission

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DbHelper
    private lateinit var kontaktList: ArrayList<Kontakt>
    lateinit var adapterKontakt: AdapterKontakt
    var count = 0
    var position = 0
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val requestCallPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                makeCall(kontaktList[position].number)
            }
        }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dbHelper = DbHelper(this)
        kontaktList = ArrayList(dbHelper.getAllKontakt())
        adapterKontakt = AdapterKontakt()
        adapterKontakt.submitList(kontaktList)

        readContact()

        binding.recycleView.apply {
            adapter = adapterKontakt
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.LEFT) {
                    // call
                    checkCallPermisson((kontaktList[viewHolder.adapterPosition].number))
                } else {
                    // new intent SmsActivity
                    val intent = Intent(this@MainActivity, SmsActivity::class.java)
                    intent.putExtra("sms", kontaktList[viewHolder.adapterPosition].number)
                    intent.putExtra("name", kontaktList[viewHolder.adapterPosition].name)
                    startActivity(intent)
                }
            }

        })
        itemTouchHelper.attachToRecyclerView(binding.recycleView)

        binding.apply {
            addContakt.setOnClickListener {
                addContaktDatabase()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        position?.apply {
            adapterKontakt.notifyItemChanged(this)
        }
    }

    private fun addContaktDatabase() {
        val bindingdialog = DialogContaktBinding.inflate(layoutInflater, null, false)
        val dialog = AlertDialog.Builder(this@MainActivity)
            .setView(bindingdialog.root)
            .create()
        bindingdialog.apply {
            saveKontakt.setOnClickListener {
                val name = etName.text.toString()
                val number = etPhone.text.toString()
                if (name.isEmpty() || number.isEmpty()) {
                    return@setOnClickListener
                }
                val kontakt = Kontakt(
                    name = name,
                    number = number
                )
                dbHelper.addKontakt(kontakt)
                kontaktList.add(kontakt)
                position = kontaktList.size - 1
                dialog.dismiss()
            }
            cancell.setOnClickListener {
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun checkCallPermisson(phoneNumber: String) {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CALL_PHONE
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            makeCall(phoneNumber)
        } else {
            requestCallPermission.launch(android.Manifest.permission.CALL_PHONE)
        }
    }


    private fun makeCall(phoneNumber: String) {
        Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber")).apply {
            startActivity(this)
        }
    }

    @SuppressLint("Range")
    @RequiresApi(Build.VERSION_CODES.O)
    fun readContact() {
        askPermission(android.Manifest.permission.READ_CONTACTS) {
            //all permissions already granted or just granted
            val contacts = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null
            )
            while (contacts!!.moveToNext()) {
                val contact = Kontakt(
                    contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                )

                kontaktList.add(contact)
            dbHelper.addKontakt(contact)
            }

            adapterKontakt.submitList(kontaktList)
            contacts!!.close()
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Ruxsat bermasangiz ilova ishlay olmaydi ruxsat bering...")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if (e.hasForeverDenied()) {
                //the list of forever denied permissions, user has check 'never ask again'

                // you need to open setting manually if you really need it
                e.goToSettings();
            }
        }


    }

}