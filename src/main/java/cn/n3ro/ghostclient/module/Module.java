package cn.n3ro.ghostclient.module;

import cn.n3ro.ghostclient.Client;
import cn.n3ro.ghostclient.management.FileManager;
import cn.n3ro.ghostclient.utils.PlayerUtil;
import cn.n3ro.ghostclient.value.Value;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Module {
    public int key;
    public String name;
    public Category category;
    public boolean enable;
    public static Minecraft mc = Minecraft.getMinecraft();
    public String displayname;
    private CheckboxMenuItem checkboxMenuItem;
    public boolean arraylistremove = true;
    public float animx = 0;
    public List<Value> values;
    public boolean enableonfirst = false;



    public Module(String name,Category category){
        this.name = name;
        this.category = category;
        this.key = -1;
        this.values = new ArrayList();
    }

    public boolean wasArrayRemoved() {
        return this.arraylistremove;
    }

    public void setArrayRemoved(boolean arraylistremove) {
        this.arraylistremove = arraylistremove;
    }

    public void setAnimx(float aanimx) {
        this.animx = aanimx;
    }

    public float getAnimx() {
        return this.animx;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean isEnable() {
        return enable;
    }

    protected void addValues(Value... values) {
        Value[] var5 = values;
        int var4 = values.length;

        for (int var3 = 0; var3 < var4; ++var3) {
            Value value = var5[var3];
            this.values.add(value);
        }

    }

    public List<Value> getValues() {
        return this.values;
    }

    public void onToggle(){
        
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public int getKey() {
        return key;
    }

    public void set(boolean stage){
        this.enable = stage;
        this.onToggle();
        if (stage){
            if (mc.theWorld != null){
                this.onEnable();
//                if (mc.thePlayer != null)
//                    mc.thePlayer.playSound("random.click", 0.2F, 0.6F);
            }

            EventManager.register(this);
        }else{
            if (mc.theWorld != null){
//                if (mc.thePlayer != null)
//                    mc.thePlayer.playSound("random.click", 0.2F, 0.5F);
                this.onDisable();
            }
            EventManager.unregister(this);
        }
    }

    public void toggleModule() {
        if (this.checkboxMenuItem != null) {
            this.checkboxMenuItem.setState(!this.checkboxMenuItem.getState());
        }
        this.set(!this.isEnable());
    }
    public void setCheckboxMenuItem(CheckboxMenuItem checkboxMenuItem) {
        this.checkboxMenuItem = checkboxMenuItem;
    }

    public void onDisable(){
        
    }
    
    public void onEnable(){
        
    }
    
}
