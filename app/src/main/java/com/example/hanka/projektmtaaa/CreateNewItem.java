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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

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
    ProgressDialog pdia;
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

                if(isConnected()) {
                    new POSTAsyncTask().execute(URL);
                }else{
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(CreateNewItem.this);
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
//// TODO: 14. 4. 2016 osetrit vstupy, ze clovek nemoze zadat nejaku blbost.. spravit to blbovzdorne, to iste plati aj pri edit a cakacky

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

        protected void onPreExecute(){
            super.onPreExecute();
            pdia = new ProgressDialog(CreateNewItem.this, R.style.AppTheme_NoActionBar);
            pdia.setMessage("Creating...");
            pdia.show();

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("ahoj")){
                showAlert();
            }
            if (result.equals("bad number")){
                showAlert1();
            }
            else{
           // Toast.makeText(getBaseContext(), "Sent!", Toast.LENGTH_LONG).show();
            Intent a = new Intent(CreateNewItem.this, Display.class);
            pdia.dismiss();
            startActivity(a);
            }
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
        RadioButton radioSexButtonSmoking = (RadioButton) findViewById(selectedId1);
        RadioButton radioSexButtonlactoza = (RadioButton) findViewById(selectedId2);
        RadioButton radioSexButtonglukoza = (RadioButton) findViewById(selectedId3);

        final Spinner spin = (Spinner) findViewById(R.id.spin);
        final Integer edittext11;
        final Integer edittext31;
        EditText editText4 = (EditText) findViewById(R.id.editText4);
        EditText editText5 = (EditText) findViewById(R.id.editText5);
        Spinner editText = (Spinner) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editName);
        String wifiBool = (String) radioSexButton.getText();
        Boolean hodnotaWifi;
        switch (wifiBool) {
            case "Yes":
                hodnotaWifi = true; // they are executed if variable == c1
                break;
            default:
                hodnotaWifi = false; // they are executed if none of the above case is satisfied
                break;
        }
        String smokeBool = (String) radioSexButtonSmoking.getText();
        Boolean hodnotaSmoke;
        switch (smokeBool) {
            case "Yes":
                hodnotaSmoke = true; // they are executed if variable == c1
                break;
            default:
                hodnotaSmoke = false; // they are executed if none of the above case is satisfied
                break;
        }
        String lactoseBool = (String) radioSexButtonlactoza.getText();
        Boolean hodnotaLactose;
        switch (lactoseBool) {
            case "Yes":
                hodnotaLactose = true; // they are executed if variable == c1
                break;
            default:
                hodnotaLactose = false; // they are executed if none of the above case is satisfied
                break;
        }
        String glukozaBool = (String) radioSexButtonglukoza.getText();
        Boolean hodnotaGlukoza;
        switch (glukozaBool) {
            case "Yes":
                hodnotaGlukoza = true; // they are executed if variable == c1
                break;
            default:
                hodnotaGlukoza = false; // they are executed if none of the above case is satisfied
                break;
        }
        String edittext3 = spin.getSelectedItem().toString();
        switch (edittext3) {
            case "Bakery cafe":
                edittext11 = 1; // they are executed if variable == c1
                Log.i(TAG, "Bakery cafe" + edittext3);
                break;
            case "Cafe":
                edittext11 = 2; // they are executed if variable == c2
                Log.i(TAG, "Cafe" + edittext3);
                break;
            default:
                edittext11 = 3; // they are executed if none of the above case is satisfied
                Log.i(TAG, "zvysok" + edittext3);
                break;
        }
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
// TODO: 16. 4. 2016 asi bude zly format na cislo.. no asi urcite  
        String isPhone = "^[0-9]{4}\\-?[0-9]{3}\\-?[0-9]{3}$";
        if (editText5.getText().toString().matches(isPhone)){
            Log.i(TAG,"is phone numer OK");
            return "bad number";
        }

        if (editText5.getText().toString().equals("") || editText4.getText().toString().equals("") || editText2.getText().toString().equals("") ||  radioSexGroup.getCheckedRadioButtonId()== -1 ||radioSexGroupSmoking.getCheckedRadioButtonId()== -1 || radioSexGrouplactoza.getCheckedRadioButtonId()== -1 || radioSexGroupglukoza.getCheckedRadioButtonId()== -1 ) {
            Log.i(TAG, "jedno je null");
            return "ahoj";
        }
        else {


        try {
            json.put("category", Integer.valueOf(edittext11));
            json.put("name", String.valueOf(editText2.getText().toString()));
            json.put("adress", String.valueOf(editText4.getText().toString()));
            json.put("phoneNumber", String.valueOf(editText5.getText().toString()));
            json.put("openingHours", edittext31);
            json.put("glutenFree", hodnotaGlukoza);
            json.put("lactoseFree", hodnotaLactose);
            json.put("smoking", hodnotaSmoke);
            json.put("wifi", hodnotaWifi);
            json.put("picture", "http://temp.zocalo.com.mx/galerias/bc5418699b2beba/fotos/bc5418699b2e31e_Captura-de-pantalla-2014-09-16-a-las-10.31.06.jpg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
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
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(CreateNewItem.this);
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
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(CreateNewItem.this);
        alertDialog2.setTitle("Uncorrect phone number");
        alertDialog2.setMessage("the correct format is 09xx xxx xxx");
        alertDialog2.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }
}
