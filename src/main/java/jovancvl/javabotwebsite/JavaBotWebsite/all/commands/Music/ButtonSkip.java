package jovancvl.javabotwebsite.JavaBotWebsite.all.commands.Music;

import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import jovancvl.javabotwebsite.JavaBotWebsite.all.commands.MusicButtonCommand;
import dev.arbjerg.lavalink.client.player.Track;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class ButtonSkip implements MusicButtonCommand {
    @Override
    public void run(ButtonInteractionEvent event, MusicManager musicManager) {
        Guild guild = event.getGuild();
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
