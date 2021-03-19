package cn.n3ro.ghostclient.module.modules.MOVEMENT;

import cn.n3ro.ghostclient.events.EventTick;
import cn.n3ro.ghostclient.management.ModuleManager;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.utils.JReflectUtility;
import cn.n3ro.ghostclient.utils.Mappings;
import cn.n3ro.ghostclient.value.Numbers;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import com.darkmagician6.eventapi.EventTarget;

public class Time extends Module {
    public static Numbers<Double> timerspeeds = new Numbers<Double>("TimeSpeed", 1D, 0.1D, 10D, 0.1D);
    public Time() {
        super("Timer", Category.MOVEMENT);
        this.addValues(timerspeeds);
    }

    @EventTarget
    private void onUpdate(EventTick event) {
        JReflectUtility.timer().timerSpeed = timerspeeds.getValue().floatValue();
    }
    @Override
    public void onEnable() {
        JReflectUtility.timer().timerSpeed = timerspeeds.getValue().floatValue();
    }
    @Override
    public void onDisable() {
        JReflectUtility.timer().timerSpeed = 1F;
    }

}
