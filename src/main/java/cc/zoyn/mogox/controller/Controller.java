package cc.zoyn.mogox.controller;

import cc.zoyn.mogox.Launch;
import cc.zoyn.mogox.Main;
import cc.zoyn.mogox.util.DragUtil;
import cc.zoyn.mogox.util.Java8Detector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.to2mbn.jmccc.option.JavaEnvironment;

import java.io.IOException;

public class Controller {

    private static Stage stage = Main.stage;
    private static Stage optionStage;

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
    @FXML
    public Label skinLable;
    @FXML
    public Label forumLabel;

    @FXML
    protected void forumLabelMouseEnter() {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.4, 0.4));

        forumLabel.setEffect(dropShadow);
        forumLabel.setCache(true);
    }

    /**
     * 启动器关闭
     */
    @FXML
    protected void closeStage() {
        try {
            Main.saveTemps();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        information.setText("稍等片刻, 马上就好~");
        Launch.launch(Main.getVersion(), email, password, minecraftDirectory, javaPath);
    }

    @FXML
    protected void loadOptionStage() {
        try {
            if (optionStage == null) {
                Parent root = FXMLLoader.load(Main.class.getResource("fxml/option.fxml"));
                optionStage = new Stage();
                optionStage.setTitle("mogoX 启动器 | 更多设置");
                optionStage.initStyle(StageStyle.TRANSPARENT);

                Scene scene = new Scene(root, 441, 246);
                scene.setFill(Color.TRANSPARENT);

                // 开启时进行自动配置
                TextField javaDirectory = (TextField) scene.lookup("#javaDirectory");
                javaDirectory.setText(Main.getJavaDirectory());
                TextField minecraftDirectory = (TextField) scene.lookup("#minecraftDirectory");
                minecraftDirectory.setText(Main.getMinecraftDirectory());

                // 内存设置监听
                TextField maxMemory = (TextField) scene.lookup("#maxMemory");
                maxMemory.setText(Main.getMaxMemory());
                maxMemory.textProperty().addListener((observable, oldValue, newValue) -> Main.setMaxMemory(maxMemory.getText()));
                TextField minMemory = (TextField) scene.lookup("#minMemory");
                minMemory.setText(Main.getMinMemory());
                minMemory.textProperty().addListener((observable, oldValue, newValue) -> Main.setMinMemory(minMemory.getText()));

                // 标题栏
                AnchorPane anchorPane = (AnchorPane) scene.lookup("#optionTitleBar");
                DragUtil.addDragListener(optionStage, anchorPane);

                optionStage.setScene(scene);
            }
            optionStage.show();
            Main.optionStage = optionStage;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
