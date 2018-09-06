package com.prometheus.dataIO;

import com.prometheus.core.Prometheus;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;


public final class IOAdmin implements Runnable{
    
    //Fields
    private ServerSocket server;
    private final int port;
    
    private ArrayList<Thread> clientThreads;
    private ArrayList<Client> clients;
    
    private boolean running = false;
    private boolean acceptingClients = false;

    
    //Getters and Setters
    /**
     * Retorna o {@link ServerSocket} da instância do IOAdmin.
     * 
     * @return O ServerSocket
     */
    public ServerSocket getServerSocket() {
        
        return server;
        
    }
    /**
     * Retorna o número da porta TCP utilizada pelo Socket.
     * 
     * @return O ServerSocket
     */
    public int getPort() {
        
        return port;
        
    }

    /**
     * Retorna o ArrayList contendo as {@link Thread} dos Clients conectados a esta instância do IOAdmin.
     * 
     * @return O ArrayList
     */
    public ArrayList<Thread> getClientThreads() {
        
        return clientThreads;
        
    }
    /**
     * Retorna o ArrayList contendo os {@link Client} conectados a esta instância do IOAdmin.
     * 
     * @return O ArrayList
     */
    public ArrayList<Client> getClients() {
        
        return clients;
        
    }
    
    /**
     * Verifica se esta instância do IOAdmin está em execução.
     * 
     * @return <code>True</code> se estiver em execução;<br> 
     * <code>False</code> caso contrário.
     */
    public boolean isRunning() {
        
        return running;
        
    }
    /**
     * Define se esta instância do IOAdmin está em execução.
     * 
     * @param running <code>True</code> para executar; <br>
     * <code>False</code> para o contrário.
     */
    public void setRunning(boolean running) {
        
        this.running = running;
        
    }
    
    /**
     * Verifica se esta instância do IOAdmin está aceitando novas conexões de {@link Client}.
     * 
     * @return <code>True</code> se novas conexões são aceitas;<br> 
     * <code>False</code> caso contrário.
     */
    public boolean isAcceptingClients() {
        
        return acceptingClients;
        
    }
    /**
     * Define se esta instância do IOAdmin está aceitando novas conexões de {@link Client}.
     * 
     * @param acceptingClients <code>True</code> para aceitar novas conexões; <br>
     * <code>False</code> para o contrário.
     */
    public void setAcceptingClients(boolean acceptingClients) {
        
        this.acceptingClients = acceptingClients;
        
    }
        
    
    //Factory method and contructor
    
    /**
     * Instancia e prepara um IOAdmin para receber conexões na porta especificada por parâmetro.
     * 
     * @param port Porta TCP do servidor (são válidos os valores de 50000 até 50100).
     * @return  Uma instância da classe IOAdmin se a porta for válida.
     */
    public static IOAdmin getNewIOAdmin(int port) throws IllegalArgumentException{
        
        if(validatePort(port))  return new IOAdmin(port);
        else    throw new IllegalArgumentException("A porta especificada é inválida: " + port + ". São válidos os valores de 50000 até 50100");
        
    }
    private IOAdmin (int port){
        this.port = port;
    }
    
    
    @Override
    public void run() {
        
        //Initializing
        try{
            
            server = new ServerSocket(port);
            Prometheus.getMainLogger().log("ServerSocket iniciado na porta: " + port, Thread.currentThread());
            
        }
        catch(Exception ex){
            
            Prometheus.getMainLogger().log("Nao foi possivel iniciar o ServerSocket na porta: " + port, Thread.currentThread());
            
        }
        
        
        //clientThreads = new ArrayList();
        clients = new ArrayList();
        Client temp;
                
        //Main code
        while(running){
            
            if(acceptingClients){
                
                try{

                    temp = new Client( ClientDevice.UNKNOWN, server.accept() );
                    Prometheus.getMainLogger().log("Conexao recebida de: " + temp.getSocket().getInetAddress().getHostAddress(), Thread.currentThread());
                    
                    clients.add(temp);
                    new Thread(temp).start();

                }
                catch(Exception ex){

                    Prometheus.getMainLogger().log("Nao foi possivel aceitar uma nova conexao: " + ex.getMessage(), Thread.currentThread());

                }
                
            }
            
            
            
        }
        
        //Finalizing
        try {
            
            disconnectAll();
            
        } catch (Exception ex) {
            
            Prometheus.getMainLogger().log("Nao foi possivel desconetar os clientes do ServerSocket na porta:" + port, Thread.currentThread());
            
        }
        
        try {
            
            server.close();
            
        } catch (Exception ex) {
            
            Prometheus.getMainLogger().log("Nao foi possivel encerrar o ServerSocket na porta:" + port, Thread.currentThread());
            
        }
        
    }

    private static boolean validatePort (int port){
        
        return (50000 <= port && port <= 50100);
        
    }
    
    
    
    public void disconnectAll() throws Exception{
        
        for(Client client : clients){
            
            client.disconnect();
            
        }
        
    }
    
}