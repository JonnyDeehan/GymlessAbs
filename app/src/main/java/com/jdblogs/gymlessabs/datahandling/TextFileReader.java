package com.jdblogs.gymlessabs.datahandling;

import android.content.Context;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

/**
 * Created by jonathandeehan on 12/06/2017.
 */

public class TextFileReader {

    private static final String ERROR_TAG = "Error: ";

    public String readTXTFileFromAssets(String filename, Context context){

        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(
                    context.getAssets().open(filename)));
            String line;
            while((line = reader.readLine())!= null){
                builder.append("\n");
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            Log.d(ERROR_TAG, e.getMessage().toString());
        } catch (Exception e) {
            Log.d(ERROR_TAG, e.getMessage().toString());
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                Log.d(ERROR_TAG, e.getMessage().toString());
            }
        }

        return null;
    }

    public String readXMLFileFromAssets(String filename, Context context){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(
                    context.getAssets().open(filename)));
            String line;
            while((line = reader.readLine())!= null){
                builder.append("\n");
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            Log.d(ERROR_TAG, e.getMessage().toString());
        } catch (Exception e) {
            Log.d(ERROR_TAG, e.getMessage().toString());
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                Log.d(ERROR_TAG, e.getMessage().toString());
            }
        }

        return null;

    }
}
