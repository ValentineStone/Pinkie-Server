package com.valentine.server;

public enum HTTPStatus
{
	_100 ("Continue"),
	_101 ("Switching Protocols"),
	_200 ("OK"),
	_201 ("Created"),
	_202 ("Accepted"),
	_203 ("Non-Authoritative Information"),
	_204 ("No Content"),
	_205 ("Reset Content"),
	_206 ("Partial Content"),
	_300 ("Multiple Choices"),
	_301 ("Moved Permanently"),
	_302 ("Found"),
	_303 ("See Other"),
	_304 ("Not Modified"),
	_305 ("Use Proxy"),
	_307 ("Temporary Redirect"),
	_400 ("Bad Request"),
	_401 ("Unauthorized"),
	_402 ("Payment Required"),
	_403 ("Forbidden"),
	_404 ("Not Found"),
	_405 ("Method Not Allowed"),
	_406 ("Not Acceptable"),
	_407 ("Proxy Authentication Required"),
	_408 ("Request Time-out"),
	_409 ("Conflict"),
	_410 ("Gone"),
	_411 ("Length Required"),
	_412 ("Precondition Failed"),
	_413 ("Request Entity Too Large"),
	_414 ("Request-URI Too Large"),
	_415 ("Unsupported Media Type"),
	_416 ("Requested range not satisfiable"),
	_417 ("Expectation Failed"),
	_500 ("Internal Server Error"),
	_501 ("Not Implemented"),
	_502 ("Bad Gateway"),
	_503 ("Service Unavailable"),
	_504 ("Gateway Time-out"),
	_505 ("HTTP Version not supported");
	
	private final String reasonPhrase;
	private final String header;
	private final int no;
	
	private HTTPStatus(String _reasonPhrase)
	{
		reasonPhrase = _reasonPhrase;
		
		header = "HTTP/1.1 " + name().substring(1) + " " + _reasonPhrase;
		
		no = Integer.valueOf(name().substring(1));
	}
	
	public String reasonPhrase()
	{
		return reasonPhrase;
	}
	
	public String header()
	{
		return header;
	}
	
	public int no()
	{
		return no;
	}
}