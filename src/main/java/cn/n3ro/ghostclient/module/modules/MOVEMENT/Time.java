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
	public static Numbers<Float> TimerSpeed = new Numbers<Float>("TimeSpeed", 1F, 0.0F, 2F, 1F);
    public Time() {
        super("Timer", Category.MOVEMENT);
    }
    
    @EventTarget
    private void onUpdate(EventUpdate event) {
    	 Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[]{Mappings.timer});
         timer.timerSpeed = TimerSpeed.getValue();
    }
    
    @Override
    public void onDisable() {
    	 Timer timer = ReflectionHelper.getPrivateValue(Minecraft.class, mc, new String[]{Mappings.timer});
         timer.timerSpeed = 1F;
    }
    //forge注入	
    
    //asm的用另一个类这里还没写
}
