package cn.n3ro.ghostclient.utils;

import cn.n3ro.ghostclient.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ClientUtil {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static void sendClientMessage(String string) {
        if (mc.thePlayer != null && mc.theWorld !=null) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("\u00a7b[" + Client.CLIENT_NAME + "] " + "\u00a77" + string));
        }
    }

    public static void sendMessageWithoutPrefix(String string) {
        if (mc.thePlayer != null && mc.theWorld !=null) {
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(string));
        }
    }
}
