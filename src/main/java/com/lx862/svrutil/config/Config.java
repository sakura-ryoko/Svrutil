package com.lx862.svrutil.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.fabricmc.loader.api.FabricLoader;

public class Config {
    private static final Path configFolder = Paths.get(FabricLoader.getInstance().getConfigDir().toString(),
            "svrutil-lite");

    public static Path getConfigPath(String filename) {
        try {
            configFolder.toFile().mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return configFolder.resolve(filename);
    }

    public static List<String> loadAll() {
        List<String> error = new ArrayList<>();
        if (!MainConfig.load()) {
            error.add("Main Config");
        }

        if (!CommandConfig.load()) {
            error.add("Command Config");
        }

        return error;
    }
}
