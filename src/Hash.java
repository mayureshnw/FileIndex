import java.sql.*;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.commons.io.FilenameUtils;

public class Hash{
	
	
	// NEW STUFF =======
	static List<String> filename = new ArrayList<String>();
    static List<String> dirname = new ArrayList<String>();
    static List<String> filepath = new ArrayList<String>();
    static List<String> dirpath = new ArrayList<String>();
    // LUCENE DECLARATIONS
    static Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
    // END OF NEW DECLRATIONS
    
	
	public static void main(String args[])throws ClassNotFoundException, SQLException, NoSuchAlgorithmException, IOException, ParseException{
		String maindir="/home/mayuresh/Desktop/random";
		
		Hash obj = new Hash();
		obj.walk(maindir);
		File wareHouse = new File("/home/mayuresh/Desktop/");
	    Directory directory = FSDirectory.open(wareHouse);
		
		try{
			
			String name;			
			for(int i=0;i<filename.size();i++){
				
				name = FilenameUtils.removeExtension((filename.get(i)));
				Document doc = new Document();
				System.out.println("Adding fields to doc");
				doc.add(new Field("filename",name, TextField.TYPE_STORED));
				doc.add(new Field("filepath", maindir, TextField.TYPE_STORED));
				Indexer(doc);
			}
			
			for(int i=0;i<dirname.size();i++){
				name = dirname.get(i);
				Document doc = new Document();
				doc.add(new Field("dirname",name, TextField.TYPE_STORED));
				doc.add(new Field("dirpath", dirpath.get(i), TextField.TYPE_STORED));
				Indexer(doc);
			}
			
			System.out.println("Directory hash done. Batch executed");
			System.out.println("Connection closed. Good stuff.");
			Search("hey",directory);
			}finally{
				System.out.println("Process Completed");
			}
	}
	
	private void walk( String path ) {
        
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
	
	private static void Indexer(Document Doc) throws IOException{
		File wareHouse = new File("/home/mayuresh/Desktop/");
	    Directory directory = FSDirectory.open(wareHouse);
		IndexWriterConfig  config = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
		IndexWriter iwriter = new IndexWriter(directory,config);
		iwriter.addDocument(Doc);
		System.out.println("Document added : "+Doc);
		iwriter.close();
		
	}
	private static void Search(String text,Directory directory) throws IOException, ParseException{
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);
		QueryParser parser = new QueryParser(Version.LUCENE_4_9, "dirpath",analyzer);
	    Query query = parser.parse(text);
	    ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
	    System.out.println("===============================\n");
	    System.out.println(hits);
	    for (int i = 0; i < hits.length; i++) {
		      Document hitDoc = isearcher.doc(hits[i].doc);
		      System.out.println(hitDoc);
		    }
	    System.out.println("Completed");
	    ireader.close();
	    directory.close();
	}
}	