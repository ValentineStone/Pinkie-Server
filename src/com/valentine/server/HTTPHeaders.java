package com.valentine.server;

import java.nio.charset.*;

public class HTTPHeaders
{
	
	public static final byte[] CRLF = "\r\n".getBytes(StandardCharsets.US_ASCII);
	
	public static byte[] contentType(boolean _html, HTTPCharset _charset)
	{
		return asBytes("Content-type: text/" + (_html ? "html" : "plain") + "; charset=" + _charset, HTTPCharset.US_ASCII);
	}
	
	public static byte[] contentType(String _mime)
	{
		return asBytes("Content-type: " + _mime, HTTPCharset.US_ASCII);
	}
	
	public static byte[] contentLenght(long _lenght)
	{	
		return asBytes("Content-lenght: " + _lenght, HTTPCharset.US_ASCII);
	}
	
	public static byte[] asBytes(String _string, HTTPCharset _charset)
	{
		return _string.getBytes(_charset.charset());
	}
	
	public static String preHtml(String _text)
	{
		return "<!DOCTYPE><html><body><pre>" + _text + "</pre></body></html>";
	}
	
	public static String bodyHtml(String _text)
	{
		return "<!DOCTYPE><html><body>" + _text + "</body></html>";
	}
	
	public static String h1Html(String _text)
	{
		return "<!DOCTYPE><html><body><h1>" + _text + "</h1></body></html>";
	}
}