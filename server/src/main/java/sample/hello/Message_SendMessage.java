package sample.hello;

/**
 * Created by Bella on 5/31/2017.
 */
public class Message_SendMessage {
    private String message;
    private String username;
    private String channelname;

    Message_SendMessage(String username, String channelname) {
        this.username = username;
        this.channelname = channelname;
    }

    public String getChannel() {
        return this.channelname;
    }
}
