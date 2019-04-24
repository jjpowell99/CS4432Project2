import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import simpledb.remote.SimpleDriver;

/**
 * cs4432-project2:
 * This class is used to test select, joins, and deletes on a table with no index to
 * compare it with the results of the indexed tables, specifically the extensible
 * hash we created
 * 
 * @author Jackson Powell
 * @author Andrew Nolan
 *
 */
public class NoIndexTest {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);
			Statement stmt = conn.createStatement();
			long time4; // Times for selection queries to execute on four tables
			long joinTime4; // Times for join queries to execute for each table
			int testConstant = 432;
			
			// Test Table 4
			String qry = "select a1, a2 from test4 where a1 = " + testConstant;
			long startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			long endTime = System.currentTimeMillis(); // Time at end of query
			time4 = endTime - startTime;
			
			// Print results
			System.out.println("Times for selection of a1 = " + testConstant + ":");
			System.out.println("Test4 (no index): " + time4 + " ms");
			
			// Test Joins
			System.out.println("\nTimes for join on a1 with Test5 (no index):");
			
			// Test Join Tables 4 and 5
			qry = "select a1, a2, a1, a2 from test5, test4 where a1 = test4.a1";
			startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			endTime = System.currentTimeMillis(); // Time at end of query
			joinTime4 = endTime - startTime;
			
			System.out.println("Test4 (no index): " + joinTime4 + " ms");

			//Test a delete statement
			qry = "delete from test4 where a1 = "+testConstant;
			startTime = System.currentTimeMillis();
			stmt.executeUpdate(qry);
			endTime = System.currentTimeMillis();
			long deleteTime = endTime-startTime;
			System.out.println("Time to delete from no index: "+deleteTime+" ms" );
			
			qry = "select a1, a2 from test4 where a1 = " + testConstant;
			startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			endTime = System.currentTimeMillis(); // Time at end of query
			time4 = endTime - startTime;
			
			// Print results
			System.out.println("\nTimes for selection of a1 after deletion = " + testConstant + ":");
			System.out.println("Test4 (no index): " + time4 + " ms");
			
			
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
/*Example output:
Times for selection of a1 = 432:
Test4 (no index): 0 ms

Times for join on a1 with Test5 (no index):
Test4 (no index): 1189 ms
Time to delete from no index: 16 ms

Times for selection of a1 after deletion = 432:
Test4 (no index): 16 ms 
*/