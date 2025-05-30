package de.erdbeerbaerlp.dcintegrationExecCmd;


import de.erdbeerbaerlp.dcintegration.common.DiscordIntegration;
import de.erdbeerbaerlp.dcintegration.common.addon.AddonConfigRegistry;
import de.erdbeerbaerlp.dcintegration.common.addon.DiscordIntegrationAddon;
import de.erdbeerbaerlp.dcintegration.common.storage.CommandRegistry;

public class ExecCommandAddon implements DiscordIntegrationAddon {
    static ExecuteCommandConfig cfg;
    DiscordIntegration discord;

    @Override
    public void load(DiscordIntegration dc) {
        cfg = AddonConfigRegistry.loadConfig(ExecuteCommandConfig.class, this);
        discord = dc;
        System.out.println("Addon loaded");
        if (dc.getJDA() != null) {
            CommandRegistry.registerCommand(new ExecCommand());
        }
    }

    @Override
    public void reload() {
        cfg = AddonConfigRegistry.loadConfig(cfg, this);
    }

    @Override
    public void unload(DiscordIntegration dc) {
    }

}
