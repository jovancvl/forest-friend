package jovancvl.javabotwebsite.JavaBotWebsite.all.commands.Music;

import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import jovancvl.javabotwebsite.JavaBotWebsite.all.commands.MusicSlashCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class SlashMusicControlButtons implements MusicSlashCommand {
    @Override
    public void run(SlashCommandInteractionEvent event, MusicManager musicManager) {
        // controls are pause, skip, queue, play
        event.reply("Control the bot with buttons").addActionRow(
                Button.link("http://localhost:80/controls/" + event.getGuild().getId(), "website")
        ).queue();
    }

    @Override
    public String getCommand() {
        return "control";
    }
}
