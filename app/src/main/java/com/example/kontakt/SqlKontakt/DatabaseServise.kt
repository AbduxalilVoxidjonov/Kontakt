package com.example.lesson51sql.DataBase

import com.example.contactapp.models.Kontakt

interface DatabaseServise {
    fun addKontakt(student: Kontakt)
    fun getAllKontakt():List<Kontakt>

}