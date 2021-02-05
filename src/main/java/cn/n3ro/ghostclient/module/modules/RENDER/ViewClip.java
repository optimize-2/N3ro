package cn.n3ro.ghostclient.module.modules.RENDER;

import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ViewClip extends Module {
    public ViewClip() {
        super("ViewClip", Category.RENDER);
    }

    public static boolean x = false;

    @Override
    public void onEnable() {
        x = true;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        x = false;
        super.onDisable();
    }
}
