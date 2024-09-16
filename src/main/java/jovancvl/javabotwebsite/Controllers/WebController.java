package jovancvl.javabotwebsite.Controllers;

import jovancvl.javabotwebsite.Bot.Music.MusicManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebController {

    @Autowired
    MusicManager musicManager;

    @GetMapping("")
    public String index(){
        System.out.println("home page opened");
        return "index";
    }

    @GetMapping("/controls/{server}")
    public String play(Model model, @PathVariable String server) {
        //System.out.println("controls accessed on server " + server);
        long guildId = Long.parseLong(server);
        model.addAttribute("server", guildId);
        String songName = "No song is playing";
        try {
            songName = musicManager.getLavalinkClient().getOrCreateLink(guildId).getCachedPlayer().getTrack().getInfo().getTitle();
        } catch (NullPointerException e){

        }

        List<String> songList = this.musicManager.getSongQueue(guildId).stream().map((track) -> track.getInfo().getTitle()).toList();


        model.addAttribute("song", songName);
        model.addAttribute("queue", songList);
        return "controls";
    }
}