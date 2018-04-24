package cc.zoyn.mogox.util;

import javafx.scene.control.Alert;

import java.io.*;
import java.util.Objects;

import static cc.zoyn.mogox.util.StreamUtils.readFromStream;

public final class Java8Detector {

    /**
     * 判断一个java路径是否为java8
     *
     * @param javaPath Java路径
     * @return 当版本为java8时返回true
     */
    public static boolean isJava8(String javaPath) {
        boolean validate = false;
        File file = new File(javaPath);
        if (file.exists()) {
            File releaseFile = Objects.requireNonNull(file.getParentFile()
                    .getParentFile()
                    .listFiles((dir, name) -> "release".equals(name)))[0];
            try {
                FileInputStream in = new FileInputStream(releaseFile);
                // JAVA_VERSION="1.8.0_77"
                // JAVA_VERSION="10.0.1"
                // JAVA_VERSION="1.7.0"
                String release = readFromStream(in);
//                System.out.println(release);

                String[] items = release.split("=");
                for (int i = 0; i < items.length; i++) {
                    String s = items[i];
                    if ("JAVA_VERSION".equals(s)) {
                        String javaVersion = items[i + 1].replaceAll("[a-zA-Z_]+", "");
                        if (javaVersion.contains("1.8.0")) {
                            validate = true;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return validate;
    }

}
