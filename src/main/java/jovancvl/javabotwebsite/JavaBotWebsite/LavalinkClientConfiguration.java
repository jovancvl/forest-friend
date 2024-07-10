package jovancvl.javabotwebsite.JavaBotWebsite;

import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import jovancvl.javabotwebsite.JavaBotWebsite.all.managers.CommandManager;
import net.dv8tion.jda.api.interactions.commands.ICommandReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Priority;

@Configuration
public class LavalinkClientConfiguration {

    @Bean
    public LavalinkClient lavalinkClient(){
        LavalinkClient lavalinkClient = new LavalinkClient(Helpers.getUserIdFromToken(System.getenv("TsundereBotToken")));
        MusicManager.registerLavalinkNodes(lavalinkClient);
        MusicManager.registerLavalinkListeners(lavalinkClient);
        return lavalinkClient;
    }
}
