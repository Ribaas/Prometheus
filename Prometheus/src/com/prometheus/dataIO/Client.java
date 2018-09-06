package com.prometheus.dataIO;

import com.prometheus.core.Prometheus;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable{
    
    private volatile static int lastID = 100;
    private static synchronized int getNewID(){
        
        lastID++;
        return lastID;
        
    }
    
    //Fields
    private final int ID;
    
    private ClientDevice cdvc;
    private final Socket socket;
    
    private boolean logged = false;
    
    //private Thread inputThread;
    //private Thread outputThread;
    //private ClientInput input;
    //private ClientOutput output;
    
    //Getters and Setters
    public Socket getSocket(){
        
        return this.socket;
        
    }
    
    public boolean isLogged(){
        
        return this.logged;
        
    }
    protected void setLogged(boolean logged, long authKey){
        
        this.logged = logged;
        
    }
    
    
    //Constructor
    Client(ClientDevice cdvc, Socket clientSocket) throws IOException{
        
        this.ID = Client.getNewID();
        
        this.cdvc = cdvc;
        this.socket = clientSocket;
        
        //input = new ClientInput(this);
        
        //inputThread = new Thread(input);
        //inputThread.start();

    }

    @Override
    public void run() {
        
        Prometheus.getMainLogger().log("Comunicacao estabelecida com o cliente (" + socket.getInetAddress().getHostAddress() + ").", Thread.currentThread());
        
    }
    
    
    
    public void disconnect() throws Exception{
        
                
        socket.close();
        
        
    }
    
}
