package view;

import java.awt.BorderLayout;
import java.awt.Desktop.Action;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class ViewFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;

	public String getTextField() {
		return textField.getText();
	}

	/**
	 * Launch the application.
	 */
	public static void initView() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewFrame frame = new ViewFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ViewFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblCommand = new JLabel("Command: ");
		panel.add(lblCommand);
		
		textField = new JTextField();
		panel.add(textField);
		textField.setColumns(28);
		textField.addActionListener(action);
		
		JTextPane textPane = new JTextPane();
		contentPane.add(textPane, BorderLayout.CENTER);
	}
	
	@SuppressWarnings("serial")
	AbstractAction action = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.out.println("Enter Pressed!!");
			
		}
	};

}
