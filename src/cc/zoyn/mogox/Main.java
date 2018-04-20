package cc.zoyn.mogox;

import cc.zoyn.mogox.util.DragUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.to2mbn.jmccc.option.JavaEnvironment;

public class Main extends Application {

    public static Stage stage;
    private static String javaDirectory = JavaEnvironment.current().getJavaPath().getAbsolutePath();
    private static String minecraftDirectory;

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
        AnchorPane title = (AnchorPane) scene.lookup("#mainTitleBar");

        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("resource/icon.jpg")));

        DragUtil.addDragListener(primaryStage, title);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void checkIsFirstRun() {

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
}
