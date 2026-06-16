import java.sql.*;
import javax.swing.*;

public class UpdateExpenseFrame extends JFrame {

    JTextField id, name, category, amount, date;

    public UpdateExpenseFrame() {

        setTitle("Update Expense");
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        id = new JTextField();
        name = new JTextField();
        category = new JTextField();
        amount = new JTextField();
        date = new JTextField();

        id.setBounds(150, 50, 180, 25);
        name.setBounds(150, 90, 180, 25);
        category.setBounds(150, 130, 180, 25);
        amount.setBounds(150, 170, 180, 25);
        date.setBounds(150, 210, 180, 25);

        add(new JLabel("ID")).setBounds(50, 50, 100, 25);
        add(id);

        add(new JLabel("Name")).setBounds(50, 90, 100, 25);
        add(name);

        add(new JLabel("Category")).setBounds(50, 130, 100, 25);
        add(category);

        add(new JLabel("Amount")).setBounds(50, 170, 100, 25);
        add(amount);

        add(new JLabel("Date")).setBounds(50, 210, 100, 25);
        add(date);

        JButton update = new JButton("Update");
        update.setBounds(150, 260, 100, 30);
        add(update);

        update.addActionListener(e -> updateData());
    }

    void updateData() {

        try {
            Connection con = DBConnection.getConnection();

            String sql = "UPDATE expenses SET expense_name=?, category=?, amount=?, expense_date=? WHERE id=?";

            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, name.getText());
            pst.setString(2, category.getText());
            pst.setDouble(3, Double.parseDouble(amount.getText()));
            pst.setString(4, date.getText());
            pst.setInt(5, Integer.parseInt(id.getText()));

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Updated!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}