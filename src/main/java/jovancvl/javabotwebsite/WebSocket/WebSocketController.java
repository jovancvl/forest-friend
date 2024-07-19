package jovancvl.javabotwebsite.WebSocket;

import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.Track;
import jovancvl.javabotwebsite.Bot.Music.GuildMusicManager;
import jovancvl.javabotwebsite.Bot.Music.MusicManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@Controller
public class WebSocketController {

    @Autowired
    MusicManager musicManager;

    // play, pause, add song, remove song, fast-forward/backwards?

    // play/pause
    @MessageMapping("/controls/{server}/pauseOrResume")
    @SendTo("/controls/{server}/pauseOrResume")
    public WebSocketMessage playOrPause(@DestinationVariable String server, WebSocketMessage message){
        long guildId = Long.parseLong(server);
        System.out.println("pauseOrResume fired");
        WebSocketMessage response;
        if (this.musicManager.getLavalinkClient().getOrCreateLink(guildId).getCachedPlayer().getPaused()){
            response = new WebSocketMessage("resumed");
            this.musicManager.getLavalinkClient().getOrCreateLink(guildId).getPlayer().flatMap((player) -> player.setPaused(false)).subscribe();
        } else {
            response = new WebSocketMessage("paused");
            this.musicManager.getLavalinkClient().getOrCreateLink(guildId).getPlayer().flatMap((player) -> player.setPaused(true)).subscribe();
        }
        return response;
    }

    // skip
    @MessageMapping("/controls/{server}/skip")
    //@SendTo("/controls/{server}/skip")
    public void skipSong(@DestinationVariable String server, WebSocketMessage message) {
        long guildId = Long.parseLong(server);

        Track nextTrack = this.musicManager.getSongQueue(guildId).poll();

        this.musicManager.getLavalinkClient().getOrCreateLink(guildId).getPlayer()
                .flatMap((player) -> player.setTrack(nextTrack))
                .subscribe();

        //return new WebSocketMessage(nextTrack != null ? nextTrack.getInfo().getTitle() : null);
    }

    // receives song to add to queue and adds it
    @MessageMapping("/controls/{server}/add")
    public void addSong(@DestinationVariable String server, WebSocketMessage songNameOrUrl) {
        long guildId = Long.parseLong(server);
        Link link = this.musicManager.getLavalinkClient().getOrCreateLink(guildId);
        GuildMusicManager manager = this.musicManager.getOrCreateGuildMusicManager(guildId);
        String identifier = songNameOrUrl.getMessage();

        try {
            URL url = new URI(identifier).toURL();
            identifier = url.toString();
        } catch (MalformedURLException | URISyntaxException e) {
            identifier = "ytsearch:" + identifier;
        }

        link.loadItem(identifier).subscribe(new WebsiteAudioLoader(manager, guildId));
    }
}
