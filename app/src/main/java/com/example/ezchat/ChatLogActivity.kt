package com.example.ezchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ezchat.call_classes.ChatFromItem
import com.example.ezchat.call_classes.ChatMessage
import com.example.ezchat.call_classes.ChatToItem
import com.example.ezchat.call_classes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "Chatlog"
    }

    val adapter = GroupAdapter<ViewHolder>()
    var toUser : User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        view_chat_log.adapter = adapter

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        if (user != null) {
            supportActionBar?.title = user.user_name
        }

        listenForMessages()

        send_message_btn.setOnClickListener {
            Log.d(TAG, "Attempt to send message")
            performSendMessage()
            send_message_bar.text.clear()

        }

    }
//    needs to edit, app not showing user name and text messages
    private fun listenForMessages(){
        val fromID = FirebaseAuth.getInstance().uid
        val toID = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromID/$toID")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    Log.d("checkingMsg", chatMessage.text)

                    if (chatMessage.fromID == FirebaseAuth.getInstance().uid){
                        val current_User = LatestMessagesActivity.currentUser ?: return
                        adapter.add(ChatFromItem(chatMessage.text, current_User))
                    }else{
                        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
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
        // Sending message to firebase
        val text = send_message_bar.text.toString()

        val fromID = FirebaseAuth.getInstance().uid
        val to_user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)!!.uid
        Log.d("testingP", "$to_user")

        if (fromID == null) return

        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromID/$to_user").push()
        val toRef = FirebaseDatabase.getInstance().getReference("/user-messages/$to_user/$fromID").push()

        val chat_Message = ChatMessage(reference.key!!, text, fromID, to_user, System.currentTimeMillis() / 1000)

        reference.setValue(chat_Message)
        view_chat_log.scrollToPosition(adapter.itemCount-1)

        toRef.setValue(chat_Message)
    }

}


