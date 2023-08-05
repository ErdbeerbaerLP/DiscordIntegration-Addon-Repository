package de.erdbeerbaerlp.dcintegrationVoiceNotif;


import dcshadow.net.kyori.adventure.text.Component;
import dcshadow.net.kyori.adventure.text.event.ClickEvent;
import dcshadow.net.kyori.adventure.text.event.HoverEvent;
import dcshadow.net.kyori.adventure.text.format.Style;
import dcshadow.net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import dcshadow.org.apache.commons.lang3.ArrayUtils;
import de.erdbeerbaerlp.dcintegration.common.DiscordIntegration;
import de.erdbeerbaerlp.dcintegration.common.addon.AddonConfigRegistry;
import de.erdbeerbaerlp.dcintegration.common.addon.DiscordIntegrationAddon;
import de.erdbeerbaerlp.dcintegration.common.storage.Localization;
import de.erdbeerbaerlp.dcintegration.common.util.ComponentUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.reflect.InvocationTargetException;

public class VoiceNotificationAddon extends ListenerAdapter implements DiscordIntegrationAddon {
    private VoiceConfig cfg;
    DiscordIntegration discord;

    @Override
    public void load(DiscordIntegration dc) {
        cfg = AddonConfigRegistry.loadConfig(VoiceConfig.class, this);
        discord = dc;
        DiscordIntegration.LOGGER.info("Voice-Notifications Addon loaded");
        if (dc.getJDA() != null)
            dc.getJDA().addEventListener(this);
    }

    @Override
    public void reload() {
        cfg = AddonConfigRegistry.loadConfig(cfg,this);
    }

    @Override
    public void unload(DiscordIntegration dc) {
        if (dc.getJDA() != null)
            dc.getJDA().removeEventListener(this);
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        final Class<? extends GuildVoiceUpdateEvent> aClass = event.getClass();
        AudioChannel joinedChannel,leftChannel;
        Member member;
        try {
            joinedChannel= (AudioChannel) aClass.getMethod("getChannelJoined").invoke(event);
            leftChannel = (AudioChannel) aClass.getMethod("getChannelLeft").invoke(event);
            member = (Member) aClass.getMethod("getMember").invoke(event);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        if (cfg.voiceChannels.length == 0 || (joinedChannel != null && ArrayUtils.contains(cfg.voiceChannels, joinedChannel.getId())) || (leftChannel != null && ArrayUtils.contains(cfg.voiceChannels, leftChannel.getId()))) {
            if (joinedChannel != null && leftChannel == null) {
                discord.getServerInterface().sendIngameMessage(LegacyComponentSerializer.legacySection().deserialize(cfg.joinMessage.replace("%dstName%", joinedChannel.getName())).replaceText(ComponentUtils.replace("%name%", Component.text(member.getEffectiveName()).style(Style.empty().clickEvent(ClickEvent.suggestCommand("<@" + member.getId() + ">")).hoverEvent(HoverEvent.showText(Component.text(Localization.instance().discordUserHover.replace("%user#tag%", member.getUser().getAsTag()).replace("%user%", member.getEffectiveName()).replace("%id%", member.getId()))))))));
            } else if (leftChannel != null && joinedChannel == null) {
                discord.getServerInterface().sendIngameMessage(LegacyComponentSerializer.legacySection().deserialize(cfg.leaveMessage.replace("%sourceName%", leftChannel.getName())).replaceText(ComponentUtils.replace("%name%", Component.text(member.getEffectiveName()).style(Style.empty().clickEvent(ClickEvent.suggestCommand("<@" + member.getId() + ">")).hoverEvent(HoverEvent.showText(Component.text(Localization.instance().discordUserHover.replace("%user#tag%", member.getUser().getAsTag()).replace("%user%", member.getEffectiveName()).replace("%id%", member.getId()))))))));

            } else{
                discord.getServerInterface().sendIngameMessage(LegacyComponentSerializer.legacySection().deserialize(cfg.moveMessage.replace("%sourceName%", leftChannel.getName()).replace("%dstName%", joinedChannel.getName())).replaceText(ComponentUtils.replace("%name%", Component.text(member.getEffectiveName()).style(Style.empty().clickEvent(ClickEvent.suggestCommand("<@" + member.getId() + ">")).hoverEvent(HoverEvent.showText(Component.text(Localization.instance().discordUserHover.replace("%user#tag%", member.getUser().getAsTag()).replace("%user%", member.getEffectiveName()).replace("%id%", member.getId()))))))));
            }
        }
    }

}
