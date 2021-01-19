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
            System.out.println("Error to Copy File or You already have file!");
        }
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("windows")) {
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
                System.out.println("Error to Create Run File!!");
                e.printStackTrace();
            }
        }else {
            File runfile = new File(ServerTools.fileprefix + "run.sh");
            try {
                PrintWriter writer = new PrintWriter(new FileWriter(runfile, true));
                writer.println("set echo off");
                if (nogui) {
                    writer.println("java -jar " + '"' + serverjar.getName() + '"' + " nogui");
                }else {
                    writer.println("java -jar " + '"' + serverjar.getName() + '"');
                }
                writer.close();
            } catch (IOException e) {
                System.out.println("Error to Create Run File!!");
                e.printStackTrace();
            }
        }
        if (ServerTools.eula) {
            File eulafile = new File(ServerTools.fileprefix + "eula.txt");
            try {
                PrintWriter writer = new PrintWriter(new FileWriter(eulafile, true));
                writer.println("#By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).");
                writer.println("eula=true");
                writer.close();
            } catch (IOException e) {
                System.out.println("Error to Create Eula File!!");
                e.printStackTrace();
            }
        }
        System.out.println("Setup Done!");
    }
}
