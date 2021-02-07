package cn.n3ro.ghostclient;

import cn.n3ro.ghostclient.font.FontLoaders;
import cn.n3ro.ghostclient.management.CommandManager;
import cn.n3ro.ghostclient.management.ConfigurationManager;
import cn.n3ro.ghostclient.management.FileManager;
import cn.n3ro.ghostclient.management.ModuleManager;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.utils.LoggerUtils;
// import com.sun.istack.internal.NotNull;
import cn.n3ro.ghostclient.value.Value;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.util.Iterator;

public class Client {
    // @NotNull
    public static final String CLIENT_NAME = "N3RO";
    // @NotNull
    public static final String CLIENT_VERSION = "Alpha";
    // @NotNull
    public static Client instance;


    private static boolean notificationsAllowed = false;

    public static boolean canCancle = false;

    public static boolean initiated;//check the client init success
    public static boolean runtimeobfuscationEnabled;

    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public FontLoaders fontLoaders;
    public FileManager fileManager;

    public void log(String message) {
        String prefix = "[" + CLIENT_NAME + "] ";
        System.out.println(prefix + message);
    }

    public Client() {
    	this.fileManager = new FileManager();
        FileManager.init();
        instance = this;
        initiated = true;
        try {
            Minecraft.getMinecraft().getClass().getField("func_71410_x");
            runtimeobfuscationEnabled = true;
        } catch (NoSuchFieldException e) {
            runtimeobfuscationEnabled = false;
        }
        this.moduleManager = new ModuleManager();
        this.commandManager = new CommandManager();
        this.fontLoaders = new FontLoaders();
       // ConfigurationManager.instance();
        LoggerUtils.info("ghost client init ");
    }

    public static void notificationsAllowed(boolean value) {
        notificationsAllowed = value;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
