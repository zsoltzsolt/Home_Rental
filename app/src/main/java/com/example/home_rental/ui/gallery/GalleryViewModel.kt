package com.example.home_rental.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Acesta e pagina de adaugare anunt"
    }
    val text: LiveData<String> = _text
}