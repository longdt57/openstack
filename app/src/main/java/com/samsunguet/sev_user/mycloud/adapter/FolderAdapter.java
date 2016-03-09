package com.samsunguet.sev_user.mycloud.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.samsunguet.sev_user.mycloud.object.MyFolder;

import java.util.ArrayList;

/**
 * Created by sev_user on 3/9/2016.
 */
public class FolderAdapter extends ArrayAdapter<MyFolder> {
    Activity context=null;
    ArrayList<MyFolder> myArray=null;
    int layoutId;

    public FolderAdapter(Activity context,
                          int layoutId,
                          ArrayList<MyFolder>arr) {
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = arr;
    }

    public View getView(int position, View convertView,
                       ViewGroup parent) {

        LayoutInflater inflater =
                context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);

        if (myArray.size() > 0 && position >= 0) {


            return convertView;

        }
        return null;
    }
}
