package cc.zoyn.mogox.util;

import cc.zoyn.mogox.Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 通用 工具类
 *
 * @author Zoyn
 */
public class CommonUtils {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static String getClientPath() {
        String filePath = "";
        URL url = Main.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            // 转化为utf-8编码，支持中文
            filePath = URLDecoder.decode(url.getPath(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 可执行jar包运行的结果里包含".jar"
        if (filePath.endsWith(".jar")) {
            // 获取jar包所在目录
            filePath = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        }

        File file = new File(filePath);
        filePath = file.getAbsolutePath();
        return filePath;
    }

}
