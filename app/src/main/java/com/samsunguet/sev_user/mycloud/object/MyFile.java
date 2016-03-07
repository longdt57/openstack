package com.samsunguet.sev_user.mycloud.object;

/**
 * Created by sev_user on 3/7/2016.
 */
public class MyFile {
    String path;
    String name;
    long size;
    String last_modified;

    public MyFile(String name, long size, String last_modified){
        this.name   = name;
        this.size   = size;
        this.last_modified = last_modified;
    }
    public MyFile(String path, String name, long size, String last_modified){
        this.name   = name;
        this.size   = size;
        this.last_modified = last_modified;
        this.path   = path;
    }

    public String toString(){
        return "path: "+path+"\nname: "+name+"\nsize: "+size;
    }
}
