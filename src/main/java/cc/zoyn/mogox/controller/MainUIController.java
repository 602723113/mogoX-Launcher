package cc.zoyn.mogox.controller;

import cc.zoyn.mogox.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;

public class MainUIController {

    private static Stage stage = Main.stage;

    public JFXButton registerButton;
    public JFXButton forgePasswordButton;
    public Button closeButton;
    public Button minimizeButton;
    public AnchorPane header;
    public JFXTextField accountField;
    public JFXPasswordField passwordField;
    public WebView mainWebView;
    public WebView serverStatusWebView;

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
}
