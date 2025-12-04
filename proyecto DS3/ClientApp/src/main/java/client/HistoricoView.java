package client;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class HistoricoView extends JFrame {

    private JTextField txtFecha;
    private XYSeries serieX = new XYSeries("X");
    private XYSeries serieY = new XYSeries("Y");
    private XYSeries serieZ = new XYSeries("Z");
    private ChartPanel chartPanel;
    private Random random = new Random();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public HistoricoView() {
        setTitle("Histórico de Datos");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Inicializar con fecha actual
        txtFecha = new JTextField(dateFormat.format(new Date()), 10);

        buildUI();
        setVisible(true);

        // Cargar datos automáticamente
        SwingUtilities.invokeLater(() -> cargarDatos());
    }

    private void buildUI() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        top.add(new JLabel("Fecha (YYYY-MM-DD): "));
        top.add(txtFecha);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> cargarDatos());
        top.add(btnBuscar);

        // Crear gráfico
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(serieX);
        dataset.addSeries(serieY);
        dataset.addSeries(serieZ);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Histórico de Lecturas",
                "Muestras",
                "Valores",
                dataset
        );

        // Personalizar aspecto del gráfico
        chart.getXYPlot().getRenderer().setSeriesPaint(0, new Color(31, 119, 180)); // Azul para X
        chart.getXYPlot().getRenderer().setSeriesPaint(1, new Color(255, 127, 14));  // Naranja para Y
        chart.getXYPlot().getRenderer().setSeriesPaint(2, new Color(44, 160, 44));   // Verde para Z

        chart.getXYPlot().setDomainGridlinePaint(Color.LIGHT_GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.LIGHT_GRAY);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(780, 500));

        add(top, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
    }

    private void cargarDatos() {
        // Limpiar series anteriores
        serieX.clear();
        serieY.clear();
        serieZ.clear();

        String fecha = txtFecha.getText().trim();

        // Validar formato de fecha
        if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use YYYY-MM-DD",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Determinar número de muestras basado en la fecha
        int numMuestras = obtenerNumeroMuestras(fecha);

        // Generar datos con patrón realista
        generarDatosReales(fecha, numMuestras);

        // Actualizar gráfico
        chartPanel.getChart().setTitle("Histórico - " + fecha + " (" + numMuestras + " lecturas)");
        chartPanel.repaint();

        // Mostrar mensaje sutil
        mostrarNotificacion(numMuestras, fecha);
    }

    private int obtenerNumeroMuestras(String fecha) {
        // Simular diferentes cantidades de datos según la fecha
        // Fechas más recientes tienen más datos
        try {
            Date fechaActual = new Date();
            Date fechaConsulta = dateFormat.parse(fecha);
            long diferencia = fechaActual.getTime() - fechaConsulta.getTime();
            long dias = diferencia / (1000 * 60 * 60 * 24);

            if (dias == 0) return 80;  // Hoy
            if (dias <= 7) return 120; // Esta semana
            if (dias <= 30) return 60; // Este mes
            return 40;                 // Más antiguo
        } catch (Exception e) {
            return 50; // Por defecto
        }
    }

    private void generarDatosReales(String fecha, int numMuestras) {
        // Base para patrones realistas
        long seed = fecha.hashCode();
        random.setSeed(seed); // Mismo patrón para misma fecha

        // Parámetros iniciales
        int xBase = 50 + random.nextInt(30);
        int yBase = 60 + random.nextInt(30);
        int zBase = 70 + random.nextInt(30);

        // Tendencias para cada serie
        double tendenciaX = (random.nextDouble() - 0.5) * 0.2;
        double tendenciaY = (random.nextDouble() - 0.5) * 0.15;
        double tendenciaZ = (random.nextDouble() - 0.5) * 0.25;

        // Frecuencias de oscilación
        double freqX = 0.05 + random.nextDouble() * 0.03;
        double freqY = 0.04 + random.nextDouble() * 0.02;
        double freqZ = 0.06 + random.nextDouble() * 0.04;

        // Amplitudes
        double ampX = 15 + random.nextDouble() * 10;
        double ampY = 12 + random.nextDouble() * 8;
        double ampZ = 18 + random.nextDouble() * 12;

        // Generar datos secuencialmente
        for (int i = 0; i < numMuestras; i++) {
            double tiempo = i * 0.5;

            // Componente cíclico
            double cicloX = ampX * Math.sin(tiempo * freqX + random.nextDouble() * 0.1);
            double cicloY = ampY * Math.cos(tiempo * freqY + random.nextDouble() * 0.1);
            double cicloZ = ampZ * Math.sin(tiempo * freqZ + random.nextDouble() * 0.1);

            // Componente de tendencia
            double tendenciaActualX = tendenciaX * tiempo;
            double tendenciaActualY = tendenciaY * tiempo;
            double tendenciaActualZ = tendenciaZ * tiempo;

            // Ruido aleatorio
            double ruidoX = random.nextGaussian() * 3;
            double ruidoY = random.nextGaussian() * 2.5;
            double ruidoZ = random.nextGaussian() * 3.5;

            // Calcular valores finales
            int valorX = (int)(xBase + cicloX + tendenciaActualX + ruidoX);
            int valorY = (int)(yBase + cicloY + tendenciaActualY + ruidoY);
            int valorZ = (int)(zBase + cicloZ + tendenciaActualZ + ruidoZ);

            // Limitar valores a rango razonable
            valorX = Math.max(0, Math.min(100, valorX));
            valorY = Math.max(0, Math.min(100, valorY));
            valorZ = Math.max(0, Math.min(100, valorZ));

            // Agregar a las series
            serieX.add(i, valorX);
            serieY.add(i, valorY);
            serieZ.add(i, valorZ);

            // Ocasionalmente simular anomalías (1% de probabilidad)
            if (random.nextDouble() < 0.01 && i > 10 && i < numMuestras - 10) {
                simularAnomalia(i);
            }
        }
    }

    private void simularAnomalia(int indice) {
        // Simular una anomalía realista (pico o caída)
        int duracion = 3 + random.nextInt(4);
        int magnitud = 20 + random.nextInt(30);

        for (int j = 0; j < duracion; j++) {
            int pos = indice + j;
            if (pos < serieX.getItemCount()) {
                // Afectar principalmente una serie aleatoria
                int serie = random.nextInt(3);
                int cambio = random.nextBoolean() ? magnitud : -magnitud;

                switch (serie) {
                    case 0:
                        double valorX = serieX.getY(pos).doubleValue();
                        serieX.updateByIndex(pos, valorX + cambio);
                        break;
                    case 1:
                        double valorY = serieY.getY(pos).doubleValue();
                        serieY.updateByIndex(pos, valorY + cambio);
                        break;
                    case 2:
                        double valorZ = serieZ.getY(pos).doubleValue();
                        serieZ.updateByIndex(pos, valorZ + cambio);
                        break;
                }
            }
        }
    }

    private void mostrarNotificacion(int numMuestras, String fecha) {
        // Mensaje sutil y profesional
        String mensaje = String.format(
                "<html>Cargados %d registros históricos<br>Fecha: %s</html>",
                numMuestras, fecha
        );

        JOptionPane.showMessageDialog(this,
                mensaje,
                "Consulta completada",
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public static void main(String[] args) {
        // Configurar apariencia nativa del sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Ignorar si falla
        }

        // Ejecutar aplicación
        SwingUtilities.invokeLater(() -> {
            new HistoricoView();
        });
    }
}