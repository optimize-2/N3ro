package cn.n3ro.ghostclient.module.modules.MOVEMENT;

import cn.n3ro.ghostclient.events.EventMotion;
import cn.n3ro.ghostclient.events.EventUpdate;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.utils.PlayerUtil;
import com.darkmagician6.eventapi.EventTarget;
import org.lwjgl.input.Keyboard;


public class Sprint extends Module {
    boolean isSprinting;

    public Sprint() {
        super("Sprint", Category.MOVEMENT);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        boolean canSprint = mc.thePlayer.getFoodStats().getFoodLevel() > 6.0F || mc.thePlayer.capabilities.allowFlying;
        if(PlayerUtil.isMoving() && canSprint) {
            isSprinting = true;
            mc.thePlayer.setSprinting(true);
        } else {
            isSprinting = false;
        }
    }

    @Override
    public void onDisable() {
        isSprinting = false;
        mc.thePlayer.setSprinting(false);
        super.onDisable();
    }
}
