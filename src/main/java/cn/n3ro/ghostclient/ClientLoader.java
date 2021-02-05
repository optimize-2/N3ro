package cn.n3ro.ghostclient;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

public class ClientLoader implements IFMLLoadingPlugin {
	public static boolean runtimeDeobfuscationEnabled = false;

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"cn.snowflake.rose.asm.ClassTransformer"};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		runtimeDeobfuscationEnabled = (boolean)data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
	
}
