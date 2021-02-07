package cn.n3ro.ghostclient.module.modules.RENDER;

import cn.n3ro.ghostclient.Client;
import cn.n3ro.ghostclient.events.EventRender2D;
import cn.n3ro.ghostclient.management.ModuleManager;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;

public class Arraylist extends Module {

    public Arraylist() {
        super("ArrayList", Category.RENDER);
        set(true);
    }

    @EventTarget
    private void render2D(EventRender2D event) {
        ScaledResolution sr = new ScaledResolution(mc);
       mc.fontRendererObj.drawString("N3RO CLIENT has injected...", 1, 1, -1);
        int y = 20;
        for (Module m : ModuleManager.getModList()) {
            if (m.isEnable()) {
                mc.fontRendererObj.drawString(m.name, 3, y, -1);
                y += mc.fontRendererObj.FONT_HEIGHT + 1;
            }
        }
    }


}
