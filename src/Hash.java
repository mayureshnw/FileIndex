import java.sql.*;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

public class Hash{
	
	static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
	static final String DB_URL="jdbc:mysql://localhost:3306/FileHash";
	static final String USER="root";
	static final String PASS="<Password>";
	// NEW STUFF =======
	static List<String> filename = new ArrayList<String>();
    static List<String> dirname = new ArrayList<String>();
    static List<String> filepath = new ArrayList<String>();
    static List<String> dirpath = new ArrayList<String>();
    // END OF NEW DECLRATIONS
	
	public static void main(String args[])throws ClassNotFoundException, SQLException, NoSuchAlgorithmException{
		String maindir="/home/mayuresh/work/";
		
		Hash obj = new Hash();
		obj.walk(maindir);
		
		
		try{
			Connection conn=null;
			Class.forName("com.mysql.jdbc.Driver");
			
			System.out.print("Connecting to database...\n");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			System.out.print("Connected\n");
			
			String sql1 = "insert into FileHash(md5,filename,filepath) values(?,?,?)";
			String sql2 = "insert into DirHash(md5,dirname,dirpath) values(?,?,?)";
			//===============================================================
			
			PreparedStatement stmt = conn.prepareStatement(sql1);
			
			System.out.println("Database connection made. Hashing Files...");
			String Hashval;
			String name;
			
			for(int i=0;i<filename.size();i++){
				name = FilenameUtils.removeExtension((filename.get(i)));
				//System.out.print(name);
				Hashval=md5(name);
				stmt.setString(1,Hashval);
				stmt.setString(2,name);
				stmt.setString(3,maindir);
				stmt.addBatch();
			}
			stmt.executeBatch();
			
			System.out.println("File hash done. Batch executed");
			System.out.println("Starting directory hash...");
			
			stmt = conn.prepareStatement(sql2);
			for(int i=0;i<dirname.size();i++){
				name = dirname.get(i);
				//System.out.print(name);
				Hashval=md5(name);
				stmt.setString(1,Hashval);
				stmt.setString(2,name);
				stmt.setString(3,dirpath.get(i));
				stmt.addBatch();
			}
			stmt.executeBatch();
			System.out.println("Directory hash done. Batch executed");
			conn.close();		
			System.out.println("Connection closed. Good stuff.");
			}catch(SQLException e){
				System.out.println(e);			
			}catch(ClassNotFoundException ce){
				System.out.println(ce);
			}
	}
	
	
	public void walk( String path ) {
        
        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) return;

        for ( File f : list ) {
            if ( f.isDirectory() ) {
                dirname.add(f.getName());
                dirpath.add(f.getAbsolutePath());
                walk( f.getAbsolutePath() );
            }
            else {
                filename.add(f.getName());
                filepath.add(f.getAbsolutePath());
            }
        }
    }
		
	public static String md5(String x) throws NoSuchAlgorithmException{
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			md.update(x.getBytes());
			String hash = md.digest().toString();
			return hash;
		}catch(NoSuchAlgorithmException e1){
			String hash="err1";
			return hash;
		}
	}
}	