package jovancvl.javabotwebsite.Bot.commands.Other;

import jovancvl.javabotwebsite.Bot.commands.BasicSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashPing implements BasicSlashCommand {
    @Override
    public void run(SlashCommandInteractionEvent event) {
        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                .flatMap(v ->
                        event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                ).queue(); // Queue both reply and edit
    }

    @Override
    public String name() {
        return "ping";
    }
}
