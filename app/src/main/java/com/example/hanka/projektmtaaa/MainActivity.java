package com.example.hanka.projektmtaaa;

import android.app.ProgressDialog;
import android.content.Intent;
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
                EditText b = (EditText) findViewById(R.id.TFpassword);
                String str1 = b.getText().toString();
                if (str.equals(str1))
                {
                    final Intent i = new Intent(MainActivity.this, Display.class);
                    i.putExtra("username", str);

                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,R.style.AppTheme_NoActionBar);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Authenticatin...");
                    progressDialog.show();

                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(i);
                                progressDialog.dismiss();
                                }
                            },3000);
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
}
