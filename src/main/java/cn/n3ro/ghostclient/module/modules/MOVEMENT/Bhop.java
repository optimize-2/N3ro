package cn.n3ro.ghostclient.module.modules.MOVEMENT;

import cn.n3ro.ghostclient.events.EventMotion;
import cn.n3ro.ghostclient.events.EventMove;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.utils.PlayerUtil;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.PlayerCapabilities;
import org.lwjgl.input.Keyboard;

public class Bhop extends Module {
    public Bhop() {
        super("Bhop", Category.MOVEMENT);
    }


    @EventTarget
    public void onPre(EventMove e) {
        if (e.getEntity() != mc.thePlayer){
            return;
        }

        if (PlayerUtil.isMoving()) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
                mc.thePlayer.setSprinting(true);
            }
        }
    }



}
