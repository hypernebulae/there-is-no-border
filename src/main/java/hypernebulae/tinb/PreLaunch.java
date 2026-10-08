package hypernebulae.tinb;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.spongepowered.asm.mixin.Mixins;

public class PreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        String version = FabricLoader.getInstance()
                .getModContainer("minecraft")
                .orElseThrow()
                .getMetadata()
                .getVersion()
                .getFriendlyString();

        if (version.startsWith("26.1")) {
            Mixins.addConfiguration("tinb-26.1.mixins.json");
        } else if (version.startsWith("26.2")) {
            Mixins.addConfiguration("tinb-26.2.mixins.json");
        } else if (version.startsWith("26.3")) {
            Mixins.addConfiguration("tinb-26.3.mixins.json");
        }
    }
}