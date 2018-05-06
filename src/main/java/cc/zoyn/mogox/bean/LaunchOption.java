package cc.zoyn.mogox.bean;

public class LaunchOption {

    private String minecraftDirectory;
    private String javaPath;
    private String email;
    private String password;
    private String version;
    private String maxMemory;
    private String minMemory;
    private String userName;

    public String getMinecraftDirectory() {
        return minecraftDirectory;
    }

    public void setMinecraftDirectory(String minecraftDirectory) {
        this.minecraftDirectory = minecraftDirectory;
    }

    public String getJavaPath() {
        return javaPath;
    }

    public void setJavaPath(String javaPath) {
        this.javaPath = javaPath;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(String maxMemory) {
        this.maxMemory = maxMemory;
    }

    public String getMinMemory() {
        return minMemory;
    }

    public void setMinMemory(String minMemory) {
        this.minMemory = minMemory;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
