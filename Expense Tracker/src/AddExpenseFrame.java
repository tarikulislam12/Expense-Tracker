import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import javax.swing.*;

public class AddExpenseFrame extends JPanel {

    JTextField name = new JTextField(15);

    JComboBox<String> categoryBox = new JComboBox<>();
    JTextField amount = new JTextField(15);
    JTextField date = new JTextField(15);

    public AddExpenseFrame() {

        setLayout(new GridBagLayout());
        setBackground(new Color(240, 242, 245));

        date.setText(LocalDate.now().toString());

        loadCategories(); 

        categoryBox.setEditable(true); 

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);

        JLabel title = new JLabel("Add Expense");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JLabel l1 = new JLabel("Name");
        JLabel l2 = new JLabel("Category");
        JLabel l3 = new JLabel("Amount");
        JLabel l4 = new JLabel("Date");

        l1.setFont(labelFont);
        l2.setFont(labelFont);
        l3.setFont(labelFont);
        l4.setFont(labelFont);

        JButton save = new JButton("SAVE");
        save.setBackground(new Color(0, 123, 255));
        save.setForeground(Color.WHITE);

        int y = 0;

        gbc.gridx = 0; gbc.gridy = y++;
        gbc.gridwidth = 2;
        card.add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = y;
        card.add(l1, gbc);
        gbc.gridx = 1;
        card.add(name, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        card.add(l2, gbc);
        gbc.gridx = 1;
        card.add(categoryBox, gbc); 
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        card.add(l3, gbc);
        gbc.gridx = 1;
        card.add(amount, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        card.add(l4, gbc);
        gbc.gridx = 1;
        card.add(date, gbc);
        y++;

        gbc.gridx = 0; gbc.gridy = y;
        gbc.gridwidth = 2;
        card.add(save, gbc);

        add(card);

        save.addActionListener(e -> saveData());
    }

    // ================= LOAD CATEGORY FROM DB =================
    void loadCategories() {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "SELECT DISTINCT category FROM expenses";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            categoryBox.addItem(""); // empty first

            while (rs.next()) {
                categoryBox.addItem(rs.getString(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= SAVE DATA =================
    void saveData() {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "INSERT INTO expenses(expense_name, category, amount, expense_date) VALUES (?,?,?,?)";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, name.getText());

            // 🔥 combo box selected OR typed value
            String category = categoryBox.getEditor().getItem().toString();
            pst.setString(2, category);

            pst.setDouble(3, Double.parseDouble(amount.getText()));
            pst.setString(4, date.getText());

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Saved Successfully!");

            name.setText("");
            amount.setText("");
            date.setText(LocalDate.now().toString());

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error Saving Data!");
        }
    }
}