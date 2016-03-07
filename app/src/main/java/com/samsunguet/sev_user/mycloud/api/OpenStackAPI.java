package com.samsunguet.sev_user.mycloud.api;

import android.util.Log;
import android.widget.Toast;

import com.samsunguet.sev_user.mycloud.log.MyLog;
import com.samsunguet.sev_user.mycloud.object.Token;
import com.samsunguet.sev_user.mycloud.object.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sev_user on 3/7/2016.
 */
public class OpenStackAPI {
    String url;
    User user;

    public OpenStackAPI(String url, User user){
        this.url    = url;
        this.user   = user;
    }

    public boolean setTokenandStorageurl(){
        StringBuilder result = new StringBuilder();
        String request = "{" +
                "\"auth\": {\"tenantName\": \""+user.getTenant().getName()+"\",       " +
                " \"passwordCredentials\":        " +
                    "{\"username\": \""+user.getUsername()+"\", \"password\": \""+user.getPassword()+"\"}}}";
        MyLog.log(request);

        try{
            HttpURLConnection connection = (HttpURLConnection) new
                    URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(request.getBytes().length);

            MyLog.log("start write request");
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStream out = new
                    BufferedOutputStream(connection.getOutputStream());
            out.write(request.getBytes());
            out.close();

            MyLog.log("start get data");

            int statusCode = connection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                MyLog.log("User unauthorized");
                return false;
            }
            if (statusCode < 200 || statusCode >= 300) {
                MyLog.log("Unexpected status code: " + statusCode);
                String in = connection.getResponseMessage();
                MyLog.log(in);
                return false;
            }
            InputStream in = connection.getInputStream();
            InputStreamReader inread = new InputStreamReader(in);
            BufferedReader bufread = new BufferedReader(inread);
            String line = "";
            while((line=bufread.readLine())!=null) result.append(line);

            bufread.close();;
            inread.close();
            in.close();

            JSONObject jsonObject = new JSONObject(result.toString());
            JSONObject jsonObject1 = jsonObject.getJSONObject("access");
            String token = jsonObject1.getString("token");

            //set token
            user.setToken(new Token(token));

            JSONArray jsonArray = jsonObject1.getJSONArray("serviceCatalog");
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonLineItem = jsonArray.getJSONObject(i);
                String value = jsonLineItem.getString("type");

                if (value.compareTo("object-store") == 0) {
                    JSONArray jarray2 = jsonLineItem.getJSONArray("endpoints");
                    JSONObject object = jarray2.getJSONObject(0);

                    //set storage url
                    user.setStorageUrl(object.getString("publicURL"));
                }

            }


        }catch (Exception e){
            MyLog.log(e.toString());
            return false;
        }

        return true;
    }
    public User getUser(){return user;}
}
