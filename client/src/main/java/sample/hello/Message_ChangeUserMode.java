package sample.hello;

/**
 * Created by Bella on 5/31/2017.
 */
public class Message_ChangeUserMode extends  Message {
    private UserMode mode;

    public Message_ChangeUserMode(UserMode mode)
    {
        this.mode =mode;
    }

    public void setUserMode (UserMode mode)
    {
        this.mode = mode;
    }

    public UserMode getUserMode()
    {
        return mode;
    }
}
