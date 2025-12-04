package client;

import javax.swing.*;
import java.awt.*;

public class InicioView extends JFrame {

    public InicioView() {
        setTitle("Cliente - Vista Inicial");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(235, 243, 250));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Panel del logo circular
        LogoPanel logo = new LogoPanel();
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titulo = new JLabel("Sistema de monitoreo");
        titulo.setFont(new Font("Times", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel autor = new JLabel("Jesús Esquer, Desarrollo 3");
        autor.setFont(new Font("Times", Font.PLAIN, 16));
        autor.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnMonitor = new JButton("Monitor");
        btnMonitor.setBackground(new Color(150, 230, 170));
        btnMonitor.setFocusPainted(false);

        JButton btnHistorico = new JButton("Histórico");
        btnHistorico.setBackground(new Color(255, 215, 120));
        btnHistorico.setFocusPainted(false);

        // Redirección a tus ventanas reales
        btnMonitor.addActionListener(e -> new MonitorView().setVisible(true));
        btnHistorico.addActionListener(e -> new HistoricoView().setVisible(true));

        JPanel botones = new JPanel();
        botones.setOpaque(false);
        botones.add(btnMonitor);
        botones.add(btnHistorico);

        // Espaciado y acomodado
        panel.add(Box.createVerticalStrut(40));
        panel.add(logo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(titulo);
        panel.add(autor);
        panel.add(Box.createVerticalStrut(30));
        panel.add(botones);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InicioView().setVisible(true));
    }
}
