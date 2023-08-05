package de.erdbeerbaerlp.dcintegrationMySQL;

import de.erdbeerbaerlp.dcintegration.common.DiscordIntegration;
import de.erdbeerbaerlp.dcintegration.common.addon.AddonConfigRegistry;
import de.erdbeerbaerlp.dcintegration.common.addon.DiscordIntegrationAddon;

public class MySQLAddon implements DiscordIntegrationAddon {
    static MySQLConfig cfg;
    @Override
    public void load(DiscordIntegration dc) {
        cfg = AddonConfigRegistry.loadConfig(MySQLConfig.class,this);
    }

    @Override
    public void reload() {
        cfg = AddonConfigRegistry.loadConfig(cfg, this);
    }

    @Override
    public void unload(DiscordIntegration dc) {
    }
}
