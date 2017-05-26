package sample.hello;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bella on 5/16/2017.
 */
public class Message_NewClient implements Serializable {
    private String username;
    private String channel;
    private String timeStamp ;

    Message_NewClient(String username, String channel)
    {
        this.username = username;
        this.channel = channel;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        timeStamp  = dateFormat.format(new Date());
    }

    public String getUsername(){
        return this.username;
    }

    public String getChannel(){
        return this.channel;
    }

    public String getTimeStamp(){
        return this.timeStamp;
    }

}
