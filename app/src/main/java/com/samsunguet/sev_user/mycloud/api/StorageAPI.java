package com.samsunguet.sev_user.mycloud.api;

import android.renderscript.ScriptGroup;


import com.samsunguet.sev_user.mycloud.log.MyLog;
import com.samsunguet.sev_user.mycloud.object.MyFolder;
import com.samsunguet.sev_user.mycloud.object.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Created by sev_user on 3/7/2016.
 */
public class StorageAPI {
    User user;

    public StorageAPI(User user){
        this.user = user;
    }

    public ArrayList<MyFolder> getFolder(){
        ArrayList<MyFolder> folders = new ArrayList<MyFolder>();
        StringBuilder result = new StringBuilder();

        try {
            MyLog.log("start getfolder connection");
            HttpURLConnection connection = (HttpURLConnection)
                    new URL(user.getStorageUrl()+"?format=json").openConnection();
            connection.setRequestProperty("X-Auth-Token", user.getToken().getId());

            MyLog.log(user.getStorageUrl() + "?format=json");
            MyLog.log(user.getToken().getId());

            int statusCode = connection.getResponseCode();
            if(statusCode != 200){
                MyLog.log(statusCode+connection.getResponseMessage());
                return null;
            }
            InputStream in = connection.getInputStream();
            InputStreamReader inread = new InputStreamReader(in);
            BufferedReader bufread = new BufferedReader(inread);
            String line = "";
            while((line=bufread.readLine())!=null) result.append(line);

            bufread.close();;
            inread.close();
            in.close();
            MyLog.log(result.toString());

            JSONArray jsonArray = new JSONArray(result.toString());
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                long size   = jsonObject.getLong("bytes");
                MyLog.log(name+size);
                folders.add(new MyFolder(user.getStorageUrl(),name, size));
            }

        } catch (IOException e) {
            MyLog.log(e.toString());
        } catch (JSONException e) {
            MyLog.log(e.toString());
        }

        return folders;
    }

    public MyFolder getFolderandFileList(String path){
        String sarr[] = path.split("/");
        MyFolder myFolder = new MyFolder("", sarr[sarr.length-1],0,"");
        StringBuilder result = new StringBuilder();
        try{
            MyLog.log("start get folder and file list");
            HttpURLConnection connection = (HttpURLConnection)
                    new URL(user.getStorageUrl()+path+"?format=json").openConnection();
            MyLog.log(user.getStorageUrl()+path+"?format=json");
            connection.setRequestProperty("X-Auth-Token", user.getToken().getId());


            int statusCode = connection.getResponseCode();
            if(statusCode != 200){
                MyLog.log(statusCode+connection.getResponseMessage());
                return null;
            }
            InputStream in = connection.getInputStream();
            InputStreamReader inread = new InputStreamReader(in);
            BufferedReader bufread = new BufferedReader(inread);
            String line = "";
            while((line=bufread.readLine())!=null) result.append(line);

            bufread.close();;
            inread.close();
            in.close();
            MyLog.log(result.toString());

//            ArrayList<MyFolder> folders = new ArrayList<MyFolder>();
//            ArrayList<MyFile> files     = new ArrayList<MyFile>();
            JSONArray jsonArray = new JSONArray(result.toString());
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                long size   = jsonObject.getLong("bytes");
                String last_modified = jsonObject.getString("last_modified");

//                if(name.charAt(name.length()-1)=='/'){
//                    MyFolder tmp = new MyFolder(path, name, size, last_modified);
//                    folders.add(tmp);
//                    MyLog.log(tmp.toString());
//                }else{
//                    MyFile file = new MyFile(path, name, size, last_modified);
//                    files.add(file);
//                    MyLog.log(file.toString());
//                }
                myFolder.addFolderorFile(name, size, last_modified);

            }
            MyLog.log(myFolder.showAll());

//            myFolder.setFolders(folders);
//            myFolder.setFiles(files);


        }catch (Exception e){
            MyLog.log(e.toString());
        }

        return myFolder;
    }

    public boolean downloadFile(String sourcepath, String filename, String sourcedes){
        try{
            HttpURLConnection connection = (HttpURLConnection)
                    new URL(user.getStorageUrl()+sourcepath+"/"+filename).openConnection();
            connection.setRequestProperty("X-Auth-Token", user.getToken().getId());
            MyLog.log(user.getStorageUrl()+sourcepath+"/"+filename);

            int statusCode = connection.getResponseCode();
            if(statusCode != 200){
                MyLog.log(statusCode+connection.getResponseMessage());
                return false;
            }
            InputStream in = connection.getInputStream();
            BufferedInputStream bufin = new BufferedInputStream(in, 1024*5);

            FileOutputStream fout = new FileOutputStream(new File(sourcedes+"/"+filename));
            byte[] buff = new byte[1024*5];

            int len;
            int total = 0;
            int lastUpdated = 0;
            int percent = 0;
            int fileSize = connection.getContentLength();
            MyLog.log(fileSize+"");

            while((len=bufin.read(buff))!=-1){
                total+=len;
                percent = (int) (total * 100 / fileSize);
                fout.write(buff, 0, len);
                String str = new String(buff, "UTF-8");
                MyLog.log(len+str);

            }

            bufin.close();
            in.close();

            fout.close();

        }catch (Exception e){
            MyLog.log(e.toString());
            return false;
        }
        return true;
    }

    public boolean uploadFile(String sourcepath, String despath) throws MalformedURLException {
        String[] filenames = sourcepath.split("/");
        String filename = filenames[filenames.length-1];

        try {
            File file = new File((sourcepath));
            MyLog.log(sourcepath);
            FileInputStream fin = new FileInputStream(new File(sourcepath));
            BufferedInputStream bufread = new BufferedInputStream(fin);

            long size = file.length();


            HttpURLConnection connection = (HttpURLConnection) new URL(user.getStorageUrl()+despath+"/"+filename).openConnection();
            MyLog.log(user.getStorageUrl()+despath+"/"+filename);
            connection.setRequestProperty("X-Auth-Token", user.getToken().getId());
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");
            connection.setFixedLengthStreamingMode((int) size);
            MyLog.log((int) size+"");
            OutputStream out = new BufferedOutputStream(connection.getOutputStream());

            byte[] buff = new byte[1024*5];

            int len;
            int total = 0;
            int lastUpdated = 0;
            int percent = 0;
            while((len=bufread.read(buff))!=-1){
                total+= len;
                MyLog.log(len+"");
                out.write(buff, 0, len);
                String str = new String(buff, "UTF-8");
                MyLog.log(str);
            }

            bufread.close();;
            fin.close();
            out.close();

            int statusCode = connection.getResponseCode();
            MyLog.log(statusCode + connection.getResponseMessage());
            if(statusCode != 201){
                return false;
            }
        } catch (IOException e) {
            MyLog.log(e.toString());
            return false;
        }

        return true;
    }
    public boolean createFolder(String path){
        if(path.charAt(path.length()-1)!='/') path+="/";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(user.getStorageUrl()+path).openConnection();
            MyLog.log(user.getStorageUrl() + path);
            connection.setRequestProperty("X-Auth-Token", user.getToken().getId());
            MyLog.log(user.getToken().getId());
            connection.setDoOutput(true);
            connection.setRequestMethod("PUT");

            int statuscode = connection.getResponseCode();
            MyLog.log(statuscode+ connection.getResponseMessage());
        } catch (IOException e) {
            MyLog.log(e.toString());
        }
        return true;
    }

    public boolean deleteFileorEmptyfolder(String path){

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(user.getStorageUrl()+path).openConnection();
            MyLog.log(user.getStorageUrl() + path);
            connection.setRequestProperty("X-Auth-Token", user.getToken().getId());
            MyLog.log(user.getToken().getId());
            connection.setRequestMethod("DELETE");

            int statuscode = connection.getResponseCode();
            MyLog.log(statuscode+ connection.getResponseMessage());
        } catch (IOException e) {
            MyLog.log(e.toString());
        }
        return true;
    }


}
