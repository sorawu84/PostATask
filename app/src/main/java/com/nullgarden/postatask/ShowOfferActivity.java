package com.nullgarden.postatask;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShowOfferActivity extends AppCompatActivity {

    RecyclerView showRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_offer);

        showRecyclerView = findViewById(R.id.showRecyclerView);
        showRecyclerView.setHasFixedSize(true);
        showRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String postkey = getIntent().getExtras().getString("postId");


        final TextView showTitleTv = findViewById(R.id.showTitleTv);

        DatabaseReference titleRef = FirebaseDatabase.getInstance().getReference().child("TaskPost").child(postkey).child("Title");
                titleRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        showTitleTv.setText(dataSnapshot.getValue().toString());
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

        public void setName(String name){
            TextView postName = mView.findViewById(R.id.showNameTv);
            postName.setText(name);
        }

        public void setContent(String content){
            TextView postContent = mView.findViewById(R.id.showOfferTv);
            postContent.setText(content);
        }

        public void setTime(String time){
            TextView postTime = mView.findViewById(R.id.showTimeTv);
            postTime.setText(time);
        }

        public void setHead(Context contect,String head){
            ImageView postImage = mView.findViewById(R.id.showHead);
            Picasso.with(contect).load(head).into(postImage);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        String postkey = getIntent().getExtras().getString("postId");

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("TaskPost").child(postkey).child("Offers");
        Query query = mRef.orderByChild("TimeStamp");
        FirebaseRecyclerAdapter<ShowOfferTicket,ViewHolder> fbrv = new FirebaseRecyclerAdapter<ShowOfferTicket, ViewHolder>(
                ShowOfferTicket.class,
                R.layout.show_offer_ticket,
                ViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, ShowOfferTicket model, int position) {
                viewHolder.setContent(model.getContent());
                viewHolder.setHead(getApplicationContext(),model.getHead());
                viewHolder.setName(model.getName());
                viewHolder.setTime(model.getTime());

            }
        };
        showRecyclerView.setAdapter(fbrv);
    }
}
