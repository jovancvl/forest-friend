package jovancvl.javabotwebsite.JavaBotWebsite.all.commands;

import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface MusicSlashCommand {
    void run(SlashCommandInteractionEvent event, MusicManager musicManager);
    String getCommand();
}
