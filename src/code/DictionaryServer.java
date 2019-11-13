package app.code;

import java.io.IOException;
import java.net.*;

public class DictionaryServer {
    private IOManager ioManager;
    private int PORT;
    private ServerSocket server;

    public DictionaryServer(int port){
        this.PORT=port;
    }

    public void initialize_server(){
        try{

            this.server= new ServerSocket(this.PORT);
            this.ioManager=new IOManager();

        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void start_server(){
        this.ioManager.populateDictionary();
        while(true){
            try {
                System.out.println("----server start-----");
                Socket socket= this.server.accept();
                new UserHandler(socket,this.ioManager).start();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }


}
