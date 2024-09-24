package com.example.chatflow.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.example.chatflow.R
import com.example.chatflow.imageData
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class imageShareAdapte(var context: Context,var imageList:ArrayList<imageData>):RecyclerView.Adapter<ViewHolder>(){

    var SEND_IMAGE = 1
    var RECIVED_IMAGE=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==1){
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.send_image,parent,false)
            return SendImageViewHolder(itemView)
        }else{
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recived_image,parent,false)
            return RecivedImageViewHolder(itemView)
        }


    }



    override fun getItemViewType(position: Int): Int {
        val currentImage = imageList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid?.equals(currentImage.senderId) == true){
            return SEND_IMAGE
        }else
            return RECIVED_IMAGE

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentImage = imageList[position]
        if (holder.javaClass==SendImageViewHolder::class.java){
            val viewHolder = holder as SendImageViewHolder
          Picasso.get().load(currentImage.imageUri).into(holder.SendImage)
        }else{
            val viewHolder = holder as RecivedImageViewHolder
            Picasso.get().load(currentImage.imageUri).into(holder.RecivedImage)
        }
    }
    class SendImageViewHolder(var itemView: View):ViewHolder(itemView) {
        var SendImage = itemView.findViewById<ShapeableImageView>(R.id.sendImage)

    }
    class RecivedImageViewHolder(var itemView: View):ViewHolder(itemView) {
        var RecivedImage = itemView.findViewById<ShapeableImageView>(R.id.recivedImage)

    }
}