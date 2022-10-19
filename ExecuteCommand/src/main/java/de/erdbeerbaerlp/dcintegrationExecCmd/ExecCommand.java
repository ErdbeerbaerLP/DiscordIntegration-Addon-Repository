package de.erdbeerbaerlp.dcintegrationExecCmd;

import de.erdbeerbaerlp.dcintegration.common.discordCommands.DiscordCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;

public class ExecCommand extends DiscordCommand {

    protected ExecCommand() {
        super("exec", ExecCommandAddon.cfg.commandDescription);
        addOption(OptionType.STRING,"cmd",ExecCommandAddon.cfg.commandArgDescription);
    }

    @Override
    public boolean adminOnly() {
        return ExecCommandAddon.cfg.adminOnly;
    }

    @Override
    public void execute(SlashCommandInteractionEvent ev, ReplyCallbackAction reply) {

    }
}
