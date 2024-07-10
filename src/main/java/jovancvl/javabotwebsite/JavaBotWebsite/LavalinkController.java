package jovancvl.javabotwebsite.JavaBotWebsite;

import dev.arbjerg.lavalink.client.player.Track;
import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import jovancvl.javabotwebsite.JavaBotWebsite.all.managers.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
public class LavalinkController {

    @Autowired
    MusicManager musicManager;

    @GetMapping("/controls/{server}")
    public String play(@PathVariable String server) {
        //System.out.println(server);
        long guildId = Long.parseLong(server);
        return musicManager.getLavalinkClient().getOrCreateLink(guildId).getCachedPlayer().getTrack().getInfo().getTitle();
    }

    @PostMapping("/controls/{server}")
    public String skip(@PathVariable String server){
        long guildId = Long.parseLong(server);
        Track nextTrack = musicManager.getSongQueue(guildId).poll();

        musicManager.getLavalinkClient().getOrCreateLink(guildId).getPlayer()
                .flatMap((player) -> player.setTrack(nextTrack)).subscribe();
        return musicManager.getLavalinkClient().getOrCreateLink(guildId).getCachedPlayer().getTrack().getInfo().getTitle();
    }
}