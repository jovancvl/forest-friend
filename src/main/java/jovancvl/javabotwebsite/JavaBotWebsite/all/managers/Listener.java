package jovancvl.javabotwebsite.JavaBotWebsite.all.managers;

import jovancvl.javabotwebsite.JavaBotWebsite.all.Constants;
import dev.arbjerg.lavalink.client.LavalinkClient;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateTimeOutEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Listener extends ListenerAdapter {

    @Autowired
    CommandManager commandManager;

    private static final Logger LOG = LoggerFactory.getLogger(Listener.class);

    @Override
    public void onReady(ReadyEvent event) {
        LOG.info("{} is ready!", event.getJDA().getSelfUser().getName());
        System.out.println(event.getJDA().getSelfUser().getName() + " is online!");
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        commandManager.run(event);
    }

    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event){
        Member m = event.getEntity();
        Guild g = event.getGuild();

        try {
            Role r = g.getRolesByName("in vc", true).get(0);

            if (m.getVoiceState().inAudioChannel()){
                g.addRoleToMember(m, r).queue();
            } else {
                g.removeRoleFromMember(m, r).queue();
            }
        } catch (ArrayIndexOutOfBoundsException e){
            //System.out.println("no role found");
        }
    }

    @Override
    public void onGuildMemberUpdateTimeOut(@NotNull GuildMemberUpdateTimeOutEvent event){
        //System.out.println("Timeout update fired");
        Member m = event.getMember();
        if (m.getIdLong() == Constants.yourDaddy || m.getIdLong() == 117390865730764802L){
            //System.out.print(event.getNewTimeOutEnd() + "---------");
            if (event.getNewTimeOutEnd() != null){
                m.removeTimeout().queue();
                System.out.println("removed timeout from " + m.getAsMention());
            }
        } else {
            System.out.println("id of timed out member " + m.getIdLong());
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        commandManager.runButton(event);
    }
}
