/*
 * Decompiled with CFR 0_132.
 */
package cn.n3ro.ghostclient.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;

public class FontLoaders {
	public CFontRenderer getFont(int size) {
		return new CFontRenderer(new Font("default", 0, size), true, true);
	}
}
