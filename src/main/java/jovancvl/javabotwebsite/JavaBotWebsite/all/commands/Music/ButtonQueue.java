package jovancvl.javabotwebsite.JavaBotWebsite.all.commands.Music;

import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import jovancvl.javabotwebsite.JavaBotWebsite.all.commands.MusicButtonCommand;
import dev.arbjerg.lavalink.client.player.Track;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.List;

public class ButtonQueue implements MusicButtonCommand {
    @Override
    public void run(ButtonInteractionEvent event, MusicManager musicManager) {
        final Guild guild = event.getGuild();
        final var link = musicManager.getLavalinkClient().getOrCreateLink(guild.getIdLong());
        final var player = link.getCachedPlayer();

        StringBuilder sb = new StringBuilder();
        List<Track> songs = musicManager.getSongQueue(guild.getIdLong()).stream().toList();

        sb.append(String.format("Now playing: %s | <%s>\n", player.getTrack().getInfo().getTitle(), player.getTrack().getInfo().getUri().toString()));

        int limit = Math.min(songs.size(), 10);
        for (int i = 0; i < limit; i++) {
            Track s = songs.get(i);
            sb.append(String.format("%d. %s | <%s>\n", i, s.getInfo().getTitle(), s.getInfo().getUri()));
        }
        event.reply(sb.toString()).queue();
    }

    @Override
    public String getCommand() {
        return "queue";
    }
}
