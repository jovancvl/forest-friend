package jovancvl.javabotwebsite.JavaBotWebsite;

import dev.arbjerg.lavalink.client.AbstractAudioLoadResultHandler;
import dev.arbjerg.lavalink.client.player.*;
import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.GuildMusicManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;


public class WebsiteAudioLoader extends AbstractAudioLoadResultHandler {

    @Autowired
    private SimpMessagingTemplate template;
    private final GuildMusicManager manager;
    private final long guildId;

    public WebsiteAudioLoader(GuildMusicManager manager, long guildId) {
        this.manager = manager;
        this.guildId = guildId;
    }

    @Override
    public void ontrackLoaded(@NotNull TrackLoaded trackLoaded) {
        final Track track = trackLoaded.getTrack();

        this.manager.scheduler.enqueue(track);

        WebSocketMessage response = new WebSocketMessage(track.getInfo().getTitle());
        String url = "/controls/" + this.guildId + "/addSong";

        this.template.convertAndSend(url, response);
    }

    @Override
    public void onPlaylistLoaded(@NotNull PlaylistLoaded playlistLoaded) {
        List<Track> songList = playlistLoaded.getTracks();
        List<String> songNamesList = new LinkedList<>(songList.stream().map((song) -> song.getInfo().getTitle()).toList());

        WebSocketMessage response = new WebSocketMessage("Playlist loaded", songNamesList);

        this.manager.scheduler.enqueuePlaylist(songList);

        this.template.convertAndSend("/controls/" + this.guildId + "/addPlaylist", response);
    }

    @Override
    public void onSearchResultLoaded(@NotNull SearchResult searchResult) {
        final List<Track> tracks = searchResult.getTracks();

        if (tracks.isEmpty()) {
            // send message back that nothing was found
            this.noMatches();
            return;
        }

        final Track firstTrack = tracks.getFirst();

        this.manager.scheduler.enqueue(firstTrack);

        // send message back that a song was found
        WebSocketMessage response = new WebSocketMessage(firstTrack.getInfo().getTitle());
        String url = "/controls/" + this.guildId + "/addSong";

        this.template.convertAndSend(url, response);

    }

    @Override
    public void noMatches() {
        WebSocketMessage response = new WebSocketMessage("No matches");
        String url = "/controls/" + this.guildId + "/noMatches";

        this.template.convertAndSend(url, response);

    }

    @Override
    public void loadFailed(@NotNull LoadFailed loadFailed) {
        WebSocketMessage response = new WebSocketMessage("loadfailed");
        String url = "/controls/" + this.guildId + "/loadFailed";

        this.template.convertAndSend(url, response);
    }
}
