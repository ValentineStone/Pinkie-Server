package com.valentine.server.gui;

import java.io.*;

import javax.swing.*;

public class JTextAreaPrintStream extends PrintStream
{
	JTextArea jTextArea;
	
	public JTextAreaPrintStream(JTextArea _jTextArea)
	{
		super(new JTextAreaOutputStream(_jTextArea));
		jTextArea = _jTextArea;
	}
	
	

}
