package sample.hello;

import java.io.Serializable;

/**
 * Created by Roy on 01/06/2017.
 */
public abstract class Message implements Serializable {
    public abstract String getUserName();
    public abstract String getRoomName();
}
