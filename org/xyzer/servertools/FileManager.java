package org.xyzer.servertools;

import java.io.*;
import java.nio.file.Files;

public class FileManager {

    public FileManager(String jar, boolean nogui, String Title) {
        File filecreate = new File(ServerTools.fileprefix);
        File serverjar = new File(jar);
        if (!filecreate.exists()) {
            filecreate.mkdir();
        }
        File filejar = new File(ServerTools.fileprefix + serverjar.getName());
        try {
            Files.copy(serverjar.toPath(), filejar.toPath());
        } catch (IOException e) {
            System.out.println("Error to Copy File..");
            e.printStackTrace();
        }
        File runfile = new File(ServerTools.fileprefix + "run.bat");
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(runfile, true));
            writer.println("@echo off");
            writer.println("Title " + Title);
            if (nogui) {
                writer.println("java -jar " + '"' + serverjar.getName() + '"' + " nogui");
            }else {
                writer.println("java -jar " + '"' + serverjar.getName() + '"');
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error to Create PrintFile!!");
            e.printStackTrace();
        }
    }
}
