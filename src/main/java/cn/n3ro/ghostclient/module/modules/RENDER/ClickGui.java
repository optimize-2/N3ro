package cn.n3ro.ghostclient.module.modules.RENDER;

import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.module.modules.RENDER.GuiClick.GuiClickUI;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClickGui extends Module {
    public static int memoriseX = 30;
    public static int memoriseY = 30;
    public static int memoriseWheel = 0;
    public static List<Module> memoriseML = new CopyOnWriteArrayList<>();
    public static Category memoriseCatecory = null;

    public ClickGui() {
        super("ClickGui", Category.RENDER);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new GuiClickUI());
        GuiClickUI.setX(memoriseX);
        GuiClickUI.setY(memoriseY);
        GuiClickUI.setWheel(memoriseWheel);
        GuiClickUI.setInSetting(memoriseML);
        if (memoriseCatecory != null)
            GuiClickUI.setCategory(memoriseCatecory);
        this.set(false);
    }
}
