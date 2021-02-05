package cn.n3ro.ghostclient.module.modules.RENDER;

import cn.n3ro.ghostclient.events.EventRender2D;
import cn.n3ro.ghostclient.events.EventRender3D;
import cn.n3ro.ghostclient.events.EventUpdate;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.utils.Colors;
import cn.n3ro.ghostclient.utils.JReflectUtility;
import cn.n3ro.ghostclient.utils.PlayerUtil;
import cn.n3ro.ghostclient.utils.RenderUtil;
import cn.n3ro.ghostclient.value.Numbers;
import cn.n3ro.ghostclient.value.Option;
import com.darkmagician6.eventapi.EventTarget;
import com.sun.javafx.geom.Vec3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class ESP extends Module {
    private ArrayList<Vec3f> points = new ArrayList();
    public final List<Entity> collectedEntities = new ArrayList<Entity>();
    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
    private final int color = Color.WHITE.getRGB();
    private final int backgroundColor = new Color(0, 0, 0, 120).getRGB();
    private final int black = Color.BLACK.getRGB();

    public ESP() {
        super("ESP", Category.RENDER);
        int i = 0;
        while (i < 8) {
            this.points.add(new Vec3f());
            ++i;
        }
    }

    @EventTarget
    public void onScreen(EventRender2D event) {
        this.collectEntities();
            GL11.glPushMatrix();

            float partialTicks = JReflectUtility.getRenderPartialTicks();
            ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            int scaleFactor = scaledResolution.getScaleFactor();
            double scaling = (double) scaleFactor / Math.pow(scaleFactor, 2.0);
            GL11.glScaled((double) scaling, (double) scaling, (double) scaling);
            int black = this.black;
            int color = this.color;
            int background = this.backgroundColor;
            float scale = 0.65f;
            float upscale = 1.0f / scale;
            FontRenderer fr = mc.fontRendererObj;
            RenderManager renderMng = mc.getRenderManager();
            EntityRenderer entityRenderer = mc.entityRenderer;
            boolean outline = true;
            boolean health = true;
//            StreamerMod streamerMode = this.streamerMode;
            List<Entity> collectedEntities = this.collectedEntities;
            int collectedEntitiesSize = collectedEntities.size();
            for (int i = 0; i < collectedEntitiesSize; ++i) {
                ItemStack itemStack;
                EntityLivingBase entityLivingBase;
                boolean living;
                Entity entity = collectedEntities.get(i);
                if (!this.isValid(entity) || !RenderUtil.isInViewFrustrum(entity))
                    continue;
                double x = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
                double y = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
                double z = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
                double width = (double) entity.width / 1.5;
                double height = (double) entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ),
                        new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ),
                        new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ),
                        new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ),
                        new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
//                entityRenderer.setupCameraTransform(partialTicks, 0);
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = this.project2D(scaleFactor, vector.x - renderMng.viewerPosX,
                            vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
                    if (vector == null || !(vector.z >= 0.0) || !(vector.z < 1.0))
                        continue;
                    if (position == null) {
                        position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                    }
                    position.x = Math.min(vector.x, position.x);
                    position.y = Math.min(vector.y, position.y);
                    position.z = Math.max(vector.x, position.z);
                    position.w = Math.max(vector.y, position.w);
                }
                if (position == null)
                    continue;
                entityRenderer.setupOverlayRendering();
                double posX = position.x;
                double posY = position.y;
                double endPosX = position.z;
                double endPosY = position.w;
                if (outline) {
                    RenderUtil.drawRect(posX - 0.5, posY, posX + 0.5 - 0.5, endPosY, color);
                    RenderUtil.drawRect(posX, endPosY - 0.5, endPosX, endPosY, color);
                    RenderUtil.drawRect(posX - 0.5, posY, endPosX, posY + 0.5, color);
                    RenderUtil.drawRect(endPosX - 0.5, posY, endPosX, endPosY, color);
                }
                if (living = entity instanceof EntityLivingBase) {
                    entityLivingBase = (EntityLivingBase) entity;
                    if (health) {
                        float maxHealth;
                        float hp = entityLivingBase.getHealth();
                        if (hp > (maxHealth = entityLivingBase.getMaxHealth())) {
                            hp = maxHealth;
                        }
                        double hpPercentage = hp / maxHealth;
                        double hpHeight = (endPosY - posY) * hpPercentage;
                        if (hp > 0.0f) {
                            int healthColor = getHealthColor(entityLivingBase);
                            RenderUtil.drawRect(posX - 3.0, endPosY, posX - 2.5, endPosY - hpHeight, healthColor);
                        }
                    }
                }
            }
            GL11.glPopMatrix();
            GlStateManager.enableBlend();
            ((EntityRenderer) entityRenderer).setupOverlayRendering();
    }

    public static void glColor(int hex) {
        float alpha = (float) (hex >> 24 & 255) / 255.0f;
        float red = (float) (hex >> 16 & 255) / 255.0f;
        float green = (float) (hex >> 8 & 255) / 255.0f;
        float blue = (float) (hex & 255) / 255.0f;
        GL11.glColor4f((float) red, (float) green, (float) blue, (float) (alpha == 0.0f ? 1.0f : alpha));
    }

    private void collectEntities() {
        this.collectedEntities.clear();
        List playerEntities = mc.theWorld.loadedEntityList;
        int playerEntitiesSize = playerEntities.size();
        for (int i = 0; i < playerEntitiesSize; ++i) {
            Entity entity = (Entity) playerEntities.get(i);
            if (!this.isValid(entity))
                continue;
            this.collectedEntities.add(entity);
        }
    }

    private boolean isValid(Entity entity) {
        if (entity == mc.thePlayer) {
            return false;
        }
        if (entity.isDead) {
            return false;
        }
        if (entity.isInvisible()) {
            return false;
        }
        if (entity instanceof EntityItem) {
            return false;
        }
        if (entity instanceof EntityAnimal) {
            return false;
        }
        if (entity instanceof EntityPlayer) {
            return true;
        } else {
            return false;
        }
    }

    private Vector3d project2D(int scaleFactor, double x, double y, double z) {
        GL11.glGetFloat((int) 2982, (FloatBuffer) this.modelview);
        GL11.glGetFloat((int) 2983, (FloatBuffer) this.projection);
        GL11.glGetInteger((int) 2978, (IntBuffer) this.viewport);
        if (GLU.gluProject((float) ((float) x), (float) ((float) y), (float) ((float) z), (FloatBuffer) this.modelview,
                (FloatBuffer) this.projection, (IntBuffer) this.viewport, (FloatBuffer) this.vector)) {
            return new Vector3d(this.vector.get(0) / (float) scaleFactor,
                    ((float) Display.getHeight() - this.vector.get(1)) / (float) scaleFactor, this.vector.get(2));
        }
        return null;
    }
    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0f, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0f, 1.0f, 1.0f) | -16777216;
    }
}
