package com.prometheus.core;

import com.prometheus.log.Logger;
import com.prometheus.user.AcConnection;
import com.prometheus.user.Session;

import java.io.IOException;

/**
 * Classe principal do sistema.
 */
public class Prometheus {
    
    /**
     * O Logger principal do sistema, instanciado durante o ínicio do mesmo.
     */
    public static Logger mainLogger;
    
    public static void main(String[] args) throws InterruptedException {
        
        mainLogger = Logger.getNewLogger("test1.txt");

        mainLogger.log("A aplicacao esta iniciando");
        
        mainLogger.log("InThread Session running");
        
        mainLogger.log("A aplicacao esta encerrando");
        
        close();
    }
    
    /**
     * Fecha a aplicação terminando seus serviços.
     */
    public static void close(){
        
        try{
            
            mainLogger.getBufferedWriter().close();
            
        }
        catch(IOException ex){
            System.out.println("Nao foi possivel encerrar o Logger");
            return;
        }
        
        System.exit(0);
        
    }

}
