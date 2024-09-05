package jovancvl.javabotwebsite.Bot.Music;

import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.LavalinkNode;
import dev.arbjerg.lavalink.client.NodeOptions;
import dev.arbjerg.lavalink.client.event.*;
import dev.arbjerg.lavalink.client.player.Track;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Service
public class MusicManager {
    private static final Logger LOG = LoggerFactory.getLogger(MusicManager.class);

    private static final HashMap<Long, GuildMusicManager> guildMusicManagers = new HashMap<>();

    @Autowired
    LavalinkClient lavalinkClient;

    public Queue<Track> getSongQueue(long guildId){
        return this.getOrCreateGuildMusicManager(guildId).scheduler.queue;
    }

    public LavalinkClient getLavalinkClient() {
        return lavalinkClient;
    }

    public GuildMusicManager getOrCreateGuildMusicManager(long guildId) {
        synchronized(this) {
            var mng = guildMusicManagers.get(guildId);

            if (mng == null) {
                mng = new GuildMusicManager(guildId, this.lavalinkClient);
                guildMusicManagers.put(guildId, mng);
            }

            return mng;
        }
    }

    public static void registerLavalinkNodes(LavalinkClient client) {
        client.addNode(
                new NodeOptions.Builder()
                        .setName("localhost")
                        .setServerUri("http://[::1]:2333")
                        .setPassword("youshallnotpass")
                        .setHttpTimeout(60000L) // in ms
                        .build()
        );

        client.getNodes().forEach((node) -> node.on(TrackStartEvent.class).subscribe((event) -> {
            final LavalinkNode node1 = event.getNode();

            LOG.trace(
                    "{}: track started: {}",
                    node1.getName(),
                    event.getTrack().getInfo()
            );
        }));
    }

    public static void registerLavalinkListeners(LavalinkClient client) {
        client.on(ReadyEvent.class).subscribe((event) -> {
            final LavalinkNode node = event.getNode();

            LOG.info(
                    "Node '{}' is ready, session id is '{}'!",
                    node.getName(),
                    event.getSessionId()
            );
        });

        client.on(StatsEvent.class).subscribe((event) -> {
            final LavalinkNode node = event.getNode();

            LOG.info(
                    "Node '{}' has stats, current players: {}/{} (link count {})",
                    node.getName(),
                    event.getPlayingPlayers(),
                    event.getPlayers(),
                    client.getLinks().size()
            );
        });


        client.on(TrackStartEvent.class).subscribe((event) ->
                Optional.ofNullable(guildMusicManagers.get(event.getGuildId())).ifPresent(
                        (mng) -> mng.scheduler.onTrackStart(event.getTrack())
                ));

        client.on(TrackEndEvent.class).subscribe((event) ->
                Optional.ofNullable(guildMusicManagers.get(event.getGuildId())).ifPresent(
                        (mng) -> mng.scheduler.onTrackEnd(event.getTrack(), event.getEndReason())
                ));

        client.on(EmittedEvent.class).subscribe((event) -> {
            final var node = event.getNode();

            LOG.info(
                    "Node '{}' emitted event: {}",
                    node.getName(),
                    event
            );
        });

    }
}
