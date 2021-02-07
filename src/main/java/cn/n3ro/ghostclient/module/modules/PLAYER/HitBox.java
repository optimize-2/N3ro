package cn.n3ro.ghostclient.module.modules.PLAYER;

import java.util.Iterator;
import java.util.List;

import com.darkmagician6.eventapi.EventTarget;

import cn.n3ro.ghostclient.events.EventUpdate;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.utils.EntitySize;
import cn.n3ro.ghostclient.value.Numbers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class HitBox extends Module {

	public static Numbers<Float> Width = new Numbers<Float>("Width", 0.6f, 0.0f, 5f, 0.1f);
	public static Numbers<Float> Height = new Numbers<Float>("Height", 1.8f, 0.0f, 5f, 0.1f);
	 public HitBox() {
	        super("HitBox", Category.PLAYER);
	    }
	    
	    @EventTarget
	    private void onUpdate(EventUpdate event) {
	    	for(EntityPlayer player : getPlayersList()) {
				if(!check(player)) continue;
				float width = (float)(Width.getValue());
				float height = (float)(Height.getValue());
				setEntityBoundingBoxSize(player, width, height);
			}
	    	
	    }
	    
	    @Override
	    public void onDisable() {
	    	System.out.println("sad");
	    }
	    
	    public static void setEntityBoundingBoxSize(Entity entity, float width, float height) {
			EntitySize size = getEntitySize(entity);
			entity.width = size.width;
			entity.height = size.height;
			double d0 = (double) (width) / 2.0D;
			entity.setEntityBoundingBox(new AxisAlignedBB(entity.posX - d0, entity.posY, entity.posZ - d0, entity.posX + d0,
					entity.posY + (double) height, entity.posZ + d0));
		}
		public static EntitySize getEntitySize(Entity entity) {
			EntitySize entitySize = new EntitySize(0.6F, 1.8F);
			return entitySize;
		}
		
		public boolean check(EntityLivingBase entity) {
			if(entity instanceof EntityPlayerSP) { return false; }
			if(entity.isDead) { return false; }
			return true;
	    }
		
	    public static List<EntityPlayer> getPlayersList() {
			return Minecraft.getMinecraft().theWorld.playerEntities;
		}
}
