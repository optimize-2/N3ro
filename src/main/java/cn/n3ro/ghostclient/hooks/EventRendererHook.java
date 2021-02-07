package cn.n3ro.ghostclient.hooks;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.darkmagician6.eventapi.EventManager;

import cn.n3ro.ghostclient.events.EventRender2D;
import cn.n3ro.ghostclient.events.EventRender3D;
import cn.n3ro.ghostclient.events.EventUpdate;
import cn.n3ro.ghostclient.utils.ASMUtil;
import cn.n3ro.ghostclient.utils.GLUProjection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;


public class EventRendererHook implements Opcodes {
	
	  
	  public static void Event3DLoad(){
		  Event3D();
    }
	  
	  public static void transformEvent2DLoad(ClassNode clazz, MethodNode method) {
		  if (method.name.equalsIgnoreCase("func_175180_a") | method.name.equalsIgnoreCase("renderGameOverlay")){
			//  InsnList insnList1 = new InsnList();
	            InsnList insnList2 = new InsnList();
//	            insnList1.add(new VarInsnNode(Opcodes.ALOAD,1));
//	            method.instructions.insert(insnList1);

	         
	            method.instructions.insert(new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(EventRendererHook.class), "hook2D", "(F)V", false));
	        
	         
		  }  
	        
	    }
	  public static void hook2D(){
	        EventManager.call(new EventRender2D());
	   }
	    public static void Event3D(){
	        GLUProjection projection = GLUProjection.getInstance();
	        IntBuffer viewPort = GLAllocation.createDirectIntBuffer(16);
	        FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
	        FloatBuffer projectionPort = GLAllocation.createDirectFloatBuffer(16);
	        GL11.glGetFloat(2982, modelView);
	        GL11.glGetFloat(2983, projectionPort);
	        GL11.glGetInteger(2978, viewPort);
	        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
	        projection.updateMatrices(viewPort, modelView, projectionPort, (double)scaledResolution.getScaledWidth() / (double)Minecraft.getMinecraft().displayWidth, (double)scaledResolution.getScaledHeight() / (double)Minecraft.getMinecraft().displayHeight);
	        EventRender3D ed3 = new EventRender3D();
	        EventManager.call(ed3);
	    }

   
}
