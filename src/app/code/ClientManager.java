package app.code;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientManager {

    private  Socket socket;
    private  int PORT=7789;
    private  String ADDRESS="127.0.0.1";
    private  ObjectOutputStream outputStream;
    private  ObjectInputStream inputStream;
    private  boolean connected=false;

    public ClientManager(){}

    public ClientManager(int PORT,String address){
        this.PORT=PORT;
        this.ADDRESS=address;
    }

    public String initialize_connection(){
        try {
            this.socket=new Socket(this.ADDRESS,this.PORT);
            this.outputStream=new ObjectOutputStream(this.socket.getOutputStream());
            this.inputStream=new ObjectInputStream(this.socket.getInputStream());
            this.connected=true;
            return "Connection established successfully.";
        } catch (Exception e) {
            this.connected=false;
            return "Unable to connect to the server please make sure the server is started and try again. ";
        }
    }

    public Word getWord(String key){
        Word word=null;
        try {
            Message message=new Message(Commads.GETWORD,key);
            this.outputStream.writeObject(message);
            this.outputStream.flush();
            word=(Word)((Message) this.inputStream.readObject()).data;
        }catch (Exception e) { this.connected=false;}

        return word;
    }

    public boolean addWord(Word word){
        if(this.getWord(word.getWord())!=null) return false;
        boolean answer=false;
        try {
            Message message=new Message(Commads.ADD,word);
            this.outputStream.writeObject(message);
            this.outputStream.flush();
            answer=(boolean)((Message) this.inputStream.readObject()).data;
        }catch (Exception e) { this.connected=false;}
        return answer;
    }

    public boolean removeWord(String key){
        boolean answer=false;
        try {
            Message message=new Message(Commads.DELETE,key);
            this.outputStream.writeObject(message);
            this.outputStream.flush();
            answer=(boolean)((Message) this.inputStream.readObject()).data;
        }catch (Exception e) { this.connected=false;}
        return answer;
    }

    public boolean replaceWord(Word word){
        boolean answer=false;
        try {
            Message message=new Message(Commads.UPDATE,word);
            this.outputStream.writeObject(message);
            this.outputStream.flush();
            answer=(boolean)((Message) this.inputStream.readObject()).data;
        }catch (Exception e) { this.connected=false;}
        return answer;
    }

    public List getSuggestion(String key){
        List meanings=null;
        try {
            Message message=new Message(Commads.SEARCH,key);
            this.outputStream.writeObject(message);
            this.outputStream.flush();
            meanings=(List) ((Message) this.inputStream.readObject()).data;
        } catch (Exception e) {this.connected=false;}
        return meanings;
    }

    public boolean isConnected(){
        return this.connected;
    }

    public void exit(){
        try {
            this.outputStream.close();
            this.inputStream.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
