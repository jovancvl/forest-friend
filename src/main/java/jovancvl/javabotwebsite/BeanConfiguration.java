package jovancvl.javabotwebsite;

import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import jovancvl.javabotwebsite.Bot.Constants;
import jovancvl.javabotwebsite.Bot.Music.MusicManager;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class BeanConfiguration {

    @Bean
    public LavalinkClient lavalinkClient(){
        LavalinkClient lavalinkClient = new LavalinkClient(Helpers.getUserIdFromToken(Constants.botToken));
        MusicManager.registerLavalinkNodes(lavalinkClient);
        MusicManager.registerLavalinkListeners(lavalinkClient);
        return lavalinkClient;
    }
}
