package jovancvl.javabotwebsite.Bot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface BasicSlashCommand {
    void run(SlashCommandInteractionEvent event);
    String name();
}
