package cn.n3ro.ghostclient.events;

import com.darkmagician6.eventapi.events.Event;

public class EventRender2D implements Event {
    public float partialTicks;

    public EventRender2D(float partialTicks){
        this.partialTicks = partialTicks;
    }

    public EventRender2D(){
    }
}
