package cn.n3ro;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import net.minecraft.launchwrapper.LaunchClassLoader;

public class Agent {
	public static Instrumentation instrumentation;
	public static void agentmain(final String args, final Instrumentation instrumentation) {
		try {
			Agent.instrumentation = instrumentation;
			loadThisJar();
			forceloadclass();
			retransform();
		}
		catch (Exception e) {
			LogManager.getLogger().log(Level.ERROR, ExceptionUtils.getStackTrace(e));
		}
	}
	
	public static void loadThisJar() {
		LaunchClassLoader cl = getLaunchClassLoader();
		if (cl != null) {
			URL url = Agent.class.getProtectionDomain().getCodeSource().getLocation();
			cl.addURL(url);
		}
	}
	
	public static void forceloadclass() {
		LaunchClassLoader cl = getLaunchClassLoader();
		if (cl != null) {
			for (String name : ClassTransformer.classNameSet) {
				try {
					cl.loadClass(name);
				} catch (ClassNotFoundException e) {
					LogManager.getLogger().log(Level.ERROR, "can not load class " + name);
				}
			}
		}
	}
	
	public static LaunchClassLoader getLaunchClassLoader() {
        for (Class c : instrumentation.getAllLoadedClasses()) {
            if (c.getClassLoader() != null
                    && c.getClassLoader().getClass().getName().equals("net.minecraft.launchwrapper.LaunchClassLoader")) {
                Object cl = c.getClassLoader();
                return (LaunchClassLoader)cl;
            }
        }
        return null;
	}
	
    public static void addToMinecraftClassLoader(Class... classes) {

        for (Class c : instrumentation.getAllLoadedClasses()) {
            if (c.getClassLoader() != null
                    && c.getClassLoader().getClass().getName().equals("net.minecraft.launchwrapper.LaunchClassLoader")) {
                Object cl = c.getClassLoader();
                try {
                    Method addUrl = cl.getClass().getDeclaredMethod("addURL", URL.class);
                    addUrl.setAccessible(true);
                    for (Class clazz : classes )
                        addUrl.invoke(cl, clazz.getProtectionDomain().getCodeSource().getLocation());
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                	e.printStackTrace();
                } catch (IllegalAccessException e) {
                	e.printStackTrace();
                }
                break;
            }
        }
    }
    
    public static void retransform() {
    	ClassTransformer ct = new ClassTransformer();
    	instrumentation.addTransformer(ct, true);
        for (Class clazz : instrumentation.getAllLoadedClasses()) {
        	if (ClassTransformer.needTransform(clazz.getName())) {
		        try {
					instrumentation.retransformClasses(clazz);
				} catch (UnmodifiableClassException e) {			
					LogManager.getLogger().log(Level.ERROR, ExceptionUtils.getStackTrace(e));
				}
        	}
        }
        instrumentation.removeTransformer(ct);
    }
}
