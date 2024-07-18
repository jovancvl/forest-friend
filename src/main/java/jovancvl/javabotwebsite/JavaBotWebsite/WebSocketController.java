package jovancvl.javabotwebsite.JavaBotWebsite;

import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.player.Track;
import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.AudioLoader;
import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.GuildMusicManager;
import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.MalformedURLException;
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
            URL url = new URL(identifier);
            identifier = url.toString();
        } catch (MalformedURLException e) {
            identifier = "ytsearch:" + identifier;
        }

        link.loadItem(identifier).subscribe(new WebsiteAudioLoader(manager, guildId));
    }
}
