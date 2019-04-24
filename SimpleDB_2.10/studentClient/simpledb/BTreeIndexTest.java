import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import simpledb.remote.SimpleDriver;
/**
 * cs4432-project2:
 * This class is used to test select, joins, and deletes on a table with a BTree Index to
 * compare it with the results of a non indexed table andthe other indexed tables, specifically 
 * the extensible hash we created
 * @author Jackson Powell
 * @author Andrew Nolan
 *
 */
public class BTreeIndexTest {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			
			Driver d = new SimpleDriver(); 
			conn = d.connect("jdbc:simpledb://localhost", null);
			Statement stmt = conn.createStatement();
			long time3; // Times for selection queries to execute on four tables
			long joinTime3; // Times for join queries to execute for each table
			int testConstant = 432;

			// Test Table 3
			String qry = "select a1, a2 from test3 where a1 = " + testConstant;
			long startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			long endTime = System.currentTimeMillis(); // Time at end of query
			time3 = endTime - startTime;

			// Print results
			System.out.println("Times for selection of a1 = " + testConstant + ":");
			System.out.println("Test3 (b-tree): " + time3 + " ms");
			
			// Test Joins
			System.out.println("\nTimes for join on a1 with Test5 (BTree Index):");

			// Test Join Tables 3 and 5
			qry = "select a1, a2, a1, a2 from test5, test3 where a1 = test3.a1";
			startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			endTime = System.currentTimeMillis(); // Time at end of query
			joinTime3 = endTime - startTime;
			System.out.println("Test3 (b-tree): " + joinTime3 + " ms");
			
			//Test a delete statement
			qry = "delete from test3 where a1 = "+testConstant;
			startTime = System.currentTimeMillis();
			stmt.executeUpdate(qry);
			endTime = System.currentTimeMillis();
			long deleteTime = endTime-startTime;
			System.out.println("Time to delete from btree index: "+deleteTime+" ms" );
			
			qry = "select a1, a2 from test3 where a1 = " + testConstant;
			startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			endTime = System.currentTimeMillis(); // Time at end of query
			time3 = endTime - startTime;

			// Print results
			System.out.println("\nTimes for selection of a1 after deletion= " + testConstant + ":");
			System.out.println("Test3 (b-tree): " + time3 + " ms");
			
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
/* Example output:
Times for selection of a1 = 432:
Test3 (b-tree): 0 ms

Times for join on a1 with Test5 (BTree Index):
Test3 (b-tree): 924 ms
Time to delete from btree index: 0 ms

Times for selection of a1 after deletion= 432:
Test3 (b-tree): 0 ms
*/
