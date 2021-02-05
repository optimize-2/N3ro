package cn.n3ro.ghostclient.command.commands;

import cn.n3ro.ghostclient.command.Command;
import cn.n3ro.ghostclient.management.ModuleManager;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.utils.ClientUtil;

public class CommandToggle extends Command {

    public CommandToggle(String[] commands) {
        super(commands);
        this.setArgs(".toggle <module>");
    }

    @Override
    public void onCmd(String[] args) {
        if(args.length < 2) {
            ClientUtil.sendClientMessage(this.getArgs());
        } else {
            String mod = args[1];
            for (Module m : ModuleManager.getModList()) {
                if(m.getName().equalsIgnoreCase(mod)) {
                    m.set(!m.isEnable());
                    ClientUtil.sendClientMessage( m.getName() + " " + (!m.isEnable() ? "was \u00a7cdisabled" : "was \u00a7aenabled"));
                    return;
                }
            }
        }
    }
}
