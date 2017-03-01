package com.valentine.server;

import java.io.*;

public class Main
{
	public static void main(String[] _args) throws IOException
	{
		Arguments args = new Arguments(_args);
		String root = args.containsValue("r") ? args.get("r") : "/home/www";
		
		HTTPServer server = new HTTPServer(root);
		
		server.open(80);
	}
}
