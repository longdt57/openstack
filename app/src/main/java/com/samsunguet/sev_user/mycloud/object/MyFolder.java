package com.samsunguet.sev_user.mycloud.object;

import java.util.ArrayList;

/**
 * Created by sev_user on 3/7/2016.
 */
public class MyFolder {
    String path;
    String name;
    long size;
    String last_modified;

    ArrayList<MyFolder> folders;
    ArrayList<MyFile> files;

    public MyFolder(){}
    public MyFolder(String name, long size){
        this.name = name;
        this.size = size;
    }
    public MyFolder(String path, String name, long size, String last_modified){
        this.path = path;
        this.name = name;
        this.size = size;
        this.last_modified = last_modified;
    }
    public MyFolder(String path, String name, long size){
        this.path = path;
        this.name = name;
        this.size = size;
    }

    public void setFolders(ArrayList<MyFolder> folders){
        this.folders = folders;
    }
    public void setFiles(ArrayList<MyFile> files){this.files = files;}

    public String toString(){
        return "path: "+path +"\nname: "+name+"\nsize: "+size;
    }

}
