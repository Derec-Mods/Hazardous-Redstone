package io.github.derexxd.hazardous_redstone;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hazardous_redstone implements ModInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger("Hazardous Redstone");

    @Override
    public void onInitialize() {
    String modVersion = FabricLoader.getInstance()
        .getModContainer("hazardous_redstone")
        .map(modContainer -> modContainer.getMetadata().getVersion().getFriendlyString())
        .orElse("unknown");

    String minecraftVersion = FabricLoader.getInstance()
        .getModContainer("minecraft")
        .map(modContainer -> modContainer.getMetadata().getVersion().getFriendlyString())
        .orElse("unknown");

    String loaderVersion = FabricLoader.getInstance()
        .getModContainer("fabricloader")
        .map(modContainer -> modContainer.getMetadata().getVersion().getFriendlyString())
        .orElse("unknown");

    LOGGER.info("");
    LOGGER.info("  |_______|                             ");
    LOGGER.info("  | Derex |     Hazardous Redstone v{}", modVersion);
    LOGGER.info("  |_______|     Running on Fabric Loader {} - Minecraft {}", loaderVersion, minecraftVersion);
    LOGGER.info("");
    }
}
