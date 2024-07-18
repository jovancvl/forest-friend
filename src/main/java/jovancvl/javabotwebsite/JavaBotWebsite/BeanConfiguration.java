package jovancvl.javabotwebsite.JavaBotWebsite;

import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class BeanConfiguration {

    @Bean
    public LavalinkClient lavalinkClient(){
        LavalinkClient lavalinkClient = new LavalinkClient(Helpers.getUserIdFromToken(System.getenv("TsundereBotToken")));
        MusicManager.registerLavalinkNodes(lavalinkClient);
        MusicManager.registerLavalinkListeners(lavalinkClient);
        return lavalinkClient;
    }
}
