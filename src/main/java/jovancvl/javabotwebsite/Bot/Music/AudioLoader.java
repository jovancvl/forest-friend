package jovancvl.javabotwebsite.Bot.Music;

import jovancvl.javabotwebsite.Bot.Constants;
import jovancvl.javabotwebsite.Bot.MyUserData;
import dev.arbjerg.lavalink.client.AbstractAudioLoadResultHandler;
import dev.arbjerg.lavalink.client.player.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AudioLoader extends AbstractAudioLoadResultHandler {
    private final SlashCommandInteractionEvent event;
    private final GuildMusicManager mngr;

    public AudioLoader(SlashCommandInteractionEvent event, GuildMusicManager mngr) {
        this.event = event;
        this.mngr = mngr;
    }

    @Override
    public void ontrackLoaded(@NotNull TrackLoaded result) {
        final Track track = result.getTrack();

        var userData = new MyUserData(event.getUser().getIdLong());

        track.setUserData(userData);

        this.mngr.scheduler.enqueue(track);

        final var trackTitle = track.getInfo().getTitle();

        event.getHook().sendMessage("Adding to queue: " + trackTitle + " | <" + track.getInfo().getUri() + '>')
                .addActionRow(Button.link(Constants.websiteURL + event.getGuild().getId(), "Website"))
                .queue();
    }

    @Override
    public void onPlaylistLoaded(@NotNull PlaylistLoaded result) {
        final int trackCount = result.getTracks().size();
        event.getHook()
                .sendMessage("Added " + trackCount + " tracks to the queue from " + result.getInfo().getName() + "!")
                .addActionRow(Button.link(Constants.websiteURL + event.getGuild().getId(), "Website"))
                .queue();

        this.mngr.scheduler.enqueuePlaylist(result.getTracks());
    }

    @Override
    public void onSearchResultLoaded(@NotNull SearchResult result) {
        final List<Track> tracks = result.getTracks();

        if (tracks.isEmpty()) {
            event.getHook().sendMessage("No tracks found!").queue();
            return;
        }

        final Track firstTrack = tracks.getFirst();

        event.getHook().sendMessage("Adding to queue: " + firstTrack.getInfo().getTitle() + " | <" + firstTrack.getInfo().getUri() + '>')
                .addActionRow(Button.link(Constants.websiteURL + event.getGuild().getId(), "Website"))
                .queue();

        this.mngr.scheduler.enqueue(firstTrack);
    }

    @Override
    public void noMatches() {
        event.getHook().sendMessage("No matches found for your input!").queue();
    }

    @Override
    public void loadFailed(@NotNull LoadFailed result) {
        event.getHook().sendMessage("Failed to load track! " + result.getException().getMessage()).queue();
    }
}