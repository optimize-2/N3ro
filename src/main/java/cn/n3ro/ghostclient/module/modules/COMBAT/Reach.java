package cn.n3ro.ghostclient.module.modules.COMBAT;

import cn.n3ro.ghostclient.events.EventUpdate;
import cn.n3ro.ghostclient.management.ModuleManager;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.value.Numbers;

import com.darkmagician6.eventapi.EventTarget;

public class Reach extends Module {

	@SuppressWarnings("unused")
	public static Numbers<Double> reach = new Numbers<Double>("Reach", 3.0d, 3.0d, 6.0d, 0.1d);
    public Reach() {
        super("Reach", Category.COMBAT);
        this.addValues(reach);
    }
    
    @EventTarget
    private void onUpdate(EventUpdate event) {
    	System.out.println("Range: "+reach.getValue());
    	
    }
    
    @Override
    public void onDisable() {
    	
    }

    public static double getReach() {
		return ModuleManager.getModuleByName("Reach").isEnable() ? reach.getValue().floatValue() : 3.0f;
	}

	
}
