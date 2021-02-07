package cn.n3ro.ghostclient.module.modules.RENDER;

import cn.n3ro.ghostclient.Client;
import cn.n3ro.ghostclient.events.EventRender2D;
import cn.n3ro.ghostclient.font.CFontRenderer;
import cn.n3ro.ghostclient.font.FontLoaders;
import cn.n3ro.ghostclient.management.ModuleManager;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.utils.PaletteUtil;
import cn.n3ro.ghostclient.value.Numbers;
import cn.n3ro.ghostclient.value.Option;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HUD extends Module {
    public static Numbers<Double> hudR = new Numbers("Red", 50d, 0, 255, 5);
    public static Numbers<Double> hudG = new Numbers("Green", 120d, 0, 255, 5);
    public static Numbers<Double> hudB = new Numbers("Blue", 255d, 0, 255, 5);

    public static Option<Boolean> hudTitle = new Option("WaterMark", true);
    public static Option<Boolean>hudPosition = new Option("Position", true);
    public static Option<Boolean>hudArraylist = new Option("Arraylist", true);
    public static Option<Boolean>hudFade = new Option("Fade", true);
   
    public HUD() {
        super("HUD", Category.RENDER);
        set(true);
        addValues(hudR, hudG, hudB, hudFade, hudTitle, hudPosition, hudArraylist);
    }

    @EventTarget
    private void render2D(EventRender2D event) {
        ScaledResolution sr = new ScaledResolution(mc);
        CFontRenderer font = FontLoaders.default20;

        if(hudTitle.getValue()) {
            int titlecolor;
            if (hudFade.getValue()) {
                titlecolor = PaletteUtil.fade(new Color(HUD.hudR.getValue().intValue(), HUD.hudG.getValue().intValue(), HUD.hudB.getValue().intValue()), 100, 50).getRGB();
            } else {
                titlecolor = new Color(HUD.hudR.getValue().intValue(), HUD.hudG.getValue().intValue(), HUD.hudB.getValue().intValue()).getRGB();
            }
            String firstname = Client.CLIENT_NAME.substring(0, 1);
            String theothername = Client.CLIENT_NAME.substring(1);
            font.drawStringWithShadow(firstname, 3, 3, titlecolor);
            font.drawStringWithShadow(theothername, 3 + font.getStringWidth(firstname) + 1.5, 3, -1);
        }

        if(hudPosition.getValue()) {
            String coords = "\u00a7fX : \u00a77" + (int) mc.thePlayer.posX + "   \u00a7fY : \u00a77" + (int) mc.thePlayer.posY + "   \u00a7fZ : \u00a77" + (int) mc.thePlayer.posZ;
            font.drawStringWithShadow(coords, sr.getScaledWidth() / 2 - font.getStringWidth(coords) / 2, 20, -1);
        }

        if(hudArraylist.getValue()) {
            ArrayList<Module> sorted = new ArrayList<Module>();
            String name;
            for (Module m : ModuleManager.getModList()) {
                if ((!m.isEnable() && m.wasArrayRemoved() && m.getAnimx() == 0))
                    continue;
                sorted.add(m);
            }
            sorted.sort((o1, o2) -> font.getStringWidth(o2.getDisplayname() == null ? o2.getName() : String.format("%s %s", o2.getName(), o2.getDisplayname())) - font.getStringWidth(o1.getDisplayname() == null ? o1.getName() : String.format("%s %s", o1.getName(), o1.getDisplayname())));
            int y = 3;
            for (Module m : sorted) {
                int nextIndex = sorted.indexOf(m) + 1;
                Module nextModule = null;
                if (sorted.size() > nextIndex) {
                    nextModule = this.getNextEnabledModule(sorted, nextIndex);
                }
                name = m.getDisplayname() == null ? m.getName() : String.format("%s %s", m.getName(), m.getDisplayname());
                if (m.isEnable()) {
                    m.setArrayRemoved(false);
                    if (mc.thePlayer.ticksExisted >= 30) {
                        m.setAnimx(Math.min(m.getAnimx() + font.getStringWidth(name) / 12, font.getStringWidth(name)));
                    } else {
                        m.setAnimx(font.getStringWidth(name));
                    }
                } else {
                    if (m.getAnimx() <= 0) {
                        m.setArrayRemoved(true);
                    } else {
                        if (mc.thePlayer.ticksExisted >= 30) {
                            m.setAnimx(Math.max(m.getAnimx() - font.getStringWidth(name) / 12, 0));
                        } else {
                            m.setAnimx(0);
                        }
                    }
                }
                int color;
                if (hudFade.getValue()) {
                    color = PaletteUtil.fade(new Color(HUD.hudR.getValue().intValue(), HUD.hudG.getValue().intValue(), HUD.hudB.getValue().intValue()), 100, sorted.indexOf(nextModule) * 2 + 50).getRGB();
                } else {
                    color = new Color(HUD.hudR.getValue().intValue(), HUD.hudG.getValue().intValue(), HUD.hudB.getValue().intValue()).getRGB();
                }
                int x = (int) (sr.getScaledWidth() - m.getAnimx());
                font.drawStringWithShadow(m.getName(), x - 3, y, color);
                y += 12;
            }
        }
    }

    private Module getNextEnabledModule(List<Module> modules, int startingIndex) {
        int modulesSize = modules.size();
        for (int i = startingIndex; i < modulesSize; ++i) {
            Module module = modules.get(i);
            if (!module.isEnable())
                continue;
            return module;
        }
        return null;
    }
}
