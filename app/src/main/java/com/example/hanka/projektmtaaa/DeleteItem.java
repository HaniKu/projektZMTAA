package com.example.hanka.projektmtaaa;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Hanka on 14. 4. 2016.
 */
public class DeleteItem extends AppCompatActivity {
    Socket socket;
    String text = "not connected";
    ArrayList<String> reads = new ArrayList<String>();
    private static final String TAG = "MyActivity";
    private String url = "https://api.backendless.com/v1/data/skuska/";
    private String id = null;
    public DeleteItem(String id) {
        this.id = id;
        zmazItem(id);
    }




    public void zmazItem(String id){
        IO.Options opts = new IO.Options();

        opts.secure = false;
        opts.port = 1341;
        opts.reconnection = true;
        opts.forceNew = true;
        opts.timeout = 5000;

        final JSONObject js = new JSONObject();
        try {
            String url = "/data/testHana/"+id;
            js.put("url", url);
        } catch(Exception e) {
            e.printStackTrace();
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
                socket.emit("delete", js, new Ack() {
                    @Override
                    public void call(Object... args) {

                        try {
                            text = Arrays.toString(args);       //server odpoved
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (!text.equals("")) {
                            reads.add(text);
                            Log.d(TAG,"post response: "+ reads.toString());
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
                    Log.d(TAG,"event response: "+ reads.toString());
                }
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {

                text = Arrays.toString(args);
                if (!text.equals("")) {
                    reads.add(text);
                    Log.d(TAG, "disconnect response: " + reads.toString());
                }
            }

        });
        socket.connect();
    }

}
