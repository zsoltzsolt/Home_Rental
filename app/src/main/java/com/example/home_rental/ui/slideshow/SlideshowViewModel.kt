package com.example.home_rental.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Acesta este pagina de vizulaizare anunturi"
    }
    val text: LiveData<String> = _text
}