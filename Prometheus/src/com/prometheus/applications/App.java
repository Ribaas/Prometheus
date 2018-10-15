package com.prometheus.applications;

final class App {

	//Fields
	private String label;
	private Process proc;
	
	
	//Getters
	String getLabel() {
		
		return label;
		
	}
	
	synchronized Process getProc() {
		
		return proc;
		
	}
	
	
	//Constructor
	App(String label, ProcessBuilder builder) throws Exception{
		
		this.label = label;
		
		this.proc = builder.start();
		
	}
	
}
