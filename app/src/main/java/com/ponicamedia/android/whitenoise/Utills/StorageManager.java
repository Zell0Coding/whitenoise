package com.ponicamedia.android.whitenoise.Utills;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StorageManager {


    public static void writeToFile(String file, String data, Context context){
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(context.getFilesDir(), file),false);
            FileWriter fileWriter = new FileWriter(outputStream.getFD());
            fileWriter.write(data);
            fileWriter.flush();
            fileWriter.close();
            outputStream.getFD().sync();
            outputStream.close();
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }

    public static void clearFile(String file, Context context){
        try{
            new FileOutputStream(new File(context.getFilesDir(), file),false).close();
        }catch (Exception exp){
            exp.printStackTrace();
        }
    }

    public static String readFromFile(String file, Context context){
        String res = "";
        try{
            InputStream inputStream = context.openFileInput(file);

            if(inputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String tempStringJson = "";
                StringBuilder stringBilder = new StringBuilder();
                while ((tempStringJson = bufferedReader.readLine())!=null){
                    stringBilder.append(tempStringJson);
                }

                inputStream.close();
                res = stringBilder.toString();
                Log.d("res",res);
            }
        }catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            res = null;
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
            res = null;
        }
        return res;
    }



}
