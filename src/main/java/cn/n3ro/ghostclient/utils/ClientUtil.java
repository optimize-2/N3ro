package cn.n3ro.ghostclient.utils;

import java.awt.Color;

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

    public static int reAlpha(int color, float alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, alpha).getRGB();
    }
}
