package jovancvl.javabotwebsite.JavaBotWebsite;

import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    MusicManager musicManager;

    @GetMapping("/controls/{server}")
    public String play(Model model, @PathVariable String server) {
        //System.out.println(server);
        long guildId = Long.parseLong(server);
        String songName = musicManager.getLavalinkClient().getOrCreateLink(guildId).getCachedPlayer().getTrack().getInfo().getTitle();
        List<String> songList = this.musicManager.getSongQueue(guildId).stream().map((track) -> track.getInfo().getTitle()).toList();

        model.addAttribute("server", guildId);
        model.addAttribute("song", songName);
        model.addAttribute("queue", songList);
        return "controls";
    }
}