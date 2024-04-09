package com.ramutil.validate_json;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

public class App {
	static String schemaFilePath, jsonDataFilePath;

	public static void main(String[] args) {

		JFrame jf = new JFrame("JSON Authenticator");
		JPanel panel = new JPanel(new GridLayout(2, 2));
		JPanel btnAuthPanel = new JPanel();
		btnAuthPanel.setLayout(new BorderLayout());

		JLabel lblSchema = new JLabel("Select the JSON Schema file");
		lblSchema.setBorder(new EmptyBorder(10, 20, 10, 50));
		JButton btnSchema = new JButton("Select Schema");
		btnSchema.setPreferredSize(new Dimension(120, 30));
		btnSchema.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel lblJson = new JLabel("Select the JSON data file");
		lblJson.setBorder(new EmptyBorder(10, 20, 10, 50));
		JButton btnJson = new JButton("Select Json");
		btnJson.setPreferredSize(new Dimension(120, 30));
		btnJson.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblSchema);
		panel.add(btnSchema);
		panel.add(lblJson);
		panel.add(btnJson);

		JButton btnAuthenticate = new JButton("Authenticate");
		btnAuthenticate.setPreferredSize(new Dimension(350, 30));
		btnAuthenticate.setHorizontalAlignment(SwingConstants.CENTER);
		btnAuthPanel.add(btnAuthenticate, BorderLayout.SOUTH);

		jf.setLayout(new FlowLayout());
		jf.add(panel);
		jf.add(btnAuthPanel);
		jf.pack();
		btnSchema.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int res = fileChooser.showOpenDialog(jf);
				if (res == JFileChooser.APPROVE_OPTION) {
					schemaFilePath = fileChooser.getSelectedFile().getAbsolutePath();
					btnSchema.setText(fileChooser.getSelectedFile().getName());
				}
			}
		});
		btnJson.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int res = fileChooser.showOpenDialog(jf);
				if (res == JFileChooser.APPROVE_OPTION) {
					jsonDataFilePath = fileChooser.getSelectedFile().getAbsolutePath();
					btnJson.setText(fileChooser.getSelectedFile().getName());
				}
			}
		});
		btnAuthenticate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (StringUtils.isNotEmpty(schemaFilePath) && StringUtils.isNotEmpty(jsonDataFilePath)) {
					String status = authenicate();
					ChildFrame cf = new ChildFrame("JSON Status");
					cf.setData(status);
					schemaFilePath = null;
					jsonDataFilePath = null;
					btnSchema.setText("Select Schema");
					btnJson.setText("Select Json");
				} else {
					JOptionPane.showMessageDialog(jf, "First select the Schema and Json file");
				}
			}
		});
		jf.setSize(500, 200);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);
		jf.setVisible(true);
	}

	public static String authenicate() {
		StringBuilder sb = new StringBuilder();
		try {
			String jsonArrayData = readFileAsString(jsonDataFilePath);
			String schemaString = readFileAsString(schemaFilePath);

			// Parse JSON data and schema
			JsonNode jsonToValidate = JsonLoader.fromString(jsonArrayData);
			JsonNode schemaNode = JsonLoader.fromString(schemaString);

			// Create a schema validator
			JsonSchema schema = JsonSchemaFactory.byDefault().getJsonSchema(schemaNode);

			// Validate each element in the JSON array
			for (JsonNode element : jsonToValidate) {
				ProcessingReport report = schema.validate(element);
				if (report.isSuccess()) {
					sb.append("JSON data is valid!\n");
				} else {
					sb.append("JSON data is invalid. Errors:\n");
					sb.append(report + "\n");
				}
			}
		} catch (IOException | ProcessingException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return sb.toString();
	}

	public static String readFileAsString(String filePath) throws IOException {
		return new String(Files.readAllBytes(Paths.get(filePath)));
	}
}
