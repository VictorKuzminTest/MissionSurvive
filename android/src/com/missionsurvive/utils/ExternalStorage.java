package com.missionsurvive.utils;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ExternalStorage {

    public static String loadStringFromExternalFile(String directory, String fileName){
        if(isExternalStorageAvailable()){
            if(isExternalDirectoryExists(directory)){
                try{
                    InputStream inputStream = readFile(Environment.getExternalStorageDirectory().
                            getAbsolutePath() + File.separator + directory + "/" + fileName);
                    return getStringFromFile(inputStream);
                }
                catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static boolean isExternalStorageAvailable(){
        String state = Environment.getExternalStorageState();
        if(state.equals((Environment.MEDIA_MOUNTED))){
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean isExternalDirectoryExists(String directory){
        File externalDir = new File(Environment.getExternalStorageDirectory(), directory);
        if(externalDir.exists()){
            return true;
        }
        else{
            return false;
        }
    }

    public static InputStream readFile(String fullPath) throws IOException {
        return new FileInputStream(fullPath);
    }

    public static String getStringFromFile(InputStream inputStream){
        try{
            return loadTextFile(inputStream);
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets array list of filename strings inside app's dir.
     * @return
     */
    public static ArrayList<String> getListFiles(String directory){
        if(isExternalStorageAvailable()){
            if(isExternalDirectoryExists(directory)){
                File externalDir = new File(Environment.getExternalStorageDirectory(), directory);
                File[] listFiles = externalDir.listFiles();
                if(listFiles != null){ //...if there are any files inside dir...
                    ArrayList<String> fileNames = new ArrayList<String>(); //...create new array list...
                    for(int fileNum = 0; fileNum < listFiles.length; fileNum++){ //...put file names into this array list.
                        fileNames.add(listFiles[fileNum].getName());
                    }
                    return fileNames;
                }
            }
        }
        return null;
    }

    public static String loadTextFile(InputStream inputStream) throws IOException{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        int len = 0;
        while((len = inputStream.read(bytes)) > 0)
            byteArrayOutputStream.write(bytes, 0, len);
        return new String(byteArrayOutputStream.toByteArray(), "UTF8");
    }

    public static File createDir(String dirName){
        File externalDir = new File(Environment.getExternalStorageDirectory(), dirName);
        externalDir.mkdir();
        return externalDir;
    }

    private static void writeTextFile(File file, String text) throws IOException{
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        bufferedWriter.write(text);
        bufferedWriter.close();
    }

    /**
     * If MEDIA_MOUNTED: checks if app's folder exists (if not - creates it);
     * writes file into this folder.
     * @param directory
     * @param fileName
     * @param text
     */
    public static void writeFileIntoExternalStorage(String directory, String fileName, String text){
        if(isExternalStorageAvailable()){
            File externalDir = new File(Environment.getExternalStorageDirectory(), directory);
            if(!isExternalDirectoryExists(directory)){
                externalDir = createDir(directory);
            }

            File textFile = new File(externalDir.getAbsolutePath() + File.separator + fileName);
            try{
                writeTextFile(textFile, text);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}
