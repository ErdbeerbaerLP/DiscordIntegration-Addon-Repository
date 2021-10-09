package de.erdbeerbaerlp.dcintegrationExecCmd;


import de.erdbeerbaerlp.dcintegration.common.Discord;
import de.erdbeerbaerlp.dcintegration.common.addon.AddonConfigRegistry;
import de.erdbeerbaerlp.dcintegration.common.addon.DiscordIntegrationAddon;
import de.erdbeerbaerlp.dcintegration.common.storage.CommandRegistry;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.concurrent.CompletableFuture;

public class ExecCommandAddon implements DiscordIntegrationAddon, EventListener {
    static VoiceConfig cfg;
    Discord discord;

    @Override
    public void load(Discord dc) {
        cfg = AddonConfigRegistry.registerConfig(VoiceConfig.class, this);
        discord = dc;
        System.out.println("Addon loaded");
        if (dc.getJDA() != null) {
            if (CommandRegistry.registerCommand(new ExecCommand()))
                dc.getJDA().addEventListener(this);
        }
    }

    @Override
    public void reload() {
        cfg = AddonConfigRegistry.loadConfig(cfg);
    }

    @Override
    public void unload(Discord dc) {
        if (dc.getJDA() != null) {
            dc.getJDA().removeEventListener(this);
        }
    }

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof SlashCommandEvent) {
            if(((SlashCommandEvent) event).getName().equals("exec")){
                final CompletableFuture<InteractionHook> reply = ((SlashCommandEvent) event).deferReply().submit();
                final OptionMapping cmd = ((SlashCommandEvent) event).getOption("cmd");
                discord.srv.runMcCommand(cmd.getAsString(),reply,((SlashCommandEvent) event).getUser());
            }
        }
    }
}
