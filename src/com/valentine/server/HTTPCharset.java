package com.valentine.server;

import java.nio.charset.*;

public enum HTTPCharset
{
	UTF_8("utf-8", StandardCharsets.UTF_8),
	US_ASCII("us-ascii", StandardCharsets.US_ASCII);
	
	private String string;
	private Charset charset;
	
	HTTPCharset(String _string, Charset _charset)
	{
		string  = _string;
		charset = _charset;
	}
	
	public String toString()
	{
		return string;
	}
	
	public Charset charset()
	{
		return charset;
	}
}
