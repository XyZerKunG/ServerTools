package org.xyzer.servertools;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.Desktop;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static org.xyzer.servertools.DataStatic.*;

public class ServerTools extends Application implements Initializable {

    public static String fileprefix = "NewServer/";
    public static boolean eula = false;

    public double x, y;
    public Parent root;
    public Scene main;

    public Parent page01;
    public Scene setup;

    public ChoiceBox<String> paperlist;
    public ChoiceBox<String> spigotlist;
    public ChoiceBox<String> snapshotlist;
    public ChoiceBox<String> mojanglist;
    public Button nextpage;
    public Label status;
    public TextField filename;
    public TextField consolename;
    public TextField jvm;
    public CheckBox nogui;
    public CheckBox meula;
    public Button setupbutton;
    public CheckBox autorun;
    public Label downloadtext;
    public Button back;
    public Button runbutton;

    public static void main(String[] args) throws Exception{
        if (args.length == 1) {
            String isterminal = args[0];
            if (isterminal.equals("terminal")) {
                new Terminal();
            }else {
                System.out.println(isterminal);
            }
        }else {
            launch(args);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        page = 0;
        root = FXMLLoader.load(getClass().getResource("ui/main.fxml"));
        main = new Scene(root);
        stage.setScene(main);
        stage.setTitle("Server Tool");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("img/logo.png")));
        stage.initStyle(StageStyle.UNDECORATED);

        // Event
        root.setOnMousePressed(e -> {
            x = e.getSceneX();
            y = e.getSceneY();
        });
        root.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - x);
            stage.setY(e.getScreenY() - y);
        });
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (location.toString().endsWith("main.fxml")) {
            System.out.println("Load API..");
            ArrayList<String> datalist = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://launchermeta.mojang.com/mc/game/version_manifest.json").openStream()));
                StringBuilder data = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
                reader.close();
                JSONObject object = new JSONObject(data.toString());
                JSONArray version = object.getJSONArray("versions");
                String latestrelease = "";
                String latestsnapshot = "";
                for (int i = 0; i < version.length(); i++) {
                    JSONObject gamedata = new JSONObject(version.get(i).toString());
                    ServerType.datalist.put(gamedata.get("id").toString(), gamedata.get("url").toString());
                    String type = gamedata.get("type").toString();
                    if (type.equals("release")) {
                        if (latestrelease.isEmpty()) {
                            mojanglist.setValue(gamedata.get("id").toString());
                            latestrelease = gamedata.get("id").toString();
                        }
                        datalist.add(gamedata.get("id").toString());
                        mojanglist.getItems().add(gamedata.get("id").toString());
                    }else {
                        if (latestsnapshot.isEmpty()) {
                            snapshotlist.setValue(gamedata.get("id").toString());
                            latestsnapshot = gamedata.get("id").toString();
                        }
                        snapshotlist.getItems().add(gamedata.get("id").toString());
                    }
                }
            }catch (Exception e) {
                System.err.println("Error to Read Mojang API!");
            }
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://papermc.io/api/v1/paper/").openStream()));
                StringBuilder data = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
                reader.close();
                JSONObject object = new JSONObject(data.toString());
                JSONArray version = object.getJSONArray("versions");
                for (int i = 0; i < version.length(); i++) {
                    paperlist.getItems().add(version.get(i).toString());
                }
                paperlist.setValue(version.get(0).toString());
            }catch (Exception e) {
                System.err.println("Error to Read Paper API!");
            }
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = builder.parse(new URL("https://hub.spigotmc.org/nexus/content/groups/public/org/spigotmc/spigot-api/maven-metadata.xml").openStream());
                spigotlist.setValue(document.getElementsByTagName("latest").item(0).getTextContent().split("-")[0]);
                NodeList versionlist = document.getElementsByTagName("version");
                for (int i = versionlist.getLength() -1; i > -1; i--) {
                    String s1 = versionlist.item(i).getTextContent();
                    spigotmap.put(s1.split("-")[0], s1);
                    spigotserver01.add(s1.split("-")[0]);
                    spigotserver02.add(s1);
                    spigotlist.getItems().add(s1.split("-")[0]);
                }
            }catch (Exception e) {
                System.err.println("Error to Read Spigot API!");
            }
            System.out.println("Api Load Done!");
        }
        if (isset) {
            if (location.toString().endsWith("main.fxml")) {
                nextpage.setStyle("-fx-background-color: #0B84FF; -fx-background-radius: 20 20 20 20;");
            }
            status.setText(type + " - " + version);
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

    public void min(MouseEvent e) {
        Stage s = (Stage) ((Node)e.getSource()).getScene().getWindow();
        if (s.isFullScreen()) {
            s.setFullScreen(false);
        }
        s.setIconified(true);
    }

    public void close(MouseEvent e) {
        System.exit(0);
    }

    public void info(MouseEvent e) {
        System.out.println("====== Info App ======");
        System.out.println("ServerTools: v1.0.3");
        System.out.println("Design by XyZerKunG");
        System.out.println("======================");
    }

    public void getpaper(MouseEvent e) {
        version = paperlist.getValue();
        type = ServerType.PAPER;
        isset = true;
        status.setText(type + " - " + version);
        nextpage.setStyle("-fx-background-color: #0B84FF; -fx-background-radius: 20 20 20 20;");
    }

    public void getspigot(MouseEvent e) {
        version = spigotlist.getValue();
        type = ServerType.SPIGOT;
        isset = true;
        status.setText(type + " - " + version);
        nextpage.setStyle("-fx-background-color: #0B84FF; -fx-background-radius: 20 20 20 20;");
    }

    public void getmojang(MouseEvent e) {
        version = mojanglist.getValue();
        type = ServerType.VANILLA;
        isset = true;
        status.setText(type + " - " + version);
        nextpage.setStyle("-fx-background-color: #0B84FF; -fx-background-radius: 20 20 20 20;");
    }

    public void getsnapshot(MouseEvent e) {
        version = snapshotlist.getValue();
        type = ServerType.SNAPSHOT;
        isset = true;
        status.setText(type + " - " + version);
        nextpage.setStyle("-fx-background-color: #0B84FF; -fx-background-radius: 20 20 20 20;");
    }

    public void setuppage(MouseEvent e) throws Exception {
        page = 1;
        Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
        if (isset) {
            page01 = FXMLLoader.load(getClass().getResource("ui/setup.fxml"));
            setup = new Scene(page01);
            window.setScene(setup);
        }
    }

    public void tomain(MouseEvent e) throws Exception {
        if (page != 2) {
            page = 0;
            Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("ui/main.fxml"));
            main = new Scene(root);
            window.setScene(main);
        }
    }

    public void setSetup(MouseEvent e) throws Exception {
        String a = filename.getText();
        String b = consolename.getText();
        if (!a.replaceAll(" ", "").equals("") && !b.replaceAll(" ", "").equals("") && !filename.getText().contains("/")) {
            if (!setupbutton.getStyle().contains("-fx-background-color: #0B84FF")) {
                setupbutton.setText("Setting");
                setupbutton.setStyle("-fx-background-color: #0B84FF; -fx-background-radius: 20 20 20 20;");
            }else {
                page = 2;
                fileprefix = filename.getText() + "/";
                eula = meula.isSelected();
                ConsoleName = consolename.getText();
                Nogui = nogui.isSelected();
                AutoRun = autorun.isSelected();
                Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
                window.setScene(new Scene(FXMLLoader.load(getClass().getResource("ui/done.fxml"))));
            }
        }else {
            setupbutton.setText("Check");
            setupbutton.setStyle("-fx-background-color: #acabac; -fx-background-radius: 20 20 20 20;");
        }
    }

    private void downloadfile(URL url) throws Exception {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        double size = (double) http.getContentLengthLong();
        BufferedInputStream in = new BufferedInputStream(http.getInputStream());
        FileOutputStream fileout = new FileOutputStream(type.toLowerCase() + "-" + version + ".jar");
        BufferedOutputStream out = new BufferedOutputStream(fileout, 1024);
        byte[] bytes = new byte[1024];
        double download = 0.00;
        int percent = 0;
        int read = 0;
        System.out.println("Download Server File...");
        int save = 0;
        while ((read = in.read(bytes, 0, 1024)) > 0) {
            out.write(bytes, 0, read);
            download += read;
            percent = (int) ((download*100)/size);
            if ((percent - save) == 5) {
                System.err.println("Download: " + percent + "%");
                downloadtext.setText("Download: " + percent + "%");
                save = percent;
            }
        }
        downloadtext.setText("Download Done!");
        out.close();
        in.close();
        System.out.println("Download Done!");
    }

    public void download(URL url, String filename) throws Exception {
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        double size = (double) http.getContentLengthLong();
        BufferedInputStream in = new BufferedInputStream(http.getInputStream());
        FileOutputStream fileout = new FileOutputStream(filename);
        BufferedOutputStream out = new BufferedOutputStream(fileout, 1024);
        byte[] bytes = new byte[1024];
        double download = 0.00;
        int percent = 0;
        int read = 0;
        int save = 0;
        while ((read = in.read(bytes, 0, 1024)) > 0) {
            out.write(bytes, 0, read);
            download += read;
            percent = (int) ((download*100)/size);
            if ((percent - save) == 5) {
                System.err.println("Download: " + percent + "%");
                save = percent;
            }
        }
        downloadtext.setText("Download " + "Done!");
        out.close();
        in.close();
        System.out.println("Download Done!");
    }

    @FXML
    private void onRun(MouseEvent e) {
        try {
            runbutton.setOpacity(0);
            if (type.equals(ServerType.PAPER)) {
                downloadfile(new URL("https://papermc.io/api/v1/paper/" + version + "/latest/download"));
            }else if (type.equals(ServerType.VANILLA) || type.equals(ServerType.SNAPSHOT)) {
                System.out.println("Request Mojang Data..");
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(ServerType.datalist.get(version)).openStream()));
                    StringBuilder data = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        data.append(line);
                    }
                    reader.close();
                    JSONObject object = new JSONObject(new JSONObject(new JSONObject(data.toString()).get("downloads").toString()).get("server").toString());
                    System.out.println("Obtain Request Mojang Data!");
                    downloadfile(new URL(object.get("url").toString()));
                }catch (Exception ev) {
                    System.err.println("This version not support or error!");
                    return;
                }
            }else if (type.equals(ServerType.SPIGOT)) {
                System.out.println("Check Server Data..");
                BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://cdn.getbukkit.org/spigot/" + type.toLowerCase() + "-" + version + ".jar").openStream()));
                StringBuilder data = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    data.append(line);
                }
                reader.close();
                if (!data.toString().contains("RESTRICTED")) {
                    try {
                        System.out.println("Download Server 01..");
                        download(new URL("https://cdn.getbukkit.org/spigot/" + type.toLowerCase() + "-" + version + ".jar"),type.toLowerCase() + "-" + version + ".jar");
                    }catch (Exception ev) {
                        System.err.println("Error to Download Spigot!");
                        return;
                    }
                }else {
                    try {
                        System.out.println("Download Server 02..");
                        download(new URL("https://cdn.getbukkit.org/spigot/" + type.toLowerCase() + "-" + spigotmap.get(version) + "-latest.jar"),type.toLowerCase() + "-" + version + ".jar");
                    }catch (Exception ev) {
                        System.err.println("Error to Download Spigot!");
                        return;
                    }
                }
            }
            new FileManager(type.toLowerCase() + "-" + version + ".jar", Nogui, ConsoleName);
            if (AutoRun) {
                if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                    Runtime.getRuntime().exec("cmd /c cd \"" + fileprefix.replaceAll("/", "") + "\" && start run.bat");
                }else {
                    System.err.println("AutoRun Not Support " + System.getProperty("os.name"));
                }
            }
            Stage window = (Stage) ((Node)e.getSource()).getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("ui/main.fxml"));
            main = new Scene(root);
            window.setScene(main);
        }catch (Exception ev) {
            ev.printStackTrace();
            runbutton.setOpacity(1);
            System.err.println("Error to Setup File!");
        }
    }

    public void downloadTools() {
        System.out.println("Download Spigot Tools..");
        try {
            download(new URL("https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar"), "spigot-tools.jar");
        }catch (Exception e) {
            System.err.println("Error to Download Spigot Tools!");
        }
    }
}
