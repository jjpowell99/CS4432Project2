

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import simpledb.remote.SimpleDriver;
public class CreateTestTables {
	final static int maxSize=100000;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn=null;
		Driver d = new SimpleDriver();
		String host = "localhost"; //you may change it if your SimpleDB server is running on a different machine
		String url = "jdbc:simpledb://" + host;
		String qry="Create table test1" +
				"( a1 int," +
				"  a2 int"+
				")";
		Random rand=null;
		Statement s=null;
		try {
			conn = d.connect(url, null);
			s=conn.createStatement();
			s.executeUpdate("Create table test1" +
					"( a1 int," +
					"  a2 int"+
					")");
			s.executeUpdate("Create table test2" +
					"( b1 int," +
					"  b2 int"+
					")");
			s.executeUpdate("Create table test3" +
					"( c1 int," +
					"  c2 int"+
					")");
			s.executeUpdate("Create table test4" +
					"( d1 int," +
					"  d2 int"+
					")");
			s.executeUpdate("Create table test5" +
					"( e1 int," +
					"  e2 int"+
					")");

			s.executeUpdate("create sh index idx1 on test1 (a1)");
			s.executeUpdate("create ex index idx2 on test2 (b1)");
			s.executeUpdate("create bt index idx3 on test3 (c1)");
			char a = 'a';
			for(int i=1;i<6;i++)
			{
				if(i!=5)
				{
					rand=new Random(1);// ensure every table gets the same data
					for(int j=0;j<maxSize;j++)
					{
						s.executeUpdate("insert into test"+i+" ("+(a+i)+"1,"+(a+i)+"2) values("+rand.nextInt(1000)+","+rand.nextInt(1000)+ ")");
					}
				}
				else//case where i=5
				{
					for(int j=0;j<maxSize/2;j++)// insert 10000 records into test5
					{
						s.executeUpdate("insert into test"+i+" (e1,e2) values("+j+","+j+ ")");
					}
				}
			}
			conn.close();

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


