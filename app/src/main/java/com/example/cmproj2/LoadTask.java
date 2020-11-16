package com.example.cmproj2;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/*
public class LoadTask extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {

    private Exception e=null;

    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... args) {
        ArrayList<String> result;
        try {
            result=load(args[0]);
        }
        catch (Exception e) {
            result = null;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String text) {

    }

    public ArrayList<String> load(ArrayList<String> titles) {
        ArrayList<String> content = new ArrayList<>();
        for (String title : titles) {
            Scanner scan = null;
            try {
                scan = new Scanner(openFileInput(title + ".txt"));
                String allText = ""; // read entire file
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                    allText += line;
                }
                content.add(allText);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                content.add("");
            }
        }
        return content;
    }
}
*/