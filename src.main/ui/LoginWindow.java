package main.ui;

import main.delegates.LoginWindowDelegate;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The class is only responsible for displaying and handling the login GUI.
 * From demo project CPSC304_Java_Project with changes
 */
public class LoginWindow extends JFrame implements ActionListener {
    private static final int TEXT_FIELD_WIDTH = 10;
    private static final int MAX_LOGIN_ATTEMPTS = 3;

    // running accumulator for login attempts
    private int loginAttempts;

    // components of the login window
    private JTextField usernameField;
    private JPasswordField passwordField;

    // delegate
    private LoginWindowDelegate delegate;

    public LoginWindow() {
        super("User Login");
    }

    public void showFrame(LoginWindowDelegate delegate) {
        this.delegate = delegate;
        loginAttempts = 0;

        JLabel usernameLabel = new JLabel("Enter username: ");
        JLabel passwordLabel = new JLabel("Enter password: ");

        usernameLabel.setForeground(Color.WHITE);
        passwordLabel.setForeground(Color.WHITE);

        usernameField = new JTextField(TEXT_FIELD_WIDTH);
        passwordField = new JPasswordField(TEXT_FIELD_WIDTH);
        passwordField.setEchoChar('*');

        usernameField.setText("ora_CWLID");

        JButton loginButton = new JButton("Log In");

        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPane.setLayout(gb);
//        contentPane.setBackground(new Color(34, 34, 34)); // Dark gray color
        contentPane.setBackground(new Color(48, 26, 49)); // Dark gray color


        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel logoLabel = null;
        try {
            BufferedImage logo = ImageIO.read(new File("Resources/Images/craiyon_103106_basketball_manager_logo.png"));
            logoLabel = new JLabel(new ImageIcon(logo));
        } catch (IOException e) {
            System.out.println(e);
        }

        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 10, 20, 10);
        gb.setConstraints(logoLabel, c);
        contentPane.add(logoLabel);

        // place the username label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(usernameLabel, c);
        contentPane.add(usernameLabel);

        // place the text field for the username
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(usernameField, c);
        contentPane.add(usernameField);

        // place password label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(0, 10, 10, 0);
        gb.setConstraints(passwordLabel, c);
        contentPane.add(passwordLabel);

        // place the password field
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(0, 0, 10, 10);
        gb.setConstraints(passwordField, c);
        contentPane.add(passwordField);

        // place the login button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(loginButton, c);
        contentPane.add(loginButton);

        // register login button with action event handler
        loginButton.addActionListener(this);

        // anonymous inner class for closing the window
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // size the window to obtain a best fit for the components
        this.pack();

        // center the frame
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

        // make the window visible
        this.setVisible(true);

        // place the cursor in the text field for the username
        usernameField.requestFocus();
    }

    public void handleLoginFailed() {
        loginAttempts++;
        passwordField.setText(""); // clear password field
    }

    public boolean hasReachedMaxLoginAttempts() {
        return (loginAttempts >= MAX_LOGIN_ATTEMPTS);
    }

    /**
     * ActionListener Methods
     */
    @Override
    public void actionPerformed(ActionEvent e) {
//		delegate.login("ora_kookevk", "a16389124");
        delegate.login("ora_kookevk", String.valueOf(passwordField.getPassword()));
    }
}
