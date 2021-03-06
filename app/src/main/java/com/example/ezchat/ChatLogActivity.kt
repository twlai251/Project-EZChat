package com.example.ezchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ezchat.models.ChatMessage
import com.example.ezchat.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from.view.*
import kotlinx.android.synthetic.main.chat_to.view.*

class ChatLogActivity : AppCompatActivity() {
    companion object {
        val TAG = "ChatLogTest"
    }

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        if (user != null) {
            supportActionBar?.title = user.user_name
        }
        view_chat_log.adapter = adapter

        listenForMessages()

        send_message_btn.setOnClickListener {
            Log.d(TAG, "Attempt to send message")
            performSendMessage()
            send_message_bar.text.clear()

        }
    }
    private fun listenForMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("/messages")
        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {

                    if (chatMessage.fromID == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatFromItem(chatMessage.text))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text))
                    }
                }

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })

    }
    private fun performSendMessage() {
        // how do we actually send a message to firebase...
        val text = send_message_bar.text.toString()

        val fromID = FirebaseAuth.getInstance().uid
        val to_user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)!!.uid
        if (fromID == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()

        val chatMessage = ChatMessage(reference.key!!, text, fromID, to_user, System.currentTimeMillis() / 1000)
        reference.setValue(chatMessage)
    }
    //END
}





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