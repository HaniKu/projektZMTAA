package com.example.hanka.projektmtaaa;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class OneItemDisplay extends AppCompatActivity {
    ListView List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_item_display);
        List = (ListView) findViewById(R.id.textik);
    String cities = getIntent().getStringExtra("");

       // tv.setText(cities);
        String skuska = "https://api.backendless.com/v1/data/skuska?where=objectId%20%3D%20'"+cities+"'";
        Log.d("skuska","som tu");
        new HttpAsyncTask().execute(skuska);
        if (isConnected());

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
            Toast.makeText(getBaseContext(), "you are connected!", Toast.LENGTH_LONG).show();
            return true;
        }
        else
        {
            Toast.makeText(getBaseContext(), "no internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }

    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String>
    {       //thread na ziskanie url
        @Override
        protected String doInBackground(String... urls) {

            try {
                return httpGET(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //  Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

            //tv.setText(result);
            Log.d("skuska",result);
            vypisJson(result);
        }
    }

    public void vypisJson(String strJson){
        //  ArrayAdapter<String> adapter = new ArrayAdapter<String>();
        ArrayList<String> adapter = new ArrayList<String>();
        final ArrayList<String> objectID = new ArrayList<String>();
        try {
            JSONObject jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("data");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String meno = jsonObject.optString("meno").toString();
                String adress = jsonObject.optString("adress").toString();
                String openingHours = jsonObject.optString("openingHours").toString();
                String phoneNumber = jsonObject.optString("phoneNumber").toString();
                boolean wifi = Boolean.parseBoolean(jsonObject.optString("wifi").toString());
                boolean smoking = Boolean.parseBoolean(jsonObject.optString("smoking").toString());
                boolean lactoseFree = Boolean.parseBoolean(jsonObject.optString("lactoseFree").toString());
                boolean glutenFree = Boolean.parseBoolean(jsonObject.optString("glutenFree").toString());
               // String infro =" \n name= "+ meno +" \n adress= "+ adress;
                String infro =" \n name= "+ meno +" \n adress= "+ adress +" \n " + " \n opening hours= "+ openingHours +" \n phone Number= "+ phoneNumber +" \n "+" \n wifi= "+ wifi +" \n " + " \n smoking= "+ smoking +" \n lactoseFree = "+ lactoseFree +" \n "+" \n glutenFree= "+ glutenFree +" \n " ;
                adapter.add(infro);
               // Log.d("skuska",infro);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        // Log.d("log", String.valueOf(objectID));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, adapter);
        List.setAdapter(adapter1);

    }
}
