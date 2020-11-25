package org.xyzer.servertools;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Terminal {

    public Terminal() {
        while (true) {
            Scanner obj = new Scanner(System.in);
            System.out.print("> ");
            String cmd = obj.nextLine();
            if (cmd.equals("exit")) {
                System.exit(0);
            }else {
                checkcmd(cmd);
            }
        }
    }

    private void checkcmd(String cmd) {
        if (cmd.startsWith("create")) {
            Scanner obj = new Scanner(System.in);
            System.out.print("Enter Server File: ");
            String file = obj.nextLine();
            System.out.print("Title Name: ");
            String titlename = obj.nextLine();
            System.out.print("Nogui (y/n): ");
            String yorn = obj.nextLine();
            boolean noguibool;
            if (yorn.equals("y")) {
                noguibool = true;
            } else if (yorn.equals("n")) {
                noguibool = false;
            } else {
                noguibool = true;
                System.out.println("User Default!");
            }
            if (!file.contains(".jar")) {
                System.out.println(file + " | Not a file Minecraft Server");
                return;
            }else {
                System.out.println("Create File..");
                if (noguibool) {
                    new FileManager(file, true, titlename);
                }else {
                    new FileManager(file, false, titlename);
                }
            }
        }else if (cmd.startsWith("setname")) {
            String[] name = cmd.split(" ");
            if (name.length == 2) {
                System.out.println("SetFolderName..");
                ServerTools.fileprefix = name[1] + "/";
            }else {
                System.out.println("Unknown Commands!");
                System.out.println("Please Type: help");
            }
        }else if(cmd.startsWith("download")) {
            String[] want = cmd.split(" ");
            if (want.length == 2) {
                String check = want[1];
                switch (check) {
                    case "bukkit":
                        runWeb("https://getbukkit.org");
                        System.out.println("Run Web..");
                        break;
                    case "spigot":
                        runWeb("https://getbukkit.org/download/spigot");
                        System.out.println("Run Web..");
                        break;
                    case "paper":
                        runWeb("https://papermc.io/downloads");
                        System.out.println("Run Web..");
                        break;
                    case "vanilla":
                        runWeb("https://mcversions.net/");
                        System.out.println("Run Web..");
                        break;
                    default:
                        System.out.println("Unknown Commands!");
                        System.out.println("Please Type: help");
                        break;
                }
            }else {
                System.out.println("Unknown Commands!");
                System.out.println("Please Type: help");
            }
        }else if (cmd.startsWith("help")) {
            System.out.println("======= Help =======");
            System.out.println("create | to Create Server");
            System.out.println("setname <Name> | to SetFolderName");
            System.out.println("download ( paper | spigot | bukkit | vanilla ) | to Download Server");
        }else {
            System.out.println("Unknown Commands!");
            System.out.println("Please Type: help");
        }
    }

    private void runWeb(String web) {
        Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            URI oURL = new URI(web);
            desktop.browse(oURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
