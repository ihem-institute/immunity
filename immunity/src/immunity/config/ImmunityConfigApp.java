package immunity.config;

import immunity.ModelProperties;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.codehaus.jackson.map.ObjectMapper;

import javax.swing.JTable;


public class ImmunityConfigApp {

	private JFrame frame;
	private JTextField textField;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImmunityConfigApp window = new ImmunityConfigApp();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ImmunityConfigApp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		Dimension d = new Dimension(600,400);
		frame.setSize(new Dimension(800, 800)); 
        frame.setPreferredSize(new Dimension(800, 800));
		frame.setBounds(100, 100, 800, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
        frame.pack();
		
		JButton btnNewButton = new JButton("Ok");
		btnNewButton.setBounds(336, 227, 117, 29);
		btnNewButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// display/center the jdialog when the button is pressed
				ObjectMapper objectMapper = new ObjectMapper();
				double rcyl = Double.parseDouble(textField.getText());
		        JDialog d = new JDialog(frame, "Hello", true);
		        d.setLocationRelativeTo(frame);
		        d.setVisible(true);
		        
		        ModelProperties cellProperties = ModelProperties.getInstance();
		        cellProperties.getCellK().put("rcyl", rcyl);
		        try {
					objectMapper.writeValue(new File(ModelProperties.configFilename), cellProperties);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		frame.getContentPane().add(btnNewButton);
		textField = new JTextField();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			ModelProperties cellProperties = objectMapper.readValue(new File(ModelProperties.configFilename), ModelProperties.class);
			textField.setText(Double.toString(cellProperties.getCellK().get("rcyl")));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		textField.setBounds(126, 37, 134, 28);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblMyLabel = new JLabel("rcyl");
		lblMyLabel.setBounds(26, 43, 61, 16);
		frame.getContentPane().add(lblMyLabel);
		Object rowData[][] = { { "Row1-Column1", "Row1-Column2", "Row1-Column3" },
		        { "Row2-Column1", "Row2-Column2", "Row2-Column3" } };
		    Object columnNames[] = { "Column One", "Column Two", "Column Three" };
		    JTable table = new JTable(rowData, columnNames);
		    table.setBounds(126, 37, 134, 28);
		    JScrollPane scrollPane = new JScrollPane(table);
		    scrollPane.setSize(500, 100);
		    scrollPane.setLocation(40, 99);
		    frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		    frame.setSize(300, 150);		
				
	}
}
