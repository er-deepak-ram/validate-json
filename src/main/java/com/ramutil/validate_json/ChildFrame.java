package com.ramutil.validate_json;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ChildFrame extends JFrame implements ActionListener{
	
	private JTextArea txtData;
	private JScrollPane jsp;
	private JButton btnBack;
	private JPanel subPanel;
	private ChildFrame obj;
	
	public ChildFrame(String title) {
		super(title);
		txtData = new JTextArea(5, 40);
		jsp = new JScrollPane(txtData);
		subPanel = new JPanel();
		btnBack = new JButton("Back");
		subPanel.add("South", btnBack);
		add(jsp);
		getContentPane().add("South", subPanel);
		btnBack.addActionListener(this);
		
		setSize(1000, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void setData(String data) {
		obj = this;
		txtData.setText(data);
		txtData.setLineWrap(true);
		txtData.setWrapStyleWord(true);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		obj.dispose();
	}
}
