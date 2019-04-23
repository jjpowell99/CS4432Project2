import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import simpledb.remote.SimpleDriver;

public class CheckTestingTables {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn=null;
		Driver d = new SimpleDriver();
		String host = "localhost"; //you may change it if your SimpleDB server is running on a different machine
		String url = "jdbc:simpledb://" + host;
		String qry="update test1 set a1 = a1";
		Random rand=null;
		Statement s=null;
		try {
			conn = d.connect(url, null);
			s=conn.createStatement();
			System.out.println(s.executeUpdate(qry));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
