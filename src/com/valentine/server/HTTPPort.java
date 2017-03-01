package com.valentine.server;

import java.io.*;
import java.net.*;

import javax.net.ssl.*;

public class HTTPPort
{
	public static enum State
	{
		CREATED,
		RUNNING,
		STOPPED,
		JOINED,
		INTERRUPTED
	};

	private final HTTPServer httpServer;
	private final ServerSocket serverSocket;
	private final Thread thread;
	private final int port;
	private final boolean isSSL;
	
	private volatile State state = State.CREATED;
	
	public HTTPPort(ServerSocket _serverSocket, HTTPServer _httpServer) throws IOException
	{
		httpServer   = _httpServer;
		serverSocket = _serverSocket;
		port         = serverSocket.getLocalPort();
		isSSL        = serverSocket instanceof SSLServerSocket;
		
		thread = new Thread
		(
			() ->
			{
				state = State.RUNNING;
				while (state == State.RUNNING)
				{
					try
					{
						Socket socket = serverSocket.accept();
						HTTPRequest httpRequest = new HTTPRequest(socket, this);
						new Thread(httpRequest, "HTTPRequest " + httpRequest.hashCode())
							.start();
					}
					catch (IOException _e)
					{}
				}
			},
			"HTTPPort " + (isSSL ? "SSL " : "") + hashCode()
		);
		thread.start();
	}
	
	public State close() throws IOException
	{
		switch (state)
		{
			case RUNNING:
				state = State.STOPPED;
				serverSocket.close();
				try
				{
					thread.join();
					state = State.JOINED;
				}
				catch (InterruptedException _exc)
				{
					return state = State.INTERRUPTED;
				}
				finally
				{
					serverSocket.close();
				}
				
				return state;
				
			default:
				throw new IllegalStateException();
			
		}
	}

	public ServerSocket getServerSocket()
	{
		return serverSocket;
	}

	public Thread getThread()
	{
		return thread;
	}

	public int getPort()
	{
		return port;
	}

	public boolean isSSL()
	{
		return isSSL;
	}

	public State getState()
	{
		return state;
	}

	public HTTPServer getHttpServer()
	{
		return httpServer;
	}
}
