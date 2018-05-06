package cc.zoyn.mogox;

import cc.zoyn.mogox.bean.LaunchOption;
import cc.zoyn.mogox.util.CommonUtils;
import cc.zoyn.mogox.util.DragUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.to2mbn.jmccc.option.JavaEnvironment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main extends Application {

    public static Stage stage;
    private static String javaDirectory = JavaEnvironment.current().getJavaPath().getAbsolutePath();
    private static String minecraftDirectory;
    private static String email = "";
    private static String password = "";
    private static String version = "未找到版本!";
    private static String maxMemory = "1024";
    private static String minMemory = "0";
    public static Stage optionStage;
    private static Font fangZhengZhunYuan;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        // 配置文件检查
        checkIsFirstRun();
        fangZhengZhunYuan = Font.loadFont(getClass().getResourceAsStream("/FangZhengZhunYuanJianTi.tff"), 18);

        Parent root = FXMLLoader.load(getClass().getResource("/ui.fxml"));
        primaryStage.setTitle("mogoX 启动器");
        // 标题栏设置
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, 800, 500);
        // 为了使用css圆角, 所以背景需要透明色
        scene.setFill(Color.TRANSPARENT);
        AnchorPane header = (AnchorPane) scene.lookup("#header");
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
        // 自动填入账号和密码
        JFXTextField accountField = (JFXTextField) scene.lookup("#accountField");
        accountField.setText(getEmail());
        JFXPasswordField passwordField = (JFXPasswordField) scene.lookup("#passwordField");
        passwordField.setText(getPassword());
//
//        // 当输入完后自动设置内存中的值
//        accountField.textProperty().addListener((observable, oldValue, newValue) -> Main.setEmail(accountField.getText()));
//        passwordField.textProperty().addListener((observable, oldValue, newValue) -> Main.setPassword(passwordField.getText()));

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
}
