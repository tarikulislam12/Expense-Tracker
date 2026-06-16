#Personal Expense Tracker

A desktop application built with Java Swing and MySQL to help users manage daily expenses efficiently.

Features
Add new expenses
View all expenses
Expense statistics with Pie Chart
Category-wise expense tracking
Auto-suggestion for expense categories
MySQL database integration
User-friendly dashboard interface
Technologies Used
Java
Java Swing
JDBC
MySQL
JFreeChart
Database

Database Name:

expense_tracker

Example Table:

CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    expense_name VARCHAR(100),
    category VARCHAR(50),
    amount DOUBLE,
    expense_date DATE
);
How to Run
Clone the repository
git clone https://github.com/your-username/PersonalExpenseTracker.git
Create the MySQL database
CREATE DATABASE expense_tracker;
Update database credentials in DBConnection.java
String url = "jdbc:mysql://localhost:3306/expense_tracker";
String user = "root";
String password = "your_password";
Add MySQL Connector JAR and JFreeChart JAR
Run:
Dashboard.java
Screenshots

Add screenshots of:

Dashboard
Add Expense
View Expenses
Statistics (Pie Chart)
Author

Tarikul Islam
CSE Student
