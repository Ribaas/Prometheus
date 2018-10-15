package com.prometheus.applications;

import java.io.File;
import java.io.IOException;

public class AppManager {
    
    
	//Fields
	private static App appInstance;
	
    
    public static void startApplication(String... args) throws Exception{
    	
    	//Verifies if there is no arg
    	if(args.length == 0) throw new IllegalArgumentException("Nenhuma aplicação recebida");
    	
    	//Verifies if there is an App running
    	if(appInstance != null && appInstance.getProc().isAlive()) {
    		
			//If the App running is the same as the one required to start, an Exception is thrown, otherwise the App is closed
    		if(appInstance.getLabel().equals(args[0])) throw new IllegalArgumentException("Aplicação já em execução");
    		else stopApplication();
    		
    	}

    	//Verifies the arg and proceeds accordingly
    	switch(args[0]) {
    	
	    	case "netflix":
	    		//open netflix
	    		appInstance = new App( args[0], new ProcessBuilder("cmd",  "/C start \"\" netflix.bat").directory(new File("scripts")) );
	    		break;
	    		
	    	case "spotify":
	    		//open spotify
	    		appInstance = new App( args[0], new ProcessBuilder("cmd",  "/C start \"\" spotify.bat").directory(new File("scripts")) );
	    		break;
	    		
			default:
				throw new IllegalArgumentException("Aplicação inexistente");
    	
    	}
    	
    	
    }
    
    public static void stopApplication() {
    	
    	//if(appInstance != null && appInstance.getProc().isAlive()) appInstance.getProc().destroy();
    	
    	//Temporary implementation
    	try {
			new ProcessBuilder("cmd",  "/C start taskkill /F /IM chrome.exe").start();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
}
