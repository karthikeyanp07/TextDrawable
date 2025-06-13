package com.sample.textdrawable

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.contacttextdrawable.ContactTextDrawable
import com.sample.textdrawable.databinding.ItemUserBinding

class UserAdapter(private val users: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>()
{
    inner class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder
    {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int)
    {
        val user = users[position]
        with(holder.binding) {
            userName.text = user.login
            userId.text = "ID: ${user.id}"

            val fallbackDrawable = ContactTextDrawable.builder()
                .beginConfig()
                .width(100)
                .height(100)
                .textColor(Color.WHITE)
                .withBorder(2)
                .fontSize(30)
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(user.login, Color.BLUE)

            userAvatar.setImageDrawable(fallbackDrawable)
        }
    }

    override fun getItemCount(): Int = users.size
}