package com.lx862.svrutil;

import java.util.Collection;
import java.util.Iterator;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.Person;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.resource.featuretoggle.FeatureSet;

public class ModInfo {
    public static final String MOD_NAME = "SvrUtil-Lite";
    public static final String MOD_ID = "svrutil-lite";
    public static DynamicRegistryManager registryManager = DynamicRegistryManager.EMPTY;
    public static FeatureSet featureSet = FeatureSet.empty();

    public static String getVersion() {
        try {
            return FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion()
                    .getFriendlyString();
        } catch (Exception e) {
            e.printStackTrace();
            return "<Unknown>";
        }
    }

    public static String getDescription() {
        try {
            return FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getDescription();
        } catch (Exception e) {
            e.printStackTrace();
            return "<Unknown>";
        }
    }

    public static String getAuthors() {
        try {
            Collection<Person> authoColl = FabricLoader.getInstance().getModContainer(ModInfo.MOD_ID).get()
                    .getMetadata().getAuthors();
            String authoString = "";
            final Iterator<Person> iterator = authoColl.iterator();
            for (; iterator.hasNext();) {
                if (authoString == "") {
                    authoString = iterator.next().getName();
                } else {
                    authoString = authoString + ", " + iterator.next().getName();
                }
            }

            return authoString;
        } catch (Exception e) {
            e.printStackTrace();
            return "<Unknown>";
        }
    }

    public static String getHomepage() {
        try {
            return FabricLoader.getInstance().getModContainer(ModInfo.MOD_ID).get().getMetadata().getContact()
                    .get("homepage").get();
        } catch (Exception e) {
            e.printStackTrace();
            return "<Unknown>";
        }
    }

    public static void setRegistryManager(DynamicRegistryManager reg)
    {
        if (!reg.equals(DynamicRegistryManager.EMPTY))
        {
            registryManager = reg;
        }
    }

    public static void setFeatures(FeatureSet features)
    {
        if (!features.equals(FeatureSet.empty()))
        {
            featureSet = features;
        }
    }
}
