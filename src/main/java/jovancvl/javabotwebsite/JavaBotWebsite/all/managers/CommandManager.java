package jovancvl.javabotwebsite.JavaBotWebsite.all.managers;

import jovancvl.javabotwebsite.JavaBotWebsite.all.Music.MusicManager;
import jovancvl.javabotwebsite.JavaBotWebsite.all.commands.Music.*;
import jovancvl.javabotwebsite.JavaBotWebsite.all.commands.MusicSlashCommand;
import jovancvl.javabotwebsite.JavaBotWebsite.all.commands.BasicSlashCommand;
import jovancvl.javabotwebsite.JavaBotWebsite.all.commands.Other.SlashPing;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class CommandManager {
    private static final HashMap<String, BasicSlashCommand> commands = new HashMap<>();
    private static final HashMap<String, MusicSlashCommand> musicCommands = new HashMap<>();
    @Autowired
    public MusicManager musicManager;


    public CommandManager() {
        this.addCommand(new SlashPing());
        this.addMusicCommand(new SlashJoin());
        this.addMusicCommand(new SlashLeave());
        this.addMusicCommand(new SlashPlay());
        this.addMusicCommand(new SlashStop());
        this.addMusicCommand(new SlashSkip());
        this.addMusicCommand(new SlashQueue());
        this.addMusicCommand(new SlashWebsiteControl());
    }

    public void addCommand(BasicSlashCommand command){
        commands.put(command.getCommand(), command);
    }

    public void addMusicCommand(MusicSlashCommand command){
        musicCommands.put(command.getCommand(), command);
    }

    public void run(SlashCommandInteractionEvent event) {
        String name = event.getName();

        BasicSlashCommand cmd = commands.get(name);
        if (cmd != null){
            cmd.run(event);
            return;
        }

        MusicSlashCommand musicCommand = musicCommands.get(name);
        if (musicCommand != null) {
            musicCommand.run(event, this.musicManager);
            return;
        }

        event.reply("Command does not exist.").queue();
        System.out.printf("Command %s does not exist.", name);

    }
}
