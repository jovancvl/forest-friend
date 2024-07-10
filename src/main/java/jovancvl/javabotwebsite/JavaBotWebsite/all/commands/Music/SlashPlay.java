package jovancvl.javabotwebsite.JavaBotWebsite.all.commands.Music;

import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.AudioLoader;
import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import jovancvl.javabotwebsite.JavaBotWebsite.all.commands.MusicSlashCommand;
import dev.arbjerg.lavalink.client.Link;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.net.MalformedURLException;
import java.net.URL;

public class SlashPlay implements MusicSlashCommand {
    @Override
    public void run(SlashCommandInteractionEvent event, MusicManager musicManager) {
        Guild guild = event.getGuild();
        Member m = event.getMember();
        Member b = event.getGuild().getSelfMember();
        MemberVCState state = VoiceChannelHelper.ifMemberAndBotInSameVC(m, b);

        switch (state) {
            case MEMBER_NOT_IN_VC :
                event.reply("You need to be in a voice channel.").queue();
                return;
            case BOT_NOT_IN_VC:
                event.getJDA().getDirectAudioController().connect(m.getVoiceState().getChannel());
                break;
            case DIFFERENT_VC:
                event.reply("We need to be in the same voice channel.").queue();
                return;
        }


        event.deferReply(false).queue();
        String identifier = event.getOption("query").getAsString();
        final long guildId = guild.getIdLong();
        final Link link = musicManager.getLavalinkClient().getOrCreateLink(guildId);
        final var mngr = musicManager.getOrCreateGuildMusicManager(guildId);

        try {
            URL url = new URL(identifier);
            identifier = url.toString();
        } catch (MalformedURLException e) {
            identifier = "ytsearch:" + identifier;
        }

        link.loadItem(identifier).subscribe(new AudioLoader(event, mngr));
    }

    @Override
    public String getCommand() {
        return "play";
    }
}
