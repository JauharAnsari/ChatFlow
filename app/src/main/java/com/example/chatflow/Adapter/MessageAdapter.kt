package com.example.chatflow.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.chatflow.Message
import com.example.chatflow.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso

class messageAdapter(var context: Context,var messageList:ArrayList<Message>):RecyclerView.Adapter<ViewHolder>() {


    var ITEM_RECEIVED = 1
    var ITEM_SEND = 2
    var IMAGE_RECEIVED = 3
    var IMAGE_SEND = 4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

      return when (viewType){

          ITEM_SEND ->{
              val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_send,parent,false)
              SendViewHolder(itemView)
          }
          ITEM_RECEIVED ->{
              val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_recived,parent,false)
              ReceivedViewHolder(itemView)
          }
          IMAGE_SEND ->{
              val itemView = LayoutInflater.from(parent.context).inflate(R.layout.send_image,parent,false)
              SendImageViewHolder(itemView)
          }
          IMAGE_RECEIVED ->{
              val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recived_image,parent,false)
              ReceivedImageViewHolder(itemView)
          }
          else -> throw IllegalArgumentException("Invalid view type")






      }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]
        when (holder) {
            is SendViewHolder -> holder.sendMessage.text = message.message ?: ""
            is ReceivedViewHolder -> holder.receiviedMessage.text = message.message ?: ""
            is SendImageViewHolder -> {
                holder.sendImage.visibility = View.VISIBLE
                if (!message.imageUri.isNullOrEmpty()) {
                    Picasso.get().load(message.imageUri).into(holder.sendImage)
                } else {
                    // Load a placeholder image or hide the ImageView
                    holder.sendImage.setImageResource(R.drawable.gallary)
                    // or
                    // holder.sendImage.visibility = View.GONE
                }
            }
            is ReceivedImageViewHolder -> {
                holder.receivedImage.visibility = View.VISIBLE
                if (!message.imageUri.isNullOrEmpty()) {
                    Picasso.get().load(message.imageUri).into(holder.receivedImage)
                } else {
                    // Load a placeholder image or hide the ImageView
                    holder.receivedImage.setImageResource(R.drawable.gallary)
                    // or
                    // holder.receivedImage.visibility = View.GONE
                }
            }
        }
    }


    // this Function is Important for Initialize Both Message Layout send and receive layout
    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if (FirebaseAuth.getInstance().currentUser?.uid == currentMessage.senderId) {
            if (currentMessage.imageUri != null) IMAGE_SEND else ITEM_SEND
        } else {
            if (currentMessage.imageUri != null) IMAGE_RECEIVED else ITEM_RECEIVED
        }

    }

        override fun getItemCount(): Int {
            return messageList.size
        }

        class SendImageViewHolder(itemView: View) : ViewHolder(itemView) {
            val sendImage: ShapeableImageView = itemView.findViewById(R.id.sendImage)
        }

        class ReceivedImageViewHolder(itemView: View) : ViewHolder(itemView) {
            val receivedImage: ShapeableImageView = itemView.findViewById(R.id.recivedImage)
        }

        class SendViewHolder(itemView: View) : ViewHolder(itemView) {
            val sendMessage: TextView = itemView.findViewById(R.id.sendMessage)
        }

        class ReceivedViewHolder(itemView: View) : ViewHolder(itemView) {
             val receiviedMessage: TextView = itemView.findViewById(R.id.recivedMessage)

        }
    }
