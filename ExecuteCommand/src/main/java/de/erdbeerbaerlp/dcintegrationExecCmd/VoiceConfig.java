package de.erdbeerbaerlp.dcintegrationExecCmd;

import dcshadow.com.moandjiezana.toml.TomlComment;
import de.erdbeerbaerlp.dcintegration.common.addon.AddonConfiguration;


public class VoiceConfig extends AddonConfiguration {
    @TomlComment("Command description of the execute command")
    public String commandDescription = "Executes an ingame command";
    public String commandArgDescription = "Command to execute (without slash)";

    @TomlComment({"Should this command only be available to admin roles?","Giving it to non-admins is NOT RECOMMENDED"})
    public boolean adminOnly = true;
}
