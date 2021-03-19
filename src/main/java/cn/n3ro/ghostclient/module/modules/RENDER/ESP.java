package cn.n3ro.ghostclient.module.modules.RENDER;

import cn.n3ro.ghostclient.events.EventRender3D;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;

import com.darkmagician6.eventapi.EventTarget;


public class ESP extends Module {


    public ESP() {
        super("ESP", Category.RENDER);
    }

    @EventTarget
    public void onScreen(EventRender3D event) {
    }





}
