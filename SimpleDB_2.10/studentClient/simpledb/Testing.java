import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import simpledb.buffer.BufferMgr;
import simpledb.remote.SimpleDriver;
import simpledb.server.SimpleDB;

public class Testing {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("This is a testing program");
		Connection conn = null;
		try {
			// Step 1: connect to database server
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);

			// Step 2: execute the query
			Statement stmt = conn.createStatement();
			System.out.println(SimpleDB.bufferMgr());
			String qry = "select SName "
			           + "from STUDENT "
			           + "where sname = 'joe'";
			ResultSet rs = stmt.executeQuery(qry);

			// Step 3: loop through the result set
			System.out.println("Name\tMajor");
			while (rs.next()) {
				String sname = rs.getString("SName");
				//String dname = rs.getString("DName");
				System.out.println(sname + "\t");
			}
			System.out.println(SimpleDB.bufferMgr());
			rs.close();
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
