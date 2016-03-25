package connection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by User on 16/03/2016.
 */
public class Connection {

    public static JSONObject getJson(String url){

        InputStream is = null;
        String result = "";
        JSONObject jsonObject = null;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet conn = new HttpGet();
            conn.setHeader("Content-Type", "text/plain; charset=utf-8");
            conn.setURI(new URI(url));
            HttpResponse response = httpclient.execute(conn);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch(Exception e) {
            e.toString();
            return null;
        }

        // Read response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
        } catch (Exception e) {
            return null;
        }

        try {
            jsonObject = new JSONObject(result);
        } catch(JSONException e) {
            return null;
        }

        return jsonObject;

    }

    public Bitmap downloadImage(String imageHttpAddress) throws IOException {
        URL imageUrl = null;
        imageUrl = new URL(imageHttpAddress);
        HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
        conn.connect();
        return BitmapFactory.decodeStream(conn.getInputStream());

    }


}