package app.code;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserHandler extends Thread{
    private Socket socket;
    private IOManager ioManager;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public UserHandler(Socket socket,IOManager ioManager){
        this.socket=socket;
        this.ioManager=ioManager;

        try {
            this.outputStream= new ObjectOutputStream(this.socket.getOutputStream());
            this.inputStream= new ObjectInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            this.outputStream=null;
            this.inputStream=null;
        }
    }


    @Override
    public void run() {
        try {
            Message message=new Message(Commads.SEARCH,"");
            while(message.command!=Commads.EXIT){
                message=(Message) inputStream.readObject();
                switch (message.command){
                    case ADD:{
                        message.data=ioManager.addWord((Word)message.data);
                        ioManager.saveChanges();
                        break;
                    }case DELETE:{
                        message.data=ioManager.removeWord((String) message.data);
                        ioManager.saveChanges();
                        break;
                    }case SEARCH:{
                        message.data=ioManager.getSuggestion((String) message.data);
                        break;
                    }case UPDATE:{
                        message.data=ioManager.replaceWord((Word)message.data);
                        ioManager.saveChanges();
                        break;
                    }case GETWORD:{
                        message.data=ioManager.getWord((String)message.data);
                    }
                }
                outputStream.writeObject(message);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
