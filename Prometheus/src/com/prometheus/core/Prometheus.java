package com.prometheus.core;

import com.prometheus.log.Logger;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Classe principal do sistema.
 */
public class Prometheus {
    
    /**
     * O Logger principal do sistema, instanciado durante o ínicio do mesmo.
     */
    public static Logger mainLogger;
    
    public static void main(String[] args) {
        
        mainLogger = Logger.getNewLogger("test1.txt");

        mainLogger.log("A aplicacao esta iniciando");
        
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
