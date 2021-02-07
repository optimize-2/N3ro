package cn.n3ro;

import java.io.ByteArrayInputStream;
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
import cn.n3ro.ghostclient.module.modules.COMBAT.Reach;
import cn.n3ro.ghostclient.utils.ClassNodeUtils;
import cn.n3ro.ghostclient.utils.LoggerUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;


import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public class ClassTransformer implements IClassTransformer, ClassFileTransformer, Opcodes {

    public static Set<String> classNameSet;

    static {
        classNameSet = new HashSet<String>();
        String[] nameArray = new String[]{
        		"net.minecraft.client.multiplayer.PlayerControllerMP",
        		"net.minecraft.client.gui.GuiIngame",
                "net.minecraft.client.Minecraft",
                "net.minecraft.client.entity.EntityPlayerSP",
                "net.minecraft.network.NetworkManager",
                "net.minecraft.profiler.Profiler",
                "net.minecraft.client.renderer.EntityRenderer",
                "net.minecraft.client.renderer.entity.RenderPlayer",
                "net.minecraft.entity.EntityLivingBase"
              
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


    @SuppressWarnings("deprecation")
	public byte[] transform(String name, byte[] classByte) {

        try {
            if (name.equals("net.minecraft.client.Minecraft")) {
                return transformMethods(classByte, MinecraftHook::transformMinecraft);
            }
            if (name.equalsIgnoreCase("net.minecraft.client.entity.EntityPlayerSP")) {// SP
                return transformMethods(classByte, EntityPlayerSPHook::transformEntityPlayerSP);
            }
            if (name.equalsIgnoreCase("net.minecraft.profiler.Profiler")) {
                return this.transformMethods(classByte, ProfilerHook::transformProfiler);//	2D3D-Render
            }
            if (name.equalsIgnoreCase("net.minecraft.network.NetworkManager")) { //	EventPacket
                return this.transformMethods(classByte, PacketHook::transformNetworkManager);
            }
//            if (name.equalsIgnoreCase("net.minecraft.client.gui.GuiIngame")){//	hook2D-Render
//               return transformMethods(classByte, EventRendererHook::transformEvent2DLoad);
//            }
            if (name.equalsIgnoreCase("net.minecraft.client.renderer.entity.RenderPlayer")){ //	RenderPlayer
                return this.transformMethods(classByte,MinecraftHook::transformRendererLivingEntity);
            }
            if(name.equalsIgnoreCase("net.minecraft.client.multiplayer.PlayerControllerMP")) { //reach
            	 return this.transformMethods(classByte,MinecraftHook::transformTest);
            }
            ClassPool e;
            CtClass cc;
            CtMethod cm;
           
            if(name.equals("net.minecraft.client.multiplayer.PlayerControllerMP")) {// reach
            	
//            	   e = ClassPool.getDefault();
//                   cc = e.makeClass(new ByteArrayInputStream(classByte));
//                   cm = getDeclaredMethod(cc, new String[]{"func_78757_d", "getBlockReachDistance"});
//                   cm.setBody("{ return 100;}");

             
          
            }else if (name.equals("net.minecraft.client.renderer.EntityRenderer")) {//3D renderer
            	
            	ClassNode cn = ClassNodeUtils.toClassNode(classByte);
				MethodNode mn = getDeclaredMethod(cn, "func_175068_a", "renderWorldPass");

				int index = -1;
				for (int i=0; i<mn.instructions.size(); i++) {
					AbstractInsnNode insn = mn.instructions.get(i);
					if (insn instanceof FieldInsnNode && ((FieldInsnNode)insn).getOpcode()==Opcodes.GETFIELD) {
						if (((FieldInsnNode)insn).owner.equals("net/minecraft/client/renderer/EntityRenderer")
								&& (((FieldInsnNode)insn).name.equals("renderHand")||((FieldInsnNode)insn).name.equals("field_175074_C"))) {
							index = i;
							break;
						}
					}
				}
				
				if (index >= 0) {
					mn.instructions.insertBefore(mn.instructions.get(index), ClassNodeUtils.toNodes(
							new VarInsnNode(Opcodes.ILOAD, 1),
							new VarInsnNode(Opcodes.FLOAD, 2),
							new VarInsnNode(Opcodes.LLOAD, 3),
							new MethodInsnNode(Opcodes.INVOKESTATIC,Type.getInternalName(EventRendererHook.class) , "Event3DLoad", "(IFJ)V")
							));
				}
            	
            	
//            	ClassNode cn1 = ClassNodeUtils.toClassNode(classByte);
//				MethodNode mn1 = getDeclaredMethod(cn1, "func_78473_a", "getMouseOver");
//				LabelNode labelNode = new LabelNode();
//				mn1.instructions.insertBefore(mn1.instructions.getFirst(), ClassNodeUtils.toNodes(
//						new VarInsnNode(Opcodes.ALOAD, 0),
//						new VarInsnNode(Opcodes.FLOAD, 1),
//						new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ProfilerHook.class), "getMouseOver", "(Lnet/minecraft/client/renderer/EntityRenderer;F)Z"),
//						new JumpInsnNode(Opcodes.IFEQ, labelNode),
//						new InsnNode(Opcodes.RETURN),
//						labelNode
//						));
			}

           
        } catch (Exception e) {
            LogManager.getLogger().log(Level.ERROR, ExceptionUtils.getStackTrace(e));

        }

        return classByte;
    }

	public static CtMethod getDeclaredMethod(CtClass cc, String... name) {
		CtMethod[] cms = cc.getMethods();
		for (int j = 0; j<cms.length; j++) {
			CtMethod cm = cms[j];
			for (int i = 0; i<name.length; i++) {
				if (name[i].equals(cm.getName()))
					return cm;
			}
		}

		return null;
	}
	
	public static MethodNode getDeclaredMethod(ClassNode cn, String... name) {
		List<MethodNode> list = cn.methods;
		for (int j=0; j<list.size(); j++) {
			MethodNode mn = list.get(j);
			for (int i = 0; i<name.length; i++) {
				if (name[i].equals(mn.name))
					return mn;
			}
		}
		return null;
	}

    @Override
    public byte[] transform(ClassLoader arg0, String name, Class<?> clazz, ProtectionDomain arg3, byte[] classByte)
            throws IllegalClassFormatException {
        ClientLoader.runtimeDeobfuscationEnabled = true;
        return transform(clazz.getName(), classByte);
    }

}
