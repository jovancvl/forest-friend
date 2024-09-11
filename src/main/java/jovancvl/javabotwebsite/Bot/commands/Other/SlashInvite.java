package jovancvl.javabotwebsite.Bot.commands.Other;

import jovancvl.javabotwebsite.Bot.Constants;
import jovancvl.javabotwebsite.Bot.commands.BasicSlashCommand;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class SlashInvite implements BasicSlashCommand {
    @Override
    public void run(SlashCommandInteractionEvent event) {
        event.reply("You can add this bot to your server by clicking the button below!")
                .addActionRow(Button.link(Constants.inviteUrl, Emoji.fromUnicode("U+1F917")))
                .queue();
    }

    @Override
    public String name() {
        return "invite";
    }
}
