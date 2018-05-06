package cc.zoyn.mogox.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

public class MainUIController {

    public JFXButton registerButton;
    public JFXButton forgePasswordButton;
    public Button closeButton;
    public Button minimizeButton;
    public AnchorPane header;
    public JFXTextField accountField;
    public JFXPasswordField passwordField;

    @FXML
    protected void onRegisterButtonMouseEnter() {
        registerButton.setTextFill(Paint.valueOf("#ededed"));
    }

    @FXML
    protected void onRegisterButtonMouseExit() {
        registerButton.setTextFill(Paint.valueOf("#a8a8a8"));
    }

    @FXML
    protected void onForgePasswordButtonMouseEnter() {
        forgePasswordButton.setTextFill(Paint.valueOf("#ededed"));
    }

    @FXML
    protected void onForgePasswordButtonMouseExit() {
        forgePasswordButton.setTextFill(Paint.valueOf("#a8a8a8"));
    }
}
