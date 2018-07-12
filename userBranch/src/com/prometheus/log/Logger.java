package com.prometheus.log;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe respons�vel pelo registro (Log) do sistema.
 */
public final class Logger {
    
    private final FileWriter fileWriter;
    private final BufferedWriter bufferedWriter;
    
    //Armazena um padr�o de m�scara para formatar data e hora
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
    
    /**
     * Retorna o {@link BufferedWriter} da inst�ncia do Logger.
     * 
     * @return O BufferedWriter
     */
    public BufferedWriter getBufferedWriter() {
        return bufferedWriter;
    }

    private Logger(String fileName) throws IOException{

        fileWriter = new FileWriter(fileName, true);
        bufferedWriter = new BufferedWriter(fileWriter);
        
    }
    
    /**
     * Instancia e prepara o Logger para criar um arquivo de Log, utilizando o nome passado por par�metro.
     * 
     * @param fileName  Nome do arquivo de Log com extens�o.
     * @return  Uma inst�ncia da classe Logger se n�o houver uma IOException;
     *          null caso contr�rio.
     */
    public static Logger getNewLogger(String fileName){
        
        try {
            
            return new Logger(fileName);
            
        }
        catch(IOException ex){
            
            System.out.println("Nao foi possivel iniciar um novo Logger");
            return null;
            
        }
        
    }
    
    /**
     * Registra uma linha no Log com informa��o de data e hora atual.
     * 
     * @param msg Mensagem a ser registrada no Log
     */
    public void log(String msg){
        
        try {
            
            bufferedWriter.write(LocalDateTime.now().format(dtf));
            bufferedWriter.write(" -> ");
            bufferedWriter.write(msg);
            bufferedWriter.newLine();
            
        } catch (IOException ex) {
            
            System.out.println("Nao foi possivel registrar a mensagem");
            
        }
    }
    
    /**
     * Registra uma linha no Log sem informa��o de data e hora atual.
     * 
     * @param msg mensagem a ser registrada no Log
     */
    public void logWithoutTime(String msg){
        
        try {
            
            bufferedWriter.write(" -> ");
            bufferedWriter.write(msg);
            bufferedWriter.newLine();
            
        } catch (IOException ex) {
            
            System.out.println("Nao foi possivel registrar a mensagem");
            
        }
        
    }
    
}
