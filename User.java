import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class User extends JFrame implements ActionListener {
      private int id;
	  private String fname;
      private String lname;
      private String email="";
      private String phno;
      private String password;
      private JButton Search,ViewAvailableBooks,AccountInfo,MyBorrowedBooks,ChangePassword,BorrowBook,ReturnBook;
  	  private JLabel title;
  	  JTextField bname1,aname1,category1;
  	  
  	 JButton b1,b2;
     JTextField jt1,jt2,jt3,jt4,jt5,jt6,jt7;
     JLabel toplabel,toplabel1, sidelabel, signuplabel;
     JPanel sidepanel;
     JRadioButton jrb1, jrb2,jrb3;
     
  	
    public User() {
    	setTitle("Library Management System LogIn Page");
        setSize(850, 450);
        toplabel1=new JLabel("Welcome to Library!");
        toplabel1.setBounds(530, 20, 300, 60);
        toplabel1.setFont(new Font("Arial", Font. BOLD , 17));
        add(toplabel1);
        toplabel=new JLabel("LogIn to proceed");
        toplabel.setBounds(530, 50, 300, 60);
        toplabel.setFont(new Font("Arial", Font. BOLD , 17));
        add(toplabel);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        setLayout(null);
        
        sidepanel=new JPanel();
        sidepanel.setBounds(0, 0, 380, 800);
        sidepanel.setBackground(Color.WHITE);
        
       
        ImageIcon imgicon = new ImageIcon("C:\\Users\\PMLS\\OneDrive\\Pictures\\Saved Pictures\\LibraryLogIn.JPG");
        Image scaledimage = imgicon.getImage().getScaledInstance(380,450,Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(scaledimage);
        
        sidelabel=new JLabel(icon);
        sidelabel.setBounds(100, 200, 380, 800);
        sidepanel.add(sidelabel);

        jrb1 = new JRadioButton("Librarian");
        jrb2 = new JRadioButton("User");
        
        jrb1.setBounds(440, 120, 150, 40);
        jrb1.setBackground(Color.LIGHT_GRAY);
       
        jrb2.setBounds(590, 120, 150, 40);
        jrb2.setBackground(Color.LIGHT_GRAY);
        
        ButtonGroup group = new ButtonGroup();
        group.add(jrb1);
        group.add(jrb2);
        
        add(jrb1);
        add(jrb2);
        
        JLabel jl1 = new JLabel("Enter your email:");
        jl1.setFont(new Font("Arial", Font. BOLD , 16));
        jl1.setBounds(430, 170, 200, 20);
        add(jl1);

        JLabel jl2 = new JLabel("Enter your password:");
        jl2.setFont(new Font("Arial", Font. BOLD , 16));
        
        jl2.setBounds(430, 230, 200, 20);
        add(jl2);

        jt1 = new JTextField();
        jt1.setBounds(600, 170, 200, 25);
        add(jt1);

        jt2 = new JTextField();
        jt2.setBounds(600, 230, 200, 25);
        add(jt2);

        b1 = new JButton("LogIn");
        b1.setBounds(470, 275, 90, 30);
        b1.setFont(new Font("Arial", Font. BOLD , 16));
        
        jrb3 = new JRadioButton("SignUp if you don't have an account");
        jrb3.setBounds(430,320,280,60);
        jrb3.setBackground(Color.LIGHT_GRAY);
        
        
        b1.addActionListener(this);
        jrb1.addActionListener(this);
        jrb2.addActionListener(this);
        jrb3.addActionListener(this);
        
        add(b1);
       add(sidepanel);
        add(jrb3);
        setVisible(true);
    	
    }
    
    public String getFirstName() {return fname;}
    public void setFirstName(String f) {fname=f;}
    public String getLastName() {return lname;}
    public void LastName(String l) {lname=l;}
    public String getEmail() {return email;}
    public void setEmail(String e) {email=e;}
    public String getPhoneNumber() {return phno;}
    public void setPhoneNumber(String ph) {phno=ph;}
    
    public void MyBorrowedBooks() {
    	try{
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
            System.out.println("Connected Successfully");
            String e=getEmail();
            String s="Select UserId from USER where UserEmail like ?";
            PreparedStatement preparedStatement=connection.prepareStatement(s);
            preparedStatement.setString(1, e);
            ResultSet resultSet=preparedStatement.executeQuery();
            String rs="";
            if(resultSet.next()) {
            	int id=resultSet.getInt("UserId");
            	String s1="Select * from BorrowedBook where UserID = ?";
            	PreparedStatement preparedStatement1=connection.prepareStatement(s1);
            	preparedStatement1.setInt(1, id);
            	ResultSet resultSet1=preparedStatement1.executeQuery();
            	while(resultSet1.next()) {
            		String returndate=resultSet1.getString("ReturnDate");
            		if(returndate==null) {
            		int bookid=resultSet1.getInt("BookID");
            		String bookname=resultSet1.getString("BookName");
            		String borrowdate=resultSet1.getString("BorrowDate");
            		String duedate=resultSet1.getString("DueDate");
                    rs+="\nBook Id:"+bookid+"\nBook Name:"+bookname+"\nBorrowed Date:"+borrowdate+"\nDue Date:"+duedate;      
                    }
            	}
            	if(!rs.isBlank()) {
                		JOptionPane.showMessageDialog(null, rs);
                	}
            		else {
                		JOptionPane.showMessageDialog(null,"No borrowed books found.");
                	}    	
            }
            else {
            	JOptionPane.showMessageDialog(null,"No books found.");
            }
    }catch(Exception e) {
    	JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }
    }
    public boolean changePassword(String cpassword,String npassword) {
    	try{
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
            System.out.println("Connected Successfully");
            String e=getEmail();
            String s="Select UserPassword,UserId from USER where UserEmail like ?";
            PreparedStatement preparedStatement=connection.prepareStatement(s);
            preparedStatement.setString(1, e);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()) {
            	String pass=resultSet.getString("UserPassword");
            	
            	if(cpassword.equals(pass)) {
            		
            		String updatepass="UPDATE USER SET UserPassword=? where UserId=?";
            		PreparedStatement preparedStatement1=connection.prepareStatement(updatepass);
            		int id=resultSet.getInt("UserId");
            		preparedStatement1.setString(1, npassword);
            		preparedStatement1.setInt(2, id);            		
            		preparedStatement1.executeUpdate();
            		JOptionPane.showMessageDialog(null,"Password Changed Successfully!");
            		return true;
            	}
            	else{
            		JOptionPane.showMessageDialog(null,"You entered invalid password!");
            		return false;
            	}
            }
            else {
            	JOptionPane.showMessageDialog(null,"User not found");
            	return false;
            }
	  		}catch(Exception e) {
	  			JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
	  			return false;
	  		}
    }
    public void accountinfo() {
    	try{
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
            System.out.println("Connected Successfully");
            String s="Select * from USER where UserEmail=? ";
            PreparedStatement preparedStatement=connection.prepareStatement(s);
            String e=getEmail();
            preparedStatement.setString(1, e);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()) {
            int id=resultSet.getInt("UserId");
            String firname=resultSet.getString("UserFirstName");
            String lastname=resultSet.getString("UserLastName");
            String em=resultSet.getString("UserEmail");
            String phnum=resultSet.getString("UserPhoneNumber");
            String status = resultSet.getString("Status");
            String x="SELECT BookNotReturned from Account where UserId=?";
            PreparedStatement preparedStatement1=connection.prepareStatement(x);
            preparedStatement1.setInt(1, id);
            ResultSet resultSet1=preparedStatement1.executeQuery();
            String borrow="";
            if(resultSet1.next()) {
            borrow=resultSet1.getString("BookNotReturned");
            }
            String rs="";
            rs+="UserId:"+ id +"\nUser First  Name: " + firname+"\nUser Last Name:"+lastname+"\nEmail:"+em+"\nPhone Number: 0"+phnum+"\nStatus:"+status+"\nBook not returned:"+borrow;
            if (!rs.isBlank()) {
	            JOptionPane.showMessageDialog(null, rs); }
            else
		            JOptionPane.showMessageDialog(null,"No information to display" ); 
	}}catch(Exception e) {
		JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
	}
    }
    void setinfo(int uid,String uFname,String uLname,String uemail,String uphonenumber,String pass){
      id = uid;
      fname=uFname;
   	  lname=uLname;
   	  email=uemail;
   	  phno=uphonenumber;
   	  password=pass;
    }
  	public User(String umail)
  	{
  	    email=umail;
  	  try{
          Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
          Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
          System.out.println("Connected Successfully");
          String s="Select * from USER where UserEmail=? ";
          PreparedStatement preparedStatement=connection.prepareStatement(s);
          preparedStatement.setString(1, email);
          ResultSet resultSet=preparedStatement.executeQuery();
          if(resultSet.next()) {
          int id=resultSet.getInt("UserId");
          String firname=resultSet.getString("UserFirstName");
          String lastname=resultSet.getString("UserLastName");
          String phnum=resultSet.getString("UserPhoneNumber");
          String password = resultSet.getString("UserPassword");
          setinfo(id,firname,lastname,email,phnum,password);
          updateuservalidity(id);
          }
  	}catch(Exception e) {
  		JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
	}
          
  	   
  	  setTitle("Library Management System");
      setSize(800, 700);
      setLayout(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setLocationRelativeTo(null);
      getContentPane().setBackground(Color.LIGHT_GRAY);
      setResizable(false);
      JLabel title = new JLabel("Welcome User!");
      title.setBounds(450, 20, 300, 60);
      title.setFont(new Font("Arial", Font.BOLD, 20));
      JLabel bname = new JLabel("Search by book name");
      JLabel aname = new JLabel("Search by author name");
      JLabel category = new JLabel("Search by category name");
      bname.setBounds(450, 100, 300, 30);
      aname.setBounds(450, 150, 300, 30);
      category.setBounds(450, 200, 300, 30);
      bname1 = new JTextField();
      bname1.setBounds(620, 100, 150, 30);   
      aname1 = new JTextField();
      aname1.setBounds(620, 150, 150, 30);   
      category1 = new JTextField();
      category1.setBounds(620, 200, 150, 30);
      Search = new JButton("Search Book");
      ChangePassword = new JButton("Change Password");
      AccountInfo = new JButton("Account Information");
      BorrowBook = new JButton("Borrow Book");
      ReturnBook = new JButton("Return Book");
      MyBorrowedBooks = new JButton("My Borrowed Books");
      ViewAvailableBooks = new JButton("View Available Books");
      Search.setBounds(450, 250, 300, 30); 
      ChangePassword.setBounds(450, 300, 300, 30);
      AccountInfo.setBounds(450, 350, 300, 30);
      BorrowBook.setBounds(450, 400, 300, 30);
      ReturnBook.setBounds(450, 450, 300, 30);
      MyBorrowedBooks.setBounds(450, 500, 300, 30); 
      ViewAvailableBooks.setBounds(450, 550, 300, 30); 
      add(title);
      add(bname);
      add(bname1);
      add(aname);
      add(aname1);
      add(category);
      add(category1);
      add(Search);
      add(ChangePassword);
      add(AccountInfo);
      add(BorrowBook);
      add(ReturnBook);
      add(MyBorrowedBooks);
      add(ViewAvailableBooks);
      JPanel spanel = new JPanel();
      spanel.setBounds(0, 0, 380, 700);
      spanel.setBackground(Color.WHITE);
      spanel.setLayout(null); 
      ImageIcon imgicon = new ImageIcon("C:\\Users\\PMLS\\Downloads\\user.jpg");
      Image scaledimage = imgicon.getImage().getScaledInstance(380, 700, Image.SCALE_SMOOTH);
      ImageIcon sicon = new ImageIcon(scaledimage);
      JLabel sidelabel = new JLabel(sicon);
      sidelabel.setBounds(0, 0, 380, 700);
      spanel.add(sidelabel);
      add(spanel);
      Search.addActionListener(this);
      ChangePassword.addActionListener(this);
      AccountInfo.addActionListener(this);
      BorrowBook.addActionListener(this);
      ReturnBook.addActionListener(this);
      MyBorrowedBooks.addActionListener(this);
      ViewAvailableBooks.addActionListener(this);
      setVisible(true);
  		}
  	     
  	void userlogin(String e, String p) {
  		  String email = e;
  		  String password = p;
  		  try {
          Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
          Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
          PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USER WHERE UserEmail = ? AND UserPassword = ?");
          preparedStatement.setString(1, email);
          preparedStatement.setString(2, password);
          ResultSet resultSet = preparedStatement.executeQuery();
          String rs  =" ";
          while(resultSet.next()){
      	  String em = resultSet.getString("UserEmail");
      	  String pass = resultSet.getString("UserPassword");
      	  rs+= em+" "+pass;
          }
          if (!(rs.isBlank())) {
      	    System.out.println(rs);
             new User(email); 
             dispose();
           } else {
              JOptionPane.showMessageDialog(this, "Invalid email or password.");
           }
  		  }catch (Exception a) {
			    a.printStackTrace();
			    JOptionPane.showMessageDialog(this, "Error: " + a.getMessage());
		   }
		}
  	void usersignup(String fname,String lname, String em, String phn,String pass) {
  		try {
				
			   Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		       Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
		       System.out.println("Connected Successfully");
		       String sql1 = "SELECT MAX(UserId) AS MaxID FROM USER";
		       PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
		       ResultSet rs1 =preparedStatement1.executeQuery();
		       int uid = 1446000; 
		       if (rs1.next()) {
		           int maxId = rs1.getInt("MaxID");
		           if (!rs1.wasNull()) {
		               uid = maxId + 1;
		           }
		       }
		       String sql = "INSERT INTO USER(UserId,UserFirstName,UserLastName,UserEmail,UserPhoneNumber,UserPassword,Status) VALUES (?, ?, ?, ?, ?, ?,?)";
		       PreparedStatement preparedStatement = connection.prepareStatement(sql);
		       
		       preparedStatement.setInt(1, uid);
		       preparedStatement.setString(2, fname);
		       preparedStatement.setString(3, lname);
		       preparedStatement.setString(4, em);
		       preparedStatement.setString(5, phn);
		       preparedStatement.setString(6, pass);
		       preparedStatement.setString(7,  "Unblocked");
		       System.out.println("uid: " + uid);
		       System.out.println("fname: " + fname);
		       System.out.println("lname: " + lname);
		       System.out.println("email: " + em);
		       System.out.println("phone: " + phn);
		       System.out.println("password: " + pass);
		       int d = preparedStatement.executeUpdate();
		       
		       if(d > 0) {
		           System.out.println("Signupsuccessful");
		       } else {
		           JOptionPane.showMessageDialog(this, "Signup failed.");
		       }

			   } catch (Exception a) {
				    a.printStackTrace();
				    JOptionPane.showMessageDialog(this, "Error: " + a.getMessage());
			   }
			
			}
  	void updateuservalidity(int id) {
  		try {
  			   int bookid=0;       
  	           LocalDate dueDate = null;
  	           LocalDate currentDate = LocalDate.now();
			   Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
		       Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
		       System.out.println("Connected Successfully");
		       String sql1 = "SELECT * FROM BorrowedBook WHERE UserID = ? AND ReturnDate is NULL";
		       PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
		       preparedStatement1.setInt(1, id);
		      
		       ResultSet rs1 =preparedStatement1.executeQuery();
		       while(rs1.next()) {
		    	   bookid=rs1.getInt("BookID");
		    	   dueDate = rs1.getDate("DueDate").toLocalDate();
		    	   
		       }
		       if(dueDate != null && currentDate.isAfter(dueDate)){
		    	   PreparedStatement preparedStatement2=connection.prepareStatement("UPDATE USER SET Status = ? WHERE UserId = ?");
		           preparedStatement2.setString(1, "Blocked");
		           preparedStatement2.setInt(2, id);
		           preparedStatement2.executeUpdate();		           
		           PreparedStatement preparedStatement=connection.prepareStatement("UPDATE Account SET  BookNotReturned = ? WHERE UserId = ?");
		           preparedStatement.setInt(1, bookid);
		           preparedStatement.setInt(2, id);
		           preparedStatement.executeUpdate();	
		          
		       }
		       
		       
		       
  		}catch (Exception a) {
		    a.printStackTrace();
		    JOptionPane.showMessageDialog(this, "Error: " + a.getMessage());
	   }
  	}
    boolean	userisvalid(int uid ) {
    	try {
    		String status="";
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	       Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
	       System.out.println("Connected Successfully");
	       String sql1 = "SELECT Status FROM USER WHERE UserID = ? ";
	       PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
	       preparedStatement1.setInt(1, uid);
	       ResultSet rs1 =preparedStatement1.executeQuery();
	       if (rs1.next()) {
	            status = rs1.getString("Status");
	            if ("Blocked".equalsIgnoreCase(status)) return false;
	            if ("Unblocked".equalsIgnoreCase(status)) return true;
	        }
	       
    	}catch (Exception a) {
		    a.printStackTrace();
		    JOptionPane.showMessageDialog(null, "Error: " + a.getMessage());
	   }
    	return false;
    }
  	void booknotreturned(int id) {
  		try {
    		int bid =0;
    	Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	       Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
	       System.out.println("Connected Successfully");
	       String sql1 = "SELECT BookNotReturned FROM Account WHERE UserId = ? ";
	       PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
	       preparedStatement1.setInt(1, id);
	       ResultSet rs1 =preparedStatement1.executeQuery();
	       if (rs1.next()) {
	            bid=rs1.getInt("BookNotReturned");
	        }
	       String sql = "SELECT BookName FROM Book WHERE BookId = ? ";
	       PreparedStatement preparedStatement = connection.prepareStatement(sql);
	       preparedStatement.setInt(1, bid);
	       ResultSet rs =preparedStatement.executeQuery();
	       String book="";
	       if (rs.next()) {
	            book=rs.getString("BookName");
	        }
	       JOptionPane.showMessageDialog(null, "Book Id:"+bid+" Book Name:"+book);
	       
    	}catch (Exception a) {
		    a.printStackTrace();
		    JOptionPane.showMessageDialog(this, "Error: " + a.getMessage());
	   }
    	
  	}
  	
  	    public void actionPerformed(ActionEvent AE) {
  	    	
  	    	if (AE.getSource() == b1){
  
  	    			 if(jrb2.isSelected()) {
  	    				String email = jt1.getText();
  	    	          String password = jt2.getText();
  	    	          userlogin(email,password);
  	    	         }else if (jrb1.isSelected()) {
  	    	        	String email = jt1.getText();
    	    	          String password = jt2.getText();
  	    	    		Librarian lib = new Librarian();
  	    	    		lib.librarianlogin(email, password);
  	    	    		dispose();
  	    	    	}
  	    		}
  	    	if(jrb3!=null && jrb3.isSelected()) {
  	    		new Signup();
  	    	}
  	  		if(AE.getSource()==Search){
  	  			String bName=bname1.getText();
  	  			String aName=aname1.getText();
  	  			String cName=category1.getText();
  	  			
  	  			if(!bName.isBlank()&&!aName.isBlank()&&!cName.isBlank()) {
  	  				Book b=new Book();
  	  				b.searchByAll(bName, aName, cName, this.id);
  	  			}
  	  		if(!bName.isBlank()&&!aName.isBlank()) {
  	  			Book b=new Book();
  	  		    b.searchByBookAndAuthorName(aName,bName, this.id);
  	  		    return;}
  	  		if(!bName.isBlank() && !cName.isBlank()) {
  	  		Book b=new Book();
	  		    b.searchByBookAndCategoryName(cName,bName, this.id); 
	  		    return;
  	  		}
  	  		if(!aName.isBlank()&& !cName.isBlank()) {
  	  			Book b=new Book();
  	  			b.searchByAuthorAndCategoryName(aName, cName, this.id);
  	  			return;
  	  		}
  	  		if(!bName.isBlank()) {
  	  			Book b=new Book();
  	  			b.searchBookByName(bName, this.id);
  	  			return;
  	  			}
  	      
  	  		if(!cName.isBlank()) {
  	  			Book b1=new Book();
  	  		    b1.searchBookByCategoryName(cName, this.id);
  	  		    return;
  	  		}
  	  		if(!aName.isBlank()) {
  	  			Book b=new Book();
  	  			b.searchBookByAuthorname(aName, this.id);
  	  			return;
  	  		}
  	  		}
  	  		
  	  if(AE.getSource()==ChangePassword) {
  	  			String e=getEmail();
  	  			User u=new User(e);
  	  		JPanel panel = new JPanel();
  	      panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

  	      JLabel currentLabel = new JLabel("Enter Current Password:");
  	      JPasswordField currentPass = new JPasswordField(15);
  	      JLabel newLabel = new JLabel("Enter New Password:");
  	      JPasswordField newPass = new JPasswordField(15);

  	      panel.add(currentLabel);
  	      panel.add(currentPass);
  	      panel.add(Box.createVerticalStrut(10));
  	      panel.add(newLabel);
  	      panel.add(newPass);

  	      int result = JOptionPane.showConfirmDialog(null, panel, "Change Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

  	      if (result == JOptionPane.OK_OPTION) {
  	          String current = new String(currentPass.getPassword());
  	          String newp = new String(newPass.getPassword());

  	          boolean success = u.changePassword(current, newp);
  	          if (success) {
  	              JOptionPane.showMessageDialog(null, "Password changed successfully.");
  	          } else {
  	              JOptionPane.showMessageDialog(null, "Failed to change password. Check your current password.");
  	          }
  	      }
  	  }if(AE.getSource()==BorrowBook) {
  		if (userisvalid(this.id)) {
  		    JPanel panel = new JPanel();
  		    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

  		    JLabel label1 = new JLabel("Enter Book Name:");
  		    JTextField bookNameField = new JTextField(15);
  		    JLabel label2 = new JLabel("Enter Book ID:");
  		    JTextField bookIdField = new JTextField(15);

  		    panel.add(label1);
  		    panel.add(bookNameField);
  		    panel.add(Box.createVerticalStrut(10));
  		    panel.add(label2);
  		    panel.add(bookIdField);

  		    int result = JOptionPane.showConfirmDialog(null, panel, "Borrow Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

  		    if (result == JOptionPane.OK_OPTION) {
  		        String bookname = bookNameField.getText().trim();
  		        String bookidText = bookIdField.getText().trim();

  		        try {
  		            int bookid = Integer.parseInt(bookidText);
  		            Book b = new Book();
  		            b.borrowbook(bookname, bookid, this.id);
  		        } catch (NumberFormatException ex) {
  		            JOptionPane.showMessageDialog(null, "Invalid Book ID. Please enter a numeric value.");
  		        }
  		    }

  		} else {
  		    JOptionPane.showMessageDialog(null, "You are not allowed to borrow books! First return previous book.");
  		    booknotreturned(this.id);
  		}

  	 }if(AE.getSource()==ReturnBook) {
  		JPanel panel = new JPanel();
  		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

  		JLabel label1 = new JLabel("Enter Book Name:");
  		JTextField bookNameField = new JTextField(15);
  		JLabel label2 = new JLabel("Enter Book ID:");
  		JTextField bookIdField = new JTextField(15);

  		panel.add(label1);
  		panel.add(bookNameField);
  		panel.add(Box.createVerticalStrut(10)); 
  		panel.add(label2);
  		panel.add(bookIdField);

  		int result = JOptionPane.showConfirmDialog(null, panel, "Return Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

  		if (result == JOptionPane.OK_OPTION) {
  		    String bookname = bookNameField.getText().trim();
  		    String bookidText = bookIdField.getText().trim();

  		    try {
  		        int bookid = Integer.parseInt(bookidText);
  		        Book b = new Book();
  		        b.returnbook(bookname, bookid, this.id);
  		    } catch (NumberFormatException ex) {
  		        JOptionPane.showMessageDialog(null, "Invalid Book ID. Please enter a numeric value.");
  		    }
  		}

  	 }if(AE.getSource()==MyBorrowedBooks) {
  	  			String e=getEmail();
  	  			User u=new User(e);
  	  			u.MyBorrowedBooks();
  	  		}
  	  	if(AE.getSource()==AccountInfo) {
  			String e=getEmail();
  			User u=new User(e);
  			u.accountinfo();
  	  		}
  	  	if(AE.getSource()==ViewAvailableBooks) {
  	  		Book b=new Book();
  	  		b.showAvailableBooks(this.id, this);
  	  	}
  	    } 
  	  class Signup extends JFrame implements ActionListener {
  	    JLabel tl;
  	    JRadioButton jrb4, jrb5;

  	    Signup() {
  	        setTitle("Library Management System Signup Page");
  	        setSize(850, 450);
  	        setLayout(null);
  	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            getContentPane().setBackground(Color.LIGHT_GRAY);
            setLayout(null);
            sidepanel=new JPanel();
            sidepanel.setBounds(0, 0, 380, 800);
            sidepanel.setBackground(Color.WHITE);
            
           
            ImageIcon imgicon = new ImageIcon("C:\\Users\\PMLS\\OneDrive\\Pictures\\Saved Pictures\\LibraryLogIn.JPG");
            Image scaledimage = imgicon.getImage().getScaledInstance(380,450,Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(scaledimage);
            
            sidelabel=new JLabel(icon);
            sidelabel.setBounds(100, 200, 380, 800);
            sidepanel.add(sidelabel);
            add(sidepanel);
  	        tl = new JLabel("Signup to proceed");
  	        tl.setBounds(500, 20, 300, 60);
  	        tl.setFont(new Font("Arial", Font.BOLD, 16));
  	        add(tl);

  	        jrb4 = new JRadioButton("Librarian");
  	        jrb5 = new JRadioButton("User");

  	        jrb4.setBounds(460, 80, 150, 40);
  	        jrb4.setBackground(Color.LIGHT_GRAY);
  	        jrb5.setBounds(610, 80, 150, 40);
  	        jrb5.setBackground(Color.LIGHT_GRAY);

  	        ButtonGroup group = new ButtonGroup();
  	        group.add(jrb4);
  	        group.add(jrb5);

  	        add(jrb4);
  	        add(jrb5);

  	        JLabel jl3 = new JLabel("First Name");
  	        jl3.setFont(new Font("Arial", Font.BOLD, 16));
  	        jl3.setBounds(430, 130, 200, 20);
  	        add(jl3);

  	        jt3 = new JTextField();
  	        jt3.setBounds(580, 130, 200, 25);
  	        jt3.setEditable(true);
  	        add(jt3);

  	        JLabel jl4 = new JLabel("Last Name");
  	        jl4.setFont(new Font("Arial", Font.BOLD, 16));
  	        jl4.setBounds(430, 170, 200, 20);
  	        add(jl4);

  	        jt4 = new JTextField();
  	        jt4.setBounds(580, 170, 200, 25);
  	        jt4.setEditable(true);
  	        add(jt4);

  	        JLabel jl5 = new JLabel("Email");
  	        jl5.setFont(new Font("Arial", Font.BOLD, 16));
  	        jl5.setBounds(430, 210, 200, 20);
  	        add(jl5);

  	        jt5 = new JTextField();
  	        jt5.setBounds(580, 210, 200, 25);
  	        jt5.setEditable(true);
  	        add(jt5);

  	        JLabel jl6 = new JLabel("Phone Number");
  	        jl6.setFont(new Font("Arial", Font.BOLD, 16));
  	        jl6.setBounds(430, 250, 200, 20);
  	        add(jl6);

  	        jt6 = new JTextField();
  	        jt6.setBounds(580, 250, 200, 25);
  	        jt6.setEditable(true);
  	        add(jt6);

  	        JLabel jl7 = new JLabel("Password");
  	        jl7.setFont(new Font("Arial", Font.BOLD, 16));
  	        jl7.setBounds(430, 290, 200, 20);
  	        add(jl7);

  	        jt7 = new JTextField();
  	        jt7.setBounds(580, 290, 200, 25);
  	        jt7.setEditable(true);
  	        add(jt7);

  	        b2 = new JButton("SignUp");
  	        b2.setBounds(480, 330, 90, 30);
  	        b2.setFont(new Font("Arial", Font.BOLD, 16));
  	        b2.addActionListener(this);
  	        add(b2);

  	        setVisible(true);
  	    }

  	    public void actionPerformed(ActionEvent e) {
  	        if (e.getSource() == b2) {
  	            if (jrb5.isSelected()) {
  	                String fname = jt3.getText();
  	                String lname = jt4.getText();
  	                String em = jt5.getText();
  	                String phn = jt6.getText();
  	                String pass = jt7.getText();
  	                usersignup(fname, lname, em, phn, pass);
  	                userlogin(em,pass);
  	            } else if (jrb4.isSelected()) {
  	                String fname = jt3.getText();
  	                String lname = jt4.getText();
  	                String em = jt5.getText();
  	                String phn = jt6.getText();
  	                String pass = jt7.getText();
  	                Librarian lib = new Librarian();
  	                lib.librariansignup(fname, lname, em, phn, pass);
	                lib.librarianlogin(em, pass);
  	            }
  	        }
  	    }
  	}

  	    
  	  public static void main(String args[]) {
  	  	     new User();
  	  	  }
  	  }