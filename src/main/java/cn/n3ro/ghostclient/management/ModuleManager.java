package cn.n3ro.ghostclient.management;

import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.module.modules.COMBAT.HitBox;
import cn.n3ro.ghostclient.module.modules.COMBAT.Reach;
import cn.n3ro.ghostclient.module.modules.MOVEMENT.Bhop;
import cn.n3ro.ghostclient.module.modules.MOVEMENT.Eagle;
import cn.n3ro.ghostclient.module.modules.MOVEMENT.InvMove;
import cn.n3ro.ghostclient.module.modules.MOVEMENT.Sprint;
import cn.n3ro.ghostclient.module.modules.MOVEMENT.Time;
import cn.n3ro.ghostclient.module.modules.RENDER.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    public static ArrayList<Module> modList = new ArrayList<>();

    public ModuleManager() {
        //Combat
    	addModule(new HitBox());
    	addModule(new Reach());

        //Misc


        //Movement
    	addModule(new Bhop());
        addModule(new Eagle());
        addModule(new InvMove());
        addModule(new Sprint());
        addModule(new Time());

        //Player


        //Render
        addModule(new Chams());
        addModule(new ClickGui());
        addModule(new ESP());
        addModule(new HUD());
        addModule(new NoHurtCam());
        addModule(new ViewClip());
    }


    public void addModule(Module module) {
        modList.add(module);
    }

    public static List<Module> getModList() {
        return modList;
    }

    public List<Module> getModules() {
        return modList;
    }

    public static List<Module> getModules(Category category) {
        ArrayList<Module> mods = new ArrayList<Module>();
        for (Module mod : ModuleManager.getModList()) {
            if (mod.getCategory() == category) {
                mods.add(mod);
            }
        }
        return mods;
    }
    public static <T extends Module> T getModule(Class<T> clazz) {
        return (T) modList.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null);
    }

    public static Module getModuleByName(String name) {
        for (Module m : ModuleManager.getModList()) {
            if (!m.getName().equalsIgnoreCase(name))
                continue;
            return m;
        }
        return null;
    }
}
