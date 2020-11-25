package org.xyzer.servertools;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

public class Gui extends JFrame{

    private JButton terminal_but;
    private JPanel panel;
    private JPanel panel2;
    private JLabel statustext;
    private JButton getStart;
    private JButton paper;
    private JButton bukkit;
    private JButton vanilla;
    private JButton openFileButton;
    private JPanel panel1;
    private JCheckBox checknogui;
    private JTextField filepath;
    private JTextField nameserver;
    private JTextField filename;
    private JButton setup;

    public Gui() {
        setTitle("XyZer Server Tools");
        setSize(840, 512);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("terminal.png").getImage());
        statustext.setText("Normal");
        add(panel);
        panel1.setVisible(false);

        terminal_but.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File terminal = new File("terminal.bat");
                    PrintWriter writer = new PrintWriter(new FileWriter(terminal, true));
                    writer.println("@echo off");
                    writer.println("Title XyZer Server Tools Terminal");
                    writer.println("java -jar \"XyZer Server Tools.jar\" terminal");
                    writer.close();
                    System.exit(0);
                } catch (IOException ioException) {
                    String Error = "Error to Create Terminal File..";
                    statustext.setText(Error);
                    System.err.println(Error);
                    ioException.printStackTrace();
                }
            }
        });

        paper.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                statustext.setText("Navigation to Paper Server Download");
                ServerTools.runWeb("https://papermc.io/downloads");
            }
        });

        bukkit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                statustext.setText("Navigation to Bukkit and Spigot Server Download");
                ServerTools.runWeb("https://getbukkit.org");
            }
        });

        vanilla.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                statustext.setText("Navigation to Vanilla Server Download");
                ServerTools.runWeb("https://mcversions.net/");
            }
        });

        getStart.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                panel1.setVisible(true);
                panel2.setVisible(false);
            }
        });

        setup.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!(filename.getText().equals("") && String.valueOf(filename.getText()).equals("null"))) {
                    ServerTools.fileprefix = filename.getText() + "/";
                }
                if (checknogui.isSelected()) {
                    new FileManager(filepath.getText(), true, nameserver.getText());
                }else {
                    new FileManager(filepath.getText(), false, nameserver.getText());
                }
            }
        });
    }
}
