package com.nullgarden.postatask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;

public class PostActivity extends AppCompatActivity {

    int G_RE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("TaskPost").push();
        final ProgressDialog progress = new ProgressDialog(this);

        final TextView titlefield = findViewById(R.id.titleField);
        final TextView detailField = findViewById(R.id.detailField);
        final TextView priceField = findViewById(R.id.priceField);
        ImageView imageField = findViewById(R.id.imageField);



        Button sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String title = titlefield.getText().toString();
                final String detail = detailField.getText().toString();
                final String price = priceField.getText().toString();
                if(TextUtils.isEmpty(title) || TextUtils.isEmpty(detail) || TextUtils.isEmpty(price)){
                    Toast.makeText(PostActivity.this, "All fields should not be empty.", Toast.LENGTH_SHORT).show();
                }else{
                    progress.setMessage("Posting..");
                    progress.show();

                    String simpleDate = new SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
                    String simpleTime = new SimpleDateFormat("h:mm a").format(new java.util.Date());
                    String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
                    Long timeStampP = Long.parseLong(timeStamp);

                    String timeStampR = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
                    Long timeStampRR = Long.parseLong(timeStampR);
                    Long timeStampRRR = -1 * timeStampRR;

                    mDatabase.child("Time").setValue(simpleTime);
                    mDatabase.child("Date").setValue(simpleDate);
                    mDatabase.child("TimeStamp").setValue(timeStampP);
                    mDatabase.child("TimeStampR").setValue(timeStampRRR);
                    mDatabase.child("Title").setValue(title);
                    mDatabase.child("Detail").setValue(detail);
                    mDatabase.child("Price").setValue(price);
                    mDatabase.child("UID").setValue(mAuth.getCurrentUser().getUid());

                    DatabaseReference getHeadRef = FirebaseDatabase.getInstance().getReference().child("TaskUsers").child(mAuth.getCurrentUser().getUid()).child("ProfilePicture");
                    getHeadRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mDatabase.child("Head").setValue(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference getNameRef = FirebaseDatabase.getInstance().getReference().child("TaskUsers").child(mAuth.getCurrentUser().getUid()).child("Name");
                    getNameRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            mDatabase.child("Name").setValue(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    if(resultUri != null){
                        mDatabase.child("Image").setValue(resultUri.toString());
                        StorageReference filepath = FirebaseStorage.getInstance().getReference().child("TaskImages").child(mAuth.getCurrentUser().getUid()).child(resultUri.getLastPathSegment());
                        filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            }
                        });
                    }

                    progress.dismiss();
                    startActivity(new Intent(PostActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

        imageField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImage();
            }
        });

    }

    private void cropImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,G_RE);

    }

    Uri resultUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == G_RE && resultCode == RESULT_OK){
            Uri uri = data.getData();

            CropImage.activity(uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                ImageView image = findViewById(R.id.imageField);
                Picasso.with(PostActivity.this).load(resultUri).fit().centerCrop().into(image);
            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();

            }else{
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
