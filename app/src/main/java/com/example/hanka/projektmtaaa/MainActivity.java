package com.example.hanka.projektmtaaa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public  class MainActivity extends AppCompatActivity  {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Blogin = (Button) findViewById(R.id.Blogin);
        assert Blogin != null;
        Blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText a = (EditText) findViewById(R.id.TFusername);
                String str = a.getText().toString();
              //  a.setText("");
                EditText b = (EditText) findViewById(R.id.TFpassword);
                String str1 = b.getText().toString();
                if (str1.equals("123") && str.equals("panko@gmail.com"))
                {
                    if(isConnected()) {
                        final Intent i = new Intent(MainActivity.this, Display.class);
                        startActivity(i);
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public boolean isConnected()            //zistujem ci som online
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
            alertDialog2.setTitle("No Internet connection");
            alertDialog2.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }


                    });
            alertDialog2.show();
            return false;
        }

    }
}
