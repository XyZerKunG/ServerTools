package org.xyzer.servertools;

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

        }
    }
}
