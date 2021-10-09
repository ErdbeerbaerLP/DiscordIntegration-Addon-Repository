package de.erdbeerbaerlp.dcintegrationExecCmd;

import de.erdbeerbaerlp.dcintegration.common.discordCommands.DiscordCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class ExecCommand extends DiscordCommand {

    protected ExecCommand() {
        super("exec", ExecCommandAddon.cfg.commandDescription);
        addOption(OptionType.STRING,"cmd",ExecCommandAddon.cfg.commandArgDescription);
    }

    @Override
    public void execute(SlashCommandEvent ev) {

    }

    @Override
    public boolean adminOnly() {
        return ExecCommandAddon.cfg.adminOnly;
    }
}
