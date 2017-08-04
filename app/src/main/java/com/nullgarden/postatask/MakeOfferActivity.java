package com.nullgarden.postatask;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;

public class MakeOfferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_offer);

        final String postKey = getIntent().getExtras().getString("postId");
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("TaskPost").child(postKey).child("Offers");
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        final TextView offerTv = findViewById(R.id.offerTv);
        Button sendOfferBtn = findViewById(R.id.sendOfferBtn);

        sendOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MakeOfferActivity.this);
                builder.setMessage("Are you sure want to make this offer?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String simpleDate = new SimpleDateFormat("d-M-yyyy h:mm a").format(new java.util.Date());

                        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
                        Long timeStampP = Long.parseLong(timeStamp);

                        String timeStampR = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
                        Long timeStampRR = Long.parseLong(timeStampR);
                        Long timeStampRRR = -1 * timeStampRR;

                        String offer = offerTv.getText().toString();
                        final DatabaseReference mDate = mDatabase.push();
                        mDate.child("Content").setValue(offer);
                        mDate.child("UID").setValue(mAuth.getCurrentUser().getUid());
                        mDate.child("Email").setValue(mAuth.getCurrentUser().getEmail());
                        mDate.child("Time").setValue(simpleDate);
                        mDate.child("TimeStamp").setValue(timeStampP);
                        mDate.child("TimpStampR").setValue(timeStampRRR);

                        DatabaseReference mNameRef = FirebaseDatabase.getInstance().getReference().child("TaskUsers").child(mAuth.getCurrentUser().getUid());
                        mNameRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        mDate.child("Name").setValue(dataSnapshot.child("Name").getValue());
                                        mDate.child("Head").setValue(dataSnapshot.child("ProfilePicture").getValue());
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }
}
