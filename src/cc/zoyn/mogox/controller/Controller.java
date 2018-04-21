package cc.zoyn.mogox.controller;

import cc.zoyn.mogox.Launch;
import cc.zoyn.mogox.Main;
import cc.zoyn.mogox.util.DragUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.to2mbn.jmccc.option.JavaEnvironment;

import java.io.IOException;

public class Controller {

    private static Stage stage = Main.stage;

    @FXML
    public TextField accountField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button startButton;
    @FXML
    public Label information;
    @FXML
    public AnchorPane mainTitleBar;
    @FXML
    public Button closeButton;
    @FXML
    public Button minimizeButton;
    @FXML
    public ChoiceBox versionChoice;

    /**
     * 启动器关闭
     */
    @FXML
    protected void closeStage() {
        stage.setOnCloseRequest(event -> Main.saveTemps());
        stage.close();
    }

    /**
     * 最小化，任务栏可见图标
     */
    @FXML
    private void minimizeStage() {
        stage.setIconified(true);
    }

    /**
     * 开始游戏被单击
     */
    @FXML
    protected void onStart() {
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
        information.setText("稍等片刻, 马上就好~");
        String javaPath = Main.getJavaDirectory();
        if (javaPath == null || javaPath.isEmpty()) {
            javaPath = JavaEnvironment.current().getJavaPath().getAbsolutePath();
        }
        Launch.launch(Main.getVersion(), email, password, minecraftDirectory, javaPath);
    }

    @FXML
    protected void loadOptionStage() {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("fxml/option.fxml"));
            Stage optionStage = new Stage();
            optionStage.setTitle("mogoX 启动器 | 更多设置");
            optionStage.initStyle(StageStyle.TRANSPARENT);

            Scene scene = new Scene(root, 441, 246);
            scene.setFill(Color.TRANSPARENT);
            // 开启时进行自动配置
            TextField javaDirectory = (TextField) scene.lookup("#javaDirectory");
            javaDirectory.setText(Main.getJavaDirectory());
            TextField minecraftDirectory = (TextField) scene.lookup("#minecraftDirectory");
            minecraftDirectory.setText(Main.getMinecraftDirectory());

            // 标题栏
            AnchorPane anchorPane = (AnchorPane) scene.lookup("#optionTitleBar");
            DragUtil.addDragListener(optionStage, anchorPane);

            optionStage.setScene(scene);
            optionStage.setMaximized(false);
            optionStage.show();
            Main.optionStage = optionStage;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
