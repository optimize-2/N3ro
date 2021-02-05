package cn.n3ro;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Field;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import cn.n3ro.ghostclient.Client;
import cn.n3ro.ghostclient.ClientLoader;
import cn.n3ro.ghostclient.hooks.*;
import cn.n3ro.ghostclient.utils.LoggerUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;


import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class ClassTransformer implements IClassTransformer, ClassFileTransformer, Opcodes {

    public static Set<String> classNameSet;

    static {
        classNameSet = new HashSet<String>();
        String[] nameArray = new String[]{
                "net.minecraft.client.Minecraft",
                "net.minecraft.client.entity.EntityPlayerSP",
                "net.minecraft.network.NetworkManager",
                "net.minecraft.client.renderer.EntityRenderer",
                "net.minecraft.client.renderer.entity.RenderPlayer",
                "net.minecraft.profiler.Profiler",
        };
        for (int i = 0; i < nameArray.length; i++) {
            classNameSet.add(nameArray[i]);
        }
    }

    public static boolean needTransform(String name) {
        return classNameSet.contains(name);
    }


    @Override
    public byte[] transform(String name, String transformedName, byte[] classByte) {
        return transform(transformedName, classByte);
    }

    private byte[] transformMethods(byte[] bytes, BiConsumer<ClassNode, MethodNode> transformer) {
        ClassReader classReader = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);
        classNode.methods.forEach(m ->
                transformer.accept(classNode, m)
        );
        LoggerUtils.info("transform -> " + classNode.name);
        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }


    public byte[] transform(String name, byte[] classByte) {

        try {
            if (name.equals("net.minecraft.client.Minecraft")) {
                return transformMethods(classByte, MinecraftHook::transformMinecraft);
            }
            if (name.equalsIgnoreCase("net.minecraft.client.entity.EntityPlayerSP")) {
                return transformMethods(classByte, EntityPlayerSPHook::transformEntityPlayerSP);
            }
            if (name.equalsIgnoreCase("net.minecraft.profiler.Profiler")) {
                return this.transformMethods(classByte, ProfilerHook::transformProfiler);
            }
            if (name.equalsIgnoreCase("net.minecraft.network.NetworkManager")) { //EventPacket
                return this.transformMethods(classByte, NetworkManagerHook::transformNetworkManager);
            }
            if (name.equalsIgnoreCase("net.minecraft.client.renderer.EntityRenderer")){//3d
                return transformMethods(classByte, MinecraftHook::transformRenderEntityRenderer);
            }
            if (name.equalsIgnoreCase("net.minecraft.client.renderer.entity.RenderPlayer")){
                return this.transformMethods(classByte,MinecraftHook::transformRendererLivingEntity);
            }

        } catch (Exception e) {
            LogManager.getLogger().log(Level.ERROR, ExceptionUtils.getStackTrace(e));

        }

        return classByte;
    }


    @Override
    public byte[] transform(ClassLoader arg0, String name, Class<?> clazz, ProtectionDomain arg3, byte[] classByte)
            throws IllegalClassFormatException {
        ClientLoader.runtimeDeobfuscationEnabled = true;
        return transform(clazz.getName(), classByte);
    }

}
