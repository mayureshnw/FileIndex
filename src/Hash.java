import java.sql.*;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class Hash{
	
	static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
	static final String DB_URL="jdbc:mysql://localhost:3306/FileHash";
	static final String USER="root";
	static final String PASS="password";
	
	static List<String> dir = new ArrayList<String>();
	static List<String> file = new ArrayList<String>();
	static List<String> dirpath = new ArrayList<String>(); // add path of file
	static List<String> filepath = new ArrayList<String>(); // add path of directory
	static String maindir="/home/mayuresh/work/Gitstuff/medusa";
	static String subdir;
	
	public static void main(String args[])throws ClassNotFoundException, SQLException, NoSuchAlgorithmException{
		try{
			Connection conn=null;
			Class.forName("com.mysql.jdbc.Driver");
			
			System.out.print("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			System.out.print("Connected");
			//===============================================================
			String sql = "insert into HashTable(md5,filename,filepath) values(?,?,?)";
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			//===============================================================
			dirList(maindir);
			String filename;
			System.out.print("DirList completed. Displaying files\n");
			String Hashval;
			for(int i=0;i<file.size();i++){
				filename = file.get(i);
				Hashval=md5(filename);
				stmt.setString(1,Hashval);
				stmt.setString(2,filename);
				stmt.setString(3,maindir);
				stmt = conn.prepareStatement(sql);
				stmt.addBatch();
			}
				
				//System.out.print(Hashval);
			
			stmt.executeBatch();
			//================================================================
			conn.close();		
			}catch(SQLException e){
				System.out.println(e);			
			}catch(ClassNotFoundException ce){
				System.out.println("The class not found exception");
			}
	}
	
	
	public static void dirList(String maindir){
		
		File folder = new File(maindir);
		File[] listOfFiles = folder.listFiles();
		String var;
		for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		        //System.out.println("File " + listOfFiles[i].getName());
		        var = listOfFiles[i].getName();
		        file.add(var);
		      } else if (listOfFiles[i].isDirectory()) {
		        //System.out.println("Directory " + listOfFiles[i].getName());
		    	  var=listOfFiles[i].getName();
		    	  dir.add(var);
		      }
		}
	}	
		
	public static String md5(String x) throws NoSuchAlgorithmException{
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			md.update(x.getBytes());
			//System.out.print("got MD5 ");
			//System.out.print(md.digest());
			String ret = md.digest().toString();
			//System.out.print(" ret received.\n");
			return ret;
		}catch(NoSuchAlgorithmException e1){
			//System.out.print(e1);
			String ret="err1";
			return ret;
		}
	}
	
}	