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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

public class OneItemDisplay extends AppCompatActivity {
    ListView List;
    FloatingActionButton edit;
    FloatingActionButton delete;
    ImageView makePhotoBigger;
    String cities;
    private ProgressDialog pdia;
    private static final String TAG = "MyActivity";
    public String getPoleJson() {
        return poleJson;
    }
    String urlObrazka;
    String poleJson = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_item_display);
        List = (ListView) findViewById(R.id.textik);
        cities = getIntent().getStringExtra("");
        Log.d(TAG, "idecko je "+cities);
        String skuska = "https://api.backendless.com/v1/data/skuska?where=objectId%20%3D%20'"+cities+"'";
        Log.d("skuska", "som tu");
        if(isConnected()) {
            new HttpAsyncTask().execute(skuska);
        }

        edit = (FloatingActionButton) findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View arg0) {
                Log.i(TAG, "pole pred novou aktivitou" + getPoleJson());
                Intent myIntent = new Intent(OneItemDisplay.this,
                        EditItem.class);
                myIntent.putExtra("", getPoleJson());
                myIntent.putExtra("id", cities);
                if(isConnected()) {
                    startActivity(myIntent);
                }else{
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(OneItemDisplay.this);
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

        delete = (FloatingActionButton) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener()

        {
            public void onClick (View arg0){
                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        OneItemDisplay.this);
                alertDialog2.setTitle("Confirm Delete...");
                alertDialog2.setMessage("Are you sure you want delete this file?");
                alertDialog2.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteItem delete = new DeleteItem(cities);
                                Intent a = new Intent(OneItemDisplay.this, Display.class);
                                if(isConnected()) {
                                    Toast.makeText(getBaseContext(), "Deleted!", Toast.LENGTH_LONG).show();
                                    startActivity(a);
                                }else{
                                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(OneItemDisplay.this);
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
                alertDialog2.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog2.show();
            }
        });

        makePhotoBigger = (ImageView) findViewById(R.id.imageView);
        makePhotoBigger.setOnClickListener(new View.OnClickListener()

        {
            public void onClick (View arg0){
                Log.i(TAG, "pole pred novou aktivitou" + getPoleJson());
                Intent myIntent = new Intent(OneItemDisplay.this,
                        MakePhotoBigger.class);
                myIntent.putExtra("", urlObrazka);
                myIntent.putExtra("id", cities);
                startActivity(myIntent);
            }
        });
    }

    public String httpGET(String urlStr) throws IOException
    {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty("application-ID", "CCB8E7ED-C40B-4D67-FF14-5FD1DC41F500");
        conn.addRequestProperty("secret-key",  "A92106B5-AACE-6ACD-FF2A-9F2F83830600");

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
        return sb.toString();
    }


    public boolean isConnected()            //zistujem ci som online
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {

            AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(OneItemDisplay.this);
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
                Log.d(TAG, "idem cez url  " + (urls[0]));
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

        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(OneItemDisplay.this, R.style.AppTheme_NoActionBar);
            pdia.setMessage("Loading...");
            pdia.show();
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
                Log.d(TAG, "rozaprsovat treba " + result);
                pdia.dismiss();
                vypisJson(result);
        }
    }

    public void vypisJson(String strJson){
        poleJson = strJson;
        ArrayList<String> adapter = new ArrayList<String>();
        final ArrayList<String> objectID = new ArrayList<String>();
        try {
            JSONObject jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray  jsonArray = jsonRootObject.optJSONArray("data");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                Integer kategoria = Integer.parseInt(jsonObject.optString("category").toString());
                String kategory;
                switch (kategoria) {
                    case 1:
                        kategory = "bakery cafe";
                        break;
                    case 2:
                        kategory = "cafe";
                        break;
                    default:
                        kategory = "coffee to go";
                        break;
                }
                String meno = jsonObject.optString("name").toString();
                String adress = jsonObject.optString("adress").toString();
                Integer openingHours = Integer.parseInt(jsonObject.optString("openingHours").toString());
                String phoneNumber = (jsonObject.optString("phoneNumber").toString());
                String cislo;
                switch (openingHours) {
                    case 1:
                        cislo = "nonstop";
                        break;
                    case 2:
                        cislo = "closed";
                        break;
                    default:
                        cislo = "from 10 to 20";
                        break;
                }
                boolean wifi = Boolean.parseBoolean(jsonObject.optString("wifi").toString());
                String wifibool;
                if (wifi == true) {
                    wifibool = "Yes";
                } else {
                    wifibool = "No";
                }
                boolean smoking = Boolean.parseBoolean(jsonObject.optString("smoking").toString());
                String smokebool;
                if (smoking == true) {
                    smokebool = "Yes";
                } else {
                    smokebool = "No";
                }
                boolean lactoseFree = Boolean.parseBoolean(jsonObject.optString("lactoseFree").toString());
                String lactabool;
                if (lactoseFree == true) {
                    lactabool = "Yes";
                } else {
                    lactabool = "No";
                }
                boolean glutenFree = Boolean.parseBoolean(jsonObject.optString("glutenFree").toString());
                String glutenbool;
                if (glutenFree == true) {
                    glutenbool = "Yes";
                } else {
                    glutenbool = "No";
                }
                urlObrazka = jsonObject.optString("picture").toString();
                Log.d(TAG,urlObrazka);
                String infro ="\n type: "+kategory+" \n " +" \n name: "+ meno +" \n " +" \n adress: "+ adress +" \n " + " \n opening hours: "+ cislo +" \n " +" \n phone Number: "+ phoneNumber +" \n "+" \n wifi: "+ wifibool +" \n " + " \n smoking: "+ smokebool +" \n " +" \n lactoseFree: "+ lactabool +" \n "+" \n glutenFree: "+ glutenbool +" \n " ;
                adapter.add(infro);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, adapter);
        List.setAdapter(adapter1);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load(urlObrazka).into(imageView);
    }

    public void req_timed(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(OneItemDisplay.this);
        alertDialog2.setTitle("LOADING CANCELLED");
        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent a = new Intent(OneItemDisplay.this, MainActivity.class);
                        startActivity(a);
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }
}
