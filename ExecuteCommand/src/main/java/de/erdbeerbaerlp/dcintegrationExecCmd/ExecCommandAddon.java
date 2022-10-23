package de.erdbeerbaerlp.dcintegrationExecCmd;


import de.erdbeerbaerlp.dcintegration.common.Discord;
import de.erdbeerbaerlp.dcintegration.common.addon.AddonConfigRegistry;
import de.erdbeerbaerlp.dcintegration.common.addon.DiscordIntegrationAddon;
import de.erdbeerbaerlp.dcintegration.common.storage.CommandRegistry;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.concurrent.CompletableFuture;

public class ExecCommandAddon implements DiscordIntegrationAddon, EventListener {
    static ExecuteCommandConfig cfg;
    Discord discord;

    @Override
    public void load(Discord dc) {
        cfg = AddonConfigRegistry.loadConfig(ExecuteCommandConfig.class, this);
        discord = dc;
        System.out.println("Addon loaded");
        if (dc.getJDA() != null) {
            if (CommandRegistry.registerCommand(new ExecCommand()))
                dc.getJDA().addEventListener(this);
        }
    }

    @Override
    public void reload() {
        cfg = AddonConfigRegistry.loadConfig(cfg, this);
    }

    @Override
    public void unload(Discord dc) {
        if (dc.getJDA() != null) {
            dc.getJDA().removeEventListener(this);
        }
    }

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof SlashCommandInteractionEvent) {
            if(((SlashCommandInteractionEvent) event).getName().equals("exec")){
                final CompletableFuture<InteractionHook> reply = ((SlashCommandInteractionEvent) event).deferReply().submit();
                final OptionMapping cmd = ((SlashCommandInteractionEvent) event).getOption("cmd");
                discord.srv.runMcCommand(cmd.getAsString(),reply,((SlashCommandInteractionEvent) event).getUser());
            }
        }
    }
}
