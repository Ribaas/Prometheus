package com.prometheus.log;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe responsável pelo registro (Log) do sistema.
 */
public final class Logger {
    
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

    private Logger(String fileName) throws IOException{

        fileWriter = new FileWriter(fileName);
        bufferedWriter = new BufferedWriter(fileWriter);
        
    }
    
    /**
     * Instancia e prepara o Logger para criar um arquivo de Log, utilizando o nome passado por parâmetro.
     * 
     * @param fileName  Nome do arquivo de Log com extensão.
     * @return  Uma instância da classe Logger se não houver uma IOException;
     *          null caso contrário.
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
     * Registra uma linha no Log com informação de data e hora atual.
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
     * Registra uma linha no Log sem informação de data e hora atual.
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
