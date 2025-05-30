package de.erdbeerbaerlp.dcintegrationExecCmd;

import de.erdbeerbaerlp.dcintegration.common.DiscordIntegration;
import de.erdbeerbaerlp.dcintegration.common.discordCommands.DiscordCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

import java.util.concurrent.CompletableFuture;

public class ExecCommand extends DiscordCommand {

    protected ExecCommand() {
        super("exec", ExecCommandAddon.cfg.commandDescription);
        addOption(OptionType.STRING,"cmd",ExecCommandAddon.cfg.commandArgDescription, true);
    }

    @Override
    public boolean adminOnly() {
        return ExecCommandAddon.cfg.adminOnly;
    }

    @Override
    public void execute(SlashCommandInteractionEvent ev, ReplyCallbackAction reply) {
        final OptionMapping cmd = ev.getOption("cmd");
        DiscordIntegration.INSTANCE.getServerInterface().runMcCommand(cmd.getAsString(),reply.submit(),ev.getUser());
    }
}
