package cn.n3ro.ghostclient.module.modules.MOVEMENT;

import cn.n3ro.ghostclient.events.EventUpdate;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class InvMove extends Module {
    private boolean isWalking;

    public InvMove() {
        super("InvMove", Category.MOVEMENT);
    }
    
    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat)) {
            isWalking = true;

            KeyBinding[] key = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
                    mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight,
                    mc.gameSettings.keyBindSprint, mc.gameSettings.keyBindJump};
            KeyBinding[] array;
            for (int length = (array = key).length, i = 0; i < length; ++i) {
                KeyBinding b = array[i];
                KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
            }
        }
        else {
            if (isWalking) {
                isWalking = false;
            }
        }
    }
    
}
