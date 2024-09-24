package com.example.chatflow.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatflow.R
import com.example.chatflow.UserData
import com.example.chatflow.message_Activity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class userAdapter(var context : Context,var userList: MutableList<UserData> ):RecyclerView.Adapter<userAdapter.ViewHolder>() {
    private var filteredUserList: MutableList<UserData> =userList

    class ViewHolder(var itemview:View):RecyclerView.ViewHolder(itemview) {
        var userName = itemview.findViewById<TextView>(R.id.name)
        var profilePic = itemview.findViewById<CircleImageView>(R.id.shapeableImageView)


    }
    fun setFilterList(userList: MutableList<UserData>){
        this.userList=userList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemview = LayoutInflater.from(parent.context).inflate(R.layout.user_list,parent,false)
        return ViewHolder(itemview)
    }

    override fun getItemCount(): Int {
        return userList.size

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentUser = userList[position]
        holder.userName.text=currentUser.name.toString()
        if (currentUser.imageUrl==null){
            Picasso.get().load(R.drawable.profile).into(holder.profilePic)

        }else{
            Picasso.get().load(currentUser.imageUrl).into(holder.profilePic)

        }

        holder.itemview.setOnClickListener {
            val intent = Intent(context, message_Activity::class.java)
            intent.putExtra("NAME",currentUser.name)
            intent.putExtra("IMAGE",currentUser.imageUrl)
            intent.putExtra("EMAIL_FOR_CALL",currentUser.email)
            intent.putExtra("UID",currentUser.uid)
            context.startActivity(intent)

        }
    }




}
