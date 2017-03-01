package com.valentine.server;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.Map.*;

public class HTTPResponse
{
	public static enum ContentType
	{
		FILE,
		FOLDER,
		JAR,
		PHP,
		CGI,
		UNKNOWN,
		MISSING;
	}
	
	public static final String[] autoFiles =
	new String[]
	{
		"index.php",
		"index.html",
		"index.jar",
		"index.exe"
	};
	
	private OutputStream os;
	private String uri;
	private Path path;
	private String absolutePath;
	private String querry;
	private boolean isPathReal;
	private boolean isPathFolder;
	private String mimeType;
	private HTTPStatus status;
	private ContentType contentType;
	
	private final HTTPRequest request;
	
	
	public HTTPResponse(HTTPRequest _request) throws IOException
	{
		request = _request;
		
		os         = request.getSocket().getOutputStream();
		uri        = request.getRequestURI();
		
		int qindex = uri.indexOf('?');
		
		
		setPath(Paths.get(qindex == -1 ? uri : uri.substring(0, qindex)));
		querry = qindex == -1 ? "" : uri.substring(qindex + 1);
		
		switch (contentType)
		{
			case JAR:
			case FILE:
			case PHP:
			case UNKNOWN:
			case CGI:
				status = HTTPStatus._200;
				break;
			case MISSING:
				status = HTTPStatus._404;
				break;
			case FOLDER:
			{
				Path file = resolveDir(path);
				
				if (file != null)
				{
					setPath(file);
					status = HTTPStatus._200;
				}
				else
					status = HTTPStatus._404;
				
				break;
			}
			default:
				status = HTTPStatus._500;
		}
		
		os.write(HTTPHeaders.asBytes(status.header(), HTTPCharset.US_ASCII));
		os.write(HTTPHeaders.CRLF);
		
		if (!request.getKeepAlive())
		{
			os.write(HTTPHeaders.asBytes("Connection: close", HTTPCharset.US_ASCII));
			os.write(HTTPHeaders.CRLF);
		}
		
		switch (contentType)
		{
			case FILE:
			case UNKNOWN:
				writeFile();
				break;
			case JAR:
				writeCGIEFF("java", "-jar");
				break;
			case PHP:
				writeCGIE("/bin/php7/php-cgi");
				break;
			case CGI:
				writeCGIE(absolutePath);
				break;
			case FOLDER:
			case MISSING:
				writeStatus();
				break;
			default:
				break;
		}
		
		os.flush();
	}
	
	private void writeFile() throws IOException
	{
		os.write(HTTPHeaders.contentLenght(path.toFile().length()));
		os.write(HTTPHeaders.CRLF);
		os.write(HTTPHeaders.contentType(mimeType));
		os.write(HTTPHeaders.CRLF);
		os.write(HTTPHeaders.CRLF);
		Files.copy(path, os);
	}
	
	private void writeStatus() throws IOException
	{
		byte[] html = HTTPHeaders.asBytes(HTTPHeaders.h1Html(status.header()), HTTPCharset.US_ASCII);
		writeHtml(html, HTTPCharset.US_ASCII);
	}
	
	private void writeHtml(byte[] _html, HTTPCharset _charset) throws IOException
	{
		os.write(HTTPHeaders.contentLenght(_html.length));
		os.write(HTTPHeaders.CRLF);
		os.write(HTTPHeaders.contentType(true, _charset));
		os.write(HTTPHeaders.CRLF);
		os.write(HTTPHeaders.CRLF);
		os.write(_html);
	}
	
	private void writeCGIE(String _exec, String ... _args) throws IOException
	{
		String[] cmd = new String[_args.length + 1];
		cmd[0] = _exec;
		System.arraycopy(_args, 0, cmd, 1, _args.length);
		writeCGI(cmd);
	}
	
	private void writeCGIEFF(String _exec, String _fileFlag, String ... _args) throws IOException
	{
		String[] cmd = new String[_args.length + 3];
		cmd[0] = _exec;
		cmd[1] = _fileFlag;
		cmd[2] = absolutePath;
		System.arraycopy(_args, 0, cmd, 3, _args.length);
		writeCGI(cmd);
	}
	
	/*
	private void writeCGIEF(String _exec, String ... _args) throws IOException
	{
		String[] cmd = new String[_args.length + 2];
		cmd[0] = _exec;
		cmd[1] = absolutePath;
		System.arraycopy(_args, 0, cmd, 2, _args.length);
		writeCGI(cmd);
	}
	*/
	
	private void writeCGI(String ... _cmd) throws IOException
	{
		ProcessBuilder processBuilder = new ProcessBuilder(_cmd);
		
		loadCGIenv(processBuilder.environment());
		
		Process process = processBuilder.start();
		
		OutputStream pos = process.getOutputStream();
		InputStream pis = process.getInputStream();
		InputStream perr = process.getErrorStream();
		
		InputStream is = request.getSocket().getInputStream();
		
		for (int i = 0; i < request.getContentLenght() && is.available() > 0; i++)
		{
			pos.write(is.read());
		}
		pos.flush();
		pos.close();
		
		new Thread
		(
			() ->
			{
				try
				{
					byte[] bytes = new byte[1024];
					int len;
					
					while (0 < (len = perr.read(bytes)))
					{
						System.err.write(bytes, 0, len);
					}
				}
				catch (IOException _e) {}
			}
		).start();
		
		
		byte[] bytes = new byte[1024];
		int len;
		
		while (0 < (len = pis.read(bytes)))
		{
			os.write(bytes, 0, len);
		}
		
		process.destroyForcibly();
	}
	
	private void loadCGIenv(Map<String, String> _env)
	{
		_env.put("QUERY_STRING", querry == null ? "" : querry);
		_env.put("REQUEST_METHOD", request.getMethod());
		_env.put("REDIRECT_STATUS", String.valueOf(status.no()));
		_env.put("PATH_TRANSLATED", absolutePath);
		_env.put("HTTPS", request.isSSL() ? "Yes" : "No");
		_env.put("CONTENT_LENGTH", String.valueOf(request.getContentLenght()));
		String reqMime = request.getRequestHeaders().get("Content-Type");
		if (reqMime != null)
			_env.put("CONTENT_TYPE", reqMime);
		
		for (Entry<String, String> entry : request.getRequestHeaders().entrySet())
		{
			String key = "HTTP_" + entry.getKey().replaceAll("-", "_").toUpperCase();
			_env.put(key, entry.getValue());
		}
	}
	
	private Path resolveDir(Path _path)
	{
		Path path;
		for (String name : autoFiles)
		{
			path = _path.resolve(name);
			if (Files.exists(path))
				return path;
		}
		return null;
	}
	
	private void setPath(Path _path) throws IOException
	{
		path = _path;
		absolutePath = _path.toFile().getAbsolutePath();
		
		isPathReal = Files.exists(path);
		isPathFolder = Files.isDirectory(path);
		mimeType = Files.probeContentType(path);

		if (isPathReal)
			if (mimeType == null)
			{
				if (isPathFolder)
				{
					contentType = ContentType.FOLDER;
				}
				else
				{
					String pathStr = path.toString();
					
					if (pathStr.endsWith(".jar"))
						contentType = ContentType.JAR;
					else if (pathStr.endsWith(".php"))
						contentType = ContentType.PHP;
					else if (pathStr.endsWith(".exe") || pathStr.endsWith(".cgi") || pathStr.endsWith(".bat"))
						contentType = ContentType.CGI;
					else
						contentType = ContentType.UNKNOWN;
				}
			}
			else
				contentType = ContentType.FILE;
		else
			contentType = ContentType.MISSING;
	}
}
