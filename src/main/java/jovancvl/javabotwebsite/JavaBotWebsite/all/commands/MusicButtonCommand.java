package jovancvl.javabotwebsite.JavaBotWebsite.all.commands;

import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface MusicButtonCommand {
    void run(ButtonInteractionEvent event, MusicManager musicManager);
    String getCommand();
}
