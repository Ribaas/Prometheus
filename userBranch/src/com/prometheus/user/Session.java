package com.prometheus.user;

public final class Session extends Thread /*extends Thread, is a heir*/ /*implements Runnable as a JInterface*/{
	
	//current thread
	Thread currrentSession; 
	
	//constructor
	protected Session() {
		
		//thread instantiating and getting able to run
		currrentSession = new Thread();			
		currrentSession.start();
		currrentSession.run();

	}
	
	public void run(){
		
		User user = new User();
	
	}
}
