package com.lx862.svrutil;

import net.fabricmc.loader.api.FabricLoader;

public class ModInfo {
    public static final String MOD_NAME = "SvrUtil-Lite";
    public static final String MOD_ID = "svrutil-lite";

    public static String getVersion() {
        try {
            return FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion()
                    .getFriendlyString();
        } catch (Exception e) {
            e.printStackTrace();
            return "<Unknown>";
        }
    }
}
