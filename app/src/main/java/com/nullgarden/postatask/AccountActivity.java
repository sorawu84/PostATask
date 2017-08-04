package com.nullgarden.postatask;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AccountActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final TextView nameTv = findViewById(R.id.nameTv);
        final ImageView headImage = findViewById(R.id.headImage);
        final TextView aboutTv = findViewById(R.id.aboutTv);
        final TextView aboutField = findViewById(R.id.aboutField);
        final Button saveBtn = findViewById(R.id.saveBtn);
        final Button saveNameBtn = findViewById(R.id.saveNameBtn);
        final TextView nameField = findViewById(R.id.nameField);

        nameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameTv.setVisibility(View.GONE);
                saveNameBtn.setVisibility(View.VISIBLE);
                nameField.setVisibility(View.VISIBLE);
                saveNameBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = nameField.getText().toString();
                        mDatabase.child("TaskUsers").child(mAuth.getCurrentUser().getUid()).child("Name").setValue(name);
                        nameTv.setVisibility(View.VISIBLE);
                        saveNameBtn.setVisibility(View.GONE);
                        nameField.setVisibility(View.GONE);
                    }
                });
            }
        });

        aboutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutTv.setVisibility(View.GONE);
                aboutField.setVisibility(View.VISIBLE);
                saveBtn.setVisibility(View.VISIBLE);
                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String about = aboutField.getText().toString();
                        mDatabase.child("TaskUsers").child(mAuth.getCurrentUser().getUid()).child("About").setValue(about);
                        aboutTv.setVisibility(View.VISIBLE);
                        aboutField.setVisibility(View.GONE);
                        saveBtn.setVisibility(View.GONE);
                    }
                });

            }
        });

        Button logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setMessage("Are you sure want to logout?");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        startActivity(new Intent(AccountActivity.this,MainActivity.class));
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

        final DatabaseReference mRef = mDatabase.child("TaskUsers").child(mAuth.getCurrentUser().getUid());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nameTv.setText(dataSnapshot.child("Name").getValue().toString());
                Picasso.with(AccountActivity.this).load(dataSnapshot.child("ProfilePicture").getValue().toString()).into(headImage);

                if(dataSnapshot.hasChild("About")){
                    aboutTv.setText(dataSnapshot.child("About").getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
