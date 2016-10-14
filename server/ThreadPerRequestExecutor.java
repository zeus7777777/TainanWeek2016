package server;

import java.util.concurrent.Executor;

public class ThreadPerRequestExecutor implements Executor
{

	public void execute(Runnable r)
	{
		new Thread(r).start();
	}
}
