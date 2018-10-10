package com.prometheus.core;

import com.prometheus.dataIO.IOAdmin;
import com.prometheus.log.Logger;

/**
 * Classe principal do sistema.
 */
public class Prometheus {
    
    //Fields
    private static Logger mainLogger;
    private static IOAdmin mainIO;

    
    //Getters and Setters
    
    /**
     * Retorna o {@link Logger} principal do sistema, instanciado durante o ínicio do mesmo.
     * 
     * @return O Logger
     */
    public static Logger getMainLogger(){
        
        return Prometheus.mainLogger;
        
    }
    
    
    
    public static void main(String[] args) throws Exception{
        
    	//Logger initialization
        mainLogger = Logger.getNewLogger("log1.txt");
        mainLogger.log("A aplicacao esta iniciando", Thread.currentThread());
        
        //Main IOAdmin initialization
        mainIO = IOAdmin.getNewIOAdmin(50000);
        mainIO.setRunning(true);
        mainIO.setAcceptingClients(true);
        new Thread(mainIO, "thIOAdmin").start();
        
        
        //Waits the client connection and communication before closing the application
        Thread.sleep(15000);
        
        
        close();
        
    }
    
    /**
     * Fecha a aplicação terminando seus serviços.
     */
    public static void close() throws InterruptedException{
        
        mainLogger.log("A aplicacao esta encerrando", Thread.currentThread());
        
        mainIO.close();
        Thread.sleep(500);
        
        try{

            mainLogger.closeLogger();
            
        }
        catch(Exception ex){
            
            System.out.println("Nao foi possivel encerrar o Logger");
            ex.printStackTrace();
            
        }
        
        System.exit(0);
        
    }

}
