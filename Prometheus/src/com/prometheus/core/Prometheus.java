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
    
    public static void main(String[] args) throws InterruptedException{
        
        mainLogger = Logger.getNewLogger("test1.txt");
        mainLogger.log("A aplicacao esta iniciando", Thread.currentThread());
        
        mainIO = IOAdmin.getNewIOAdmin(50000);
        mainIO.setRunning(true);
        mainIO.setAcceptingClients(true);
        new Thread(mainIO, "thIOAdmin").start();
        
        Thread.sleep(8000);
        
        close();
        
    }
    
    /**
     * Fecha a aplicação terminando seus serviços.
     */
    public static void close() throws InterruptedException{
        
        mainLogger.log("A aplicacao esta encerrando", Thread.currentThread());
        
        mainIO.setAcceptingClients(false);
        mainIO.setRunning(false);
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
