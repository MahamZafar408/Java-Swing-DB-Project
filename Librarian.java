import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Librarian extends JFrame implements ActionListener 
{
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;

    public String getFirstName() 
    { 
    	return firstName;  
    }
    public void setFirstName(String fName) 
    {
    	firstName = fName; 
    }
    
    public String getLastName() 
    { 
    	return lastName; 
    }
    public void setLastName(String lName) 
    {
    	lastName = lName; 
    }
    public String getEmail() 
    { 
    	return email; 
    }
    public void setEmail(String mail)
    {
    	email = mail; }
    
    public String getPhoneNumber() 
    {
    	return phoneNumber;
    }
    public void setPhoneNumber(String phno) 
    { 
    	phoneNumber = phno; 
    }
    public String getPassword()
    { 
    	return password;
    }
    
    public void setPassword(String pswrd)
    {
    	password = pswrd; 
    }
    
    private JButton addBookButton, removeBookButton, viewUserButton, viewBorrowedButton, availableBooksButton , blockedUsersButton;
    private JLabel titleLabel;
    Librarian(){
    	firstName="";
         lastName="";
        email="";
        phoneNumber="";
         password="";
    }

    public Librarian(String email) {
        setTitle("Library Management System ");
        setSize(800, 500);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
        
        ImageIcon imgicon = new ImageIcon("C:\\Users\\PMLS\\Downloads\\userpage.jpeg");
        Image scaledimage = imgicon.getImage().getScaledInstance(380, 500, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaledimage);
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setBounds(0, 0, 380, 500);
        add(imageLabel);
        
        
        titleLabel = new JLabel("Welcome Librarian!");
        titleLabel.setBounds(450, 20, 300, 60);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel);

        addBookButton = new JButton("Add Book");
        addBookButton.setBounds(450, 130, 300, 30);
        addBookButton.addActionListener(this);
        add(addBookButton);

        removeBookButton = new JButton("Remove Book");
        removeBookButton.setBounds(450, 180, 300, 30);
        removeBookButton.addActionListener(this);
        add(removeBookButton);

        viewUserButton = new JButton("View Users");
        viewUserButton.setBounds(450, 230, 300, 30);
        viewUserButton.addActionListener(this);
        add(viewUserButton);

        viewBorrowedButton = new JButton("View Borrowed Books");
        viewBorrowedButton.setBounds(450, 280, 300, 30);
        viewBorrowedButton.addActionListener(this);
        add(viewBorrowedButton);

        availableBooksButton = new JButton("Available Books");
        availableBooksButton.setBounds(450, 330, 300, 30);
        availableBooksButton.addActionListener(this);
        add(availableBooksButton);
        
        blockedUsersButton = new JButton("Blocked Users");
        blockedUsersButton.setBounds(450, 380, 300, 30);
        blockedUsersButton.addActionListener(this);
        add(blockedUsersButton);

        setVisible(true);
    }
  
    void librarianlogin(String e, String p) {
    	String email = e;
		  String password = p;
		  try {
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Librarian WHERE LibrarianEmail = ? AND LibrarianPassword = ?");
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        String rs  =" ";
        while(resultSet.next()){
           	String em = resultSet.getString("LibrarianEmail");
           	String pass = resultSet.getString("LibrarianPassword");
      	    rs+= em+" "+pass;
        }
         if (!(rs.isBlank())) {
      	 System.out.println(rs);
           JOptionPane.showMessageDialog(this, "Login successful!");
            new Librarian(email); 
           dispose();
         } else {
           JOptionPane.showMessageDialog(this, "Invalid email or password.");
         }
  
  } catch (Exception a) {
  	 a.printStackTrace();
      JOptionPane.showMessageDialog(this, "Connection error.");
  }
 }
    
    void librariansignup(String fname,String lname, String em, String phn,String pass) {
    	try {
    		 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
  	       Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
  	       System.out.println("Connected Successfully");
  	       String sql1 = "SELECT MAX(LibrarianId) AS MaxID FROM Librarian";
  	       PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
  	       ResultSet rs1 =preparedStatement1.executeQuery();
  	       int lid = 1442000; 
  	       if (rs1.next()) {
  	           int maxId = rs1.getInt("MaxID");
  	               lid = maxId + 1;
  	           
  	       }
  	       String sql = "INSERT INTO Librarian(LibrarianId,LibrarianFirstName,LibrarianLastName,LibrarianEmail,LibrarianPhoneNumber,LibrarianPassword) VALUES (?, ?, ?, ?, ?, ?)";
  	       PreparedStatement preparedStatement = connection.prepareStatement(sql);
  	       
  	       preparedStatement.setInt(1, lid);
  	       preparedStatement.setString(2, fname);
  	       preparedStatement.setString(3, lname);
  	       preparedStatement.setString(4, em);
  	       preparedStatement.setString(5, phn);
  	       preparedStatement.setString(6, pass);
  	       System.out.println("uid: " + lid);
  	       System.out.println("fname: " + fname);
  	       System.out.println("lname: " + lname);
  	       System.out.println("email: " + em);
  	       System.out.println("phone: " + phn);
  	       System.out.println("password: " + pass);
  	       int d = preparedStatement.executeUpdate();
  	       
  	       if(d > 0) {
  	           JOptionPane.showMessageDialog(this, "Signup successful!");
  	       } else {
  	           JOptionPane.showMessageDialog(this, "Signup failed.");
  	       }

  		   } catch (Exception a) {
  			    a.printStackTrace();
  			    JOptionPane.showMessageDialog(this, "Error: " + a.getMessage());
  			}
    	
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == addBookButton) {
                addBook();
            }
            else if (e.getSource() == removeBookButton) {
                removeBook();
            }
            else if (e.getSource() == viewUserButton) {
                viewUsers();
            } 
            else if (e.getSource() == viewBorrowedButton) {
                viewBorrowedBooks();
            } 
            else if (e.getSource() == availableBooksButton) {
                viewAvailableBooks();
            }else if(e.getSource()==blockedUsersButton)
            {
                viewBlockedUsers();
        }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, " Error : " + ex.getMessage());
        }
    }

    private void addBook() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        
        JTextField bookNameField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField editionField = new JTextField();
        JTextField publisherField = new JTextField();
        JTextField yearField = new JTextField();
        
        panel.add(new JLabel("Book Name:"));
        panel.add(bookNameField);
        panel.add(new JLabel("Author:"));
        panel.add(authorField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Edition:"));
        panel.add(editionField);
        panel.add(new JLabel("Publisher:"));
        panel.add(publisherField);
        panel.add(new JLabel("Publication Year:"));
        panel.add(yearField);
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Book",  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String bookName = bookNameField.getText();
                String author = authorField.getText();
                String category = categoryField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                String edition = editionField.getText();
                String publisher = publisherField.getText();
                int year = Integer.parseInt(yearField.getText());
                
                Book book = new Book(bookName, new Author(author), quantity, edition, publisher, year);
                boolean success = book.addbook(bookName, author, category, quantity, edition, publisher, year);
                
                if (success) 
                {
                    JOptionPane.showMessageDialog(this, "Book added successfully!");
                } else 
                {
                    JOptionPane.showMessageDialog(this, "Failed to add book. It may already exist.");
                }
            } 
             catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

private void removeBook() {
    String bookid = JOptionPane.showInputDialog(this, "Enter Book ID to remove:");
    
    if (bookid != null && !bookid.trim().isEmpty()) {
        try {
            int bookId = Integer.parseInt(bookid);
            Book book = new Book();
            boolean success = book.removebook(bookId);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Book removed successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to remove book.");
            }
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}


    
private void viewBorrowedBooks() {
    try {
        String res = "<html><body><table border='1'><tr><th>Book ID</th><th>Book Name</th><th>Borrow Date</th><th>Return Date</th><th>Due Date</th><th>User ID</th></tr>";
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
        
        
        String query = "SELECT BookId, BookName, BorrowDate, ReturnDate, DueDate, UserId FROM BorrowedBook";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        
        while (resultSet.next()) {
            int bookId = resultSet.getInt("BookId");
            String bookName = resultSet.getString("BookName");
            String borrowDate = resultSet.getString("BorrowDate");
            String returnDate = resultSet.getString("ReturnDate");
            String dueDate = resultSet.getString("DueDate");
            int userId = resultSet.getInt("UserId");
            
            res += "<tr><td>" + bookId + "</td><td>" + bookName + "</td><td>" + (borrowDate != null ? borrowDate : "N/A") + "</td><td>" + (returnDate != null ? returnDate : "Not returned") + "</td><td>" + (dueDate != null ? dueDate : "N/A") + "</td><td>" + userId + "</td></tr>";
        }
        
        res += "</table></body></html>";
        
        JFrame frame = new JFrame("Borrowed Books");
        JLabel label = new JLabel(res);
        JScrollPane scrollPane = new JScrollPane(label);
        frame.add(scrollPane);
        frame.setSize(900, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        connection.close();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error viewing borrowed books: " + ex.getMessage());
    }
}

    private void viewAvailableBooks() 
    {
    	try {
		String res = "<html><body><table border='1'><tr><td>BookID</td><td>BookName</td><td>CategoryId</td><td>Authors</td><td>Edition</td><td>PublisherName</td><td>PublicationYear</td><td>Quantity</td></tr>";
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
        System.out.println("Connected Successfully");
        String av = "Yes";
        PreparedStatement preparedStatement1=connection.prepareStatement("select * from Book where Availability like '"+av+"'");
        ResultSet resultSet=preparedStatement1.executeQuery();
        while(resultSet.next()){
            String BookName=resultSet.getString("BookName");
            int BookId=resultSet.getInt("BookId");
            int cid=resultSet.getInt("CategoryId");
            String authors=resultSet.getString("Authors");
            String edition=resultSet.getString("Edition");
            String publishername=resultSet.getString("PublisherName");
            String pubdate=resultSet.getString("PublicationYear");
            int quantity=resultSet.getInt("Quantity");
            PreparedStatement preparedStatement2=connection.prepareStatement("select CategoryName from Category where CategoryId like '"+cid+"'");
            ResultSet resultSet2=preparedStatement2.executeQuery();
            String category=" ";
            if (resultSet2.next()) {
                category = resultSet2.getString("CategoryName");
            }
            res += "<tr><td>" + BookId + "</td><td>" + BookName + "</td><td>" + category + "</td><td>" + authors + "</td><td>" + edition + "</td><td>" + publishername + "</td><td>" + pubdate + "</td><td>" + quantity + "</td></tr>";
         }	
        res += "</table></body></html>";
        JFrame frame = new JFrame("Available Books");
        JLabel label = new JLabel(res.toString());
        JScrollPane scrollPane = new JScrollPane(label);

        frame.add(scrollPane);
        frame.setSize(1050, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);   
    
	}catch(Exception a){
        System.out.println("Error in connection"+ a.getMessage());
	}
    }
    
    private void viewBlockedUsers() {
    	try {
    	    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
    	    Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
    	    System.out.println("Connected Successfully");

    	    String query = "SELECT UserId, UserFirstName, UserLastName, UserEmail, UserPhoneNumber, Status FROM USER WHERE Status = 'Blocked'";
    	    PreparedStatement statement = connection.prepareStatement(query);
    	    ResultSet resultSet = statement.executeQuery();

    	    JFrame frame = new JFrame("Blocked Users");
    	    frame.setSize(1000, 400);
    	    frame.setLocationRelativeTo(null);
    	    frame.setLayout(new BorderLayout());

    	    DefaultTableModel model = new DefaultTableModel(new Object[]{
    	        "User ID", "First Name", "Last Name", "Email", "Phone Number", "Status"
    	    }, 0);
    	    JTable table = new JTable(model);
    	    JScrollPane scrollPane = new JScrollPane(table);
    	    frame.add(scrollPane, BorderLayout.CENTER);

    	   
    	    boolean found = false;
    	    while (resultSet.next()) {
    	        found = true;
    	        int userId = resultSet.getInt("UserId");
    	        String firstName = resultSet.getString("UserFirstName");
    	        String lastName = resultSet.getString("UserLastName");
    	        String email = resultSet.getString("UserEmail");
    	        String phone = resultSet.getString("UserPhoneNumber");
    	        String status = resultSet.getString("Status");

    	        model.addRow(new Object[]{userId, firstName, lastName, email, phone, status});
    	    }

    	    if (!found) {
    	        JOptionPane.showMessageDialog(null, "No blocked users found.");
    	        return;
    	    }

    	    
    	    JButton removeButton = new JButton("Remove Selected User");
    	    frame.add(removeButton, BorderLayout.SOUTH);

    	    removeButton.addActionListener(e -> {
    	        int row = table.getSelectedRow();
    	        if (row == -1) {
    	            JOptionPane.showMessageDialog(frame, "Please select a user to remove.");
    	            return;
    	        }

    	        int selectedUserId = (int) model.getValueAt(row, 0);
    	        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to remove user ID " + selectedUserId + "?", "Confirm", JOptionPane.YES_NO_OPTION);

    	        if (confirm == JOptionPane.YES_OPTION) {
    	            try {
    	                PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM USER WHERE UserId = ?");
    	                deleteStmt.setInt(1, selectedUserId);
    	                int rowsAffected = deleteStmt.executeUpdate();

    	                if (rowsAffected > 0) {
    	                    JOptionPane.showMessageDialog(frame, "User removed successfully.");
    	                    model.removeRow(row);
    	                } else {
    	                    JOptionPane.showMessageDialog(frame, "Failed to remove user.");
    	                }
    	            } catch (Exception ex) {
    	                JOptionPane.showMessageDialog(frame, "Error removing user: " + ex.getMessage());
    	            }
    	        }
    	    });

    	    frame.setVisible(true);
    	} catch (Exception e) {
    	    JOptionPane.showMessageDialog(null, "Error viewing blocked users: " + e.getMessage());
    	}

    }
    
    
    private void viewUsers() {
        try {
            String res = "<html><body><table border='1'><tr><th>User ID</th><th>First Name</th><th>Last Name</th><th>Email</th><th>Phone Number</th><th>Status</th><th>Books Borrowed</th></tr>";
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
            
            
            String userQuery = "SELECT UserId, UserFirstName, UserLastName, UserEmail, UserPhoneNumber, Status FROM USER";
            PreparedStatement userStatement = connection.prepareStatement(userQuery);
            ResultSet userResult = userStatement.executeQuery();
            
            while (userResult.next()) {
                int userId = userResult.getInt("UserId");
                String firstName = userResult.getString("UserFirstName");
                String lastName = userResult.getString("UserLastName");
                String email = userResult.getString("UserEmail");
                String phoneNumber = userResult.getString("UserPhoneNumber");
                String status = userResult.getString("Status");
                
               
                String countQuery = "SELECT COUNT(*) AS bookCount FROM BorrowedBook WHERE UserId = ? AND ReturnDate IS NULL";
                PreparedStatement countStatement = connection.prepareStatement(countQuery);
                countStatement.setInt(1, userId);
                ResultSet countResult = countStatement.executeQuery();
                int bookCount = 0;
                if (countResult.next()) {
                    bookCount = countResult.getInt("bookCount");
                }
                
                res += "<tr><td>" + userId + "</td><td>" + firstName + "</td><td>" + lastName + "</td><td>" + 
                       email + "</td><td>" + phoneNumber + "</td><td>" + status + "</td><td>" + 
                       bookCount + "</td></tr>";
            }
            
            res += "</table></body></html>";
            
            JFrame frame = new JFrame("Library Users");
            JLabel label = new JLabel(res);
            JScrollPane scrollPane = new JScrollPane(label);
            frame.add(scrollPane);
            frame.setSize(1000, 400);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            
            connection.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error viewing users: " + ex.getMessage());
        }
    }
    
}