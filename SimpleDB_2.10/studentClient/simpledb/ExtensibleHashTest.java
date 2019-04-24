import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import simpledb.remote.SimpleDriver;

/**
 * cs4432-project2:
 * This class is used to test select, joins, and deletes on a table with an Extensible Hash Index to
 * compare it with the results of a non indexed table and the other indexed tables
 * 
 * The extensible hash was also created by us, so the testing file also serves to demonstrate that
 * the functionality of SimpleDB remains intact.
 * 
 * @author Jackson Powell
 * @author Andrew Nolan
 *
 */
public class ExtensibleHashTest {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);
			Statement stmt = conn.createStatement();
			long time2; // Times for selection queries to execute on four tables
			long joinTime2; // Times for join queries to execute for each table
			int testConstant = 432;
			
			// Test Table 2
			String qry = "select a1, a2 from test2 where a1 = " + testConstant;
			long startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			long endTime = System.currentTimeMillis(); // Time at end of query
			time2 = endTime - startTime;

			
			// Print results
			System.out.println("Times for selection of a1 = " + testConstant + ":");
			System.out.println("Test2 (extensible hash): " + time2 + " ms");
			
			// Test Joins
			System.out.println("\nTimes for join on a1 with Test5 (Extensible Hash):");


//			// Test Join Tables 2 and 5
			qry = "select a1, a2, a1, a2 from test5, test2 where a1 = test2.a1";
			startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			endTime = System.currentTimeMillis(); // Time at end of query
			joinTime2 = endTime - startTime;
			System.out.println("Test2 (extensible hash): " + joinTime2 + " ms");

			//Test a delete statement
			qry = "delete from test2 where a1 = "+testConstant;
			startTime = System.currentTimeMillis();
			stmt.executeUpdate(qry);
			endTime = System.currentTimeMillis();
			long deleteTime = endTime-startTime;
			System.out.println("\nTime to delete from extensible hash: "+deleteTime+" ms" );
			
			// Test Table 2
			qry = "select a1, a2 from test2 where a1 = " + testConstant;
			startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			endTime = System.currentTimeMillis(); // Time at end of query
			time2 = endTime - startTime;

			// Print results
			System.out.println("Times for selection of a1 = " + testConstant + ":");
			System.out.println("Test2 (extensible hash): " + time2 + " ms");
			
			stmt.executeUpdate("insert into test2 (a1,a2) values(" + testConstant + ","
					+ testConstant + ")");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			// Step 4: close the connection
			try {
				if (conn != null)
					conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
