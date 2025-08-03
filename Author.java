import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Author {
private String name;
private int id;



Author(String name){
	
	this.name=name;
	try{
		
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
        System.out.println("Connected Successfully");
        String sql1 = "SELECT MAX(AuthorId) AS MaxID FROM Author";
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
        ResultSet rs1 =preparedStatement1.executeQuery();
        id = 1445000; 
        if(rs1.next()) {
            id = rs1.getInt("MaxID") + 1;
        }
       
	}catch(Exception e){
        System.out.println("Error in connection"+ e.getMessage());

    }
	
	 
}
Author(Author a){
	this.name=a.name;
	this.id=a.id;
}

String getName() {
	return name;
}
int getid() {
	return id;
}
int addauthor(String name) {
	
	int authid=-1;
  try{
		
		
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
        System.out.println("Connected Successfully");
        
        PreparedStatement preparedStatement=connection.prepareStatement("select AuthorId from Author where AuthorName like '"+name+"'");
        ResultSet resultSet=preparedStatement.executeQuery();
        if (resultSet.next()) {
            authid = resultSet.getInt("AuthorId");
            System.out.print("Author already exists");
            return authid;  
        } else {
            
            String sql1 = "SELECT MAX(AuthorId) AS MaxID FROM Author";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
            ResultSet rs1 = preparedStatement1.executeQuery();
            
            authid = 1445000; 
            if (rs1.next()) {
                int maxId = rs1.getInt("MaxID");
                if (!rs1.wasNull()) {
                    authid = maxId + 1;
                }
            }
        String sql = "INSERT INTO Author(AuthorId,AuthorName) VALUES (?, ?)";
        PreparedStatement preparedStatement2 = connection.prepareStatement(sql);
        preparedStatement2.setInt(1, authid);
        preparedStatement2.setString(2, name);
        int c= preparedStatement2.executeUpdate();
        if(c>0) { 
          System.out.print("Data inserted successfully");
        
        }
        else  System.out.print("Already exists");
        
        
        }
     }catch(Exception e){
        System.out.println("Author Error in connection"+ e.getMessage());

    }
  return 1;
   
	

}


public String toString() {
	return "\nID: " + id +"\nName: "+ name;
}
}