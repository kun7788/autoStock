package com.autoStock.r;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.math.R.RserverConf;
import org.math.R.Rsession;

import com.autoStock.Co;

public class RServeController {
	private static final int instancePoolSize = 10;
	
	private ArrayList<RProcess> listOfRServeProcesses = new ArrayList<RProcess>();
	
	public void start(){
		for (int i=0; i<instancePoolSize; i++){
			try {
				Process process = Runtime.getRuntime().exec("C:\\Program Files\\R\\R-3.0.1\\bin\\R.exe -e \"library(Rserve);Rserve(FALSE,args='--no-save --slave --RS-port 100" + i + "')\" --no-save --slave");
				listOfRServeProcesses.add(new RProcess(i, 1000 + i, process));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		Co.println("--> Started instances: " + instancePoolSize);
	}
	
	public synchronized Rsession getRSession(){
		for (RProcess rProcess : listOfRServeProcesses){
			if (rProcess.isBusy == false){
				rProcess.isBusy = true;
				
				Co.println("--> Providing instnace at port: " + rProcess.port);
				
				RserverConf rserverConf = new RserverConf("localhost", rProcess.port, null, null, null);
			    return Rsession.newRemoteInstance(new PrintStream(new OutputStream() {@Override public void write(int b) {}}), rserverConf);
			}
		}
		
		throw new IllegalStateException("No RSessions are available");
	}
	
	public static class RProcess {
		public int index;
		public int port;
		public Process process;
		public boolean isBusy;
		
		public RProcess(int index, int port, Process process) {
			this.index = index;
			this.port = port;
			this.process = process;
		}
	}
}