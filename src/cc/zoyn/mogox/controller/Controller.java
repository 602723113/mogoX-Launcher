package cc.zoyn.mogox.controller;

import cc.zoyn.mogox.Launch;
import cc.zoyn.mogox.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
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

    /**
     * 启动器关闭
     */
    @FXML
    protected void closeStage() {
        stage.close();
        System.out.println("启动器关闭");
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
        Launch.launch("1.8.9", email, password, minecraftDirectory, javaPath);
    }

    @FXML
    protected void loadOptionStage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("option.fxml"));
            Stage optionStage = new Stage();
            optionStage.setTitle("mogoX 启动器 | 更多设置");
            optionStage.setScene(new Scene(root, 441, 246));
            optionStage.setMaximized(false);
            optionStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
