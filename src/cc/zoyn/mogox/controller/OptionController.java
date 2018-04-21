package cc.zoyn.mogox.controller;

import cc.zoyn.mogox.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.to2mbn.jmccc.option.JavaEnvironment;

import java.io.File;

public class OptionController {

    private static Stage stage = Main.stage;

    @FXML
    public TextField minecraftDirectory;
    @FXML
    public TextField javaDirectory;
    @FXML
    public Button javaChooser;
    @FXML
    public AnchorPane optionTitleBar;

    /**
     * 综合设置关闭
     */
    @FXML
    protected void closeStage() {
        Main.optionStage.close();
    }

    /**
     * 最小化，任务栏可见图标
     */
    @FXML
    private void minimizeStage() {
        Main.optionStage.setIconified(true);
    }

    @FXML
    public void onChooseJava() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("设置你的Java路径");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("javaw", "javaw.exe"),
                new FileChooser.ExtensionFilter("java", "java.exe"),
                new FileChooser.ExtensionFilter("All", "*.*")
        );

        File file = chooser.showOpenDialog(stage);
        if (file != null) {
            javaDirectory.setText(file.getAbsolutePath());
            Main.setJavaDirectory(file.getAbsolutePath());
        }
    }

    public TextField getMinecraftDirectory() {
        return minecraftDirectory;
    }

    public TextField getJavaDirectory() {
        return javaDirectory;
    }
}
