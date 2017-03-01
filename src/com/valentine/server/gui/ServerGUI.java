package com.valentine.server.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;

import com.valentine.server.*;

public class ServerGUI extends JFrame
{
	private HTTPServer server;
	
	private JPanel contentPane;
	
	private JLabel lblPort;
	private JFormattedTextField frmtdtxtfldPort;
	private JToggleButton btnToggle;

	
	
	private JLabel lblKeystore;
	private JTextField txtKeystore;

	private JLabel lblJksPass;
	private JTextField txtJksPass;

	private JLabel lblKeyPass;
	private JTextField txtKeyPass;
	
	private JLabel lblSSLProtocol;
	private JTextField txtSSLProtocol;
	
	
	
	private JLabel lblPortSSL;
	private JFormattedTextField frmtdtxtfldPortSSL;
	private JToggleButton btnToggleSSL;
	
	private JTextArea errTextArea;
	private JScrollPane errScrollPane;
	private JTextArea outTextArea;
	private JScrollPane outScrollPane;
	private JTextField txtRoot;
	private JPanel centralizer;
	private JLabel lblPinkiePie;
	

	/**
	 * Launch the application.
	 */
	public static void main(String... _args)
	{
		try
		{
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		}
		catch (Throwable _ext)
		{
			_ext.printStackTrace();
		}
		
		HTTPServer server = new HTTPServer("/home/www");
		
		EventQueue.invokeLater
		(
			new Runnable()
			{
				public void run()
				{
					try
					{
						ServerGUI frame = new ServerGUI(server);
						frame.setVisible(true);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		);
	}

	/**
	 * Create the frame.
	 */
	public ServerGUI(HTTPServer _server)
	{
		setTitle("Pinkie Server");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ServerGUI.class.getResource("/pinkieicon.png")));
		server = _server;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1020, 777);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		centralizer = new JPanel();
		contentPane.add(centralizer, BorderLayout.NORTH);
		centralizer.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		JPanel controlPanel = new JPanel();
		centralizer.add(controlPanel);
		GridBagLayout gbl_controlPanel = new GridBagLayout();
		gbl_controlPanel.columnWidths = new int[] {120, 120, 120, 120};
		gbl_controlPanel.rowHeights = new int[] {30, 10, 30, 10, 30};
		gbl_controlPanel.columnWeights = new double[]{0.0, 1.0, 1.0};
		gbl_controlPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		controlPanel.setLayout(gbl_controlPanel);
		
		btnToggle = new JToggleButton("Start");
		btnToggle.addActionListener((ActionEvent _e) -> {toggle();});
		
		txtRoot = new JTextField();
		txtRoot.setHorizontalAlignment(SwingConstants.CENTER);
		txtRoot.setText("/home/www");
		GridBagConstraints gbc_txtRoot = new GridBagConstraints();
		gbc_txtRoot.insets = new Insets(0, 0, 5, 0);
		gbc_txtRoot.gridwidth = 4;
		gbc_txtRoot.fill = GridBagConstraints.BOTH;
		gbc_txtRoot.gridx = 0;
		gbc_txtRoot.gridy = 0;
		controlPanel.add(txtRoot, gbc_txtRoot);
		txtRoot.setColumns(10);
		
		lblPort = new JLabel("Port");
		lblPort.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblPort = new GridBagConstraints();
		gbc_lblPort.fill = GridBagConstraints.BOTH;
		gbc_lblPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblPort.gridx = 0;
		gbc_lblPort.gridy = 1;
		controlPanel.add(lblPort, gbc_lblPort);
		
		lblPortSSL = new JLabel("SSL port");
		GridBagConstraints gbc_lblSslPort = new GridBagConstraints();
		gbc_lblSslPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblSslPort.gridx = 1;
		gbc_lblSslPort.gridy = 1;
		controlPanel.add(lblPortSSL, gbc_lblSslPort);
		
		lblSSLProtocol = new JLabel("SSL protocol");
		GridBagConstraints gbc_lblSslProtocol = new GridBagConstraints();
		gbc_lblSslProtocol.insets = new Insets(0, 0, 5, 5);
		gbc_lblSslProtocol.gridx = 2;
		gbc_lblSslProtocol.gridy = 1;
		controlPanel.add(lblSSLProtocol, gbc_lblSslProtocol);
		
		lblKeystore = new JLabel("Key store");
		lblKeystore.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblKeyStore = new GridBagConstraints();
		gbc_lblKeyStore.fill = GridBagConstraints.BOTH;
		gbc_lblKeyStore.insets = new Insets(0, 0, 5, 0);
		gbc_lblKeyStore.gridx = 3;
		gbc_lblKeyStore.gridy = 1;
		controlPanel.add(lblKeystore, gbc_lblKeyStore);
		
		frmtdtxtfldPort = new JFormattedTextField(NumberFormat.getIntegerInstance());
		frmtdtxtfldPort.setText("80");
		GridBagConstraints gbc_frmtdtxtfldPort = new GridBagConstraints();
		gbc_frmtdtxtfldPort.fill = GridBagConstraints.BOTH;
		gbc_frmtdtxtfldPort.insets = new Insets(0, 0, 5, 5);
		gbc_frmtdtxtfldPort.gridx = 0;
		gbc_frmtdtxtfldPort.gridy = 2;
		controlPanel.add(frmtdtxtfldPort, gbc_frmtdtxtfldPort);
		
		frmtdtxtfldPortSSL = new JFormattedTextField(NumberFormat.getIntegerInstance());
		frmtdtxtfldPortSSL.setText("443");
		GridBagConstraints gbc_frmtdtxtfldPortSSL = new GridBagConstraints();
		gbc_frmtdtxtfldPortSSL.insets = new Insets(0, 0, 5, 5);
		gbc_frmtdtxtfldPortSSL.fill = GridBagConstraints.BOTH;
		gbc_frmtdtxtfldPortSSL.gridx = 1;
		gbc_frmtdtxtfldPortSSL.gridy = 2;
		controlPanel.add(frmtdtxtfldPortSSL, gbc_frmtdtxtfldPortSSL);
		
		txtSSLProtocol = new JTextField();
		txtSSLProtocol.setText("TLSv1.2");
		GridBagConstraints gbc_txtProtocol = new GridBagConstraints();
		gbc_txtProtocol.insets = new Insets(0, 0, 5, 5);
		gbc_txtProtocol.fill = GridBagConstraints.BOTH;
		gbc_txtProtocol.gridx = 2;
		gbc_txtProtocol.gridy = 2;
		controlPanel.add(txtSSLProtocol, gbc_txtProtocol);
		txtSSLProtocol.setColumns(10);
		
		txtKeystore = new JTextField();
		txtKeystore.setText("/keystore.jks");
		GridBagConstraints gbc_txtKeystore = new GridBagConstraints();
		gbc_txtKeystore.insets = new Insets(0, 0, 5, 0);
		gbc_txtKeystore.fill = GridBagConstraints.BOTH;
		gbc_txtKeystore.gridx = 3;
		gbc_txtKeystore.gridy = 2;
		controlPanel.add(txtKeystore, gbc_txtKeystore);
		txtKeystore.setColumns(10);
		GridBagConstraints gbc_btnToggle = new GridBagConstraints();
		gbc_btnToggle.insets = new Insets(0, 0, 5, 5);
		gbc_btnToggle.gridheight = 2;
		gbc_btnToggle.fill = GridBagConstraints.BOTH;
		gbc_btnToggle.gridx = 0;
		gbc_btnToggle.gridy = 3;
		controlPanel.add(btnToggle, gbc_btnToggle);
		
		btnToggleSSL = new JToggleButton("Start");
		btnToggleSSL.addActionListener((ActionEvent _e) -> {toggleSSL();});
		GridBagConstraints gbc_btnToggleSSL = new GridBagConstraints();
		gbc_btnToggleSSL.fill = GridBagConstraints.BOTH;
		gbc_btnToggleSSL.gridheight = 2;
		gbc_btnToggleSSL.insets = new Insets(0, 0, 5, 5);
		gbc_btnToggleSSL.gridx = 1;
		gbc_btnToggleSSL.gridy = 3;
		controlPanel.add(btnToggleSSL, gbc_btnToggleSSL);
		
		lblJksPass = new JLabel("JKS pass");
		lblJksPass.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblJksPass = new GridBagConstraints();
		gbc_lblJksPass.fill = GridBagConstraints.BOTH;
		gbc_lblJksPass.insets = new Insets(0, 0, 5, 5);
		gbc_lblJksPass.gridx = 2;
		gbc_lblJksPass.gridy = 3;
		controlPanel.add(lblJksPass, gbc_lblJksPass);
		
		lblKeyPass = new JLabel("Key pass");
		lblKeyPass.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblKeyPass = new GridBagConstraints();
		gbc_lblKeyPass.fill = GridBagConstraints.BOTH;
		gbc_lblKeyPass.insets = new Insets(0, 0, 5, 0);
		gbc_lblKeyPass.gridx = 3;
		gbc_lblKeyPass.gridy = 3;
		controlPanel.add(lblKeyPass, gbc_lblKeyPass);
		
		txtJksPass = new JPasswordField();
		txtJksPass.setText("PinkiePie");
		GridBagConstraints gbc_txtJkspass = new GridBagConstraints();
		gbc_txtJkspass.fill = GridBagConstraints.BOTH;
		gbc_txtJkspass.insets = new Insets(0, 0, 5, 5);
		gbc_txtJkspass.gridx = 2;
		gbc_txtJkspass.gridy = 4;
		controlPanel.add(txtJksPass, gbc_txtJkspass);
		txtJksPass.setColumns(10);
		
		txtKeyPass = new JPasswordField();
		txtKeyPass.setText("PinkiePie");
		txtKeyPass.setToolTipText("");
		GridBagConstraints gbc_txtKeypass = new GridBagConstraints();
		gbc_txtKeypass.insets = new Insets(0, 0, 5, 0);
		gbc_txtKeypass.fill = GridBagConstraints.BOTH;
		gbc_txtKeypass.gridx = 3;
		gbc_txtKeypass.gridy = 4;
		controlPanel.add(txtKeyPass, gbc_txtKeypass);
		txtKeyPass.setColumns(10);
		
		lblPinkiePie = new JLabel("");
		lblPinkiePie.setHorizontalAlignment(SwingConstants.CENTER);
		lblPinkiePie.setIcon(new ImageIcon(ServerGUI.class.getResource("/pinkiepie.png")));
		contentPane.add(lblPinkiePie, BorderLayout.SOUTH);
		
		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.2);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane, BorderLayout.CENTER);
		
		outPanel = new JPanel();
		splitPane.setLeftComponent(outPanel);
		outPanel.setLayout(new BorderLayout(0, 0));
		
				
				
		outTextArea = new JTextArea();
		outTextArea.setTabSize(4);
		outTextArea.setEditable(false);
		outTextArea.setForeground(Color.LIGHT_GRAY);
		outTextArea.setBackground(Color.BLACK);
		outTextArea.setFont(new Font("Consolas", Font.PLAIN, 16));
		((DefaultCaret) outTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		outTextArea.setWrapStyleWord(true);
		outTextArea.setLineWrap(true);
		
		System.setOut(new JTextAreaPrintStream(outTextArea));
		
		outScrollPane = new JScrollPane(outTextArea);
		outPanel.add(outScrollPane, BorderLayout.CENTER);
		outScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		ctrPanelOut = new JPanel();
		outPanel.add(ctrPanelOut, BorderLayout.NORTH);
		ctrPanelOut.setLayout(new BorderLayout(0, 0));
		
		lblSystemout = new JLabel("");
		ctrPanelOut.add(lblSystemout, BorderLayout.CENTER);
		lblSystemout.setHorizontalAlignment(SwingConstants.LEFT);
		lblSystemout.setIcon(new ImageIcon(ServerGUI.class.getResource("/stdout.png")));
		
		btnClearOut = new JButton("cls");
		btnClearOut.addActionListener((ActionEvent _evt) -> outTextArea.setText(""));
		ctrPanelOut.add(btnClearOut, BorderLayout.EAST);
		
		errPanel = new JPanel();
		splitPane.setRightComponent(errPanel);
		errPanel.setLayout(new BorderLayout(0, 0));
		
		
		
		errTextArea = new JTextArea();
		errTextArea.setTabSize(4);
		errTextArea.setEditable(false);
		errTextArea.setForeground(Color.LIGHT_GRAY);
		errTextArea.setBackground(Color.BLACK);
		errTextArea.setFont(new Font("Consolas", Font.PLAIN, 16));
		((DefaultCaret) errTextArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		errTextArea.setWrapStyleWord(true);
		errTextArea.setLineWrap(true);
		
		System.setErr(new JTextAreaPrintStream(errTextArea));
		
		errScrollPane = new JScrollPane(errTextArea);
		errPanel.add(errScrollPane, BorderLayout.CENTER);
		errScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		ctrPanelErr = new JPanel();
		errPanel.add(ctrPanelErr, BorderLayout.NORTH);
		ctrPanelErr.setLayout(new BorderLayout(0, 0));
		
		lblSystemerr = new JLabel("");
		ctrPanelErr.add(lblSystemerr);
		lblSystemerr.setIcon(new ImageIcon(ServerGUI.class.getResource("/stderr.png")));
		lblSystemerr.setHorizontalAlignment(SwingConstants.LEFT);
		
		btnClearErr = new JButton("cls");
		btnClearErr.addActionListener((ActionEvent _evt) -> errTextArea.setText(""));
		ctrPanelErr.add(btnClearErr, BorderLayout.EAST);
	}
	
	private void toggle()
	{
		boolean stopping = !btnToggle.isSelected();
		int port = Integer.valueOf(frmtdtxtfldPort.getText());
		
		try
		{
			if (stopping)
				server.close(port);
			else
				server.open(port);
		}
		catch (IOException _e)
		{
			_e.printStackTrace();
		}
		
		applyRoot();
		
		frmtdtxtfldPort.setEnabled(stopping);
		lblPort.setEnabled(stopping);
		
		btnToggle.setText(stopping ? "Start" : "Stop");
	}
	
	private void toggleSSL()
	{
		boolean stopping = !btnToggleSSL.isSelected();
		int port = Integer.valueOf(frmtdtxtfldPortSSL.getText());
		
		try
		{
			if (stopping)
				server.close(port);
			else
				server.open
				(
					port,
					txtSSLProtocol.getText(),
					txtKeystore.getText(),
					txtJksPass.getText(),
					txtKeyPass.getText()
				);
		}
		catch (Exception _e)
		{
			_e.printStackTrace();
		}

		applyRoot();
		
		frmtdtxtfldPortSSL.setEnabled(stopping);
		
		lblKeystore.setEnabled(stopping);
		txtKeystore.setEnabled(stopping);

		lblJksPass.setEnabled(stopping);
		txtJksPass.setEnabled(stopping);

		lblKeyPass.setEnabled(stopping);
		txtKeyPass.setEnabled(stopping);
		
		lblSSLProtocol.setEnabled(stopping);
		txtSSLProtocol.setEnabled(stopping);
		
		btnToggleSSL.setText(stopping ? "Start" : "Stop");
	}
	
	private void applyRoot()
	{
		txtRoot.setEnabled( !(btnToggle.isSelected() || btnToggleSSL.isSelected()) );
		server.setRoot(txtRoot.getText());
	}
	
	private static final long serialVersionUID = -1957536327859185159L;
	private JSplitPane splitPane;
	private JPanel outPanel;
	private JPanel errPanel;
	private JLabel lblSystemout;
	private JLabel lblSystemerr;
	private JPanel ctrPanelOut;
	private JPanel ctrPanelErr;
	private JButton btnClearOut;
	private JButton btnClearErr;
}
