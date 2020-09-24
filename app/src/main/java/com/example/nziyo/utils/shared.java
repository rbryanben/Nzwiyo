package com.example.nziyo.utils;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class shared extends Application {

    //Global Variable
    private static final String TAG = "shared";

    /*
       This function will return a list of music paths
     */
    public ArrayList<String> getMusicPaths(){
        ArrayList<String> MUSIC_PATHS = new ArrayList<>();
        ArrayList<String> ROOT_FOLDERS = new ArrayList<>();
        ArrayList<String> SUB_FOLDERS = new ArrayList<>();

        //get root Directory
        File ROOT_Directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        //get root folders
        for (File root_file : ROOT_Directory.listFiles()){
             //add to root folders
            if (root_file.isDirectory()){
                ROOT_FOLDERS.add(root_file.getAbsolutePath());
            }
        }

        try {
            //get all sub directories
            for (String sub_folder : ROOT_FOLDERS) {
                //get subfolder
                File subFolder = new File(sub_folder);
                for (File folder : subFolder.listFiles()) {
                    if (!SUB_FOLDERS.contains(folder.getAbsolutePath()) && folder.isDirectory()) {
                        SUB_FOLDERS.add(folder.getAbsolutePath());
                    }
                }
            }
        }
        catch (Exception ex){
            Log.d(TAG, "getMusicPaths: "+ex.toString());
        }

            //Merge folder
            SUB_FOLDERS.addAll(ROOT_FOLDERS);
        try {
            for (String path : SUB_FOLDERS) {
                File file = new File(path);
                for (File subFiles : file.listFiles()) {
                    if (subFiles.getName().endsWith(".mp3")) {
                        MUSIC_PATHS.add(subFiles.getAbsolutePath());
                    }
                }
            }
        }
        catch (Exception ex){
            Log.d(TAG, "getMusicPaths: "+ex);
        }

        return MUSIC_PATHS ;
    }
}
