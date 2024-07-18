package jovancvl.javabotwebsite.JavaBotWebsite.all.commands.Music;

import jovancvl.javabotwebsite.JavaBotWebsite.all.Constants;
import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import jovancvl.javabotwebsite.JavaBotWebsite.all.commands.MusicSlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class SlashWebsiteControl implements MusicSlashCommand {
    @Override
    public void run(SlashCommandInteractionEvent event, MusicManager musicManager) {

        Member m = event.getMember();
        Member b = event.getGuild().getSelfMember();
        MemberVCState state = VoiceChannelHelper.ifMemberAndBotInSameVC(m, b);

        switch (state) {
            case MEMBER_NOT_IN_VC :
                event.reply("You need to be in a voice channel.").queue();
                return;
            case BOT_NOT_IN_VC:
                event.getJDA().getDirectAudioController().connect(m.getVoiceState().getChannel());
                break;
            case DIFFERENT_VC:
                event.reply("We need to be in the same voice channel.").queue();
                return;
        }

        event.reply("Click the button to access music control on the website").addActionRow(
                Button.link(Constants.websiteURL + event.getGuild().getId(), "website")
        ).queue();
    }

    @Override
    public String getCommand() {
        return "control";
    }
}
