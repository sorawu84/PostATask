package com.nullgarden.postatask;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String postKey = getIntent().getExtras().getString("postId");

        final TextView titleDetail = findViewById(R.id.titleDetail);
        final TextView nameDetail = findViewById(R.id.nameDetail);
        final ImageView headDetail = findViewById(R.id.headDetail);
        final ImageView imageDetail = findViewById(R.id.imageDetail);
        final TextView detailDetail = findViewById(R.id.detailDetail);
        final TextView priceDetail = findViewById(R.id.priceDetail);
        final ImageView removeBtn = findViewById(R.id.removeBtn);
        final Button offerBtn = findViewById(R.id.offerBtn);
        final Button viewOfferBtn = findViewById(R.id.viewOfferBtn);
        final Button askBtn = findViewById(R.id.askBtn);

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("TaskPost");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(postKey)){
                    String title = dataSnapshot.child(postKey).child("Title").getValue().toString();
                    String name = dataSnapshot.child(postKey).child("Name").getValue().toString();
                    String head = dataSnapshot.child(postKey).child("Head").getValue().toString();
                    String detail = dataSnapshot.child(postKey).child("Detail").getValue().toString();
                    String price = dataSnapshot.child(postKey).child("Price").getValue().toString();
                    final String uid = dataSnapshot.child(postKey).child("UID").getValue().toString();

                            askBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent chatGo = new Intent(PostDetailActivity.this,ChatActivity.class);
                                    chatGo.putExtra("userKey",uid);
                                    startActivity(chatGo);
                                }
                            });

                    Picasso.with(PostDetailActivity.this).load(head).into(headDetail);
                    titleDetail.setText(title);
                    nameDetail.setText(name);
                    detailDetail.setText(detail);
                    priceDetail.setText("RM"+price);

                    if(dataSnapshot.child(postKey).hasChild("Image")){
                        String image = dataSnapshot.child(postKey).child("Image").getValue().toString();
                        Picasso.with(PostDetailActivity.this).load(image).into(imageDetail);
                    }

                    if(mAuth.getCurrentUser().getUid().equals(uid)){
                        removeBtn.setVisibility(View.VISIBLE);
                        offerBtn.setVisibility(View.INVISIBLE);
                    }
                }

                offerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent offerAct = new Intent(PostDetailActivity.this,MakeOfferActivity.class);
                        offerAct.putExtra("postId",postKey);
                        startActivity(offerAct);
                    }
                });

                viewOfferBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent offerAct = new Intent(PostDetailActivity.this,ShowOfferActivity.class);
                        offerAct.putExtra("postId",postKey);
                        startActivity(offerAct);
                    }
                });

                removeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(PostDetailActivity.this);
                        builder.setMessage("Are you sure want to delete this post?");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDatabase.child(postKey).removeValue();
                                finish();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                });
                //end of removebtn.

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


}
