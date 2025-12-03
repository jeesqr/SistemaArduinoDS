package client;

import javax.swing.*;
import java.awt.*;

public class MainMenuView extends JFrame {

    public MainMenuView() {
        setTitle("Cliente - Menú Principal");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton monitorBtn = new JButton("Monitor en Tiempo Real");
        JButton historicoBtn = new JButton("Histórico");

        monitorBtn.addActionListener(e -> new MonitorView());
        historicoBtn.addActionListener(e -> new HistoricoView());

        setLayout(new GridLayout(2, 1));
        add(monitorBtn);
        add(historicoBtn);

        setVisible(true);
    }
}
