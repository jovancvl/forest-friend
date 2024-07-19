package jovancvl.javabotwebsite;

import jovancvl.javabotwebsite.Bot.TsundereBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.PostConstruct;

@SpringBootApplication
public class JavaBotWebsiteApplication {

	@Autowired
	TsundereBot tsundereBot;

	public static void main(String[] args) {
		SpringApplication.run(JavaBotWebsiteApplication.class, args);
	}

	@PostConstruct
	public void startBot() throws InterruptedException {
		tsundereBot.startBot();
	}

}
