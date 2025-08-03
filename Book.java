import java.awt.BorderLayout;
import java.sql.Connection;
import java.time.LocalDate;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.*;

public class Book  {
String bookname;
int bookid;
int categoryid;
int publicationyear;
String publishername;
String edition;
Author author;
int quantity;
boolean availability ;

Book(){
	bookname = "";
	 
	author=null;
	quantity = 0;
	availability = false;
	edition = " ";
	publishername = " "; 
	publicationyear=0;
}
Book(String name,Author auth, int qnty, String edi, String pubname, int pubyear) {
	bookname = name;
	 
	author=auth;
	quantity = qnty;
	availability = qnty > 0;
	edition = edi;
	publishername = pubname; 
	publicationyear=pubyear;
	
	
}
boolean addbook(String name,String authname,String category, int qnty, String edi, String pubname, int pubyear) {
	Author a = new Author(authname);
	Category cat = new Category(category);
	try {
		 
		
		 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
           Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
           System.out.println("Connected Successfully");
           String sql1 = "SELECT MAX(BookId) AS MaxID FROM Book";
           PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
           ResultSet rs1 =preparedStatement1.executeQuery();
           int bookid = 1446000; 
           if (rs1.next()) {
               int maxId = rs1.getInt("MaxID");
                   bookid = maxId + 1;
               
           }
           
           int cid = cat.addcategory(category);
           String sql = "INSERT INTO Book(BookId,BookName,CategoryId,Authors,Availability,Edition,PublisherName,PublicationYear,Quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
           PreparedStatement preparedStatement = connection.prepareStatement(sql);
           preparedStatement.setInt(1, bookid);
           preparedStatement.setString(2, name);
           preparedStatement.setInt(3, cid);
           preparedStatement.setString(4, authname);
           preparedStatement.setString(5, "Yes");
           preparedStatement.setString(6, edi);
           preparedStatement.setString(7, pubname);
           preparedStatement.setInt(8, pubyear);
           preparedStatement.setInt(9, qnty);
           int c= preparedStatement.executeUpdate();
           String authors[]=authname.split(",");
           for(int i=0;i<authors.length;i++) {
           int x =  a.addauthor(authors[i]);
           if(x==1) System.out.print("Authors inserted successfully");
           else System.out.print("Authors inserted unsuccessfully");
           }
           
          
           if(c>0) 
           System.out.print("Data inserted successfully");
           
           return true;
                
	     }
           catch(Exception e){
           System.out.println(" Book Error in connection"+ e.getMessage());
           return false;
	}
}
boolean removebook(int id) {
	try {
		
		 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
          Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
          System.out.println("Connected Successfully");
          PreparedStatement preparedStatement2=connection.prepareStatement("select CategoryId from Book where BookId = ?");
          preparedStatement2.setInt(1, id);
	       ResultSet resultSet2=preparedStatement2.executeQuery();
             PreparedStatement preparedstatement = connection.prepareStatement("DELETE FROM Book WHERE BookId = ?");
             preparedstatement.setInt(1, id);
			 int resultSet=preparedstatement.executeUpdate();
			 if(resultSet!=0)
		            System.out.print("Data deleted successfully");
			 else
				 System.out.print("Data deletion unsuccessfull");
			 
			
			
		       int cid =0;
		       if (resultSet2.next()) {
		          cid = resultSet2.getInt("CategoryId");
		          PreparedStatement preparedStatement3=connection.prepareStatement("select Quantity from Category where CategoryId =?");
		          preparedStatement3.setInt(1, cid);
			       ResultSet resultSet3=preparedStatement3.executeQuery();
			       int q=0;
			       if (resultSet3.next()) {
			    	   q=resultSet3.getInt("Quantity");
			       }
			       
		          Category cat = new Category();
		          cat.decrementquantity(cid,q);
		       }
          
          return true;
               
	     }
          catch(Exception a){
          System.out.println("Error in connection"+a.getMessage());
          return false;
	}
}
boolean borrowbook(String name, int id, int userid) {
	try {
		 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
       Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
       System.out.println("Connected Successfully");
       
       PreparedStatement preparedStatement1=connection.prepareStatement("select Quantity from Book where BookId like '"+id+"'");
       ResultSet resultSet=preparedStatement1.executeQuery();
       int quantity =0;
       if (resultSet.next()) {
          quantity = resultSet.getInt("Quantity");
       }
       if (quantity <= 0) {
           JOptionPane.showMessageDialog(null, "Book not available");
           return false;
       }else if (quantity == 1) {
    	   PreparedStatement preparedStatement2=connection.prepareStatement("UPDATE Book SET Availability = ?  WHERE  BookId =? AND BookName= ?");
           preparedStatement2.setString(1, "No");
           preparedStatement2.setInt(2, id);
           preparedStatement2.setString(3, name);
           preparedStatement2.executeUpdate();
           
       }
       String sql = "UPDATE Book SET Quantity = ?  WHERE  BookId =? AND BookName= ?";
       PreparedStatement preparedStatement = connection.prepareStatement(sql);
       preparedStatement.setInt(1, quantity-1);
       preparedStatement.setInt(2, id);
       preparedStatement.setString(3, name);
       int c= preparedStatement.executeUpdate();
       
       LocalDate borrowDate = LocalDate.now();               
       LocalDate dueDate = borrowDate.plusDays(10); 
       
       String sql1 = "SELECT MAX(BorrowedID) AS MaxID FROM BorrowedBook";
       PreparedStatement preparedStatement3 = connection.prepareStatement(sql1);
       ResultSet rs1 =preparedStatement3.executeQuery();
       int borrowedid = 1; 
       if (rs1.next()) {
           int maxId = rs1.getInt("MaxID");
           borrowedid = maxId + 1;
           
       }
       
       String sql2 = "INSERT INTO BorrowedBook(BorrowedID,BookId,BookName,BorrowDate,ReturnDate,DueDate,UserId) VALUES (?,?, ?, ?, ?, ?, ?)";
       PreparedStatement ps = connection.prepareStatement(sql2);
       ps.setInt(1, borrowedid);
       ps.setInt(2, id);
       ps.setString(3, name);
       ps.setDate(4, java.sql.Date.valueOf(borrowDate));
       ps.setDate(5, null);
       ps.setDate(6, java.sql.Date.valueOf(dueDate));
       ps.setInt(7, userid);
       ps.executeUpdate();
       
       
       
       if(c>0) 
           System.out.print("Data inserted successfully");
           
       return true;
            
	     }
       catch(Exception a){
       System.out.println("Error in connection"+a.getMessage());
       return false;
	}
}
boolean returnbook(String name, int id, int userid) {
	try {
		 Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
        System.out.println("Connected Successfully");
        PreparedStatement preparedStatement1=connection.prepareStatement("select Quantity from Book where BookId like '"+id+"'");
        ResultSet resultSet=preparedStatement1.executeQuery();
        int quantity=0;
        if(resultSet.next()) {
           quantity = resultSet.getInt("Quantity");
        }
        if(quantity==0) {
        	PreparedStatement preparedStatement2=connection.prepareStatement("UPDATE Book SET Availability = ?  WHERE  BookId =? AND BookName= ?");
            preparedStatement2.setString(1, "Yes");
            preparedStatement2.setInt(2, id);
            preparedStatement2.setString(3, name);
            preparedStatement2.executeUpdate();

            
        }
        String sql = "UPDATE Book SET Quantity = ?  WHERE  BookId =? AND BookName= ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, quantity+1);
        preparedStatement.setInt(2, id);
        preparedStatement.setString(3, name);
        int c= preparedStatement.executeUpdate();
        LocalDate returnDate = LocalDate.now();
        String sql2 = "UPDATE BorrowedBook SET ReturnDate = ? WHERE BookID = ? AND UserID = ? AND ReturnDate IS NULL";

        PreparedStatement ps = connection.prepareStatement(sql2);
        ps.setDate(1, java.sql.Date.valueOf(returnDate));
        ps.setInt(2, id);
        ps.setInt(3, userid);
        ps.executeUpdate();
         int book2=0;
        PreparedStatement preparedStatement3=connection.prepareStatement("select BookNotReturned from Account where UserId  = ?");
        preparedStatement3.setInt(1, userid);
        ResultSet resultSet2=preparedStatement3.executeQuery();
        if(resultSet2.next()) {
        	book2 =resultSet2.getInt("BookNotReturned");
        }
        if(book2>0) {
        	PreparedStatement preparedStatement4=connection.prepareStatement("UPDATE Account SET BookNotReturned = ? where UserId  = ? AND BookNotReturned = ? ");
        	preparedStatement4.setInt(1, 0);
        	preparedStatement4.setInt(2, userid);
        	preparedStatement4.setInt(3, id);
            preparedStatement4.executeUpdate();
            
            PreparedStatement preparedStatement5=connection.prepareStatement("UPDATE USER SET Status = ? where UserId  = ?");
        	preparedStatement5.setString(1, "Unblocked");
        	preparedStatement5.setInt(2, userid);
            preparedStatement5.executeUpdate();
            
        }else JOptionPane.showMessageDialog(null,"Book returned!");
        if(c>0) 
            System.out.print("Data inserted successfully");
            
        return true;
             
	     }
        catch(Exception a){
        System.out.println("Error in connection"+a.getMessage());
        return false;
	}
}

public void showAvailableBooks(int uid, User u) {
    try {
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
        String sql = "SELECT BookId, BookName, Authors, CategoryId FROM Book WHERE Availability='Yes'";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        JFrame frame = new JFrame("Available Books");
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Name", "Author", "Category"}, 0);
        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        frame.add(scroll, BorderLayout.CENTER);

        while (rs.next()) {
            int id = rs.getInt("BookID");
            String name = rs.getString("BookName");
            String author = rs.getString("Authors");
            int cat = rs.getInt("CategoryId");
            String sql2 = "SELECT CategoryName FROM Category WHERE CategoryId = ?";
            PreparedStatement stmt2 = connection.prepareStatement(sql2);
            stmt2.setInt(1, cat);
            ResultSet rs2 = stmt2.executeQuery();
            while(rs2.next()) {
            	 String catname = rs2.getString("CategoryName");
            	 model.addRow(new Object[]{id, name, author, catname});
            }
           
        }

        JButton borrowButton = new JButton("Borrow Selected Book");
        frame.add(borrowButton, BorderLayout.SOUTH);

        borrowButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a book to borrow.");
                return;
            }
            int bookId = (int) model.getValueAt(row, 0);
            String bookName = (String) model.getValueAt(row, 1);

             
            if (u.userisvalid(uid)) {
                borrowbook(bookName, bookId, uid);
                
            } else {
                JOptionPane.showMessageDialog(frame, "You are blocked. Please return your previous book.");
                u.booknotreturned(uid);
            }
        });

        frame.setVisible(true);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
    }
}
boolean searchBookByName(String name , int uid){
	    try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");

	        String bookname = "SELECT * FROM Book WHERE BookName LIKE ?";
	        PreparedStatement preparedStatement = connection.prepareStatement(bookname);
	        preparedStatement.setString(1,  "%" + name +  "%" );
	        ResultSet resultSet = preparedStatement.executeQuery();

	        JFrame frame = new JFrame("Search Result");
	        frame.setSize(800, 600);
	        frame.setLayout(new BorderLayout());
	        frame.setLocationRelativeTo(null);

	        DefaultTableModel model = new DefaultTableModel(new Object[]{
	            "BookId", "BookName", "Author", "Category", "Availability", "Year", "Edition", "Publisher", "Quantity"
	        }, 0);

	        JTable table = new JTable(model);
	        JScrollPane scrollPane = new JScrollPane(table);
	        frame.add(scrollPane, BorderLayout.CENTER);

	        boolean found = false;
	        while (resultSet.next()) {
	            found = true;
	            int Bid = resultSet.getInt("BookId");
	            String bname = resultSet.getString("BookName");
	            String authorname = resultSet.getString("Authors");
	            String availability = resultSet.getString("Availability");
	            int catId = resultSet.getInt("CategoryId");
	            int year = resultSet.getInt("PublicationYear");
	            String edition = resultSet.getString("Edition");
	            String publisher = resultSet.getString("PublisherName");
	            int quantity = resultSet.getInt("Quantity");

	            PreparedStatement catStmt = connection.prepareStatement("SELECT CategoryName FROM Category WHERE CategoryId = ?");
	            catStmt.setInt(1, catId);
	            ResultSet catResult = catStmt.executeQuery();
	            String categoryName = catResult.next() ? catResult.getString("CategoryName") : "Unknown";

	            model.addRow(new Object[]{Bid, bname, authorname, categoryName, availability, year, edition, publisher, quantity});
	        }

	        if (!found) {
	            JOptionPane.showMessageDialog(null, "No book found with name: " + name);
	            return false;
	        }

	        JButton borrowButton = new JButton("Borrow Selected Book");
	        frame.add(borrowButton, BorderLayout.SOUTH);

	        borrowButton.addActionListener(e -> {
	            int row = table.getSelectedRow();
	            if (row == -1) {
	                JOptionPane.showMessageDialog(frame, "Please select a book to borrow.");
	                return;
	            }

	            int bookId = (int) model.getValueAt(row, 0);
	            String bookName = (String) model.getValueAt(row, 1);
	            String availability = (String) model.getValueAt(row, 4);

	            if (!availability.equalsIgnoreCase("Yes")) {
	                JOptionPane.showMessageDialog(frame, "This book is currently not available.");
	                return;
	            }

	            User u = new User();
	            if (u.userisvalid(uid)) {
	                borrowbook(bookName, bookId, uid);
	                frame.dispose();
	            } else {
	                JOptionPane.showMessageDialog(frame, "You are blocked. Please return your previous book.");
	                u.booknotreturned(uid);
	            }
	        });

	        frame.setVisible(true);
	        return true;

	    } catch (Exception e) {
	        System.out.println("Error in connection: " + e.getMessage());
	        return false;
	    }
	}


boolean searchBookByAuthorname(String authorname, int uid) {
	 try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
	        System.out.println("Connected Successfully");

	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Book WHERE Authors LIKE ?");
	        preparedStatement.setString(1, "%" + authorname + "%");
	        ResultSet resultSet = preparedStatement.executeQuery();

	        JFrame frame = new JFrame("Books by Author: " + authorname);
	        frame.setSize(900, 600);
	        frame.setLayout(new BorderLayout());
	        frame.setLocationRelativeTo(null);

	        DefaultTableModel model = new DefaultTableModel(new Object[]{
	            "BookId", "Book Name", "Author", "Category", "Availability", "Year", "Edition", "Publisher", "Quantity"
	        }, 0);

	        JTable table = new JTable(model);
	        JScrollPane scrollPane = new JScrollPane(table);
	        frame.add(scrollPane, BorderLayout.CENTER);

	        boolean found = false;
	        while (resultSet.next()) {
	            found = true;
	            int bookId = resultSet.getInt("BookId");
	            String bookName = resultSet.getString("BookName");
	            String author = resultSet.getString("Authors");
	            String availability = resultSet.getString("Availability");
	            int catId = resultSet.getInt("CategoryId");
	            int year = resultSet.getInt("PublicationYear");
	            String edition = resultSet.getString("Edition");
	            String publisher = resultSet.getString("PublisherName");
	            int quantity = resultSet.getInt("Quantity");

	            String categoryName = "Unknown";
	            PreparedStatement catStmt = connection.prepareStatement("SELECT CategoryName FROM Category WHERE CategoryId = ?");
	            catStmt.setInt(1, catId);
	            ResultSet catRs = catStmt.executeQuery();
	            if (catRs.next()) {
	                categoryName = catRs.getString("CategoryName");
	            }

	            model.addRow(new Object[]{
	                bookId, bookName, author, categoryName, availability, year, edition, publisher, quantity
	            });
	        }

	        if (!found) {
	            JOptionPane.showMessageDialog(null, "No book found of this Author.");
	            return false;
	        }

	        JButton borrowButton = new JButton("Borrow Selected Book");
	        frame.add(borrowButton, BorderLayout.SOUTH);

	        borrowButton.addActionListener(e -> {
	            int row = table.getSelectedRow();
	            if (row == -1) {
	                JOptionPane.showMessageDialog(frame, "Please select a book to borrow.");
	                return;
	            }

	            int selectedBookId = (int) model.getValueAt(row, 0);
	            String selectedBookName = (String) model.getValueAt(row, 1);
	            String selectedAvailability = (String) model.getValueAt(row, 4);

	            if (!selectedAvailability.equalsIgnoreCase("Yes")) {
	                JOptionPane.showMessageDialog(frame, "This book is currently not available.");
	                return;
	            }

	            User user = new User();
	            if (user.userisvalid(uid)) {
	                borrowbook(selectedBookName, selectedBookId, uid);
	                frame.dispose();
	            } else {
	                JOptionPane.showMessageDialog(frame, "You are blocked. Please return your previous book.");
	                user.booknotreturned(uid);
	            }
	        });

	        frame.setVisible(true);
	        return true;

	    } catch (Exception e) {
	        System.out.println("Error in connection: " + e.getMessage());
	        return false;
	    }
}

boolean searchBookByCategoryName(String categoryname, int uid) {
	try {
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
        System.out.println("Connected Successfully");

        PreparedStatement catStmt = connection.prepareStatement("SELECT * FROM Category WHERE CategoryName LIKE ?");
        catStmt.setString(1,  "%" +categoryname+ "%" );
        ResultSet catResult = catStmt.executeQuery();

        if (!catResult.next()) {
            JOptionPane.showMessageDialog(null, "No book found of this category.");
            return false;
        }

        int catId = catResult.getInt("CategoryId");

        PreparedStatement bookStmt = connection.prepareStatement("SELECT * FROM Book WHERE CategoryId = ?");
        bookStmt.setInt(1, catId);
        ResultSet resultSet = bookStmt.executeQuery();

        JFrame frame = new JFrame("Books in Category: " + categoryname);
        frame.setSize(950, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel(new Object[]{
            "Book Id", "Book Name", "Authors", "Availability", "Year", "Edition", "Publisher", "Quantity"
        }, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        boolean found = false;
        while (resultSet.next()) {
            found = true;
            int bookId = resultSet.getInt("BookId");
            String bname = resultSet.getString("BookName");
            String author = resultSet.getString("Authors");
            String availability = resultSet.getString("Availability");
            int year = resultSet.getInt("PublicationYear");
            String edition = resultSet.getString("Edition");
            String publisher = resultSet.getString("PublisherName");
            int quantity = resultSet.getInt("Quantity");

            model.addRow(new Object[]{
                bookId, bname, author, availability, year, edition, publisher, quantity
            });
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "No book found of this category.");
            return false;
        }

        JButton borrowButton = new JButton("Borrow Selected Book");
        frame.add(borrowButton, BorderLayout.SOUTH);

        borrowButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a book to borrow.");
                return;
            }

            int bookId = (int) model.getValueAt(row, 0);
            String bookName = (String) model.getValueAt(row, 1);
            String availability = (String) model.getValueAt(row, 3);

            if (!availability.equalsIgnoreCase("Yes")) {
                JOptionPane.showMessageDialog(frame, "This book is currently not available.");
                return;
            }

            User u = new User();
            if (u.userisvalid(uid)) {
                borrowbook(bookName, bookId, uid);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "You are blocked. Please return your previous book.");
                u.booknotreturned(uid);
            }
        });

        frame.setVisible(true);
        return true;

    } catch (Exception e) {
        System.out.println("Error in connection: " + e.getMessage());
        return false;
    }
	               
}
boolean searchByBookAndAuthorName(String aname,String bname, int uid) {
	 try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
	        System.out.println("Connected Successfully");

	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Book WHERE BookName LIKE ? AND Authors LIKE ?");
	        preparedStatement.setString(1, "%" + bname + "%");
	        preparedStatement.setString(2, "%" + aname + "%");
	        ResultSet resultSet = preparedStatement.executeQuery();

	        JFrame frame = new JFrame("Books: " + bname + " by " + aname);
	        frame.setSize(950, 600);
	        frame.setLayout(new BorderLayout());
	        frame.setLocationRelativeTo(null);

	        DefaultTableModel model = new DefaultTableModel(new Object[]{
	            "Book Id", "Book Name", "Authors", "Category", "Availability", "Year", "Edition", "Publisher", "Quantity"
	        }, 0);
	        JTable table = new JTable(model);
	        JScrollPane scrollPane = new JScrollPane(table);
	        frame.add(scrollPane, BorderLayout.CENTER);

	        boolean found = false;
	        while (resultSet.next()) {
	            found = true;
	            int bookId = resultSet.getInt("BookId");
	            String bookname = resultSet.getString("BookName");
	            String authors = resultSet.getString("Authors");
	            int catId = resultSet.getInt("CategoryId");
	            String availability = resultSet.getString("Availability");
	            int year = resultSet.getInt("PublicationYear");
	            String edition = resultSet.getString("Edition");
	            String publisher = resultSet.getString("PublisherName");
	            int quantity = resultSet.getInt("Quantity");

	            String categoryName = "Unknown";
	            PreparedStatement catStmt = connection.prepareStatement("SELECT CategoryName FROM Category WHERE CategoryId = ?");
	            catStmt.setInt(1, catId);
	            ResultSet catRs = catStmt.executeQuery();
	            if (catRs.next()) {
	                categoryName = catRs.getString("CategoryName");
	            }

	            model.addRow(new Object[]{
	                bookId, bookname, authors, categoryName, availability, year, edition, publisher, quantity
	            });
	        }

	        if (!found) {
	            JOptionPane.showMessageDialog(null, "No book found with this title and author.");
	            return false;
	        }

	        JButton borrowButton = new JButton("Borrow Selected Book");
	        frame.add(borrowButton, BorderLayout.SOUTH);

	        borrowButton.addActionListener(e -> {
	            int row = table.getSelectedRow();
	            if (row == -1) {
	                JOptionPane.showMessageDialog(frame, "Please select a book to borrow.");
	                return;
	            }

	            int selectedBookId = (int) model.getValueAt(row, 0);
	            String selectedBookName = (String) model.getValueAt(row, 1);
	            String availability = (String) model.getValueAt(row, 4);

	            if (!availability.equalsIgnoreCase("Yes")) {
	                JOptionPane.showMessageDialog(frame, "This book is currently not available.");
	                return;
	            }

	            User u = new User();
	            if (u.userisvalid(uid)) {
	                borrowbook(selectedBookName, selectedBookId, uid);
	                frame.dispose();
	            } else {
	                JOptionPane.showMessageDialog(frame, "You are blocked. Please return your previous book.");
	                u.booknotreturned(uid);
	            }
	        });

	        frame.setVisible(true);
	        return true;

	    } catch (Exception e) {
	        System.out.println("Error in connection: " + e.getMessage());
	        return false;
	    }
}
boolean searchByBookAndCategoryName(String cname,String bname, int uid) {
	try {
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
        System.out.println("Connected Successfully");

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Category WHERE CategoryName LIKE ?");
        preparedStatement.setString(1, "%" + cname + "%");
        ResultSet resultSet = preparedStatement.executeQuery();

        if (!resultSet.next()) {
            JOptionPane.showMessageDialog(null, "No records for this category.");
            return false;
        }

        int catId = resultSet.getInt("CategoryId");

        PreparedStatement bookStmt = connection.prepareStatement("SELECT * FROM Book WHERE BookName LIKE ? AND CategoryId = ?");
        bookStmt.setString(1, "%" + bname + "%");
        bookStmt.setInt(2, catId);
        ResultSet resultSet1 = bookStmt.executeQuery();

        JFrame frame = new JFrame("Books: " + bname + " in " + cname);
        frame.setSize(950, 600);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel(new Object[]{
            "Book Id", "Book Name", "Authors", "Category", "Availability", "Year", "Edition", "Publisher", "Quantity"
        }, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        boolean found = false;
        while (resultSet1.next()) {
            found = true;
            int bookId = resultSet1.getInt("BookId");
            String bookname = resultSet1.getString("BookName");
            String authorname = resultSet1.getString("Authors");
            String availability = resultSet1.getString("Availability");
            int year = resultSet1.getInt("PublicationYear");
            String edition = resultSet1.getString("Edition");
            String publisher = resultSet1.getString("PublisherName");
            int quantity = resultSet1.getInt("Quantity");

            model.addRow(new Object[]{
                bookId, bookname, authorname, cname, availability, year, edition, publisher, quantity
            });
        }

        if (!found) {
            JOptionPane.showMessageDialog(null, "No book found.");
            return false;
        }

        JButton borrowButton = new JButton("Borrow Selected Book");
        frame.add(borrowButton, BorderLayout.SOUTH);

        borrowButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(frame, "Please select a book to borrow.");
                return;
            }

            int bookId = (int) model.getValueAt(row, 0);
            String bookName = (String) model.getValueAt(row, 1);
            String availability = (String) model.getValueAt(row, 4);

            if (!availability.equalsIgnoreCase("Yes")) {
                JOptionPane.showMessageDialog(frame, "This book is currently not available.");
                return;
            }

            User u = new User();
            if (u.userisvalid(uid)) {
                borrowbook(bookName, bookId, uid);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "You are blocked. Please return your previous book.");
                u.booknotreturned(uid);
            }
        });

        frame.setVisible(true);
        return true;

    } catch (Exception e) {
        System.out.println("Error in connection: " + e.getMessage());
        return false;
    }
}
boolean searchByAuthorAndCategoryName(String aname,String cname, int uid) {
	 try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
	        System.out.println("Connected Successfully");

	        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Category WHERE CategoryName LIKE ?");
	        preparedStatement.setString(1, "%" + cname + "%");
	        ResultSet resultSet = preparedStatement.executeQuery();

	        if (!resultSet.next()) {
	            JOptionPane.showMessageDialog(null, "No records for this category.");
	            return false;
	        }

	        int catId = resultSet.getInt("CategoryId");

	        PreparedStatement bookStmt = connection.prepareStatement("SELECT * FROM Book WHERE Authors LIKE ? AND CategoryId = ?");
	        bookStmt.setString(1, "%" + aname + "%");
	        bookStmt.setInt(2, catId);
	        ResultSet resultSet1 = bookStmt.executeQuery();

	        JFrame frame = new JFrame("Books by Author: " + aname + " and Category: " + cname);
	        frame.setSize(950, 600);
	        frame.setLayout(new BorderLayout());
	        frame.setLocationRelativeTo(null);

	        DefaultTableModel model = new DefaultTableModel(new Object[]{
	            "Book Id", "Book Name", "Authors", "Category", "Availability", "Year", "Edition", "Publisher", "Quantity"
	        }, 0);
	        JTable table = new JTable(model);
	        JScrollPane scrollPane = new JScrollPane(table);
	        frame.add(scrollPane, BorderLayout.CENTER);

	        boolean found = false;
	        while (resultSet1.next()) {
	            found = true;
	            int bookId = resultSet1.getInt("BookId");
	            String bookname = resultSet1.getString("BookName");
	            String authorname = resultSet1.getString("Authors");
	            String availability = resultSet1.getString("Availability");
	            int year = resultSet1.getInt("PublicationYear");
	            String edition = resultSet1.getString("Edition");
	            String publisher = resultSet1.getString("PublisherName");
	            int quantity = resultSet1.getInt("Quantity");

	            model.addRow(new Object[]{
	                bookId, bookname, authorname, cname, availability, year, edition, publisher, quantity
	            });
	        }

	        if (!found) {
	            JOptionPane.showMessageDialog(null, "No book found.");
	            return false;
	        }

	        JButton borrowButton = new JButton("Borrow Selected Book");
	        frame.add(borrowButton, BorderLayout.SOUTH);

	        borrowButton.addActionListener(e -> {
	            int row = table.getSelectedRow();
	            if (row == -1) {
	                JOptionPane.showMessageDialog(frame, "Please select a book to borrow.");
	                return;
	            }

	            int bookId = (int) model.getValueAt(row, 0);
	            String bookName = (String) model.getValueAt(row, 1);
	            String availability = (String) model.getValueAt(row, 4);

	            if (!availability.equalsIgnoreCase("Yes")) {
	                JOptionPane.showMessageDialog(frame, "This book is currently not available.");
	                return;
	            }

	            User u = new User();
	            if (u.userisvalid(uid)) {
	                borrowbook(bookName, bookId, uid);
	                frame.dispose();
	            } else {
	                JOptionPane.showMessageDialog(frame, "You are blocked. Please return your previous book.");
	                u.booknotreturned(uid);
	            }
	        });

	        frame.setVisible(true);
	        return true;

	    } catch (Exception e) {
	        System.out.println("Error in connection: " + e.getMessage());
	        return false;
	    }
}
boolean searchByAll(String bname,String aname,String cname, int uid) {
	 try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
	        System.out.println("Connected Successfully");

	        PreparedStatement catStmt = connection.prepareStatement("SELECT * FROM Category WHERE CategoryName LIKE ?");
	        catStmt.setString(1, "%" + cname + "%");
	        ResultSet catRs = catStmt.executeQuery();

	        if (!catRs.next()) {
	            JOptionPane.showMessageDialog(null, "No record found of this category.");
	            return false;
	        }

	        int categoryId = catRs.getInt("CategoryId");

	        PreparedStatement bookStmt = connection.prepareStatement("SELECT * FROM Book WHERE Authors LIKE ? AND BookName LIKE ? AND CategoryId = ?");
	        bookStmt.setString(1, "%" + aname + "%");
	        bookStmt.setString(2, "%" + bname + "%");
	        bookStmt.setInt(3, categoryId);
	        ResultSet bookRs = bookStmt.executeQuery();

	        JFrame frame = new JFrame("Search Results");
	        frame.setSize(950, 600);
	        frame.setLayout(new BorderLayout());
	        frame.setLocationRelativeTo(null);

	        DefaultTableModel model = new DefaultTableModel(new Object[]{
	            "Book Id", "Book Name", "Authors", "Category", "Availability", "Year", "Edition", "Publisher", "Quantity"
	        }, 0);
	        JTable table = new JTable(model);
	        JScrollPane scrollPane = new JScrollPane(table);
	        frame.add(scrollPane, BorderLayout.CENTER);

	        boolean found = false;
	        while (bookRs.next()) {
	            found = true;
	            int bookId = bookRs.getInt("BookId");
	            String bookName = bookRs.getString("BookName");
	            String author = bookRs.getString("Authors");
	            String availability = bookRs.getString("Availability");
	            int year = bookRs.getInt("PublicationYear");
	            String edition = bookRs.getString("Edition");
	            String publisher = bookRs.getString("PublisherName");
	            int quantity = bookRs.getInt("Quantity");

	            model.addRow(new Object[]{
	                bookId, bookName, author, cname, availability, year, edition, publisher, quantity
	            });
	        }

	        if (!found) {
	            JOptionPane.showMessageDialog(null, "No book found.");
	            return false;
	        }

	        JButton borrowButton = new JButton("Borrow Selected Book");
	        frame.add(borrowButton, BorderLayout.SOUTH);

	        borrowButton.addActionListener(e -> {
	            int row = table.getSelectedRow();
	            if (row == -1) {
	                JOptionPane.showMessageDialog(frame, "Please select a book to borrow.");
	                return;
	            }

	            int bookId = (int) model.getValueAt(row, 0);
	            String bookName = (String) model.getValueAt(row, 1);
	            String availability = (String) model.getValueAt(row, 4);

	            if (!availability.equalsIgnoreCase("Yes")) {
	                JOptionPane.showMessageDialog(frame, "This book is currently not available.");
	                return;
	            }

	            User u = new User();
	            if (u.userisvalid(uid)) {
	                borrowbook(bookName, bookId, uid);
	                frame.dispose();
	            } else {
	                JOptionPane.showMessageDialog(frame, "You are blocked. Please return your previous book.");
	                u.booknotreturned(uid);
	            }
	        });

	        frame.setVisible(true);
	        return true;

	    } catch (Exception e) {
	        System.out.println("Error in connection: " + e.getMessage());
	        return false;
	    }
}

}
