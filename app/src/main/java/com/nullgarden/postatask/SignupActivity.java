package com.nullgarden.postatask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;

public class SignupActivity extends AppCompatActivity {

    private static final int G_RE = 321;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Button goLoginBtn = findViewById(R.id.goLoginBtn);
        goLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                finish();
            }
        });

        ImageView headBtn = findViewById(R.id.headBtn);
        headBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,G_RE);
            }
        });

        final ProgressDialog progress = new ProgressDialog(this);

        final TextView nameField = findViewById(R.id.nameSignField);
        final TextView emailField = findViewById(R.id.emailSignField);
        final TextView passwordField = findViewById(R.id.passwordSignField);
        Button signupBtn = findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = nameField.getText().toString();
                final String email = emailField.getText().toString();
                final String password = passwordField.getText().toString();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(SignupActivity.this, "All fields should not be empty.", Toast.LENGTH_SHORT).show();
                }else{
                    progress.setMessage("Loading");
                    progress.show();
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("TaskUsers").child(mAuth.getCurrentUser().getUid());

                                String since = new SimpleDateFormat("dd-MM-yyyy h:mm a").format(new java.util.Date());

                                mDatabase.child("Email").setValue(mAuth.getCurrentUser().getEmail());
                                mDatabase.child("Name").setValue(name);
                                mDatabase.child("ProfilePicture").setValue(resultUri.toString());
                                mDatabase.child("Since").setValue(since);

                                StorageReference mStorage = FirebaseStorage.getInstance().getReference();
                                StorageReference filepath = mStorage.child("TaskHead").child(mAuth.getCurrentUser().getUid()).child("ProfilePicture");
                                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    }
                                });
                                progress.dismiss();
                                startActivity(new Intent(SignupActivity.this,MainActivity.class));
                                finish();
                            }else{
                                progress.dismiss();
                                Toast.makeText(getApplicationContext(), "Signup failed, please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });
    }
    Uri resultUri;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == G_RE && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                ImageView image = findViewById(R.id.headBtn);
                Picasso.with(SignupActivity.this).load(resultUri).fit().centerCrop().into(image);

            }else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }

    }

}
