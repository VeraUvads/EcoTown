package com.example.android.ecotown.Models

import com.google.firebase.database.ServerValue
import java.sql.Timestamp

class Post constructor( var title: String,
    var description: String, var picture: String, var userId: String,
    var userPhoto: String
) {

    var timeStamp: Any = ServerValue.TIMESTAMP

}