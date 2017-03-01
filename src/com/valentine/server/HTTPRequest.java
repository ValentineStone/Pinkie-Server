package com.valentine.server;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.net.ssl.*;


public class HTTPRequest implements Runnable
{
	public static final String CRLF = "\r\n";
	
	private final Socket socket;
	private final boolean isSSL;
	private final HTTPPort httpPort;
	
    private String method;
    private String requestURI;
    private String protocolVersion;
    private int contentLenght = 0;
    
    private boolean keepAlive = true;
    
    private Map<String,String> requestHeaders = new HashMap<>();

	public HTTPRequest(Socket _socket, HTTPPort _httpPort)
	{
		httpPort = _httpPort;
		socket = Objects.requireNonNull(_socket);
		isSSL = _socket instanceof SSLSocket;
	}
	
	public void run()
	{
		try
		{
			if (readInputHeaders())
				new HTTPResponse(this);
		}
		catch (Exception _exc)
		{
			_exc.printStackTrace(System.err);
		}
		
		try
		{
			if (!socket.isClosed())
				socket.close();
		}
		catch (Exception _exc)
		{
			_exc.printStackTrace(System.err);
		}
	}
	
	private boolean readInputHeaders() throws Exception
	{
		InputStream is = socket.getInputStream();
		
		StringBuilder string = new StringBuilder(is.available());
		String field = "";
		String text = "";
		
		boolean CR = false;
		boolean DD = false;
		
		String firstLine;
		int cp;
		
		for (cp = is.read(); cp != -1; cp = is.read())
		{
			if (CR)
				if (cp == '\n')
					break;
				else
				{
					CR = false;
					string.appendCodePoint('\r');
				}
			
			if (cp == '\r')
			{
				CR = true;
				continue;
			}
			
			string.appendCodePoint(cp);
		}
		
		if (string.length() == 0)
		{
			System.out.println("EMPTY REQUEST! O_o");
			return false;
		}
		
		CR = false;

		firstLine = string.toString();
		
		String[] firstLines  = firstLine.split(" ");
		string.setLength(0);
		
		method          = firstLines[0];
		requestURI      = getHttpPort().getHttpServer().getRoot() + firstLines[1];
		protocolVersion = firstLines[2];
		
		for (cp = is.read(); cp != -1; cp = is.read())
		{
			if (DD)
				if (cp == ' ')
					continue;
				else
					DD = false;
			if (CR)
			{
				CR = false;
				if (cp == '\n')
				{
					text = string.toString();
					string.setLength(0);
					
					if (text.isEmpty() && field.isEmpty())
						break;
					else
					{
						requestHeaders.put(field, text);
						field = "";
						text = "";
					}
					
					continue;
				}
				else
					string.appendCodePoint('\r');
			}

			if (cp == '\r')
			{
				CR = true;
				continue;
			}
			
			if (cp == ':')
			{
				DD = true;
				field = string.toString();
				string.setLength(0);
				continue;
			}
			
			string.appendCodePoint(cp);
		}
		
		if (requestHeaders.get("Content-Length") != null)
			contentLenght = Integer.valueOf(requestHeaders.get("Content-Length"));

		String connection = requestHeaders.get("Connection");
		if (connection != null && connection.equals("close"))
			keepAlive = false;
		else
			keepAlive = true;
		
		return true;
	}

	public Socket getSocket()
	{
		return socket;
	}

	public String getMethod()
	{
		return method;
	}

	public String getRequestURI()
	{
		return requestURI;
	}

	public String getProtocolVersion()
	{
		return protocolVersion;
	}

	public Map<String, String> getRequestHeaders()
	{
		return requestHeaders;
	}

	public boolean isSSL()
	{
		return isSSL;
	}

	public HTTPPort getHttpPort()
	{
		return httpPort;
	}

	public int getContentLenght()
	{
		return contentLenght;
	}

	public boolean getKeepAlive()
	{
		return keepAlive;
	}
}