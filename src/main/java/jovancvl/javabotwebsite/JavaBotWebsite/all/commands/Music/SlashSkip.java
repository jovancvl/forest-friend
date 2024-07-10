package jovancvl.javabotwebsite.JavaBotWebsite.all.commands.Music;

import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import jovancvl.javabotwebsite.JavaBotWebsite.all.commands.MusicSlashCommand;
import dev.arbjerg.lavalink.client.player.Track;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashSkip implements MusicSlashCommand {
    @Override
    public void run(SlashCommandInteractionEvent event, MusicManager musicManager) {
        Guild guild = event.getGuild();
        Member m = event.getMember();
        Member b = event.getGuild().getSelfMember();
        MemberVCState state = VoiceChannelHelper.ifMemberAndBotInSameVC(m, b);

        if (state != MemberVCState.SAME_VC) {
            event.reply("We need to be in the same voice channel.").queue();
            return;
        }

        Track nextTrack = musicManager.getSongQueue(guild.getIdLong()).poll();

        musicManager.getLavalinkClient().getOrCreateLink(guild.getIdLong()).getPlayer()
                .flatMap((player) -> player.setTrack(nextTrack))
                .subscribe((player) -> event.reply(String.format("Skipping, now playing: %s | <%s>", player.getTrack().getInfo().getTitle(), player.getTrack().getInfo().getUri())).queue());

    }

    @Override
    public String getCommand() {
        return "skip";
    }

}