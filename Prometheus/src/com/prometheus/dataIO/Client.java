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
            
        	//Checks if the Client is logged, if not proceeds with authentication, otherwise proceeds with communication 
            if(!logged){
                
                //Authentication
                try {
                	
                	//Receives an InfoBlock
                    InfoBlock infoBlock = (InfoBlock)input.readObject();

                    //Verifies if the InfoBlock received is requesting Login
                    if(infoBlock.getType() == BlockType.COMMAND && infoBlock.getContent()[0].equals("<login>")){

                        //Authentication code
                        logger.log("Autenticando cliente (" + address + ")", Thread.currentThread());

                        setLogged(true, 1234L);

                    }
                    else{
                    	
                    	//If the InfoBlock received is not requesting login the access is denied 
                        output.writeObject(new InfoBlock(BlockType.INFORMATION, false, "<message>","Acesso negado!") );

                    }
                    
                }
                catch (Exception ex) {

                    logger.log("Nao foi possivel receber informação do cliente (" + address + ") : " + ex.toString(), Thread.currentThread());

                }
                
            }
            else{
            	
            	//Communication
            	
            	InfoBlock response = null;
            	boolean responseAllowed = false; //Records if a response is allowed
            	
                try {
                	
                	//Receives an InfoBlock
                    InfoBlock infoBlock = (InfoBlock)input.readObject();

                    //Checks if the InfoBlock is valid
                    if(infoBlock.isValid()){

                    	//Checks the InfoBlock type and treats it
                        switch( infoBlock.getType() ){

                            case INFORMATION:
                            	
                            	responseAllowed = false; //No response allowed
                                InfoHandler.information(this, infoBlock);
                                
                                break;
                                

                            case REQUEST:
                            	
                            	responseAllowed = true; //Response allowed
                                response = InfoHandler.request(this, infoBlock);
                                
                                break;
                                

                            case COMMAND:
                            	
                            	//Response only allowed if not a <disconnect> command
                            	if( infoBlock.getContent()[0].equals("<disconnect>") )responseAllowed = false;
                            	else responseAllowed = true;
                            	
                                response = InfoHandler.command(this, infoBlock);
                                
                                break;
                                
                        }

                    }
                
                }
                catch (Exception ex) {

                    logger.log("Nao foi possivel receber informacao do cliente (" + address + ") : " + ex.toString(), Thread.currentThread());
                    response = new InfoBlock(BlockType.INFORMATION, false, "<error>", "Não foi possivel receber informação");

                }
                
                //Checks if a response is allowed and proceed accordingly
                if(responseAllowed) {
                	
                	//If there is no response a InfoBlock is instantiated to inform it is <null>
                	if(response == null) response = new InfoBlock(BlockType.INFORMATION, false, "<null>");
                	
                	//The response is sent
                	try {
                        
                        output.writeObject(response);
                        
                    }
                    catch (Exception ex) {

                        logger.log("Nao foi possivel enviar informacao ao cliente (" + address + ") : " + ex.toString(), Thread.currentThread());

                    }
                	
                }
 
                
            }
            
        }
        
        
    }
    
    
    //Disconnects the Client by closing its Socket and Logger and removing from the IOAdmin ArrayList of clients.
    public void disconnect(){
        
        running = false;
        logged = false;
        
        try{
            //input.close();
            //output.close();
            socket.close();

            logger.log("Cliente (" + socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort() + ") desconectado", Thread.currentThread());
            Prometheus.getMainLogger().log("Cliente (" + socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort() + ") desconectado", Thread.currentThread());
            
                
        }
        catch (Exception ex) {

            logger.log("Nao foi possivel desconetar o cliente (" + socket.getInetAddress().getHostAddress() + ":" + socket.getLocalPort() + "): " + ex.toString(), Thread.currentThread());

        }
        
        parentIO.getClients().remove(this);
        
        try{

            logger.closeLogger();
            
        }
        catch(Exception ex){
            
            System.out.println("Nao foi possivel encerrar o Logger do Cliente(" + ID + ")");
            ex.printStackTrace();
            
        }
        
    }
    
}
