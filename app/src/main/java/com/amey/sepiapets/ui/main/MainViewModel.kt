package com.amey.sepiapets.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    val contentUrl = MutableLiveData<String>()

    // function to send message
    fun sendContentUrl(text: String) {
        contentUrl.value = text
    }
}