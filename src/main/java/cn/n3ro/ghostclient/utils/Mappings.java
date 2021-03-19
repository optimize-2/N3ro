package cn.n3ro.ghostclient.utils;


import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class Mappings
{
    public static String timer = isMCP() ? "timer" : "field_71428_T";
    public static String anti = isMCP() ? "MovementInput" : "field_71158_b";
    public static String isInWeb = isMCP() ? "isInWeb" : "field_70134_J";
    public static String registerReloadListener = isMCP() ? "registerReloadListener" : "func_110542_a";

    public Mappings()
    {
    }

    private static boolean isMCP()
    {
        try
        {
            return ReflectionHelper.findField(Minecraft.class, new String[] {"theMinecraft"}) != null;
        }
        catch (Exception var1)
        {
            return false;
        }
    }
}
