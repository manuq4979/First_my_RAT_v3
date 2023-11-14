package com.test.myapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileLogReadWrite {

    private static final String pathForLogFiles = "/Config/sys/apps/log";
    public static void writeFile(String text) {

        try
        {
            String mydate =android.text.format.DateFormat.format("yyyy-MM-dd", new java.util.Date()).toString();

            File ClassGen12sdDir = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(ClassGen12sdDir, pathForLogFiles);
            File TargetFile = new File(ClassGen12sdDir,pathForLogFiles+"log-"+mydate+".txt");


            if (!dir.exists()) {
                dir.mkdirs();
            }

            if (!TargetFile.exists())
            {
                TargetFile.createNewFile();
            }

            // убрал ClassGen12toBase64(text):
            String ClassGen12FinalText = text+">"+"\r\n";


            File out;
            OutputStreamWriter outStreamWriter = null;
            FileOutputStream outStream = null;

            out = new File(ClassGen12sdDir+pathForLogFiles, "log-"+mydate+".txt");

            if ( out.exists() == false ){
                out.createNewFile();
            }

            outStream = new FileOutputStream(out,true);
            outStreamWriter = new OutputStreamWriter(outStream);

            outStreamWriter.append(ClassGen12FinalText);
            outStreamWriter.flush();
            outStreamWriter.close();
            outStream.close();
            outStream.flush();

        }catch (Exception ee){}
    }

    public static ArrayList<String> getContentFilesLog(String eventType) {
        ArrayList<String> listWithFileContent = new ArrayList<>();

        try {
            File ClassGen12sdDir = android.os.Environment.getExternalStorageDirectory();
            File dir = new File(ClassGen12sdDir+pathForLogFiles);
            File[] arrFiles = dir.listFiles();

            if(arrFiles != null && arrFiles.length != 0) {
                List<File> list = Arrays.asList(arrFiles);
                BufferedReader reader = null;
                for(File fileName : list) {
                    reader = new BufferedReader(new FileReader(fileName));
                    String line = reader.readLine();
                    // на всякий добавляю к тексу имя файла, состоящие из даты его создания:
                    listWithFileContent.add("Date: "+fileName);
                    while(line != null) {
                        line = reader.readLine();
                        listWithFileContent.add(line);
                    }
                }
                return listWithFileContent;
            }

        } catch (Exception e) {}

        listWithFileContent.add("Error: array equals null or array empty");
        return listWithFileContent;
    }
}
