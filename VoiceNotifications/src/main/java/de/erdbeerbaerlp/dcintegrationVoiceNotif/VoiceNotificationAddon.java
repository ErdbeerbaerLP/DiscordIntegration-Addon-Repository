package de.erdbeerbaerlp.dcintegrationVoiceNotif;


import dcshadow.net.kyori.adventure.text.Component;
import dcshadow.net.kyori.adventure.text.event.ClickEvent;
import dcshadow.net.kyori.adventure.text.event.HoverEvent;
import dcshadow.net.kyori.adventure.text.format.Style;
import dcshadow.net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import dcshadow.org.apache.commons.lang3.ArrayUtils;
import de.erdbeerbaerlp.dcintegration.common.Discord;
import de.erdbeerbaerlp.dcintegration.common.addon.AddonConfigRegistry;
import de.erdbeerbaerlp.dcintegration.common.addon.DiscordIntegrationAddon;
import de.erdbeerbaerlp.dcintegration.common.storage.Configuration;
import de.erdbeerbaerlp.dcintegration.common.storage.Localization;
import de.erdbeerbaerlp.dcintegration.common.util.ComponentUtils;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.EventListener;

public class VoiceNotificationAddon implements DiscordIntegrationAddon, EventListener {
    private VoiceConfig cfg;
    Discord discord;

    @Override
    public void load(Discord dc) {
        cfg = AddonConfigRegistry.registerConfig(VoiceConfig.class, this);
        discord = dc;
        System.out.println("Voice-Notifications Addon loaded");
        if (dc.getJDA() != null)
            dc.getJDA().addEventListener(this);
    }

    @Override
    public void reload() {
        cfg = AddonConfigRegistry.loadConfig(cfg);
    }

    @Override
    public void unload(Discord dc) {
        if (dc.getJDA() != null)
            dc.getJDA().removeEventListener(this);
    }

    @Override
    public void onEvent(GenericEvent event) {
        if (event instanceof GenericGuildVoiceUpdateEvent evt) {
            final AudioChannel joinedChannel = evt.getChannelJoined();
            final AudioChannel leftChannel = evt.getChannelLeft();
            if (cfg.voiceChannels.length == 0 || (joinedChannel != null && ArrayUtils.contains(cfg.voiceChannels, joinedChannel.getId())) || (leftChannel != null && ArrayUtils.contains(cfg.voiceChannels, leftChannel.getId()))) {
                if (event instanceof GuildVoiceJoinEvent ev) {
                    discord.srv.sendMCMessage(LegacyComponentSerializer.legacySection().deserialize(cfg.joinMessage.replace("%dstName%", ev.getChannelJoined().getName())).replaceText(ComponentUtils.replace("%name%", Component.text(ev.getMember().getEffectiveName()).style(Style.empty().clickEvent(ClickEvent.suggestCommand("<@" + ev.getMember().getId() + ">")).hoverEvent(HoverEvent.showText(Component.text(Localization.instance().discordUserHover.replace("%user#tag%", ev.getMember().getUser().getAsTag()).replace("%user%",ev.getMember().getEffectiveName()).replace("%id%",ev.getMember().getId()))))))));
                } else if (event instanceof GuildVoiceLeaveEvent ev) {
                    discord.srv.sendMCMessage(LegacyComponentSerializer.legacySection().deserialize(cfg.leaveMessage.replace("%sourceName%", ev.getChannelLeft().getName())).replaceText(ComponentUtils.replace("%name%", Component.text(ev.getMember().getEffectiveName()).style(Style.empty().clickEvent(ClickEvent.suggestCommand("<@" + ev.getMember().getId() + ">")).hoverEvent(HoverEvent.showText(Component.text(Localization.instance().discordUserHover.replace("%user#tag%", ev.getMember().getUser().getAsTag()).replace("%user%",ev.getMember().getEffectiveName()).replace("%id%",ev.getMember().getId()))))))));

                } else if (event instanceof GuildVoiceMoveEvent ev) {
                    discord.srv.sendMCMessage(LegacyComponentSerializer.legacySection().deserialize(cfg.moveMessage.replace("%sourceName%", ev.getChannelLeft().getName()).replace("%dstName%", ev.getChannelJoined().getName())).replaceText(ComponentUtils.replace("%name%", Component.text(ev.getMember().getEffectiveName()).style(Style.empty().clickEvent(ClickEvent.suggestCommand("<@" + ev.getMember().getId() + ">")).hoverEvent(HoverEvent.showText(Component.text(Localization.instance().discordUserHover.replace("%user#tag%", ev.getMember().getUser().getAsTag()).replace("%user%",ev.getMember().getEffectiveName()).replace("%id%",ev.getMember().getId()))))))));

                }
            }
        }
    }
}
