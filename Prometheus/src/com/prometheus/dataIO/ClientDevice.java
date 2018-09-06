package com.prometheus.dataIO;

public enum ClientDevice {
    
    UNKNOWN(0), DESKTOP_UI(1), DESKTOP_CONSOLE(2), ANDROID(3);
    
    private final int id;
    
    private ClientDevice (int id){
        
        this.id = id;
        
    }
    
    public int getId(){
        
        return this.id;
        
    }
    
    public static ClientDevice getById(int id){
        
        if(0 <= id && id < ClientDevice.values().length) return ClientDevice.values()[id];
        else return ClientDevice.UNKNOWN;
        
    }

}
