package cn.n3ro.ghostclient.module.modules.COMBAT;

import cn.n3ro.ghostclient.events.EventUpdate;
import cn.n3ro.ghostclient.module.Category;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.value.Numbers;

import com.darkmagician6.eventapi.EventTarget;

public class Reach extends Module {

	@SuppressWarnings("unused")
	public static Numbers<Float> ReachRange = new Numbers<Float>("ReachRange", 8F, 0.0F, 8F, 1F);
    public Reach() {
        super("Reach", Category.COMBAT);
    }
    
    @EventTarget
    private void onUpdate(EventUpdate event) {
    	System.out.println(ReachRange.getValue());
    	
    }
    
    @Override
    public void onDisable() {
    	
    }

	

	
}
