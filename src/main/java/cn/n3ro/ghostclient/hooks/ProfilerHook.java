package cn.n3ro.ghostclient.hooks;

import cn.n3ro.ghostclient.events.EventRender2D;
import cn.n3ro.ghostclient.events.EventRender3D;
import cn.n3ro.ghostclient.management.ModuleManager;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.module.modules.COMBAT.Reach;
import cn.n3ro.ghostclient.utils.GLUProjection;
import com.darkmagician6.eventapi.EventManager;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class ProfilerHook implements Opcodes {


    public static void transformProfiler(ClassNode classNode, MethodNode methodNode) {
        if (methodNode.name.equalsIgnoreCase("startSection") || methodNode.name.equalsIgnoreCase("func_76320_a")){
            InsnList insnList = new InsnList();
            insnList.add(new VarInsnNode(ALOAD,1));
            insnList.add(new MethodInsnNode(INVOKESTATIC, Type.getInternalName(ProfilerHook.class), "startSectionHook", "(Ljava/lang/String;)V", false));
            methodNode.instructions.insert(insnList);
        }
    }
    
    public static boolean getMouseOver(EntityRenderer entityRenderer, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();
        Entity pointedEntity = null;
//        double reach = cn.n3ro.ghostclient.management.ModuleManager.getModuleByName("Reach").isEnable() ? cn.n3ro.ghostclient.module.modules.COMBAT.Reach.getReach()
//                : 4;
		if (entity != null && mc.theWorld != null) {
			mc.mcProfiler.startSection("pick");
			mc.pointedEntity = null;
		
			double d0 = ModuleManager.getModuleByName("Reach").isEnable() ? Reach.getReach()
					: (double) mc.playerController.getBlockReachDistance();
			
			mc.objectMouseOver = entity.rayTrace(ModuleManager.getModuleByName("Reach").isEnable() ? Reach.getReach() : d0,
					partialTicks);
			double d1 = d0;
			Vec3 vec3 = entity.getPositionEyes(partialTicks);
			boolean flag = false;
			if (mc.playerController.extendedReach()) {
				d0 = 6.0D;
				d1 = 6.0D;
			} else if (d0 > 3.0D) {
				flag = true;
			}

			if (mc.objectMouseOver != null) {
				d1 = mc.objectMouseOver.hitVec.distanceTo(vec3);
			}

			if (ModuleManager.getModuleByName("Reach").isEnable()) {
				d1 = Reach.getReach();
				MovingObjectPosition vec31 = entity.rayTrace(d1, partialTicks);
				if (vec31 != null) {
					d1 = vec31.hitVec.distanceTo(vec3);
				}
			}

			Vec3 var24 = entity.getLook(partialTicks);
			Vec3 vec32 = vec3.addVector(var24.xCoord * d0, var24.yCoord * d0, var24.zCoord * d0);
			pointedEntity = null;
			Vec3 vec33 = null;
			float f = 1.0F;
			List list = mc.theWorld.getEntitiesInAABBexcluding(entity,
					entity.getEntityBoundingBox().addCoord(var24.xCoord * d0, var24.yCoord * d0, var24.zCoord * d0)
							.expand((double) f, (double) f, (double) f),
					Predicates.and(EntitySelectors.NOT_SPECTATING, (p_apply_1_) -> {
						return p_apply_1_.canBeCollidedWith();
					}));
			double d2 = d1;

			for (int j = 0; j < list.size(); ++j) {
				Entity entity1 = (Entity) list.get(j);
				float f1 = entity1.getCollisionBorderSize();
				AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f1, (double) f1,
						(double) f1);
				MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
				if (axisalignedbb.isVecInside(vec3)) {
					if (d2 >= 0.0D) {
						pointedEntity = entity1;
						vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
						d2 = 0.0D;
					}
				} else if (movingobjectposition != null) {
					double d3 = vec3.distanceTo(movingobjectposition.hitVec);
					if (d3 < d2 || d2 == 0.0D) {
						if (entity1 == entity.ridingEntity) {
							if (d2 == 0.0D) {
								pointedEntity = entity1;
								vec33 = movingobjectposition.hitVec;
							}
						} else {
							pointedEntity = entity1;
							vec33 = movingobjectposition.hitVec;
							d2 = d3;
						}
					}
				}
			}

			if (pointedEntity != null && flag
					&& vec3.distanceTo(vec33) > (ModuleManager.getModuleByName("Reach").isEnable() ? Reach.getReach() : 3.0D)) {
				pointedEntity = null;
				mc.objectMouseOver = new MovingObjectPosition(MovingObjectType.MISS, vec33, (EnumFacing) null,
						new BlockPos(vec33));
			}

			if (pointedEntity != null && (d2 < d1 || mc.objectMouseOver == null)) {
				mc.objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);
				if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
					mc.pointedEntity = pointedEntity;
				}
			}

			mc.mcProfiler.endSection();
		}
        
        return true;
	}
    
    public static void startSectionHook(String info){
        if (info.equalsIgnoreCase("hand")){
            Event3D();
        }else if (info.equalsIgnoreCase("forgeHudText")){
            Event2D();
        }
    }
    public static void Event2D(){
        GlStateManager.pushMatrix();
        EventRender2D ed2 = new EventRender2D();
        EventManager.call(ed2);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
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
