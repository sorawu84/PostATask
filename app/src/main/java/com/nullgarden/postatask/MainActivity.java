package com.nullgarden.postatask;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        ImageView goPostBtn = findViewById(R.id.goCreateBtn);
        goPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,PostActivity.class));
            }
        });

        ImageView goAccountBtn = findViewById(R.id.goAccountBtn);
        goAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAuth.getCurrentUser() != null){
                    startActivity(new Intent(MainActivity.this,AccountActivity.class));
                }else{
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }
            }
        });

    }

    public static  class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title){
            TextView postTitle = mView.findViewById(R.id.titlePost);
            postTitle.setText(title);
        }

        public void setName(String name){
            TextView postName = mView.findViewById(R.id.namePost);
            postName.setText("posted by "+name);
        }

        public void setDate(String date){
            TextView postDate = mView.findViewById(R.id.dateTv);
            postDate.setText(date);
        }
        public void setTime(String time){
            TextView postTime = mView.findViewById(R.id.timeTv);
            postTime.setText(time);
        }

        public void setPrice(String price){
            TextView postPrice = mView.findViewById(R.id.priceTv);
            postPrice.setText("RM"+price);
        }

        public void setHead(Context context,String head){
            ImageView postHead = mView.findViewById(R.id.headPost);
            Picasso.with(context).load(head).into(postHead);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("TaskPost");
        Query query = mRef.orderByChild("TimeStampR");
        FirebaseRecyclerAdapter<MainTicket, ViewHolder> firebaseRecyclerViewAdapter = new FirebaseRecyclerAdapter<MainTicket, ViewHolder>(
                MainTicket.class,
                R.layout.main_ticket,
                ViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(ViewHolder viewHolder, MainTicket model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setHead(getApplicationContext(),model.getHead());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());
                viewHolder.setName(model.getName());
                viewHolder.setTitle(model.getTitle());

                final String postKey = getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singlePost = new Intent(MainActivity.this,PostDetailActivity.class);
                        singlePost.putExtra("postId",postKey);
                        startActivity(singlePost);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerViewAdapter);
    }
}
