package com.valentine.server.gui;

import java.io.*;

import javax.swing.*;

public class JTextAreaOutputStream extends OutputStream
{
	JTextArea jTextArea;
	
	public JTextAreaOutputStream(JTextArea _jTextArea)
	{
		jTextArea = _jTextArea;
	}
	
	public void write(int _b) throws IOException
	{
		jTextArea.append(String.valueOf((char) _b));
	}

}
