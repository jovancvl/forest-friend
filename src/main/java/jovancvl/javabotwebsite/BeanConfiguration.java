package jovancvl.javabotwebsite;

import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import jovancvl.javabotwebsite.Bot.Music.MusicManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
