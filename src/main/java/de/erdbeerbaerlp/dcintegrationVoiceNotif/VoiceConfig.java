package de.erdbeerbaerlp.dcintegrationVoiceNotif;

import dcshadow.com.moandjiezana.toml.TomlComment;
import de.erdbeerbaerlp.dcintegration.common.addon.AddonConfiguration;


public class VoiceConfig extends AddonConfiguration {
    @TomlComment({"Message sent to Minecraft when someone joins an voice channel", "", "Placeholders:", "%dstName% - Destination Channel of the move", "%name% - Display name (Nickname or account name) of the Discord user"})
    public String joinMessage = "\u00A76[\u00A75DISCORD\u00A76]\u00A7a + \u00A77 %name% joined the voice channel \"\u00A73%dstName%\u00A77\"";

    @TomlComment({"Message sent to Minecraft when someone left an voice channel", "", "Placeholders:", "%sourceName% - Source Channel of the move", "%name% - Display name (Nickname or account name) of the Discord user"})
    public String leaveMessage = "\u00A76[\u00A75DISCORD\u00A76]\u00A7c - \u00A77 %name% left the voice channel \"\u00A73%sourceName%\u00A77\"";

    @TomlComment({"Message sent to Minecraft when someone moved between two channels", "", "Placeholders:", "%sourceName% - Source Channel of the move", "%dstName% - Destination Channel of the move", "%name% - Display name (Nickname or account name) of the Discord user"})
    public String moveMessage = "\u00A76[\u00A75DISCORD\u00A76]\u00A77 %name% moved from the voice channel \"\u00A73%sourceName%\u00A77\" to \"\u00A73%dstName%\u00A77\"";

    @TomlComment({"Voice channels to listen for events", "Leave empty to allow all voice channels"})
    public String[] voiceChannels = new String[0];
}
