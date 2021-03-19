package cn.n3ro.ghostclient.management.FileAndConfig;

import java.io.*;
import java.util.*;

import cn.n3ro.ghostclient.management.ModuleManager;
import cn.n3ro.ghostclient.module.Module;
import net.minecraft.client.Minecraft;

/**
 * 
 * @author huangbai
 *
 * 记录功能开启/关闭的配置文件
 */
public class ModuleStateConfiguration
{
    private static volatile ModuleStateConfiguration instance = new ModuleStateConfiguration();
    private File moduleConfig;

    public ModuleStateConfiguration()
    {
        moduleConfig = new File(Minecraft.getMinecraft().mcDataDir, "/N3RO/Config/ModuleStatus.txt");
        read();
        write();
    }

    public void writeToFile()
    {
        try
        {
            FileWriter filewriter = new FileWriter(moduleConfig);
            BufferedWriter bufferedwriter = new BufferedWriter(filewriter);

            for (Iterator iterator = ModuleManager.modList.iterator(); iterator.hasNext();)
            {
            	Module module = (Module)iterator.next();
                Boolean s = Boolean.valueOf(module.isEnable());

                if (module.getCategory() != null)
                {
                	 bufferedwriter.write((new StringBuilder(String.valueOf(module.getName().toLowerCase().replaceAll(" ", "")))).append(":").append(s).append("\r\n").toString());
                }
            }

            bufferedwriter.close();
        }catch (Exception e){}
    }

    @SuppressWarnings("resource")
    public void read()
    {
        try
        {
            FileInputStream inputstream = new FileInputStream(moduleConfig.getAbsolutePath());
            DataInputStream datastream = new DataInputStream(inputstream);
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            String string;

            while ((string = bufferedreader.readLine()) != null)
            {
                String line = string.trim();
                String string2[] = string.split(":");
                String moduleName = string2[0];
                String booleanState = string2[3];

                for (Iterator iterator = ModuleManager.modList.iterator(); iterator.hasNext();)
                {
                	Module module = (Module)iterator.next();

                    if (module.getCategory() != null)
                    {
                        List modules = Arrays.asList(new String[]
                                                     {
                                                         module.getName()
                                                     });

                        for (int i = 0; i < modules.size(); i++)
                        	 if (moduleName.equalsIgnoreCase(((String)modules.get(i)).toLowerCase().replaceAll(" ", "")) && booleanState.equalsIgnoreCase("true"))                                try
                                {
                                    module.on();
                                }catch (Exception e){}
                    }
                }
            }
        }catch (Exception e){}
    }

    public void write()
    {
        if (!moduleConfig.exists())
        {
            moduleConfig.getParentFile().mkdirs();

            try
            {
                moduleConfig.createNewFile();
                writeToFile();
            }
            catch (IOException e) {}
        }else {
        	writeToFile();
        }
    }

    public static ModuleStateConfiguration instance()
    {
        return instance;
    }
}
