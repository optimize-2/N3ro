package cn.n3ro.ghostclient.command.commands;

import cn.n3ro.ghostclient.command.Command;
import cn.n3ro.ghostclient.management.ModuleManager;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.utils.ClientUtil;
import org.lwjgl.input.Keyboard;




public class CommandBind extends Command {
    public CommandBind(String[] command) {
        super(command);
        this.setArgs(".bind <mod> <key>");
    }

    @Override
    public void onCmd(String[] args) {
        System.out.println(args[0]);
        if(args.length != 3) {
            ClientUtil.sendClientMessage(this.getArgs());
        } else {
            String mod = args[1];
            int key = Keyboard.getKeyIndex((String)args[2].toUpperCase());
            for (Module m : ModuleManager.modList) {
                if (!m.getName().equalsIgnoreCase(mod)) continue;
                m.setKey(key);
                ClientUtil.sendClientMessage(String.valueOf(m.getName()) + " was bound to \u00a7d" + Keyboard.getKeyName((int)key));
//                Client.instance.fileMgr.saveKeys();
                return;
            }
            ClientUtil.sendClientMessage("Cannot find Module : " + mod);
        }
    }
}
