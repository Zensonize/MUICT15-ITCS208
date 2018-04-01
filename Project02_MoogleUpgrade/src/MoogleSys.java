import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.Font;

public class MoogleSys {

	private JFrame frame;
	private JTextField textUsername;
	private JPasswordField txtPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MoogleSys window = new MoogleSys();
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
	public MoogleSys() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblLogin = new JLabel("Moogle");
		lblLogin.setBounds(100, 35, 45, 13);
		frame.getContentPane().add(lblLogin);
		
		JLabel lbUsername = new JLabel("Username");
		lbUsername.setFont(new Font("TH Sarabun New", Font.PLAIN, 30));
		lbUsername.setBounds(24, 114, 137, 40);
		frame.getContentPane().add(lbUsername);
		
		JLabel lbPassword = new JLabel("Password");
		lbPassword.setFont(new Font("TH Sarabun New", Font.PLAIN, 30));
		lbPassword.setBounds(24, 173, 137, 40);
		frame.getContentPane().add(lbPassword);
		
		textUsername = new JTextField();
		textUsername.setBounds(150, 117, 200, 40);
		frame.getContentPane().add(textUsername);
		textUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		txtPassword.setBounds(150, 173, 200, 40);
		frame.getContentPane().add(txtPassword);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setFont(new Font("TH Sarabun New", Font.PLAIN, 30));
		btnLogin.setBounds(51, 254, 110, 40);
		frame.getContentPane().add(btnLogin);
		
		JButton btnRegister = new JButton("Register");
		btnRegister.setFont(new Font("TH Sarabun New", Font.PLAIN, 30));
		btnRegister.setBounds(223, 254, 110, 40);
		frame.getContentPane().add(btnRegister);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setFont(new Font("TH Sarabun New", Font.PLAIN, 50));
		btnSearch.setBounds(469, 133, 220, 80);
		frame.getContentPane().add(btnSearch);
	}
}
