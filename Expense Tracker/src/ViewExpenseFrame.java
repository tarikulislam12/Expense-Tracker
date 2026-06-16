import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewExpenseFrame extends JPanel {

    JTable table;
    DefaultTableModel model;

    JTextField searchField;
    JLabel totalLabel;

    JButton searchBtn, refreshBtn, editBtn, deleteBtn;

    Color bg = new Color(245, 248, 252);
    Color primary = new Color(13, 27, 42);
    Color accent = new Color(0, 168, 150);
    Color danger = new Color(220, 53, 69);
    Color warning = new Color(255, 193, 7);

    public ViewExpenseFrame() {

        setLayout(null);
        setBackground(bg);

        // ================= TITLE =================
        JLabel title = new JLabel("Expense Records");
        title.setBounds(20, 10, 300, 30);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(primary);
        add(title);

        // ================= SEARCH =================
        searchField = new JTextField();
        searchField.setBounds(20, 55, 220, 32);
        add(searchField);

        searchBtn = new JButton("Search");
        searchBtn.setBounds(250, 55, 100, 32);
        styleBtn(searchBtn, accent);
        add(searchBtn);

        refreshBtn = new JButton("Refresh");
        refreshBtn.setBounds(360, 55, 100, 32);
        styleBtn(refreshBtn, primary);
        add(refreshBtn);

        // ================= TOTAL =================
        JPanel totalPanel = new JPanel();
        totalPanel.setBounds(500, 45, 200, 45);
        totalPanel.setBackground(accent);

        totalLabel = new JLabel("Total: ৳ 0");
        totalLabel.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalPanel.add(totalLabel);

        add(totalPanel);

        // ================= TABLE =================
        model = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);

        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(primary);
        table.getTableHeader().setForeground(Color.WHITE);

        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Category");
        model.addColumn("Amount");
        model.addColumn("Date");

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 110, 680, 280);
        add(sp);

        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);

        // ================= EDIT BUTTON (PRO STYLE) =================
        editBtn = new JButton("✏ Edit Expense");
        editBtn.setBounds(200, 410, 160, 40);
        styleEditButton(editBtn);
        add(editBtn);

        // ================= DELETE BUTTON (PRO STYLE) =================
        deleteBtn = new JButton("🗑 Delete Expense");
        deleteBtn.setBounds(380, 410, 160, 40);
        styleDeleteButton(deleteBtn);
        add(deleteBtn);

        // ================= ACTIONS =================
        loadData();

        refreshBtn.addActionListener(e -> loadData());
        searchBtn.addActionListener(e -> searchData());

        editBtn.addActionListener(e -> editSelected());
        deleteBtn.addActionListener(e -> deleteSelected());
    }

    // ================= BUTTON STYLE =================
    private void styleBtn(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ================= EDIT BUTTON STYLE =================
    private void styleEditButton(JButton btn) {
        btn.setBackground(new Color(255, 193, 7)); // amber
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 150, 0), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ================= DELETE BUTTON STYLE =================
    private void styleDeleteButton(JButton btn) {
        btn.setBackground(new Color(220, 53, 69));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 30, 40), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ================= LOAD =================
    public void loadData() {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM expenses";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            model.setRowCount(0);

            double total = 0;

            while (rs.next()) {

                double amt = rs.getDouble("amount");
                total += amt;

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("expense_name"),
                        rs.getString("category"),
                        amt,
                        rs.getString("expense_date")
                });
            }

            totalLabel.setText("Total: ৳ " + total);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SEARCH =================
    public void searchData() {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM expenses WHERE expense_name LIKE ? OR category LIKE ?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, "%" + searchField.getText() + "%");
            pst.setString(2, "%" + searchField.getText() + "%");

            ResultSet rs = pst.executeQuery();

            model.setRowCount(0);

            double total = 0;

            while (rs.next()) {

                double amt = rs.getDouble("amount");
                total += amt;

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("expense_name"),
                        rs.getString("category"),
                        amt,
                        rs.getString("expense_date")
                });
            }

            totalLabel.setText("Total: ৳ " + total);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE =================
    public void deleteSelected() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row first!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            int id = (int) table.getValueAt(row, 0);

            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM expenses WHERE id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Deleted Successfully");

            loadData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= EDIT =================
    public void editSelected() {

        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row first!");
            return;
        }

        int id = (int) table.getValueAt(row, 0);

        JTextField name = new JTextField(table.getValueAt(row, 1).toString());
        JTextField category = new JTextField(table.getValueAt(row, 2).toString());
        JTextField amount = new JTextField(table.getValueAt(row, 3).toString());
        JTextField date = new JTextField(table.getValueAt(row, 4).toString());

        JPanel p = new JPanel(new GridLayout(4, 2, 5, 5));

        p.add(new JLabel("Name")); p.add(name);
        p.add(new JLabel("Category")); p.add(category);
        p.add(new JLabel("Amount")); p.add(amount);
        p.add(new JLabel("Date")); p.add(date);

        int ok = JOptionPane.showConfirmDialog(
                this, p, "Edit Expense",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (ok == JOptionPane.OK_OPTION) {

            try {
                Connection con = DBConnection.getConnection();

                String sql = "UPDATE expenses SET expense_name=?, category=?, amount=?, expense_date=? WHERE id=?";

                PreparedStatement pst = con.prepareStatement(sql);

                pst.setString(1, name.getText());
                pst.setString(2, category.getText());
                pst.setDouble(3, Double.parseDouble(amount.getText()));
                pst.setString(4, date.getText());
                pst.setInt(5, id);

                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Updated Successfully");

                loadData();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}