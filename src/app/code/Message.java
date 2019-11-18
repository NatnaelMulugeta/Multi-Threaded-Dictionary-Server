package app.code;

import java.io.Serializable;

public class Message implements Serializable {
    public Commads command;
    public Object  data;

    public Message(Commads command,Object  data){
        this.command=command;
        this.data=data;
    }
}
