package com.prometheus.core;

import com.prometheus.log.Logger;
import java.io.IOException;

/**
 * Classe principal do sistema.
 */
public class Prometheus {
    
    //Fields
    private static Logger mainLogger;

    
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
        
        
        
        close();
    }
    
    /**
     * Fecha a aplicação terminando seus serviços.
     */
    public static void close(){
        
        mainLogger.log("A aplicacao esta encerrando", Thread.currentThread());
        
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
