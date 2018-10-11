package com.prometheus.utils;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Classe para encapsulamento de dados de forma que os deixem transmissíveis entre partes do Software e através da rede.
 */
public class InfoBlock implements Serializable{
    
    //An InfoBlock is class for encapsulation of data to make it transmissible through 
    //parts of the software and through the network. Once instantiated it cannot be modified.
    
    //Fields
    public final static long TIME_THRESHOLD = 3000L; //The margin of error in ms, allowed during the validation

    
    //private final String ID;            //The InfoBLock ID consists in a two-part ID: 
                                        //the first one identifies the group this InfoBlock belongs to
                                        //the second one identifies the InfoBlock itself
    private final BlockType type;

    private final boolean immediate;
    private final Timestamp timestamp;
    private boolean validity = false;

    private final Object[] content;

    //Constructor
    public InfoBlock(BlockType type, boolean isImmediate, Object... content) {
            //this.ID = ID;
            this.type = type;

            this.immediate = isImmediate;
            this.timestamp = new Timestamp(System.currentTimeMillis());

            this.content = content;
    }

    
    //Getters
    
    /*
    public String getID() {
    
    return ID;
    
    }
    public String getBlockID() {
    String[] ids = ID.split("-");
    return ids[1];
    }
    public String getGroupID() {
    
    String[] ids = ID.split("-");
    return ids[0];
    
    }*/
    
    public BlockType getType() {
        return type;
    }
    public boolean isImmediate() {
        return immediate;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public Object[] getContent() {
        return content;
    }

    //Gets and evaluate the validity field
    public boolean isValid() {
    
        if(validity || !immediate) return true;
        
        Timestamp now = new Timestamp(System.currentTimeMillis());
        validity =  now.getTime() - timestamp.getTime() <= TIME_THRESHOLD;
        
        return validity;
    
    }
	
}
