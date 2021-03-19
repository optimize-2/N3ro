package cn.n3ro.ghostclient.management.FileAndConfig;

import java.io.*;
import java.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import cn.n3ro.ghostclient.management.ModuleManager;
import cn.n3ro.ghostclient.module.Module;




/**
 * 
 * @author huangbai
 *
 * 按键绑定的配置文件
 */
public class KeybindConfiguration
{
    private static volatile KeybindConfiguration instance = new KeybindConfiguration();
    private File keybindConfig;

    public KeybindConfiguration()
    {
        keybindConfig = new File(Minecraft.getMinecraft().mcDataDir, "/N3RO/Config/KeybindConfig.txt");
        readKeybindConfig();
        write();
    }

    public void writeKeybindConfig()
    {
        try
        {
            FileWriter filewriter = new FileWriter(keybindConfig);
            BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
            Module module;
            String s;
            for (Iterator iterator = ModuleManager.modList.iterator(); iterator.hasNext(); bufferedwriter.write((new StringBuilder(String.valueOf(module.getName().toLowerCase().replaceAll(" ", "")))).append(":").append(s).append("\r\n").toString()))
            {
                module = (Module)iterator.next();
                s = Keyboard.getKeyName(module.getKey());
            }

            bufferedwriter.close();
        }catch (Exception e){}
    }

    @SuppressWarnings("resource")
    public void readKeybindConfig()
    {
        try
        {
            FileInputStream imputstream = new FileInputStream(keybindConfig.getAbsolutePath());
            DataInputStream datastream = new DataInputStream(imputstream);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(datastream));
            String key;

            while ((key = bufferedreader.readLine()) != null)
            {
                String line = key.trim();
                String string[] = line.split(":");
                String module1 = string[0];
                String keybinding = string[3].toUpperCase();

                for (Iterator iterator = ModuleManager.modList.iterator(); iterator.hasNext();)
                {
                	Module module = (Module)iterator.next();
                    List modules = Arrays.asList(new String[]{module.getName()});

                    for (int i = 0; i < modules.size(); i++)
                        if (module1.equalsIgnoreCase(((String)modules.get(i)).toLowerCase().replaceAll(" ", "")))
                        {
                            module.setKey(Keyboard.getKeyIndex(keybinding));
                        }
                }
            }
        }catch (Exception e){}
    }

    public void write()
    {
        if (!keybindConfig.exists())
        {
            keybindConfig.getParentFile().mkdirs();

            try
            {
                keybindConfig.createNewFile();
                writeKeybindConfig();
            }
            catch (IOException e) {}
        }else {
        	writeKeybindConfig();
        }
    }

    public static KeybindConfiguration instance()
    {
        return instance;
    }
}
