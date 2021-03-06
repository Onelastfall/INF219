import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class databaseSetup {
	
	private static Connection con;
	private static boolean hasData = false;
	
	public ResultSet displayMountains() throws SQLException, ClassNotFoundException
	{
		if (con == null)
			getConnection();
		
		Statement state = con.createStatement();
		ResultSet res = state.executeQuery("SELECT * FROM mountain");
		
		return res;		
	}

	private void getConnection() throws ClassNotFoundException, SQLException 
	{
		
		Class.forName("org.sqlite.JDBC");
		con = DriverManager.getConnection("jdbc:sqlite:Mountains.db");
		initialize();
	}

	private void initialize() throws SQLException 
	{
		// IF database has data then we do not need to initialize
		if (!hasData)
		{
			hasData = true;
			
			Statement state = con.createStatement();
			ResultSet res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='mountain'");
			if (!res.next())
				System.out.print("Database or mountain table not found, building database with fixed talbes!");
			
			// Building the table
			Statement state2 = con.createStatement();
			state2.execute("CREATE TABLE mountain (M_ID INTEGER PRIMARY KEY," 
							+ "Height INTEGER," + "PromFactor INTEGER,"
							+ "Name TEXT," + "Location TEXT,"
							+ "Difficulity TEXT," + "PicAdress TEXT)");
			
			state2.execute("CREATE TABLE attributes (M_ID INTEGER," + "attribute TEXT,"
							+ "AValue TEXT," + "FOREIGN KEY(M_ID) REFERENCES mountain(M_ID)");
			
			state2.execute("CREATE TABLE trip (M_ID INTEGER," + "T_ID INTEGER PRIMARY KEY," 
							+ "date REAL," + "ShortSummary TEXT," + "Summary TEXT,"
							+ "FOREIGN KEY(M_ID) REFERENCES mountain(M_ID))");
			
			state2.execute("CREATE TABLE resources (T_ID INTEGER," + "comments TEXT,"
						   + "address TEXT," + "FOREIGN KEY(T_ID) REFERENCES trip(T_ID))");
			
			// TODO: insert data from jason scan:
			
			PreparedStatement prep = con.prepareStatement("INSERT INTO mountain values(?,?,?);");
			
			/* SQL COMMANDOS
			 *  INSERT INTO mountain (M_ID, Height, PromFactor, Name, Location, Difficulity, PicAdresse) values(1, 6962, 6962, 'Aconcagua', 'South 32.39.186, West 70.00.724', 'YDS class 2+', 'https://www.ii.uib.no/~petter/mountains/World_pic/acon_small.jpg');
			 *  INSERT INTO trip(M_ID, Date, shortSummary, Summary) VALUES(1, 29-01-2002, ' ',' ');
			 *  
			 */
		
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
