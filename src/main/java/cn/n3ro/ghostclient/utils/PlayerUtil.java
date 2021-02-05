package cn.n3ro.ghostclient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

public class PlayerUtil {
    private static Minecraft mc;

    static {
        PlayerUtil.mc = Minecraft.getMinecraft();
    }

    public static boolean isMoving() {
        if ((!mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isSneaking())) {
            return ((mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F));
        }
        return false;
    }

    public static void debug(String string) {
        if (string != null && mc.thePlayer != null )
            mc.thePlayer.addChatMessage(new ChatComponentText("§c[DEBUG] §r " + string));
    }
}
