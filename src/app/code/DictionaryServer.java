package app.code;

import javax.swing.*;
import java.io.IOException;
import java.net.*;

public class DictionaryServer {
    private IOManager ioManager;
    private int PORT=7789;
    private ServerSocket server;
    private String filename="dictionary.json";

    public DictionaryServer(){}
    public DictionaryServer(int port, String filename){
        this.PORT=port;
        this.filename=filename;
    }

    public void initialize_server(){
        try{

            this.server= new ServerSocket(this.PORT);
            this.ioManager=new IOManager(this.filename);

        }catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void start_server(){
        this.ioManager.populateDictionary();
        DictionaryThreadPool dictionaryThreadPool= new DictionaryThreadPool(10);
        System.out.println("----server started-----");
        while(true){
            try {
                Socket socket= this.server.accept();
                System.out.println("new client at "+socket.getInetAddress()+":"+socket.getPort());
                ///UserHandler userHandler=new UserHandler(socket,this.ioManager);
                ///.start();
                dictionaryThreadPool.execute(new UserHandler(socket,this.ioManager));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public static void main(String[] arg) {
        DictionaryServer server=null;
        if (arg.length>0){
            try{
                server =new DictionaryServer(Integer.parseInt(arg[0]),arg[1]);
            }catch (Exception e){
                JOptionPane.showMessageDialog(null," Please fill a valid Port number and filename. ");
                return;
            }
        }else{
            server=new DictionaryServer();
        }
        server.initialize_server();
        server.start_server();
    }

}
