package cn.n3ro.ghostclient.management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import cn.n3ro.ghostclient.Client;
import cn.n3ro.ghostclient.module.Module;
import cn.n3ro.ghostclient.utils.ClientUtil;
import cn.n3ro.ghostclient.value.Mode;
import cn.n3ro.ghostclient.value.Numbers;
import cn.n3ro.ghostclient.value.Option;
import cn.n3ro.ghostclient.value.Value;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class FileManager {
	private static File dir;
	private static final Minecraft mc = Minecraft.getMinecraft();
	private final String fileLocation;

	static {
		final File mcDataDir = Minecraft.getMinecraft().mcDataDir;
		FileManager.dir = new File(mcDataDir, Client.CLIENT_NAME);
	}

	public FileManager() {
		fileLocation = mc.mcDataDir.getAbsolutePath() + "/" + Client.CLIENT_NAME;
		File file = new File(fileLocation);
		if (!file.exists())
			file.mkdirs();

		File cfg = new File(fileLocation + "/Config");
		if (!cfg.exists()) {
			cfg.mkdirs();
		}
	}

	public void loadConfig(String configName) {
		File settingsFile = new File(fileLocation + "/Config/" + configName + "^" + "Setting" + ".txt");
		if (settingsFile.exists()) {
			try {
				String line;
				BufferedReader br = new BufferedReader(new FileReader(settingsFile));
				while ((line = br.readLine()) != null) {
					if (!line.contains(":"))
						continue;
					String name = line.split(":")[0];
					String values = line.split(":")[1];
					Module m = Client.instance.moduleManager.getModuleByName(name);
					if (m == null)
						continue;
					for (Value<?> value : m.getValues()) {
						if (!value.getName().equalsIgnoreCase(values))
							continue;
						if (value instanceof Option) {
							Option<Boolean> boolValue = (Option) value;
							boolValue.setValue(Boolean.parseBoolean(line.split(":")[2]));
							continue;
						}
						if (value instanceof Numbers) {
							Numbers<Number> numValue = (Numbers) value;
							numValue.setValue(Double.parseDouble(line.split(":")[2]));
							continue;
						}
						if (value instanceof Mode) {
							Mode<Enum<?>> enumValue = (Mode) value;
							enumValue.setMode(line.split(":")[2]);
						}
					}
				}
			} catch (Exception e) {
				ClientUtil.sendClientMessage("Failed to load config <" + configName + ">");
				e.printStackTrace();
				return;
			}
		} else {
			ClientUtil.sendClientMessage("Failed to load config <" + configName + ">");
			return;
		}

		File enabledFile = new File(fileLocation + "/Config/" + configName + "^" + "Enable" + ".txt");
		if (enabledFile.exists()) {
			try {
				String line;
				BufferedReader br = new BufferedReader(new FileReader(enabledFile));
				while ((line = br.readLine()) != null) {
					if (!line.contains(":"))
						continue;
					String[] split = line.split(":");
					Module m = Client.instance.moduleManager.getModuleByName(split[0]);
					boolean state = Boolean.parseBoolean(split[1]);
					if (m == null)
						continue;
					if ((m.isEnable() && state == true) || (!m.isEnable() && state == false))
						continue;
					m.set(state);
				}
			} catch (Exception e) {
				ClientUtil.sendClientMessage("Failed to load config <" + configName + ">");
				e.printStackTrace();
				return;
			}
		}

		File bindFile = new File(fileLocation + "/Config/" + configName + "^" + "Bind" + ".txt");
		if (bindFile.exists()) {
			try {
				String line;
				BufferedReader br = new BufferedReader(new FileReader(bindFile));
				while ((line = br.readLine()) != null) {
					if (!line.contains(":"))
						continue;
					String[] split = line.split(":");
					Module m = Client.instance.moduleManager.getModuleByName(split[0]);
					int key = Keyboard.getKeyIndex(split[1]);
					if (m == null)
						continue;
					m.setKey(key);
				}
			} catch (Exception e) {
				ClientUtil.sendClientMessage("Failed to load config <" + configName + ">");
				e.printStackTrace();
				return;
			}
		}


		ClientUtil.sendClientMessage("Succeed to load config <" + configName + ">");
	}

	public void saveConfig(String configName) {
		File settingsFile = new File(fileLocation + "/Config/" + configName + "^" + "Setting" + ".txt");
		try {
			if (!settingsFile.exists()) {
				settingsFile.createNewFile();
			}
			PrintWriter pw = new PrintWriter(settingsFile);
			for (Module m : Client.instance.getModuleManager().getModules()) {
				for (Value<?> value : m.getValues()) {
					pw.write(m.getName() + ":" + value.getName() + ":" + value.getValue() + "\n");
				}
			}
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
			ClientUtil.sendClientMessage("Failed to save config <" + configName + ">");
			return;
		}

		File enableFile = new File(fileLocation + "/Config" + "/" + configName + "^" + "Enable.txt");
		try {
			if (!enableFile.exists()) {
				enableFile.createNewFile();
			}
			PrintWriter pw = new PrintWriter(enableFile);
			for (Module m : Client.instance.moduleManager.getModList()) {
				pw.print(m.getName() + ":" + m.isEnable() + "\n");
			}
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
			ClientUtil.sendClientMessage("Failed to save config <" + configName + ">");
		}

		File bindFile = new File(fileLocation + "/Config" + "/" + configName + "^" + "Bind.txt");
		try {
			if (!bindFile.exists()) {
				bindFile.createNewFile();
			}
			PrintWriter pw = new PrintWriter(bindFile);
			for (Module m : Client.instance.moduleManager.getModList()) {
				pw.print(m.getName() + ":" + m.getKey() + "\n");
			}
			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
			ClientUtil.sendClientMessage("Failed to save config <" + configName + ">");
		}

		ClientUtil.sendClientMessage("Succeed to save config <" + configName + ">");
	}

	public static File getConfigFile(final String name) {
		final File file = new File(FileManager.dir, String.format("%s.cfg", name));
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ex) {
			}
		}
		return file;
	}

	public static void init() {
		if (!FileManager.dir.exists()) {
			FileManager.dir.mkdir();
		}
	}

	public static List<String> read(final String file) {
		final List<String> out = new ArrayList<String>();
		try {
			if (!FileManager.dir.exists()) {
				FileManager.dir.mkdir();
			}
			final File f = new File(FileManager.dir, file);
			if (!f.exists()) {
				f.createNewFile();
			}
			Throwable t = null;
			try {
				final FileInputStream fis = new FileInputStream(f);
				try {
					final InputStreamReader isr = new InputStreamReader(fis);
					try {
						final BufferedReader br = new BufferedReader(isr);
						try {
							String line = "";
							while ((line = br.readLine()) != null) {
								out.add(line);
							}
						} finally {
							if (br != null) {
								br.close();
							}
						}
						if (isr != null) {
							isr.close();
						}
					} finally {
						if (t == null) {
							final Throwable t2 = null;
							t = t2;
						} else {
							final Throwable t2 = null;
							if (t != t2) {
								t.addSuppressed(t2);
							}
						}
						if (isr != null) {
							isr.close();
						}
					}
					if (fis != null) {
						fis.close();
						return out;
					}
				} finally {
					if (t == null) {
						final Throwable t3 = null;
						t = t3;
					} else {
						final Throwable t3 = null;
						if (t != t3) {
							t.addSuppressed(t3);
						}
					}
					if (fis != null) {
						fis.close();
					}
				}
			} finally {
				if (t == null) {
					final Throwable t4 = null;
					t = t4;
				} else {
					final Throwable t4 = null;
					if (t != t4) {
						t.addSuppressed(t4);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	public static void save(final String file, final String content, final boolean append) {
		try {
			final File f = new File(FileManager.dir, file);
			if (!f.exists()) {
				f.createNewFile();
			}
			Throwable t = null;
			try {
				final FileWriter writer = new FileWriter(f, append);
				try {
					writer.write(content);
				} finally {
					if (writer != null) {
						writer.close();
					}
				}
			} finally {
				if (t == null) {
					final Throwable t2 = null;
					t = t2;
				} else {
					final Throwable t2 = null;
					if (t != t2) {
						t.addSuppressed(t2);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
