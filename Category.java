import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Category {
private String catname;
private int catid;

Category(){
	catname="";
	catid=-1;
}
Category(String name){
	
	catname=name;
try{
		
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection connection= DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
        System.out.println("Connected Successfully");
        String sql1 = "SELECT MAX(CategoryId) AS MaxID FROM Category";
        PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
        ResultSet rs1 =preparedStatement1.executeQuery();
        catid = 1444000; 
        if(rs1.next()) {
            catid = rs1.getInt("MaxID") + 1;
        }
       
	}catch(Exception e){
        System.out.println("Error in connection"+ e.getMessage());

    }
	 
}

String getName() {
	return catname;
}
int getid() {
	return catid;
}
int addcategory(String name) {
    int cid = -1;
    int quant=0;
    try {
        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
        System.out.println("Connected Successfully");
        
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT CategoryId, Quantity FROM Category WHERE CategoryName = ?");
        preparedStatement.setString(1, name);  
        ResultSet resultSet = preparedStatement.executeQuery();
        
        if (resultSet.next()) {
        	cid = resultSet.getInt("CategoryId");
        	quant = resultSet.getInt("Quantity");
        	String sql = "UPDATE Category SET Quantity = ? WHERE CategoryId = ?";
        	PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
        	preparedStatement1.setInt(1, quant + 1);
        	preparedStatement1.setInt(2, cid);
        	preparedStatement1.executeUpdate();
        	System.out.println("Already exists, quantity updated");
        	return cid;
        } else {
            
            String sql2 = "SELECT MAX(CategoryId) AS MaxID FROM Category";
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            ResultSet rs2 = preparedStatement2.executeQuery();
            
            cid = 1444000;
            if (rs2.next()) {
                cid = rs2.getInt("MaxID") + 1;
            }
            
            String sql = "INSERT INTO Category(CategoryId, CategoryName,Quantity) VALUES (?, ?)";
            PreparedStatement preparedStatement1 = connection.prepareStatement(sql);
            preparedStatement1.setInt(1, cid);
            preparedStatement1.setString(2, name);
            preparedStatement1.setInt(3, 1);
            int c = preparedStatement1.executeUpdate();
            
            if (c > 0) {
                System.out.print("Data inserted successfully");
            }
        }
       
        
    } catch (Exception e) {
        System.out.println("Category Error in connection" + e.getMessage());
    }
    return cid;
}
void decrementquantity(int id,int quant) {
	 try {
	        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
	        Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:\\Users\\PMLS\\OneDrive\\Documents\\LibraryManagementSystem.accdb");
	        System.out.println("Connected Successfully");
	        PreparedStatement preparedStatement = connection.prepareStatement( "UPDATE Category SET Quantity = ? WHERE CategoryId = ?");
	        preparedStatement.setInt(1, quant-1);
	        preparedStatement.setInt(2, id); 
            int c = preparedStatement.executeUpdate();
            
            if (c > 0) {
                System.out.print("Data inserted successfully");
            }
	        
	 } catch (Exception e) {
	        System.out.println("Category Error in connection" + e.getMessage());
	    }
	    
}

public String toString() {
	return "\nID: " + catid +"\nName: "+ catname;
}
}
