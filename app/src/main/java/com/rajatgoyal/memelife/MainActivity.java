package com.rajatgoyal.memelife;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ImageView image, download, share;
    Button next;

    private StorageReference storageRef;
    private StorageReference appfestRef;

    private int MAX;
    private int counter;
    private Uri imageUri;

    ProgressBar progress;
    FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        storageRef = FirebaseStorage.getInstance().getReference();
        appfestRef = storageRef.child("Appfest");

        image = (ImageView) findViewById(R.id.image);

        download = (ImageView) findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                download();
            }
        });

        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.GONE);

        frame = (FrameLayout) findViewById(R.id.frame);

        MAX = 49;
        counter = 0;

        next();
    }

    public void share() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(intent, "Share Image"));

    }

    public void download() {
        Toast.makeText(this, "Image downloaded.", Toast.LENGTH_SHORT).show();
    }

    public void next() {
        progress.setVisibility(View.VISIBLE);
        frame.setVisibility(View.GONE);
        int temp = (counter+1)%MAX;
        counter = (temp==0)?MAX:temp;
        appfestRef.child(counter + ".jpeg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download url
                progress.setVisibility(View.GONE);
                frame.setVisibility(View.VISIBLE);
                imageUri = uri;
//                Toast.makeText(MainActivity.this, imageUri.toString(), Toast.LENGTH_SHORT).show();
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
