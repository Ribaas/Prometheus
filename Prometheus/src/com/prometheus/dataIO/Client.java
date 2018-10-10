package com.prometheus.dataIO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.prometheus.core.Prometheus;
import com.prometheus.log.Logger;
import com.prometheus.utils.BlockType;
import com.prometheus.utils.InfoBlock;

public class Client implements Runnable{
    
    private volatile static int lastID = 0;
    private static synchronized int getNewID(){
        
        lastID++;
        return lastID;
        
    }
    
    //Fields
    private final int ID;
    
    private final IOAdmin parentIO;
    
    private ClientDevice cdvc;
    private final Socket socket;
    
    private Logger logger;
    
    private ObjectInputStream input = null;
    private ObjectOutputStream output = null;
    
    private boolean running = false;
    //private boolean reading;
    
    private boolean logged = false;
    
    //private Thread inputThread;
    //private Thread outputThread;
    //private ClientInput input;
    //private ClientOutput output;
    
    //Getters and Setters

    public int getID() {
        return ID;
    }
    
    public ClientDevice getDevice(){
        
        return this.cdvc;
        
    }
    void setDevice(ClientDevice cdvc){
        
        this.cdvc = cdvc;
        
    }
    
    public Socket getSocket(){
        
        return this.socket;
        
    }
    
    public Logger getLogger(){
        
        return this.logger;
        
    }
    
    public boolean isLogged(){
        
        return this.logged;
        
    }
    protected void setLogged(boolean logged, long authKey){

        this.logged = logged;
        
    }

    public boolean isRunning() {
        
        return running;
        
    }
    public void setRunning(boolean running) {
        
        this.running = running;
        
    }
    

    //Constructor
    Client(ClientDevice cdvc, Socket clientSocket, IOAdmin io) throws IOException{
        
        this.ID = Client.getNewID();
        
        this.cdvc = cdvc;
        this.socket = clientSocket;
        this.parentIO = io;

        //input = new ClientInput(this);
        
        //inputThread = new Thread(input);
        //inputThread.start();

    }

    @Override
    public void run() {
        
        //Initializing
        String address = socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort();
        
        String fileName = "Cliente(" + socket.getInetAddress().getHostAddress().replace('.', '-') + ").txt";
        logger = Logger.getNewLogger(fileName);
        
        try{
            
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
            
            logger.log("Comunicacao estabelecida com o cliente (" + address + ").", Thread.currentThread());
            
        }
        catch(Exception ex){
            
            logger.log("Nao foi possivel estabelecer comunicacao com o cliente (" + address + "): " + ex.toString(), Thread.currentThread());
            
            running = false;
        }
                
        
        //Main code
        while(running){
            
            if(!logged){
                
                //Authentication
                try {
            
                    InfoBlock infoBlock = (InfoBlock)input.readObject();

                    if(infoBlock.getType() == BlockType.COMMAND && infoBlock.getContent()[0].equals("<login>")){

                        //some login code
                        logger.log("Autenticando cliente (" + address + ")", Thread.currentThread());

                        setLogged(true, 1234L);

                    }
                    else{

                        output.writeObject(new InfoBlock(BlockType.INFORMATION, false, new Object[]{"<message>","Acesso negado!"} ) );

                    }
                    
                }
                catch (Exception ex) {

                    logger.log("Nao foi possivel receber informação do cliente (" + address + ") : " + ex.toString(), Thread.currentThread());

                }
                
            }
            else{
                
                try {
                
                    InfoBlock infoBlock = (InfoBlock)input.readObject();

                    if(infoBlock.isValid()){

                        switch( infoBlock.getType() ){

                            case INFORMATION:
                                InfoHandler.information(this, infoBlock);
                                break;

                            case REQUEST:
                                InfoHandler.request(this, infoBlock);
                                break;

                            case COMMAND:
                                InfoHandler.command(this, infoBlock);
                                break;

                        }

                    }
                
                }
                catch (Exception ex) {

                    logger.log("Nao foi possivel receber informação do cliente (" + address + ") : " + ex.toString(), Thread.currentThread());

                }
                
            }
            
        }
        
        
    }
    
    
    
    public void disconnect(){
        
        running = false;
        logged = false;
        
        try{
            //input.close();
            //output.close();

            socket.close();
            
            parentIO.getClients().remove(this);

            logger.log("Cliente (" + socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort() + ") desconectado", Thread.currentThread());
            Prometheus.getMainLogger().log("Cliente (" + socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort() + ") desconectado", Thread.currentThread());
            
                
        }
        catch (Exception ex) {

            logger.log("Nao foi possivel desconetar o cliente (" + socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort() + "): " + ex.toString(), Thread.currentThread());

        }
        
        try{

            logger.closeLogger();
            
        }
        catch(Exception ex){
            
            System.out.println("Nao foi possivel encerrar o Logger do Cliente(" + ID + ")");
            ex.printStackTrace();
            
        }
        
    }
    
}
