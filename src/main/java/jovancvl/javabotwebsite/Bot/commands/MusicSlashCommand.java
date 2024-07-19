package jovancvl.javabotwebsite.Bot.commands;

import jovancvl.javabotwebsite.Bot.Music.MusicManager;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface MusicSlashCommand {
    void run(SlashCommandInteractionEvent event, MusicManager musicManager);
    String getCommand();
}
