package com.valentine.server;

import java.io.*;
import java.net.*;
import java.security.*;
import java.security.cert.*;
import java.util.*;

import javax.net.ssl.*;

public class HTTPServer
{
	private final Map<Integer, HTTPPort> portListeners = new HashMap<>();
	
	private String root;
	
	
	public static void main(String[] _args) throws IOException
	{
		Arguments args = new Arguments(_args);
		String root = args.containsValue("r") ? args.get("r") : "/home/www";
		
		HTTPServer server = new HTTPServer(root);
		
		server.open(80);
	}
	
	
	public HTTPServer(String _root)
	{
		root = _root;
	}
	
	
	
	public int requireFreePort(int _port)
	{
		if (portListeners.containsKey(_port))
			throw new RuntimeException("Port already in use: " + _port);
		else
			return _port;
	}
	
	public boolean isPortFree(int _port)
	{
		return !portListeners.containsKey(_port);
	}
	
	
	
	public boolean close(int _port) throws IOException
	{
		if (!portListeners.containsKey(_port))
			return false;
		
		portListeners.get(_port).close();
		portListeners.remove(_port);
		
		return true;
	}
	
	
	
	public HTTPPort open(int _port) throws IOException
	{
		HTTPPort httpPort = new HTTPPort(new ServerSocket(requireFreePort(_port)), this);
		
		portListeners.put(_port, httpPort);
		
		return httpPort;
	}
	
	
	
	public HTTPPort open(int _port, String _protocol, String _keyStore, String _pass)
			throws UnrecoverableKeyException, KeyManagementException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException
	{
		return open(_port, _protocol, _keyStore, _pass, _pass);
	}
	
	public HTTPPort open(int _port, String _protocol, String _keyStore, String _jksPass, String _keyPass)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			IOException, UnrecoverableKeyException, KeyManagementException
	{
		KeyStore keyStore = KeyStore.getInstance("JKS");
		
		InputStream is = getClass().getResourceAsStream(_keyStore);
		keyStore.load(is, _jksPass.toCharArray());
		
		KeyManagerFactory kmf =
			KeyManagerFactory
			.getInstance(KeyManagerFactory
			.getDefaultAlgorithm());
		
		kmf.init(keyStore, _keyPass.toCharArray());
		
		TrustManagerFactory tmf =
				TrustManagerFactory
				.getInstance(TrustManagerFactory
				.getDefaultAlgorithm());
		
		tmf.init(keyStore);
		
		SSLContext tlsContext = SSLContext.getInstance(_protocol);
		tlsContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());
		
		SSLServerSocketFactory tlsSocketFactory = (SSLServerSocketFactory) tlsContext.getServerSocketFactory();
		
		HTTPPort httpPort = new HTTPPort(tlsSocketFactory.createServerSocket(requireFreePort(_port)), this);
		
		portListeners.put(_port, httpPort);
		
		return httpPort;
	}

	public String getRoot()
	{
		return root;
	}

	public void setRoot(String _root)
	{
		root = _root;
	}
}
