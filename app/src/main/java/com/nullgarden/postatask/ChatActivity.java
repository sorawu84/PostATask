package com.nullgarden.postatask;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class ChatActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    LinearLayoutManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final String userKey = getIntent().getExtras().getString("userKey");

        recyclerView = findViewById(R.id.chatRecyclerVIew);
        recyclerView.setHasFixedSize(true);
        mManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mManager);
        mManager.setReverseLayout(false);
        mManager.setStackFromEnd(true);

     //   recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() +1);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference mChat = mDatabase.child("TaskChat").child("ChatUsers");

        mChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild((mAuth.getCurrentUser().getUid()))){
                    if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(userKey)){
                        chat();
                    }else{
                        create();
                    }
                }else{
                    create();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    public void create(){
        final String userKey = getIntent().getExtras().getString("userKey");
        DatabaseReference mCreate = mDatabase.child("TaskChat");
        String pushKey = mCreate.child("ChatUsers").child(mAuth.getCurrentUser().getUid()).child(userKey).push().getKey();
        mCreate.child("ChatUsers").child(mAuth.getCurrentUser().getUid()).child(userKey).child(pushKey).setValue(pushKey);
        mCreate.child("ChatUsers").child(userKey).child(mAuth.getCurrentUser().getUid()).child(pushKey).setValue(pushKey);
        mCreate.child("ChatUID").child(pushKey);
        chat();
    }

    String chatUID;

    public void chat(){
        final String userKey = getIntent().getExtras().getString("userKey");
        final DatabaseReference mChat = mDatabase.child("TaskChat").child("ChatUsers").child(mAuth.getCurrentUser().getUid()).child(userKey);

        mChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chatUID = dataSnapshot.getValue().toString();
                final ImageView sendBtn = findViewById(R.id.sendBtn);
                final TextView inputChatField = findViewById(R.id.inputChatField);

                rView(chatUID);

                sendBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String input = inputChatField.getText().toString();
                        if(TextUtils.isEmpty(input)){
                            Toast.makeText(ChatActivity.this, "Please say a word.", Toast.LENGTH_SHORT).show();
                        }else{
                        final DatabaseReference mName = mDatabase.child("TaskUsers").child(mAuth.getCurrentUser().getUid());

                        final DatabaseReference mAdd = mDatabase.child("TaskChat").child("TaskUID").child(chatUID).push();

                        String simpleDate = new SimpleDateFormat("dd-MM-yyyy h:mm a").format(new java.util.Date());
                        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
                        Long timeStampP = Long.parseLong(timeStamp);

                        String timeStampR = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
                        Long timeStampRR = Long.parseLong(timeStampR);
                        Long timeStampRRR = -1 * timeStampRR;

                        mAdd.child("UID").setValue(userKey);
                        mAdd.child("Msg").setValue(input);
                        mAdd.child("Time").setValue(simpleDate);
                        mAdd.child("TimeStamp").setValue(timeStampP);
                        mAdd.child("TimeStampR").setValue(timeStampRRR);

                        mName.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String name = dataSnapshot.child("Name").getValue().toString();
                                String head = dataSnapshot.child("ProfilePicture").getValue().toString();
                                mAdd.child("Name").setValue(name);
                                mAdd.child("Head").setValue(head);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        inputChatField.setText("");
                        }
                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTime(String time){
            TextView postTime = mView.findViewById(R.id.timeChatTv);
            postTime.setText(time);
        }

        public void setName(String name){
            TextView postName = mView.findViewById(R.id.nameChatTv);
            postName.setText(name);
        }

        public void setMsg(String msg){
            TextView postMsg = mView.findViewById(R.id.chatTv);
            postMsg.setText(msg);
        }

        public void setHead(Context ctx,String head){
            ImageView postHead = mView.findViewById(R.id.headChat);
            Picasso.with(ctx).load(head).into(postHead);
        }

    }


    public void rView(String uID){
       DatabaseReference mRe = mDatabase.child("TaskChat").child("TaskUID").child(uID);
        Query query = mRe.orderByChild("TimeStamp");
        FirebaseRecyclerAdapter<ChatTicket,ViewHolder> fbr = new FirebaseRecyclerAdapter<ChatTicket, ViewHolder>(
                ChatTicket.class,
                R.layout.chat_ticket,
                ViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, ChatTicket model, int position) {
                viewHolder.setHead(getApplicationContext(),model.getHead());
                viewHolder.setMsg(model.getMsg());
                viewHolder.setTime(model.getTime());
                viewHolder.setName(model.getName());


            }
        };
        recyclerView.setAdapter(fbr);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}
