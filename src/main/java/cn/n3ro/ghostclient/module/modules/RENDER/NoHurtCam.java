package cn.n3ro.ghostclient.module.modules.RENDER;

import cn.n3ro.ghostclient.events.EventUpdate;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import com.darkmagician6.eventapi.EventTarget;

public class NoHurtCam extends Module {
    public NoHurtCam() {
        super("NoHurtCam", Category.RENDER);
    }

    public static boolean no;

    @Override
    public void set(boolean state) {
        if (state){
            no = true;
        }else{
            no = false;
        }
        super.set(state);
    }
}
