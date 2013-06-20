package edu.upf.nets.mercury.task;

import java.util.concurrent.ExecutionException;

public interface TaskProcessor {
	
	public void process() throws InterruptedException, ExecutionException;

}
