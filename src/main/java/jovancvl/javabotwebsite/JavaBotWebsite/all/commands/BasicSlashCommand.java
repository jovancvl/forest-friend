package jovancvl.javabotwebsite.JavaBotWebsite.all.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface BasicSlashCommand {
    void run(SlashCommandInteractionEvent event);
    String getCommand();
}