package jovancvl.javabotwebsite.Bot.commands.Voice;

import jovancvl.javabotwebsite.Bot.Music.MusicManager;
import jovancvl.javabotwebsite.Bot.commands.MusicSlashCommand;
import dev.arbjerg.lavalink.client.player.Track;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public class SlashQueue implements MusicSlashCommand {
    @Override
    public void run(SlashCommandInteractionEvent event, MusicManager musicManager) {
        final Guild guild = event.getGuild();
        final var link = musicManager.getLavalinkClient().getOrCreateLink(guild.getIdLong());
        final var player = link.getCachedPlayer();

        try {
            StringBuilder sb = new StringBuilder();
            List<Track> songs = musicManager.getSongQueue(guild.getIdLong()).stream().toList();

            sb.append(String.format("Now playing: %s | <%s>\n", player.getTrack().getInfo().getTitle(), player.getTrack().getInfo().getUri()));

            int limit = Math.min(songs.size(), 10);
            for (int i = 0; i < limit; i++) {
                Track s = songs.get(i);
                sb.append(String.format("%d. %s | <%s>\n", i, s.getInfo().getTitle(), s.getInfo().getUri()));
            }
            event.reply(sb.toString()).queue();
        } catch (NullPointerException e) {
            event.reply("No song is playing").queue();
        }

    }

    @Override
    public String name() {
        return "queue";
    }
}
