package cc.zoyn.mogox;

import cc.zoyn.mogox.bean.LaunchOption;
import cc.zoyn.mogox.util.CommonUtils;
import cc.zoyn.mogox.util.DragUtil;
import cc.zoyn.mogox.util.Java8Detector;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.to2mbn.jmccc.option.JavaEnvironment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventTarget;
import org.w3c.dom.html.HTMLAnchorElement;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main extends Application {

    public static Stage stage;
    private static Stage consoleStage;
    private static String userName = "null";
    private static String javaDirectory = JavaEnvironment.current().getJavaPath().getAbsolutePath();
    private static String minecraftDirectory;
    private static String email = "";
    private static String password = "";
    private static String version = "未找到版本!";
    private static String maxMemory = "1024";
    private static String minMemory = "0";
    public static Stage optionStage;
    // 隐藏Scrollbar的css位置
    private static String HIDE_SCROLLBAR_LOCATION = "hideScrollbar.css";
    private static boolean firstRun = true;
    // http://www.pintvc.com/Launcher/index.php
    private static final String MAIN_URL = "http://www.pintvc.com/Launcher/index.php";
    private static final String STATUS_URL = "http://www.pintvc.com/Launcher/status.php";

    static {
        try {
            HIDE_SCROLLBAR_LOCATION = Main.class.getResource("/hideScrollbar.css").toURI().toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        // 配置文件检查
        checkIsFirstRun();

        // 读取ConsoleStage
        loadConsoleStage();
        System.out.println("啦啦啦");

        Parent root = FXMLLoader.load(getClass().getResource("/ui.fxml"));
        primaryStage.setTitle("mogoX 启动器");
        // 标题栏设置
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, 865, 566);
        // 为了使用css圆角, 所以背景需要透明色
        scene.setFill(Color.TRANSPARENT);
        AnchorPane header = (AnchorPane) scene.lookup("#mainAnchorPane");
//
        // 设置标题栏上的两个开关无选中效果
        Button closeButton = (Button) scene.lookup("#closeButton");
        Button minimizeButton = (Button) scene.lookup("#minimizeButton");
        closeButton.setFocusTraversable(false);
        minimizeButton.setFocusTraversable(false);
//
//        // 版本选择器
//        ChoiceBox<String> choice = (ChoiceBox<String>) scene.lookup("#versionChoice");
//        choice.setItems(FXCollections.observableArrayList(getMinecraftVersions()));
//        choice.setValue(getVersion());
//        choice.setTooltip(new Tooltip("你可以在此设定你的版本~"));
//        // 选中时同时设定version
//        choice.getSelectionModel()
//                .selectedIndexProperty()
//                .addListener(
//                        (observable, oldValue, newValue) -> version = choice.getItems().get(newValue.intValue())
//                );
//

        // 欢迎信息
        if (!getUserName().equals("null")) {
            Label label = (Label) scene.lookup("#welcomeText");
            label.setText("Hi~" + getUserName());
        }

        // 自动填入账号和密码
        JFXTextField accountField = (JFXTextField) scene.lookup("#accountField");
        accountField.setText(getEmail());
        JFXPasswordField passwordField = (JFXPasswordField) scene.lookup("#passwordField");
        passwordField.setText(getPassword());

        // 当输入完后自动设置内存中的值
        accountField.textProperty().addListener((observable, oldValue, newValue) -> Main.setEmail(accountField.getText()));
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> Main.setPassword(passwordField.getText()));

        // WebView设置
        WebView mainWebView = (WebView) scene.lookup("#mainWebView");
        WebView serverStatusWebView = (WebView) scene.lookup("#serverStatusWebView");
        // 读取 + 隐藏滚动条
        if (isFirstRun()) {
            mainWebView.getEngine().load(MAIN_URL);
        } else {
            mainWebView.getEngine().load(MAIN_URL + "?player=" + getUserName());
        }
        WebEngine mainWebViewEngine = mainWebView.getEngine();
        mainWebViewEngine.setUserStyleSheetLocation(HIDE_SCROLLBAR_LOCATION);
        mainWebViewEngine.setJavaScriptEnabled(true);
        // 链接跳转
        mainWebViewEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                if (mainWebViewEngine.getLocation().startsWith(MAIN_URL)) {
                    NodeList nodeList = mainWebViewEngine.getDocument().getElementsByTagName("a");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        EventTarget eventTarget = (EventTarget) node;
                        eventTarget.addEventListener("click", evt -> {
                            EventTarget target = evt.getCurrentTarget();
                            HTMLAnchorElement anchorElement = (HTMLAnchorElement) target;
                            String href = anchorElement.getHref();
                            //handle opening URL outside JavaFX WebView
                            openURLByDefaultBrowser(href);
//                            System.out.println(href);
                            evt.preventDefault();
                        }, false);
                    }
                }
            }
        });

        WebEngine serverStatusWebEngine = serverStatusWebView.getEngine();
        serverStatusWebEngine.setUserStyleSheetLocation(HIDE_SCROLLBAR_LOCATION);
        serverStatusWebEngine.setJavaScriptEnabled(true);
        serverStatusWebView.setContextMenuEnabled(false);
        // 链接跳转
        serverStatusWebEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println(newValue);
            if (newValue == Worker.State.SUCCEEDED) {
                if (serverStatusWebEngine.getLocation().startsWith(STATUS_URL)) {
                    NodeList nodeList = serverStatusWebEngine.getDocument().getElementsByTagName("a");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        EventTarget eventTarget = (EventTarget) node;
                        eventTarget.addEventListener("click", evt -> {
                            EventTarget target = evt.getCurrentTarget();
                            HTMLAnchorElement anchorElement = (HTMLAnchorElement) target;
                            String href = anchorElement.getHref();
                            //handle opening URL outside JavaFX WebView
                            openURLByDefaultBrowser(href);
//                            System.out.println(href);
                            evt.preventDefault();
                        }, false);
                    }
                }
            }
        });
        serverStatusWebEngine.load(STATUS_URL);
        serverStatusWebEngine.setOnAlert(event -> {
            String data = event.getData();
//            System.out.println(data);
            if (data != null) {
                // 开始游戏操作
                if (data.startsWith("[LOGIN]")) {
                    String version = data.replaceAll("\\[LOGIN]", "");
                    String email = accountField.getText();
                    String password = passwordField.getText();
                    String minecraftDirectory = Main.getMinecraftDirectory();
                    if (email.isEmpty() || password.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("信息");
                        alert.setContentText("账号或密码不能为空!");
                        alert.show();
                        return;
                    }
                    String javaPath = Main.getJavaDirectory();
                    if (javaPath == null || javaPath.isEmpty()) {
                        javaPath = JavaEnvironment.current().getJavaPath().getAbsolutePath();
                    }
                    if (!Java8Detector.isJava8(javaPath)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("错误");
                        alert.setHeaderText(null);
                        alert.setContentText("检测到当前选中的Java版本不是1.8, 请进入 [更多设置] 选项进行更改!");
                        alert.show();
                        return;
                    }
                    // 设置使用的版本
                    setVersion(version);
                    Launch.launch(version, email, password, minecraftDirectory, javaPath);
                }
            }
        });

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.jpg")));

        DragUtil.addDragListener(primaryStage, header);
        primaryStage.setOnCloseRequest(event -> {
            try {
                saveTemps();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void checkIsFirstRun() {
        File file = new File(CommonUtils.getClientPath(), "config.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        if (file.exists()) {
            // 设置第一次运行变量
            firstRun = false;
            try {
                StringBuilder builder = new StringBuilder();
                FileInputStream stream = new FileInputStream(file);
                int index;
                byte[] bytes = new byte[1024];
                while ((index = stream.read(bytes)) != -1) {
                    builder.append(new String(bytes, 0, index, Charset.defaultCharset()));
                }
                stream.close();
                // 读取json数据
                String json = builder.toString();
                // 反序列化
                LaunchOption option = gson.fromJson(json, LaunchOption.class);
                // 数据设置
                setJavaDirectory(option.getJavaPath());
                setMinecraftDirectory(option.getMinecraftDirectory());
                setVersion(option.getVersion());
                setEmail(option.getEmail());
                setPassword(option.getPassword());
                setMaxMemory(option.getMaxMemory());
                setMinMemory(option.getMinMemory());
                setUserName(option.getUserName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // config.json不存在时自动获取相关信息
            setJavaDirectory(JavaEnvironment.current().getJavaPath().getAbsolutePath());
            setMinecraftDirectory(CommonUtils.getClientPath() + File.separator + ".minecraft");
            setVersion(getMinecraftVersions().get(0));
            setEmail("");
            setPassword("");
            try {
                saveTemps();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveTemps() throws IOException {
        File file = new File(CommonUtils.getClientPath(), "config.json");
        // 需要保存的json
        JsonObject object = new JsonObject();
        object.addProperty("minecraftDirectory", getMinecraftDirectory());
        object.addProperty("javaPath", getJavaDirectory());
        object.addProperty("email", getEmail());
        object.addProperty("password", getPassword());
        object.addProperty("version", getVersion());
        object.addProperty("maxMemory", getMaxMemory());
        object.addProperty("minMemory", getMinMemory());
        object.addProperty("userName", getUserName());
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter writer = new FileWriter(file);
        writer.write(object.toString());
        writer.close();
    }

    public static List<String> getMinecraftVersions() {
        List<String> versions = new ArrayList<>();
        File file = new File(CommonUtils.getClientPath() + File.separator + ".minecraft" + File.separator + "versions");
        if (file.exists()) {
            versions.addAll(Arrays.asList(Objects.requireNonNull(file.list())));
        }
        if (versions.isEmpty()) {
            versions.add("未找到版本!");
        }
        return versions;
    }

    /**
     * 打开默认浏览器访问页面
     */
    public static void openURLByDefaultBrowser(String url) {
        try {
            URI uri = new URI(url);
            if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                //获取系统默认浏览器打开链接
                Desktop.getDesktop().browse(uri);
                return;
            }
            System.out.println("[ERROR]当前系统无法支持打开浏览器");
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取consoleStage
     */
    private Stage loadConsoleStage() {
        if (consoleStage == null) {
            consoleStage = new Stage();
            try {
                Parent root = FXMLLoader.load(Main.class.getResource("/log.fxml"));
                Scene scene = new Scene(root, 450, 300);
                TextArea console = (TextArea) scene.lookup("#logTextArea");
                // 读取并设置启动日志
                PrintStream ps = new PrintStream(new Console(console));
                System.setOut(ps);
                System.setErr(ps);
                consoleStage.setTitle("启动器日志");
                consoleStage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return consoleStage;
    }

    public static String getJavaDirectory() {
        return javaDirectory;
    }

    public static void setJavaDirectory(String javaDirectory) {
        Main.javaDirectory = javaDirectory;
    }

    public static String getMinecraftDirectory() {
        return minecraftDirectory;
    }

    public static void setMinecraftDirectory(String minecraftDirectory) {
        Main.minecraftDirectory = minecraftDirectory;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Main.email = email;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Main.password = password;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        Main.version = version;
    }

    public static String getMaxMemory() {
        return maxMemory;
    }

    public static void setMaxMemory(String maxMemory) {
        Main.maxMemory = maxMemory;
    }

    public static String getMinMemory() {
        return minMemory;
    }

    public static void setMinMemory(String minMemory) {
        Main.minMemory = minMemory;
    }

    public static boolean isFirstRun() {
        return firstRun;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Main.userName = userName;
    }

    public static Stage getConsoleStage() {
        return consoleStage;
    }

    public class Console extends OutputStream {
        private TextArea console;

        public Console(TextArea console) {
            this.console = console;
        }

        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }

        public void write(int b) {
            appendText(String.valueOf((char) b));
        }
    }
}
