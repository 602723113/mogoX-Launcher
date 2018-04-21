package cc.zoyn.mogox;

import cc.zoyn.mogox.util.CommonUtils;
import cc.zoyn.mogox.util.DragUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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
    private static String email = " ";
    private static String password = " ";
    private static String version = " ";
    public static Stage optionStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        // 配置文件检查
        checkIsFirstRun();

        Parent root = FXMLLoader.load(getClass().getResource("fxml/gui.fxml"));
        primaryStage.setTitle("mogoX 启动器");
        // 标题栏设置
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(root, 638, 400);
        // 为了使用css圆角, 所以背景需要透明色
        scene.setFill(Color.TRANSPARENT);
        AnchorPane title = (AnchorPane) scene.lookup("#mainTitleBar");

        // 设置标题栏上的两个开关无选中效果
        Button closeButton = (Button) scene.lookup("#closeButton");
        Button minimizeButton = (Button) scene.lookup("#minimizeButton");
        closeButton.setFocusTraversable(false);
        minimizeButton.setFocusTraversable(false);

        // 版本选择器
        ChoiceBox<String> choice = (ChoiceBox<String>) scene.lookup("#versionChoice");
        choice.setItems(FXCollections.observableArrayList(getMinecraftVersions()));
        choice.setValue(getVersion());
        choice.setTooltip(new Tooltip("你可以在此设定你的版本~"));
        // 选中时同时设定version
        choice.getSelectionModel()
                .selectedIndexProperty()
                .addListener(
                        (observable, oldValue, newValue) -> version = choice.getItems().get(newValue.intValue())
                );

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("resource/icon.jpg")));

        DragUtil.addDragListener(primaryStage, title);
        primaryStage.setOnCloseRequest(event -> System.out.println("测试..."));
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
                    builder.append(new String(bytes, 0, index, Charset.forName("utf8")));
                }
                stream.close();
                String json = builder.toString();
                System.out.println(json);

                JsonObject object = new JsonObject();
                String a = gson.fromJson(json, String.class);
                System.out.println(a);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // config.json不存在时自动获取相关信息
            setJavaDirectory(JavaEnvironment.current().getJavaPath().getAbsolutePath());
            setMinecraftDirectory(CommonUtils.getClientPath() + File.separator + ".minecraft");
            setVersion(getMinecraftVersions().get(0));
            saveTemps();
        }
    }

    public static void saveTemps() {
        File file = new File(CommonUtils.getClientPath(), "config.json");
        // 需要保存的json
        JsonObject object = new JsonObject();
        object.addProperty("minecraftDirectory", getMinecraftDirectory());
        object.addProperty("javaPath", getJavaDirectory());
        object.addProperty("email", getEmail());
        object.addProperty("password", getPassword());
        object.addProperty("version", getVersion());
        if (file.exists()) {

        } else {
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(object.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

}
