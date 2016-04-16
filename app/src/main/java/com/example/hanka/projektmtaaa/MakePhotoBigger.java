package com.example.hanka.projektmtaaa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MakePhotoBigger extends AppCompatActivity {
String cities;
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_photo_bigger);
        cities = getIntent().getStringExtra("id");
        ImageView radioSexGroup = (ImageView) findViewById(R.id.photo);
        String obrazek = getIntent().getStringExtra("");
        Picasso.with(this).load(obrazek).into(radioSexGroup);

        assert radioSexGroup != null;
        radioSexGroup.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MakePhotoBigger.this,
                        OneItemDisplay.class);
                myIntent.putExtra("", cities);
                Log.d(TAG, "z obrazka posiel id " + cities);
                if(isConnected()) {
                    startActivity(myIntent);
                }else{
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MakePhotoBigger.this);
                    alertDialog2.setTitle("No Internet connection");
                    alertDialog2.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog2.show();
                }

            }
        });
    }
    public boolean isConnected()            //zistujem ci som online
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Toast.makeText(getBaseContext(), "you are connected!", Toast.LENGTH_LONG).show();
            return true;
        } else {
            return false;
        }

    }
}
