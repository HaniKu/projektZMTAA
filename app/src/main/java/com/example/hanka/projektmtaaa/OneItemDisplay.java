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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class OneItemDisplay extends AppCompatActivity {
    ListView List;
    Socket socket;
    ArrayList<String> reads = new ArrayList<String>();
    FloatingActionButton edit;
    FloatingActionButton delete;
    ImageView makePhotoBigger;
    String cities;
    String text="not connected";
    private ProgressDialog pdia;
    private static final String TAG = "MyActivity";
    public String getPoleJson() {
        return poleJson;
    }
    String urlObrazka;
    String poleJson = null;
    Boolean running = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_item_display);

        cities = getIntent().getStringExtra("");
        Log.d(TAG, "idecko je "+cities);
        String skuska = "https://api.backendless.com/v1/data/skuska?where=objectId%20%3D%20'"+cities+"'";
        Log.d("skuska", "som tu");
        if(isConnected()) {
            //new HttpAsyncTask().execute(skuska);
            spravSocket(cities);
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
            JSONArray jsonRootArray = new JSONArray(strJson);
            JSONObject jsonRootObject = jsonRootArray.optJSONObject(0);
            JSONObject jsonDataObject = jsonRootObject.optJSONObject("body");
            JSONObject jsonObject2 = jsonDataObject.optJSONObject("data");
            JSONObject jsonObject = jsonObject2.optJSONObject("nove");
            Log.d("socket data array", jsonObject.toString());

            //Iterate the jsonArray and print the info of JSONObjects


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

        } catch (JSONException e){
            e.printStackTrace();
        }

        List = (ListView) findViewById(R.id.textik);
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, adapter);
        Thread viewview = new Thread() {
            public void run() {
                while(running == true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List.setAdapter(adapter1);
                            ImageView imageView = (ImageView) findViewById(R.id.imageView);
                            Picasso.with(getBaseContext()).load(urlObrazka).into(imageView);
                        }
                    });
                    Log.d("call","Vypinam nit");
                    running = false;
                }
            }
        };
        viewview.run();

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


    public void spravSocket(String id){

        IO.Options opts = new IO.Options();

        opts.secure = false;
        opts.port = 1341;
        opts.reconnection = true;
        opts.forceNew = true;
        opts.timeout = 5000;

        final JSONObject js = new JSONObject();
        try {
            js.put("url", "/data/testHana/"+cities);    //data = tabulka
            Log.i(TAG, "som v spravSocket");
        } catch(Exception e) {
            e.printStackTrace();
            Log.i(TAG, "som v exception spravSocket");
        }

        System.out.println(js);

        try {
            socket = IO.socket("http://sandbox.touch4it.com:1341/?__sails_io_sdk_version=0.12.1", opts);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                socket.emit("get", js, new Ack() {
                    @Override
                    public void call(Object... args) {

                        try {
                            text = Arrays.toString(args);       //server odpoved
                            vypisJson(text);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (!text.equals("")) {
                            reads.add(text);
                            Log.d("get one response: ", reads.toString());
                            Log.i(TAG, "get one response "+reads.toString());
                        }
                    }
                });
            }

        }).on("event", new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                text = Arrays.toString(args);
                if (!text.equals("")) {
                    reads.add(text);
                    Log.d("event response: ", reads.toString());
                    Log.i(TAG, "event response " + reads.toString());
                }
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                text = Arrays.toString(args);
                if (!text.equals("")) {
                    reads.add(text);
                    Log.d("disconnect response: ", reads.toString());
                }
            }
        });
        socket.connect();
    }
}
