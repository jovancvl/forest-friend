package jovancvl.javabotwebsite.WebSocket;

import java.util.List;

public class WebSocketMessage {
    private String message;
    private List<String> songList = null;

    public WebSocketMessage(String message) {
        this.message = message;
    }

    public WebSocketMessage(String query, List<String> songList) {
        this.songList = songList;
        this.message = query;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getSongList() {
        return songList;
    }

    public void setSongList(List<String> songList) {
        this.songList = songList;
    }

    @Override
    public String toString() {
        return "WebSocketMessage{" +
                "message='" + message + '\'' +
                ", songList=" + songList +
                '}';
    }
}
