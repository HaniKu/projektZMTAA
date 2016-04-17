package com.example.hanka.projektmtaaa;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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
    ProgressDialog pdia;
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

                if(isConnected()) {
                    new POSTAsyncTask().execute(URL);
                }else{
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(EditItem.this);
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

    public void vypisJson(String strJson){

        EditText editText2 = (EditText) findViewById(R.id.editName);
        EditText editText4 = (EditText) findViewById(R.id.editText4);
        EditText editText5 = (EditText) findViewById(R.id.editText5);
        Spinner editText = (Spinner) findViewById(R.id.editText);

        Spinner wifi = (Spinner) findViewById(R.id.wifi);
        Spinner flukoza = (Spinner) findViewById(R.id.glukoza);
        Spinner lactoza = (Spinner) findViewById(R.id.lactosa);
        Spinner smoking = (Spinner) findViewById(R.id.smoke);
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
                String wifi1 = ((jsonObject.optString("wifi").toString()));
                switch(wifi1){
                    case "true":
                        wifi.setSelection(0);
                        break;
                    default:
                        wifi.setSelection(1);
                        break;
                }
                String smoking1 = ((jsonObject.optString("smoking").toString()));
                switch(smoking1){
                    case "true":
                        smoking.setSelection(0);
                        break;
                    default:
                        smoking.setSelection(1);
                        break;
                }
                 String lactosa1 = ((jsonObject.optString("lactoseFree").toString()));
                switch(lactosa1){
                    case "true":
                        lactoza.setSelection(0);
                        break;
                    default:
                        lactoza.setSelection(1);
                        break;
                }
                String flukoza1 = ((jsonObject.optString("glutenFree").toString()));
                switch(flukoza1){
                    case "true":
                        flukoza.setSelection(0);
                        break;
                    default:
                        flukoza.setSelection(1);
                        break;
                }
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

        protected void onPreExecute(String result){
            if(!(result == "ahoj" && result == "bad number")) {
                super.onPreExecute();
                pdia = new ProgressDialog(EditItem.this, R.style.AppTheme_NoActionBar);
                pdia.setMessage("Editing...");
                pdia.show();
            }

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("ahoj")){
                showAlert();
            } else if (result.equals("bad number")){
                showAlert1();
            }
            else {
                Intent a = new Intent(EditItem.this, Display.class);
                //pdia.dismiss();
                startActivity(a);

            }
        }

    }


    public String httpPOST(String urlStr) throws IOException {

        JSONObject json = new JSONObject();
        HttpURLConnection conn = null;
        Spinner wifi = (Spinner) findViewById(R.id.wifi);
        String wifi12 = wifi.getSelectedItem().toString();
        String hodnotaWifi;
        switch(wifi12){
            case "Yes":
                hodnotaWifi = "true"; // they are executed if variable == c1
                break;
            default:
                hodnotaWifi = "false"; // they are executed if variable == c1
                break;
        }
        Spinner flukoza = (Spinner) findViewById(R.id.glukoza);
        String flukoza12 = flukoza.getSelectedItem().toString();
        String hodnotaFlukoza;
        switch(flukoza12){
            case "Yes":
                hodnotaFlukoza = "true"; // they are executed if variable == c1
                break;
            default:
                hodnotaFlukoza = "false"; // they are executed if variable == c1
                break;
        }
        Spinner lactoza = (Spinner) findViewById(R.id.lactosa);
        String lactoza12 = lactoza.getSelectedItem().toString();
        String hodnotaLactoza;
        switch(lactoza12){
            case "Yes":
                hodnotaLactoza = "true"; // they are executed if variable == c1
                break;
            default:
                hodnotaLactoza = "false"; // they are executed if variable == c1
                break;
        }
        Spinner smoking = (Spinner) findViewById(R.id.smoke);
        String smoking12 = smoking.getSelectedItem().toString();
        String hodnotaSmoking;
        switch(smoking12){
            case "Yes":
                hodnotaSmoking = "true"; // they are executed if variable == c1
                break;
            default:
                hodnotaSmoking = "false"; // they are executed if variable == c1
                break;
        }
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

        if (editText5.getText().toString().equals("") || editText4.getText().toString().equals("") || editText2.getText().toString().equals("") ) {
            Log.i(TAG, "jedno je null");
            return "ahoj";
        }
        else {

            String isPhone = "^[0-9]{4}\\-?[0-9]{3}\\-?[0-9]{3}$";
            if (!(editText5.getText().toString().matches(isPhone))) {
                Log.i(TAG, "is phone numer OK");
                return "bad number";
            } else{
                    try {
                        json.put("name", String.valueOf(editText2.getText().toString()));
                        json.put("adress", String.valueOf(editText4.getText().toString()));
                        json.put("phoneNumber", String.valueOf(editText5.getText().toString()));
                        json.put("openingHours", Integer.valueOf(edittext31));
                        json.put("glutenFree", Boolean.valueOf(hodnotaFlukoza));
                        json.put("lactoseFree", Boolean.valueOf(hodnotaLactoza));
                        json.put("smoking", Boolean.valueOf(hodnotaSmoking));
                        json.put("wifi", Boolean.valueOf(hodnotaWifi));
                        json.put("picture", Url);
                        json.put("objectId", cities);
                        Log.i(TAG, "som v httpPOST za deklaraciami  ");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        URL url = new URL(urlStr);
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setRequestMethod("PUT");
                        conn.addRequestProperty("application-ID", "CCB8E7ED-C40B-4D67-FF14-5FD1DC41F500");
                        conn.addRequestProperty("secret-key", "A92106B5-AACE-6ACD-FF2A-9F2F83830600");
                        conn.addRequestProperty("Content-Type", "application/json");
                        conn.addRequestProperty("application-type", "REST");
                        OutputStreamWriter outs = new OutputStreamWriter(conn.getOutputStream());
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
                    } else if (conn.getResponseCode() == 404) {
                        Log.i(TAG, "unknown URL");
                    } else if (conn.getResponseCode() == 200) {
                        Log.i(TAG, "request successfull");
                    } else {
                        Log.i(TAG, (String.valueOf(conn.getResponseCode())));
                        Log.i(TAG, "error http response");
                        Log.i(TAG, "error stream: " + conn.getErrorStream().toString());
                        Log.i(TAG, conn.getResponseMessage().toString());

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

    public void showAlert(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(EditItem.this);
        alertDialog2.setTitle("Invalid data");
        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }
    public void showAlert1(){
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(EditItem.this);
        alertDialog2.setTitle("Uncorrect phone number");
        alertDialog2.setMessage("the correct format is 09xxxxxxxx");
        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }
}
