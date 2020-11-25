package org.xyzer.servertools;

import java.awt.*;
import java.net.URI;

public class ServerTools {

    public static String fileprefix = "NewServer/";

    public static void main(String[] args) {
        if (args.length == 1) {
            String isterminal = args[0];
            if (isterminal.equals("terminal")) {
                new Terminal();
            }else {
                System.out.println(isterminal);
            }
        }else {
            Gui startUI = new Gui();
            startUI.setVisible(true);
        }
    }

    public static void runWeb(String web) {
        Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            URI oURL = new URI(web);
            desktop.browse(oURL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
