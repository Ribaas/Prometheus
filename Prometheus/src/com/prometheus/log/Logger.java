package com.prometheus.log;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe responsável pelo registro (Log) do sistema.
 */
public final class Logger{
    
    private final FileWriter fileWriter;
    private final BufferedWriter bufferedWriter;
    
    //Armazena um padrão de máscara para formatar data e hora
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
    
    /**
     * Retorna o {@link BufferedWriter} da instância do Logger.
     * 
     * @return O BufferedWriter
     */
    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }
    
    //Construtor do Logger
    private Logger(String fileName) throws IOException{

        fileWriter = new FileWriter(fileName, true);
        bufferedWriter = new BufferedWriter(fileWriter);
               
    }
    
    /**
     * Instancia e prepara um Logger para criar um arquivo de Log, utilizando o nome passado por parâmetro.
     * 
     * @param fileName  Nome do arquivo de Log com extensão.
     * @return  Uma instância da classe Logger se não houver uma IOException;<br>
     *          <code>null</code> caso contrário.
     */
    public static Logger getNewLogger(String fileName){
        
        try {
            
            return new Logger(fileName);
            
        }
        catch(Exception ex){
            
            System.out.println("Nao foi possivel iniciar um novo Logger");
            ex.printStackTrace();
            
            return null;
            
        }
        
    }
    
    /**
     * Registra uma linha no Log com informação de data e hora atual.
     * 
     * @param msg Mensagem a ser registrada no Log.
     * @param th Thread que requisita a gravação no Log.
     */
    public void log(String msg, Thread th){
        
        String fullmsg = LocalDateTime.now().format(dtf) + " (" + Thread.currentThread().getStackTrace()[2].getClassName() + "[" + th.getName() + "]) -> " + msg;
        System.out.println(fullmsg);
        
        try{
            
            bufferedWriter.write(fullmsg);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            
        }
        catch(Exception ex){
            
            System.out.println("Nao foi possivel gravar no log:");
            ex.printStackTrace();
            
        }
        
    }
    
    /**
     * Registra uma linha no Log sem informação de data e hora atual.
     * 
     * @param msg mensagem a ser registrada no Log
     * @param th Thread que requisita a gravação no Log.
     */
    public void logWithoutTime(String msg, Thread th){
        
        //String que armazena a mensagem completa a ser gravada no Log
        String fullmsg = "(" + Thread.currentThread().getStackTrace()[2].getClassName() + "[" + th.getName() + "]) -> " + msg;
        System.out.println(fullmsg);
        
        try{
            
            bufferedWriter.write(fullmsg);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            
        }
        catch(Exception ex){
            
            System.out.println("Nao foi possivel gravar no log:");
            ex.printStackTrace();
            
        }
    }
    
    /**
     * Fecha o Logger e encerra a gravação de arquivo. 
     * 
     * @throws IOException se não for possível encerrar a gravação corretamente.
     */
    public void closeLogger() throws IOException{
        
        bufferedWriter.write("========================================================================================");
        bufferedWriter.newLine();
        bufferedWriter.flush();
        
        getBufferedWriter().close();
        
    }
    
}