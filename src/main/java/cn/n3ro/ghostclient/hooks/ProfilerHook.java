package cn.n3ro.ghostclient.hooks;

import cn.n3ro.ghostclient.events.EventRender2D;
import cn.n3ro.ghostclient.events.EventRender3D;
import cn.n3ro.ghostclient.utils.GLUProjection;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class ProfilerHook implements Opcodes {


    public static void transformProfiler(ClassNode classNode, MethodNode methodNode) {
        if (methodNode.name.equalsIgnoreCase("startSection") || methodNode.name.equalsIgnoreCase("func_76320_a")){
            InsnList insnList = new InsnList();
            insnList.add(new VarInsnNode(ALOAD,1));
            insnList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(ProfilerHook.class), "startSectionHook", "(Ljava/lang/String;)V", false));
            methodNode.instructions.insert(insnList);
        }
    }

    public static void startSectionHook(String info){
        if (info.equalsIgnoreCase("hand")){
            Event3D();
        }else if (info.equalsIgnoreCase("forgeHudText")){
            Event2D();
        }
    }

    public static void Event3D(){
        EventRender3D er3 = new EventRender3D();
        EventManager.call(er3);
    }


    public static void Event2D(){
        GlStateManager.pushMatrix();
        EventRender2D er = new EventRender2D();
        EventManager.call(er);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
