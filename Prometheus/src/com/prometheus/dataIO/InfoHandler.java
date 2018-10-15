package com.prometheus.dataIO;

import com.prometheus.applications.AppManager;
import com.prometheus.core.Prometheus;
import com.prometheus.utils.BlockType;
import com.prometheus.utils.InfoBlock;

public final class InfoHandler {
    
    public static void information(Client client, InfoBlock infoBlock){
        
    	//Initialization
        String information = (String)infoBlock.getContent()[0];
        
        
        //Verifies the information and treats it
        switch(information){
 
        	//Informs from which device the client is connected
            case "<device>":
                
                int deviceID = (int)infoBlock.getContent()[1];
                
                if(deviceID >= ClientDevice.values().length || deviceID < 0){
                    
                    client.setDevice(ClientDevice.getById(0));
                    
                }
                else{
                    
                    client.setDevice(ClientDevice.getById(deviceID));
                    
                }
                
                client.getLogger().log("Dispositivo do cliente: " + client.getDevice().toString(), Thread.currentThread());
        
                break;
                            
        }
        
    }
    
    public static InfoBlock request(Client client, InfoBlock infoBlock){
        
    	//Initialization
    	String request = (String)infoBlock.getContent()[0];
    	InfoBlock response = null;
    	
    	//Verifies the request and treats it
        switch(request) {
        
        	//Response to inform that the request was invalid
	        default:
	        	response = new InfoBlock(BlockType.INFORMATION, false, "<error>", "Requisição inválida");
        
        }
        
        //Returns the response
        return response;
    }
    
    public static InfoBlock command(Client client, InfoBlock infoBlock){
        
    	//Initialization
        String command = (String)infoBlock.getContent()[0];
        InfoBlock response = null;
        
        //Verifies the command and executes it
        switch(command){
            
        	//Records a message on the log
            case "<log>":
                
            	//Verifies on which log the message will be recorded and proceeds accordingly
                if(infoBlock.getContent()[1].equals("main")){
                    
                    String msg = (String) infoBlock.getContent()[2];
                    Prometheus.getMainLogger().log("Cliente(" + client.getSocket().getInetAddress().getHostAddress() + ":" + client.getSocket().getLocalPort() + "): " + msg, Thread.currentThread());
                    
                }
                else if(infoBlock.getContent()[1].equals("client")){
                    
                    String msg = (String) infoBlock.getContent()[2];
                    client.getLogger().log(msg, Thread.currentThread());
                    
                }
                else {
                	
                	//Response to inform that the command was invalid
                	response = new InfoBlock(BlockType.INFORMATION, false, "<error>", "Comando inválido");
                	
                }
                
                break;
                
            //Application related commands
            case "<application>":
            	
            	if(infoBlock.getContent()[1].equals("<start>")) {
            		
            		try {
            			
						AppManager.startApplication( (String)infoBlock.getContent()[2] );
						
					} catch (Exception e) {
						
						response = new InfoBlock(BlockType.INFORMATION, false, "<error>", "Não foi possível iniciar a aplicação: " + e.getMessage());
						
					}
            		
            	}
            	else if(infoBlock.getContent()[1].equals("<stop>")) {
            		
            		AppManager.stopApplication();
            		
            	}
            	
            	break;
                
            //Disconnects the client    
            case "<disconnect>":
                
                client.disconnect();
        
                break;
                
            //Response to inform that the command was invalid
            default:
            	response = new InfoBlock(BlockType.INFORMATION, false, "<error>", "Comando inválido");
            
        }
        
        //Returns the response
        return response;
    }
    
}
