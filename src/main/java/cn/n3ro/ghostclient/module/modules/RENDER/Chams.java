package cn.n3ro.ghostclient.module.modules.RENDER;

import cn.n3ro.ghostclient.events.EventRenderPlayer;
import cn.n3ro.ghostclient.events.EventUpdate;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.module.modules.RENDER.GuiClick.GuiClickUI;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Chams extends Module {
    public Chams() {
        super("Chams", Category.RENDER);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @EventTarget
    public void onrenderplayer(EventRenderPlayer e){
//        if (e.getType() == EventType.PRE){
//            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
//            GL11.glPolygonOffset(1.0F, -2000000F);
//        }else if (e.getType() == EventType.POST){
//            GL11.glPolygonOffset(1.0F, 2000000F);
//            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
//        }
    }
}
