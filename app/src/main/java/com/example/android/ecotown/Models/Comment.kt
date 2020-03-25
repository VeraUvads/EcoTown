package com.example.android.ecotown.Models

import com.google.firebase.database.ServerValue

class Comment(var content: String="", var uID: String="", var uName: String="") {
    var timeStamp: Any = ServerValue.TIMESTAMP

}