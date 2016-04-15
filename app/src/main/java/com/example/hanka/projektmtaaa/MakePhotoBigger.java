package com.example.hanka.projektmtaaa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MakePhotoBigger extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_photo_bigger);

        ImageView radioSexGroup = (ImageView) findViewById(R.id.photo);
        String obrazek = getIntent().getStringExtra("");
        Picasso.with(this).load(obrazek).into(radioSexGroup);
    }
}
