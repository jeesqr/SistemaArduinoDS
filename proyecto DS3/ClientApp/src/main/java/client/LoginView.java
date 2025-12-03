package client;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    public LoginView() {
        setTitle("Cliente - Login");
        setSize(350, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1));

        JTextField usuario = new JTextField();
        JButton loginBtn = new JButton("Acceder");

        panel.add(new JLabel("Usuario:"));
        panel.add(usuario);
        panel.add(loginBtn);

        loginBtn.addActionListener(e -> {
            dispose();
            new MainMenuView();
        });

        add(panel);
        setVisible(true);
    }
}
