package cn.n3ro.ghostclient.hooks;

import cn.n3ro.ghostclient.Client;
import cn.n3ro.ghostclient.ClientLoader;
import cn.n3ro.ghostclient.events.EventMotion;
import cn.n3ro.ghostclient.events.EventRender2D;
import cn.n3ro.ghostclient.events.EventRender3D;
import cn.n3ro.ghostclient.events.EventTick;
import cn.n3ro.ghostclient.management.ModuleManager;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.module.modules.COMBAT.Reach;
import cn.n3ro.ghostclient.module.modules.RENDER.Chams;
import cn.n3ro.ghostclient.module.modules.RENDER.NoHurtCam;
import cn.n3ro.ghostclient.utils.ASMUtil;
import cn.n3ro.ghostclient.utils.ClassNodeUtils;
import cn.n3ro.ghostclient.utils.GLUProjection;
import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.Objects;

public class MinecraftHook {

    public static void transformMinecraft(ClassNode clazz, MethodNode method) {
        Iterator<AbstractInsnNode> iter = method.instructions.iterator();
        while (iter.hasNext()) {
            AbstractInsnNode insn = iter.next();
            if (insn.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                MethodInsnNode methodInsn = (MethodInsnNode) insn;
                if (methodInsn.name.equals("func_71407_l") || methodInsn.name.equals("runTick")) {
                    method.instructions.insert(insn, new MethodInsnNode(Opcodes.INVOKESTATIC,//opcodes invoke static method
                            Type.getInternalName(MinecraftHook.class), //method ownner
                            "hookRunTick", //method name
                            "()V", //method desc
                            false));
                }
                if (methodInsn.name.equals("dispatchKeypresses") | method.name.equalsIgnoreCase("func_152348_aa")) {
                    method.instructions.insert(insn, new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "hookKeyHandler", "()V", false));
                }
              
            }
        }
    }

    /*
     * 
     *	just reach test
     */
    public static void transformTest(ClassNode clazz, MethodNode method) {
        if (method.name.equalsIgnoreCase("getBlockReachDistance") || method.name.equalsIgnoreCase("func_78757_d")){
            InsnList insnList = new InsnList();
            insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "getBlockReachDistance", "()F", false));
            method.instructions.insert(insnList);
        }
        
    }
    
    /*
     * 
     *	just reach test
     */
    
    public static float getBlockReachDistance_Range(){
    	if(ModuleManager.getModuleByName("Reach").isEnable() /*&& !ModManager.getModule("TPHit").isEnabled()*/) {
		return Reach.reach.getValue().floatValue();
	} else {
		return Minecraft.getMinecraft().playerController.isInCreativeMode() ? 5F :4.5F;
	}
}
    
    
 
    
    /**
     * ghost client start method
     */
    public static void hookRunTick() {
        if (!Client.initiated) {
            new Client();
        }
        EventManager.call(new EventTick());
    }

    /**
     * KeyHandler method
     */
    public static void hookKeyHandler() {
        if (Keyboard.getEventKeyState()) {
            for (Module mod : ModuleManager.getModList()) {
                if (Minecraft.getMinecraft().currentScreen == null) {
                    if (mod.getKey() != (Keyboard.getEventKey() == 0 ? Keyboard.getEventCharacter() + 256 : Keyboard.getEventKey()))
                        continue;
                    mod.set(!mod.isEnable());
                }
                break;
            }
        }
    }

    
  
    
    @SuppressWarnings("deprecation")
	public static void transformRenderEntityRenderer(ClassNode classNode, MethodNode method) {

        if (method.name.equalsIgnoreCase("hurtCameraEffect") || method.name.equalsIgnoreCase("func_78482_e")){
            InsnList insnList = new InsnList();
            insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "isNohurtcamEnable", "()Z", false));
            LabelNode labelNode = new LabelNode();
            insnList.add(new JumpInsnNode(Opcodes.IFEQ, labelNode));
            insnList.add(new InsnNode(Opcodes.RETURN));
            insnList.add(labelNode);
            method.instructions.insert(insnList);
        }
        if ((method.name.equalsIgnoreCase("orientCamera") || method.name.equalsIgnoreCase("func_78467_g")) && method.desc.equalsIgnoreCase("(F)V")){
            AbstractInsnNode target = ASMUtil.findMethodInsn(method, Opcodes.INVOKEVIRTUAL,"net/minecraft/util/Vec3",  ClientLoader.runtimeDeobfuscationEnabled ?"func_72438_d" : "distanceTo","(Lnet/minecraft/util/Vec3;)D");
            if (target != null){
                InsnList insnList2 = new InsnList();

                InsnList insnList = new InsnList();
                insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC,Type.getInternalName(MinecraftHook.class),"isViewClipEnabled","()Z",false));
                LabelNode labelNode = new LabelNode();
                insnList.add(new JumpInsnNode(Opcodes.IFNE,labelNode));
                method.instructions.insertBefore(ASMUtil.forward(target,8),insnList);
                insnList2.add(labelNode);
                insnList2.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
                method.instructions.insert(ASMUtil.forward(target,13),insnList2);
            }
        }
        if(method.name.equalsIgnoreCase("func_78473_a") || method.name.equalsIgnoreCase("getMouseOver")) {

			LabelNode labelNode = new LabelNode();
			method.instructions.insertBefore(method.instructions.getFirst(), ClassNodeUtils.toNodes(
					new VarInsnNode(Opcodes.ALOAD, 0),
					new VarInsnNode(Opcodes.FLOAD, 1),
					new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ProfilerHook.class), "getMouseOver", "(Lnet/minecraft/client/renderer/EntityRenderer;F)Z"),
					new JumpInsnNode(Opcodes.IFEQ, labelNode),
					new InsnNode(Opcodes.RETURN),
					labelNode
			));
        }
    }

  
    public static void transformRendererLivingEntity(ClassNode classNode, MethodNode method) {
        if (method.name.equalsIgnoreCase("doRender") || method.name.equalsIgnoreCase("func_76986_a")){
            InsnList insnList1 = new InsnList();
            InsnList insnList2 = new InsnList();
            insnList1.add(new VarInsnNode(Opcodes.ALOAD,1));
            insnList1.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "chamsHook1", "(Ljava/lang/Object;)V", false));
            method.instructions.insert(insnList1);

            insnList2.add(new VarInsnNode(Opcodes.ALOAD,1));
            insnList2.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(MinecraftHook.class), "chamsHook2", "(Ljava/lang/Object;)V", false));
            method.instructions.insertBefore(ASMUtil.bottom(method),insnList2);
        }
    }

    public static boolean isViewClipEnabled() {
        return Objects.requireNonNull(ModuleManager.getModuleByName("ViewClip")).isEnable();
    }

    public static boolean isNohurtcamEnable(){
        return NoHurtCam.no;
    }
    
  //  public float getBlockReachDistance() {
//    	if(ModuleManager.getModuleByName("Reach").isEnable() /*&& !ModManager.getModule("TPHit").isEnabled()*/) {
//    		return (float) Reach.getReach() + 1.5f;
//    	} else {
//    		return 4.5F;
//    	}
//    	return 10F;
//	}
//    public static void getBlockReachDistance() {
//    	System.out.println("asd111");
//    }
    public static void chamsHook1(Object object){
        if (Client.instance.moduleManager.getModuleByName("Chams").isEnable() && object instanceof EntityPlayer){
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0F, -2000000F);
        }
    }
    public static void chamsHook2(Object object){
        if (Client.instance.moduleManager.getModuleByName("Chams").isEnable() && object instanceof EntityPlayer){
            GL11.glPolygonOffset(1.0F, 2000000F);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        }
    }
}
