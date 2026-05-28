package com.yirty2.ygunmod;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeyMappings {
    public static final KeyMapping RELOAD_KEY = new KeyMapping(
            "key.ygunmod.reload",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.categories.ygunmod"
    );
    public static final KeyMapping BAYONET_KEY = new KeyMapping(
            "key.ygunmod.bayonet",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B,
            "key.categories.ygunmod"
    );
}