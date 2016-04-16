package com.example.hanka.projektmtaaa;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by Hanka on 14. 4. 2016.
 */
public class DeleteItem extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    private String url = "https://api.backendless.com/v1/data/skuska/";
    private String id = null;
    public DeleteItem(String id) {
        this.id = id;
        new DELETEAsyncTask().execute(id);

    }

    private class DELETEAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            try {
                return httpDELETE(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Nic";
        }

        protected void onPreExecute(){

        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        }
    }

    public String httpDELETE(String id) throws IOException {
        String finalUrl = url+id;
        URL url = new URL(finalUrl);
        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("DELETE");
        conn.addRequestProperty("application-ID", "CCB8E7ED-C40B-4D67-FF14-5FD1DC41F500");
        conn.addRequestProperty("secret-key",  "A92106B5-AACE-6ACD-FF2A-9F2F83830600");
        conn.addRequestProperty("application-type", "REST");

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
