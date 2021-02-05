package cn.n3ro.ghostclient.utils;


import org.apache.logging.log4j.LogManager;

public class LoggerUtils{

    public static void info(String string){
        LogManager.getLogger("n3ro").info(string);
    }

}
