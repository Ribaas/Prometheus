package com.prometheus.utils;


public enum BlockType {
    //Enum class used to classify the the InfoBlock.
    //INFORMATION means that it will just notify the receiver without waiting for a response.
    //REQUEST means it will request for a information and the sender will wait for one response.
    //COMMAND means it will send a command to the receiver and may or may not wait for an answer, depending on the situation.
    
    INFORMATION, REQUEST, COMMAND;
	
	
}
