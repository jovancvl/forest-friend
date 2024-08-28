package jovancvl.javabotwebsite.Bot.commands.Music;

import jovancvl.javabotwebsite.Bot.Music.MusicManager;
import jovancvl.javabotwebsite.Bot.commands.MusicSlashCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class SlashLeave implements MusicSlashCommand {
    @Override
    public void run(SlashCommandInteractionEvent event, MusicManager musicManager) {
        Member m = event.getMember();
        Member b = event.getGuild().getSelfMember();
        MemberVCState state = VoiceChannelHelper.ifMemberAndBotInSameVC(m, b);

        if (state != MemberVCState.SAME_VC){
            event.reply("We need to be in the same voice channel.").queue();
            return;
        }

        musicManager.getOrCreateGuildMusicManager(event.getGuild().getIdLong()).stop();
        event.getJDA().getDirectAudioController().disconnect(event.getGuild());
        event.reply("Leaving your channel!").queue();
    }

    @Override
    public String getCommand() {
        return "leave";
    }
}
