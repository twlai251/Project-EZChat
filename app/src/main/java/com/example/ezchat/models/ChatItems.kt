package com.example.ezchat.models

import com.example.ezchat.R
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from.view.*
import kotlinx.android.synthetic.main.chat_to.view.*

class ChatFromItem(val text:String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.to_message_text.text = text

        // Load users image (work on later)
//        val targetImageView = viewHolder.itemView.from_message_user_icon
//        Piscasso.get().load(uri).into(to_message_user_icon)

    }
    // receiving messages
    override fun getLayout(): Int {
        return R.layout.chat_from
    }
}

class ChatToItem(val text:String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.from_message_text.text = text

        // Load users image (work on later)
//        val targetImageView = viewHolder.itemView.to_message_user_icon
//        Piscasso.get().load(uri).into(to_message_user_icon)

    }
    // receiving messages
    override fun getLayout(): Int {
        return R.layout.chat_to
    }
}