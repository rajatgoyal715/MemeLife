package com.rajatgoyal.memelife;

import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView image;
    private StorageReference storageRef;
    private StorageReference appfestRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageRef = FirebaseStorage.getInstance().getReference().child("Appfest");
        appfestRef = storageRef.child("Appfest");

        image = (ImageView) findViewById(R.id.image);
        next();
    }

    public void next() {
        // calculate num with random generator function
        int num = (int)(Math.random()*20 + 1);
        appfestRef.child(num + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download url
                Picasso.with(getApplication()).load(uri).into(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
            }
        });
    }
}
