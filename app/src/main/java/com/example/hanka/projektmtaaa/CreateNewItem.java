package com.example.hanka.projektmtaaa;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateNewItem extends AppCompatActivity {
Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_item);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener()

        {
            public void onClick(View arg0) {

            }
        });
    }

    //new POSTAsyncTask().execute(URL, jsonstring);

    private class POSTAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            try {
                return httpPOST(urls[0], urls[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Nic";
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Sent!", Toast.LENGTH_LONG).show();
        //    Response.setText(result);
        }

    }


    public String httpPOST(String urlStr, String out) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.addRequestProperty("application-ID", "D4B8ADB1-7A2E-8F1D-FF06-57B7982C3B00");
        conn.addRequestProperty("secret-key", "8FC25AC0-1ADB-9D56-FFD4-174BEABFA200");
        conn.addRequestProperty("Content-Type", "application/json");
        conn.addRequestProperty("application-type", "REST");
        OutputStream outs  = conn.getOutputStream();
        
//// TODO: 13. 4. 2016 kuknut writer
//        writer.close();
        outs.close();

        if (conn.getResponseCode() != 200) {
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
