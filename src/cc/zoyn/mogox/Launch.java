package cc.zoyn.mogox;

import cc.zoyn.mogox.util.LogUtils;
import org.apache.commons.logging.Log;
import org.to2mbn.jmccc.auth.yggdrasil.YggdrasilAuthenticator;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.launch.ProcessListener;
import org.to2mbn.jmccc.option.*;

import java.io.File;

public class Launch {

    private static LogUtils logInstance = LogUtils.getInstance(Launch.class);

    public static void launch(String version, String email, String password, String minecraftDirectory, String javaPath) {
        Launcher launcher = LauncherBuilder.buildDefault();
        String appDataFolder = System.getenv().get("APPDATA");

        LaunchOption option = null;
        try {
            option = new LaunchOption(version, YggdrasilAuthenticator.password(email, password), new MinecraftDirectory(minecraftDirectory));
            option.setWindowSize(WindowSize.window(854, 480));
            // maytomo.vicp.cc
            option.setServerInfo(new ServerInfo("maytomo.vicp.cc", 25565));
            option.commandlineVariables().put("version_type", "MogoX");
            option.setJavaEnvironment(new JavaEnvironment(new File(javaPath)));
            // 启动
            launcher.launch(option, new ProcessListener() {
                public void onLog(String log) {
                    logInstance.info(log);
                }

                public void onErrorLog(String log) {
                    logInstance.error(log);
                }

                public void onExit(int code) {
                    logInstance.info("游戏进程退出，状态码：" + code);
                }
            });
            // launcher.launch(option);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
