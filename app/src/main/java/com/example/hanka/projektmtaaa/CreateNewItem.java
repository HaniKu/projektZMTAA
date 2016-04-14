package com.example.hanka.projektmtaaa;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

public class CreateNewItem extends AppCompatActivity {
    private Button save;
    private static final String TAG = "MyActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_item);
        save = (Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0) {
                String URL = "https://api.backendless.com/v1/data/skuska";
                new POSTAsyncTask().execute(URL);
            }
        });
    }
//// TODO: 14. 4. 2016 osetrit vstupy, ze clovek nemoze zadat nejaku blbost.. spravit to blbovzdorne, to iste plati aj pri edit
//// TODO: 14. 4. 2016 spravit zvacsenie obrazku 
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
            Intent a = new Intent(CreateNewItem.this, Display.class);
            startActivity(a);
        }

    }


    public String httpPOST(String urlStr) throws IOException {
        JSONObject json = new JSONObject();
        HttpURLConnection conn = null;
        RadioGroup radioSexGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioGroup radioSexGroupSmoking = (RadioGroup) findViewById(R.id.radioGroup2);
        RadioGroup radioSexGrouplactoza = (RadioGroup) findViewById(R.id.radioGroup3);
        RadioGroup radioSexGroupglukoza = (RadioGroup) findViewById(R.id.radioGroup4);
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        int selectedId1 = radioSexGroupSmoking.getCheckedRadioButtonId();
        int selectedId2 = radioSexGrouplactoza.getCheckedRadioButtonId();
        int selectedId3 = radioSexGroupglukoza.getCheckedRadioButtonId();
        RadioButton radioSexButton = (RadioButton) findViewById(selectedId);
        RadioButton radioSexButtonSmoking =  (RadioButton) findViewById(selectedId1);
        RadioButton radioSexButtonlactoza =  (RadioButton) findViewById(selectedId2);
        RadioButton radioSexButtonglukoza =  (RadioButton) findViewById(selectedId3);

        EditText editText3 = (EditText) findViewById(R.id.editText3);
        EditText editText4 = (EditText) findViewById(R.id.editText4);
        EditText editText5 = (EditText) findViewById(R.id.editText5);
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editName);
        String wifiBool = (String) radioSexButton.getText();
        String smokeBool = (String) radioSexButtonSmoking.getText();
        String lactoseBool = (String) radioSexButtonlactoza.getText();
        String glukozaBool = (String) radioSexButtonglukoza.getText();



        try {
            json.put("category", Integer.valueOf(editText3.getText().toString()));
            json.put("name", String.valueOf(editText2.getText().toString()));
            json.put("adress", String.valueOf(editText4.getText().toString()));
            json.put("phoneNumber", String.valueOf(editText5.getText().toString()));
            json.put("openingHours", String.valueOf(editText.getText().toString()));
            json.put("glutenFree", Boolean.valueOf(glukozaBool));
            json.put("lactoseFree", Boolean.valueOf(lactoseBool));
            json.put("smoking", Boolean.valueOf(smokeBool));
            json.put("wifi", Boolean.valueOf(wifiBool));
            json.put("picture","http://temp.zocalo.com.mx/galerias/bc5418699b2beba/fotos/bc5418699b2e31e_Captura-de-pantalla-2014-09-16-a-las-10.31.06.jpg");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        try{
        URL url = new URL(urlStr);
         conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
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
