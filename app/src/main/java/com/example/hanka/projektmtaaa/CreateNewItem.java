package com.example.hanka.projektmtaaa;
//// TODO: 6. 5. 2016 post spraveny 
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class CreateNewItem extends AppCompatActivity {
    private Button save;
    private static final String TAG = "MyActivity";
    Socket socket;
    String text = "not connected";
    ArrayList<String> reads = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_item);
        save = (Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                 if (isConnected()) {
                    spravSocket();
                    Intent a = new Intent(CreateNewItem.this, Display.class);
                    startActivity(a);

                } else {
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

    public boolean isConnected()            //zistujem ci som online
    {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void showAlert() {
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

    public void showAlert1() {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(CreateNewItem.this);
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

    public JSONObject spravJson() {
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
        EditText editText4 = (EditText) findViewById(R.id.editText4);
        EditText editText5 = (EditText) findViewById(R.id.editText5);
        Spinner editText = (Spinner) findViewById(R.id.editText);
        EditText editText2 = (EditText) findViewById(R.id.editName);

        if (editText5.getText().toString().equals("") || editText4.getText().toString().equals("") || editText2.getText().toString().equals("") || radioSexGroup.getCheckedRadioButtonId() == -1 || radioSexGroupSmoking.getCheckedRadioButtonId() == -1 || radioSexGrouplactoza.getCheckedRadioButtonId() == -1 || radioSexGroupglukoza.getCheckedRadioButtonId() == -1) {
            Log.i(TAG, "jedno je null");
            showAlert();
        } else {
            final Spinner spin = (Spinner) findViewById(R.id.spin);
            final Integer edittext11;
            final Integer edittext31;

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

            String isPhone = "^[0-9]{4}\\-?[0-9]{3}\\-?[0-9]{3}$";
            if (!(editText5.getText().toString().matches(isPhone))) {
                Log.i(TAG, "is phone numer OK");
                showAlert1();
            } else {
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
            }
        }
        return json;
    }

    public void spravSocket(){

        IO.Options opts = new IO.Options();

        opts.secure = false;
        opts.port = 1341;
        opts.reconnection = true;
        opts.forceNew = true;
        opts.timeout = 5000;

        final JSONObject js = new JSONObject();
        try {
            js.put("url", "/data/testHana");    //data = tabulka
            js.put("data", new JSONObject().put("data", new JSONObject().put("nove", spravJson()))); //json v jsonoch
            Log.i(TAG, "som v spravSocket");
        } catch(Exception e) {
            e.printStackTrace();
            Log.i(TAG, "som v exception  spravSocket");
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
                socket.emit("post", js, new Ack() {
                    @Override
                    public void call(Object... args) {

                        try {
                            text = Arrays.toString(args);       //server odpoved
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (!text.equals("")) {
                            reads.add(text);
                            Log.d("post response: ", reads.toString());
                            Log.i(TAG, "post response "+reads.toString());
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

