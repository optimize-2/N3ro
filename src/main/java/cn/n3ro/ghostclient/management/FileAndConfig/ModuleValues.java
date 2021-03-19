package cn.n3ro.ghostclient.management.FileAndConfig;

import java.io.*;
import java.util.*;

import cn.n3ro.ghostclient.management.ModuleManager;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.value.*;
import net.minecraft.client.Minecraft;

/**
 * 
 * @author huangbai
 *
 * 记录功能开启/关闭的配置文件
 */
public class ModuleValues
{
    private static volatile ModuleValues instance = new ModuleValues();
    private File moduleConfig;

    public ModuleValues()
    {
        moduleConfig = new File(Minecraft.getMinecraft().mcDataDir, "/N3RO/Config/ModuleValues.txt");
        read();
        write();
    }

    public void writeToFile()
    {
        try
        {
            FileWriter filewriter = new FileWriter(moduleConfig);
            BufferedWriter bufferedwriter = new BufferedWriter(filewriter);
            ArrayList<Module> hacks = new ArrayList<>();
            hacks.addAll(ModuleManager.modList);
            String values = "";
            for (Module mod : hacks) {
                for (Value v : mod.getValues()) {
                    values = String.valueOf(values) + String.format("%s:%s:%s:%s:%s%s", mod.getName(),  v.getName(), v.getValue(), System.lineSeparator());
                }
            }
            bufferedwriter.write(values);
            bufferedwriter.close();
        }
        catch (Exception e){}
    }
    
    public static Module getModuleByName(String name) {
    	ArrayList<Module> hacks = new ArrayList<>();
        hacks.addAll(ModuleManager.modList);
        for (Module m : hacks) {
            if (!m.getName().equalsIgnoreCase(name)) continue;
            return m;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
	public void read()
    {
    	List<String> vals = read("ModuleValues.txt");
        for (String v : vals) {
            String name = v.split(":")[0];
            String values = v.split(":")[3];
            Module m = getModuleByName(name);
            if (m == null) continue;
            for (Value value : m.getValues()) {
                if (!value.getName().equalsIgnoreCase(values)) continue;
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(v.split(":")[4]));
                    continue;
                }
                if (value instanceof Numbers) {
                    value.setValue(Double.parseDouble(v.split(":")[4]));
                    continue;
                }
                ((Mode)value).setMode(v.split(":")[4]);
            }
        }
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
            catch (IOException ioexception) { }
        }else {
        	writeToFile();
        }
    }
    
    public static List<String> read(final String file) {
        final List<String> out = new ArrayList<String>();
        try {
            final File f = new File(new File(Minecraft.getMinecraft().mcDataDir, "/config/Azrael"), file);
            if (!f.exists()) {
                f.createNewFile();
            }
            Throwable t = null;
            try {
                final FileInputStream fis = new FileInputStream(f);
                try {
                    final InputStreamReader isr = new InputStreamReader(fis);
                    try {
                        final BufferedReader br = new BufferedReader(isr);
                        try {
                            String line = "";
                            while ((line = br.readLine()) != null) {
                                out.add(line);
                            }
                        }
                        finally {
                            if (br != null) {
                                br.close();
                            }
                        }
                        if (isr != null) {
                            isr.close();
                        }
                    }
                    finally {
                        if (t == null) {
                            final Throwable t2 = null;
                            t = t2;
                        }
                        else {
                            final Throwable t2 = null;
                            if (t != t2) {
                                t.addSuppressed(t2);
                            }
                        }
                        if (isr != null) {
                            isr.close();
                        }
                    }
                    if (fis != null) {
                        fis.close();
                        return out;
                    }
                }
                finally {
                    if (t == null) {
                        final Throwable t3 = null;
                        t = t3;
                    }
                    else {
                        final Throwable t3 = null;
                        if (t != t3) {
                            t.addSuppressed(t3);
                        }
                    }
                    if (fis != null) {
                        fis.close();
                    }
                }
            }
            finally {
                if (t == null) {
                    final Throwable t4 = null;
                    t = t4;
                }
                else {
                    final Throwable t4 = null;
                    if (t != t4) {
                        t.addSuppressed(t4);
                    }
                }
            }
        }catch (IOException e) {}
        return out;
    }

    public static ModuleValues instance()
    {
        return instance;
    }
}
