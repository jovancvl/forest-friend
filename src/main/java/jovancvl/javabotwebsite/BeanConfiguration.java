package jovancvl.javabotwebsite;

import dev.arbjerg.lavalink.client.Helpers;
import dev.arbjerg.lavalink.client.LavalinkClient;
import jovancvl.javabotwebsite.Bot.Music.MusicManager;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class BeanConfiguration {

    @Bean
    public LavalinkClient lavalinkClient(){
        LavalinkClient lavalinkClient = new LavalinkClient(Helpers.getUserIdFromToken(System.getenv("TsundereBotToken")));
        System.out.println("Helpers token sent to lavalink: " + Helpers.getUserIdFromToken(System.getenv("TsundereBotToken")));
        MusicManager.registerLavalinkNodes(lavalinkClient);
        MusicManager.registerLavalinkListeners(lavalinkClient);
        return lavalinkClient;
    }

    @Bean
    public OkHttpClient okHttpClient(){
        // Needed to set a read timeout longer than 10 seconds
        return new OkHttpClient.Builder().readTimeout(30L, TimeUnit.SECONDS).build();
    }
}
