package jovancvl.javabotwebsite.JavaBotWebsite;

import jovancvl.javabotwebsite.JavaBotWebsite.all.Bot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

@SpringBootApplication
public class JavaBotWebsiteApplication {

	@Autowired
	Bot bot;

	public static void main(String[] args) {
		SpringApplication.run(JavaBotWebsiteApplication.class, args);
	}

	@PostConstruct
	public void startBot() throws InterruptedException {
		bot.startBot();
	}

}
