package cn.n3ro.ghostclient.events;

import com.darkmagician6.eventapi.events.Event;

public class EventRender3D implements Event {
    float partialTicks;

    public EventRender3D(float partialTicks){
        this.partialTicks = partialTicks;
    }

    public EventRender3D(){
    }
}
