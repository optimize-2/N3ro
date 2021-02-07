package cn.n3ro.ghostclient.module.modules.MOVEMENT;

import cn.n3ro.ghostclient.events.EventUpdate;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.utils.Mappings;
import cn.n3ro.ghostclient.value.Numbers;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import com.darkmagician6.eventapi.EventTarget;

public class Time extends Module {
    public static Numbers<Double> timerspeeds = new Numbers<Double>("TimeSpeed", 1D, 0.5D, 2D, 0.01D);
    public Time() {
        super("Timer", Category.MOVEMENT);
        this.addValues(timerspeeds);
    }
    
    @EventTarget
    private void onUpdate(EventUpdate event) {
    	 Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[]{Mappings.timer});
         timer.timerSpeed = timerspeeds.getValue().floatValue();
         
    }
    
    @Override
    public void onDisable() {
    	 Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[]{Mappings.timer});
         timer.timerSpeed = 1F;
    }

}
