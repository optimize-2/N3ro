package cn.n3ro.ghostclient.management;

import cn.n3ro.ghostclient.management.FileAndConfig.KeybindConfiguration;
import cn.n3ro.ghostclient.management.FileAndConfig.ModuleStateConfiguration;
import cn.n3ro.ghostclient.management.FileAndConfig.ModuleValues;

public class ConfigurationManager
{
    private static volatile ConfigurationManager INSTANCE = new ConfigurationManager();

    public ConfigurationManager()
    {
       
        new KeybindConfiguration();
        new ModuleStateConfiguration();
        new ModuleValues();
       
    }

    public static ConfigurationManager instance()
    {
        return INSTANCE;
    }
}
