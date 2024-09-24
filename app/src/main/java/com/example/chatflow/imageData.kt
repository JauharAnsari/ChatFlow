package com.example.chatflow

import android.net.Uri

class imageData {

    var imageUri: String? = null
    var senderId : String? = null

    constructor()
    constructor(imageUri : String?, senderId : String?){
        this.imageUri=imageUri
        this.senderId=senderId

    }

}