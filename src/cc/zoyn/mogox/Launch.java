package cc.zoyn.mogox;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import org.to2mbn.jmccc.auth.yggdrasil.YggdrasilAuthenticator;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.launch.ProcessListener;
import org.to2mbn.jmccc.option.*;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

public class Launch {


    public static void launch(String version, String email, String password, String minecraftDirectory, String javaPath) {
        Launcher launcher = LauncherBuilder.buildDefault();

        LaunchOption option = null;
        try {
            option = new LaunchOption(version, YggdrasilAuthenticator.password(email, password), new MinecraftDirectory(minecraftDirectory));
            option.setWindowSize(WindowSize.window(854, 480));
            // maytomo.vicp.cc
            option.setServerInfo(new ServerInfo("maytomo.vicp.cc", 25565));
            option.commandlineVariables().put("version_type", "mogo X");
            option.setJavaEnvironment(new JavaEnvironment(new File(javaPath)));
            option.setMaxMemory(Integer.valueOf(Main.getMaxMemory()));
            option.setMinMemory(Integer.valueOf(Main.getMinMemory()));
            System.out.println(option);

            // 启动器最小化
            Main.stage.setIconified(true);
            Main.optionStage.close();

            // 启动
            launcher.launch(option, new ProcessListener() {
                public void onLog(String log) {
                    System.out.println(log);
                }
                public void onErrorLog(String log) {
                    System.err.println(log);
                }

                public void onExit(int code) {
                    System.out.println("游戏进程退出，状态码：" + code);
                }
            });
            // launcher.launch(option);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("启动异常");
            alert.setHeaderText("启动器在启动时出现了异常...请把以下内容发送至论坛进行求助");
            alert.setContentText(e.getMessage());

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("堆栈跟踪:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);

            alert.show();
        }
    }

}
