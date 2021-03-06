import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import simpledb.remote.SimpleDriver;

public class Project2Testing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);
			Statement stmt = conn.createStatement();
			long time1, time2, time3, time4; // Times for selection queries to execute on four tables
			long joinTime1, joinTime2, joinTime3, joinTime4; // Times for join queries to execute for each table
			Random rand = new Random();
			int testConstant = rand.nextInt(1000);
			// Test Table 1
			String qry = "select a1, a2 from test1 where a1 = " + testConstant;
			long startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			long endTime = System.currentTimeMillis(); // Time at end of query
			time1 = endTime - startTime;
			
			// Test Table 2
			qry = "select a1, a2 from test2 where a1 = " + testConstant;
			startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			endTime = System.currentTimeMillis(); // Time at end of query
			time2 = endTime - startTime;
			
			// Test Table 3
			qry = "select a1, a2 from test3 where a1 = " + testConstant;
			startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			endTime = System.currentTimeMillis(); // Time at end of query
			time3 = endTime - startTime;
			
			// Test Table 4
			qry = "select a1, a2 from test4 where a1 = " + testConstant;
			startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			endTime = System.currentTimeMillis(); // Time at end of query
			time4 = endTime - startTime;
			
			// Print results
			System.out.println("Times for selection of a1 = " + testConstant + ":");
			System.out.println("Test1 (static hash): " + time1 + " ms");
			System.out.println("Test2 (extensible hash): " + time2 + " ms");
			System.out.println("Test3 (b-tree): " + time3 + " ms");
			System.out.println("Test4 (no index): " + time4 + " ms");
			
			// Test Joins
			System.out.println("\nTimes for join on a1 with Test5 (no index):");

			// Test Join Tables 1 and 5
			qry = "select a1, a2, a1, a2 from test5, test1 where a1 = test1.a1";
			startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			endTime = System.currentTimeMillis(); // Time at end of query
			joinTime1 = endTime - startTime;
			System.out.println("Test1 (static hash): " + joinTime1 + " ms");
			
			// Test Join Tables 2 and 5
			qry = "select a1, a2, a1, a2 from test5, test2 where a1 = test2.a1";
			startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			endTime = System.currentTimeMillis(); // Time at end of query
			joinTime2 = endTime - startTime;
			System.out.println("Test2 (extensible hash): " + joinTime2 + " ms");

			
			// Test Join Tables 4 and 5
			qry = "select a1, a2, a1, a2 from test5, test4 where a1 = test4.a1";
			startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			endTime = System.currentTimeMillis(); // Time at end of query
			joinTime4 = endTime - startTime;
			
			System.out.println("Test4 (no index): " + joinTime4 + " ms");

			// Test Join Tables 3 and 5
			qry = "select a1, a2, a1, a2 from test5, test3 where a1 = test3.a1";
			startTime = System.currentTimeMillis(); // Time at start of query.
			stmt.executeQuery(qry);
			endTime = System.currentTimeMillis(); // Time at end of query
			joinTime3 = endTime - startTime;
			System.out.println("Test3 (b-tree): " + joinTime3 + " ms");
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
