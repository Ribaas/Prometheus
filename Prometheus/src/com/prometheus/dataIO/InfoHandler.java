package com.prometheus.dataIO;

import com.prometheus.core.Prometheus;
import com.prometheus.utils.InfoBlock;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class InfoHandler {
    
    public static void information(Client client, InfoBlock infoBlock){
        
        String information = (String)infoBlock.getContent()[0];
        
        switch(information){
 
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
        
        //switch(request)
        return null;
    }
    
    public static InfoBlock command(Client client, InfoBlock infoBlock){
        
        String command = (String)infoBlock.getContent()[0];
        
        switch(command){
            
            case "<log>":
                
                if(infoBlock.getContent()[1].equals("main")){
                    
                    String msg = (String) infoBlock.getContent()[2];
                    Prometheus.getMainLogger().log("Cliente(" + client.getSocket().getInetAddress().getHostAddress() + ":" + client.getSocket().getLocalPort() + "): " + msg, Thread.currentThread());
                    
                }
                else if(infoBlock.getContent()[1].equals("client")){
                    
                    String msg = (String) infoBlock.getContent()[2];
                    client.getLogger().log(msg, Thread.currentThread());
                    
                }
                
                break;
                
            case "<disconnect>":
                
                client.disconnect();
        
                break;
            
        }
        
        return null;
    }
    
}
