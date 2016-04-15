package com.example.hanka.projektmtaaa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class EditItem extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    private Button save;
    String cities;
    String objectID;
    String Url;
    Integer category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String jsonCoTrebaParse = getIntent().getStringExtra("");
        cities = getIntent().getStringExtra("id");
        Log.i(TAG, "id je " + cities);
        vypisJson(jsonCoTrebaParse);
        save = (Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String URL = "https://api.backendless.com/v1/data/skuska/"+cities;
                new POSTAsyncTask().execute(URL);
            }
        });
    }

    public void vypisJson(String strJson){

        EditText editText2 = (EditText) findViewById(R.id.editName);
        EditText editText4 = (EditText) findViewById(R.id.editText4);
        EditText editText5 = (EditText) findViewById(R.id.editText5);
        Spinner editText = (Spinner) findViewById(R.id.editText);

        EditText wifi = (EditText) findViewById(R.id.wifi);
        EditText flukoza = (EditText) findViewById(R.id.glukoza);
        EditText lactoza = (EditText) findViewById(R.id.lactosa);
        EditText smoking = (EditText) findViewById(R.id.smoke);
        //  ArrayAdapter<String> adapter = new ArrayAdapter<String>();
        ArrayList<String> adapter = new ArrayList<String>();

        try {
            JSONObject jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("data");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                editText2.setText(jsonObject.optString("name").toString());
                editText4.setText(jsonObject.optString("adress").toString());
                Integer otvaracka = Integer.parseInt(jsonObject.optString("openingHours").toString());
                switch (otvaracka) {
                    case 1:
                        editText.setSelection(0);
                        break;
                    case 2:
                        editText.setSelection(1);
                        break;
                    default:
                        editText.setSelection(2);
                        break;
                }
                editText5.setText(jsonObject.optString("phoneNumber").toString());
                wifi.setText((jsonObject.optString("wifi").toString()));
                smoking.setText((jsonObject.optString("smoking").toString()));
                 lactoza.setText((jsonObject.optString("lactoseFree").toString()));
                flukoza.setText((jsonObject.optString("glutenFree").toString()));
                objectID= (jsonObject.optString("objectId").toString());
                Url = (jsonObject.optString("picture").toString());
                Log.i(TAG, "url obrazka  "+Url);
                //category = Integer.parseInt((jsonObject.optString("category").toString()));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private class POSTAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            try {
                return httpPOST(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Nic";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Sent!", Toast.LENGTH_LONG).show();
            Intent a = new Intent(EditItem.this, Display.class);
            startActivity(a);
        }

    }


    public String httpPOST(String urlStr) throws IOException {

        JSONObject json = new JSONObject();
        HttpURLConnection conn = null;
        EditText wifi = (EditText) findViewById(R.id.wifi);
        EditText flukoza = (EditText) findViewById(R.id.glukoza);
        EditText lactoza = (EditText) findViewById(R.id.lactosa);
        EditText smoking = (EditText) findViewById(R.id.smoke);
        EditText editText4 = (EditText) findViewById(R.id.editText4);
        EditText editText5 = (EditText) findViewById(R.id.editText5);
        Spinner editText = (Spinner) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editName);
        Integer edittext31;
        String edittext12 = editText.getSelectedItem().toString();
        switch (edittext12) {
            case "nonstop":
                edittext31 = 1; // they are executed if variable == c1
                break;
            case "closed":
                edittext31 = 2; // they are executed if variable == c2
                break;
            default:
                edittext31 = 3; // they are executed if none of the above case is satisfied
                break;
        }

        try {
            json.put("name", String.valueOf(editText2.getText().toString()));
            json.put("adress", String.valueOf(editText4.getText().toString()));
            json.put("phoneNumber", String.valueOf(editText5.getText().toString()));
            json.put("openingHours", Integer.valueOf(edittext31));
            json.put("glutenFree", Boolean.valueOf(flukoza.getText().toString()));
            json.put("lactoseFree", Boolean.valueOf(lactoza.getText().toString()));
            json.put("smoking", Boolean.valueOf(smoking.getText().toString()));
            json.put("wifi", Boolean.valueOf(wifi.getText().toString()));
            json.put("picture", Url);
            json.put("objectId",cities);
            Log.i(TAG, "som v httpPOST za deklaraciami  ");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try{
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("PUT");
            conn.addRequestProperty("application-ID", "CCB8E7ED-C40B-4D67-FF14-5FD1DC41F500");
            conn.addRequestProperty("secret-key", "A92106B5-AACE-6ACD-FF2A-9F2F83830600");
            conn.addRequestProperty("Content-Type", "application/json");
            conn.addRequestProperty("application-type", "REST");
            OutputStreamWriter outs  = new OutputStreamWriter(conn.getOutputStream());
            Log.i(TAG, "SENDING: " + json.toString());
            outs.write(json.toString());
            outs.flush();
            outs.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (conn.getResponseCode() == 403) {
            Log.i(TAG, "server reached; but access denied");
        }
        else if (conn.getResponseCode() == 404) {
            Log.i(TAG, "unknown URL");
        }
        else if (conn.getResponseCode() == 200) {
            Log.i(TAG, "request successfull");
        }
        else {
            Log.i(TAG,(String.valueOf(conn.getResponseCode())));
            Log.i(TAG, "error http response");
            Log.i(TAG,"error stream: "+conn.getErrorStream().toString());
            Log.i(TAG,conn.getResponseMessage().toString());

            throw new IOException(conn.getResponseMessage());
        }


        // Buffer the result into a string
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();

        conn.disconnect();
        return sb.toString();
    }
}
