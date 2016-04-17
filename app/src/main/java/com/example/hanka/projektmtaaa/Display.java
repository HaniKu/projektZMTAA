package com.example.hanka.projektmtaaa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Display extends AppCompatActivity {
    ListView List;
    FloatingActionButton pridatNovy;
    FloatingActionButton refresh;
    private ProgressDialog pdia;
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        List = (ListView) findViewById(R.id.textak);
        refresh = (FloatingActionButton) findViewById(R.id.refresh);
        pridatNovy = (FloatingActionButton) findViewById(R.id.pridatNovy);
        String skuska = "https://api.backendless.com/v1/data/skuska?where=category%20in%20(1%2C2%2C3)%20and%20openingHours%20in%20(1%2C2%2C3)";
        if(isConnected()) {
            new HttpAsyncTask().execute(skuska);
        }
        pridatNovy.setOnClickListener(new View.OnClickListener()

        {
            public void onClick (View arg0){
                Intent myIntent = new Intent(Display.this,
                        CreateNewItem.class);
                if(isConnected()) {
                    startActivity(myIntent);
                }else{
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Display.this);
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

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Display.this,
                        Display.class);
                if(isConnected()) {
                    startActivity(myIntent);
                }else{
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Display.this);
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


    public String httpGET(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty("application-ID", "CCB8E7ED-C40B-4D67-FF14-5FD1DC41F500");
        conn.addRequestProperty("secret-key", "A92106B5-AACE-6ACD-FF2A-9F2F83830600");

        if (conn.getResponseCode() != 200) {
            throw new IOException(conn.getResponseMessage());
        }
        if (conn.getResponseCode() == 403) {
            //List.setText("Error");
        }


        // Buffer the result into a string
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();

        conn.disconnect();
        Log.d(TAG, "poslat rozaprsovat  " + sb.toString());
        return sb.toString();
    }


    public boolean isConnected()            //zistujem ci som online
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Display.this);
            alertDialog2.setTitle("No Internet connection");
            alertDialog2.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }


                    });
            alertDialog2.show();
            return false;
        }

    }

    class TaskKiller extends TimerTask {
        private AsyncTask<?, ?, ?> mTask;
        public TaskKiller(AsyncTask<?, ?, ?> task) {
            this.mTask = task;
        }

        public void run() {
            mTask.cancel(true);
        }
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {       //thread na ziskanie url
        @Override
        protected String doInBackground(String... urls) {

            Timer timer = new Timer();
            timer.schedule(new TaskKiller(this), 7000);

            try {
                Log.d(TAG, "idem cez url  "+(urls[0]));
                return httpGET(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            timer.cancel();
            return "ahoj";
        }

        protected void onCancelled() {
            //Toast.makeText(getBaseContext(), "LOADING CANCELLED", Toast.LENGTH_LONG).show();
            Log.i(TAG, "**** cancelled ****");
            pdia.dismiss();
            //finish();
            req_timed();
        }

        protected void onPreExecute(){
                super.onPreExecute();
                pdia = new ProgressDialog(Display.this, R.style.AppTheme_NoActionBar);
                pdia.setMessage("Loading...");
                pdia.show();
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("ahoj")){
                showAlert1();
            }else {
                Log.d(TAG, "rozaprsovat treba " + result);
                pdia.dismiss();
                vypisJson(result);
            }
        }
    }

    public void vypisJson(String strJson) {
        //  ArrayAdapter<String> adapter = new ArrayAdapter<String>();
        ArrayList<String> adapter = new ArrayList<String>();
        final ArrayList<String> objectID = new ArrayList<String>();
        try {
            JSONObject jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("data");

            //Iterate the jsonArray and print the info of JSONObjects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String adress = jsonObject.optString("adress").toString();
                String meno = jsonObject.optString("name").toString();
                String id = jsonObject.optString("objectId").toString();
                String infro = " \n name: " + meno + " \n adress: " + adress + " \n ";

                adapter.add(infro);
                objectID.add(id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, adapter);
        List.setAdapter(adapter1);

        List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newActivity = new Intent(Display.this, OneItemDisplay.class);
                String cities = String.valueOf(parent.getItemAtPosition(position));
                String idecko = objectID.get(position);
                Log.d("log", objectID.get(position));
                newActivity.putExtra("", idecko);
                if ((isConnected())) {
                    startActivity(newActivity);
                } else {
                    final AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Display.this);
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

    public void showAlert1(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Display.this);
        alertDialog2.setTitle("Uncorrect data");
        alertDialog2.setMessage("stay on this activity?");
        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog2.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent a = new Intent(Display.this, MainActivity.class);
                        startActivity(a);
                    }
                });

        alertDialog2.show();
    }

    public void req_timed(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(Display.this);
        alertDialog2.setTitle("LOADING CANCELLED");
        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(Display.this, MainActivity.class);
                        startActivity(a);
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }
}