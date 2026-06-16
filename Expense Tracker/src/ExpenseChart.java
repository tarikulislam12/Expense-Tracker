import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.data.general.DefaultPieDataset;

public class ExpenseChart {

    public static JPanel getChartPanel() {

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(new Color(245, 246, 250));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ================= HEADER =================
        JPanel header = new JPanel();
        header.setBackground(new Color(245, 246, 250));

        JLabel title = new JLabel("Expense Analytics Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(33, 37, 41));

        header.add(title);
        main.add(header, BorderLayout.NORTH);

        // ================= DATA =================
        DefaultPieDataset dataset = new DefaultPieDataset();
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Category");
        model.addColumn("Total Amount");

        try {
            Connection con = DBConnection.getConnection();

            if (con == null) {
                main.add(errorLabel("Database Connection Failed"));
                return main;
            }

            String sql = "SELECT category, SUM(amount) FROM expenses GROUP BY category";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            boolean hasData = false;

            while (rs.next()) {
                hasData = true;

                String category = rs.getString(1);
                double amount = rs.getDouble(2);

                dataset.setValue(category, amount);
                model.addRow(new Object[]{category, "৳ " + amount});
            }

            if (!hasData) {
                main.add(errorLabel("No Expense Data Found"));
                return main;
            }

        } catch (Exception e) {
            main.add(errorLabel("Error: " + e.getMessage()));
            return main;
        }

        // ================= PIE CHART =================
        JFreeChart chart = ChartFactory.createPieChart(
                "Expense Distribution",
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setLabelFont(new Font("Segoe UI", Font.PLAIN, 12));
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {1} ৳ ({2})"));

        // 🎨 Modern colors
        plot.setSectionPaint("Food", new Color(255, 99, 132));
        plot.setSectionPaint("Transport", new Color(54, 162, 235));
        plot.setSectionPaint("Shopping", new Color(255, 206, 86));
        plot.setSectionPaint("Bills", new Color(75, 192, 192));

        plot.setBackgroundPaint(Color.WHITE);
        plot.setOutlineVisible(false);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 320));
        chartPanel.setBackground(Color.WHITE);

        // ================= TABLE =================
        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setGridColor(new Color(220, 220, 220));

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(600, 180));

        // ================= WRAP PANEL =================
        JPanel center = new JPanel(new BorderLayout(12, 12));
        center.setBackground(Color.WHITE);

        center.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        center.add(chartPanel, BorderLayout.CENTER);
        center.add(tableScroll, BorderLayout.SOUTH);

        main.add(center, BorderLayout.CENTER);

        return main;
    }

    // ================= ERROR LABEL =================
    private static JLabel errorLabel(String msg) {
        JLabel label = new JLabel(msg, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.RED);
        return label;
    }
}