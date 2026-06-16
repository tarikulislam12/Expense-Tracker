import java.awt.*;
import javax.swing.*;

public class Dashboard extends JFrame {

    JPanel contentPanel;

    public Dashboard() {

        setTitle("Expense Tracker Dashboard");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // ================= MENU =================
        JPanel menu = new JPanel();
        menu.setLayout(null);
        menu.setBounds(0, 0, 220, 550);
        menu.setBackground(new Color(31, 41, 55));
        add(menu);

        JLabel title = new JLabel("DASHBOARD");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBounds(45, 20, 150, 30);
        menu.add(title);

        JButton addBtn = new JButton("Add Expense");
        JButton viewBtn = new JButton("View Expenses");
        JButton statsBtn = new JButton("Statistics");
        JButton exitBtn = new JButton("Exit");

        JButton[] buttons = {addBtn, viewBtn, statsBtn, exitBtn};

        for (JButton b : buttons) {
            b.setFont(new Font("Segoe UI", Font.BOLD, 14));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
        }

        addBtn.setBackground(new Color(37, 99, 235));
        viewBtn.setBackground(new Color(16, 185, 129));
        statsBtn.setBackground(new Color(245, 158, 11));
        exitBtn.setBackground(new Color(220, 38, 38));

        addBtn.setBounds(20, 80, 180, 40);
        viewBtn.setBounds(20, 140, 180, 40);
        statsBtn.setBounds(20, 200, 180, 40);
        exitBtn.setBounds(20, 260, 180, 40);

        menu.add(addBtn);
        menu.add(viewBtn);
        menu.add(statsBtn);
        menu.add(exitBtn);

        // ================= CONTENT PANEL =================
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBounds(220, 0, 680, 550);
        add(contentPanel);

        showHome();

        // ================= ACTIONS =================
        addBtn.addActionListener(e -> setPanel(new AddExpenseFrame()));
        viewBtn.addActionListener(e -> setPanel(new ViewExpenseFrame()));

        // 🔥 FIXED STATISTICS (SAFE VERSION)
        statsBtn.addActionListener(e -> openStats());

        exitBtn.addActionListener(e -> System.exit(0));
    }

    // ================= SAFE STATS =================
    void openStats() {
        try {
            JPanel chartPanel = ExpenseChart.getChartPanel();

            if (chartPanel == null) {
                JOptionPane.showMessageDialog(this, "No chart data found!");
                return;
            }

            setPanel(chartPanel);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading statistics: " + ex.getMessage());
        }
    }

    // ================= PANEL SWITCH =================
    void setPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ================= HOME =================
    void showHome() {

        JPanel home = new JPanel(new BorderLayout());

        JLabel title = new JLabel("WELCOME TO EXPENSE TRACKER", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel sub = new JLabel("Manage Your Daily Expenses Easily", SwingConstants.CENTER);

        JPanel center = new JPanel(new GridLayout(2, 1));
        center.add(title);
        center.add(sub);

        home.add(center, BorderLayout.CENTER);

        setPanel(home);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}