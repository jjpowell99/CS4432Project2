import java.sql.*;
import simpledb.remote.SimpleDriver;

/**
 * CS4432-Project1:
 * @author Andrew Nolan
 * @author Jackson Powell
 * 
 * This class is used to test the functionality of the simple database program for task 1 of the homework
 * It also shows the output of running these tests
 * 
 * NOTE: The cs4432db folder that stores all of the tables should be deleted and the Startup.java program
 * 		 rerun between tests otherwise double values will be inserted into the tables.
 */
public class Examples {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			
			/**Create some tables*/
			
			Driver d = new SimpleDriver();
			conn = d.connect("jdbc:simpledb://localhost", null);
			Statement stmt = conn.createStatement();
			
			String s = "create table PROFESSOR(PId int, PName varchar(10), DeptId int)";
			stmt.executeUpdate(s);
			System.out.println("Table PROFESSOR created.");
			
			s = "insert into PROFESSOR(PId, PName, DeptId) values ";
			String[] profvals = {"(1, 'Neamtu', 10)",
					 "(2, 'Walls', 10)",
					 "(3, 'Cullon', 20)",
					 "(4, 'Christopher', 30)",
					 "(5, 'Clark', 20)",
					 "(6, 'Hamel', 10)",
					 "(7, 'Heineman', 10)",
					 "(8, 'Wills', 10)",
					 "(9, 'Servatius', 30)"};
			for (int i=0; i<profvals.length; i++)
				stmt.executeUpdate(s + profvals[i]);
			System.out.println("PROFESSOR records inserted.");
			
			s = "create table DEPARTMENT(DId int, DName varchar(8))";
			stmt.executeUpdate(s);
			System.out.println("Table DEPARTMENT created.");
			
			s = "insert into DEPARTMENT(DId, DName) values ";
			String[] deptvals = {"(10, 'compsci')",
								 "(20, 'history')",
								 "(30, 'math')"};
			for (int i=0; i<deptvals.length; i++)
				stmt.executeUpdate(s + deptvals[i]);
			System.out.println("DEPARTMENT records inserted.");
			
			s = "create table COURSE(CId int, Title varchar(20), DeptId int)";
			stmt.executeUpdate(s);
			System.out.println("Table COURSE created.");

			s = "insert into COURSE(CId, Title, DeptId) values ";
			String[] coursevals = {"(4432, 'db systems 2', 10)",
								   "(3013, 'Operating Systems', 10)",
								   "(1004, 'calculus IV', 30)",
								   "(2022, 'Discrete Math', 30)",
								   "(1334, 'History of Technology', 20)",
								   "(3900, 'Inquiry Seminar', 20)"};
			for (int i=0; i<coursevals.length; i++)
				stmt.executeUpdate(s + coursevals[i]);
			System.out.println("COURSE records inserted.");
			
			s="create table TEACHING(Tid int, CourseId int, ProfId int)";
			stmt.executeUpdate(s);
			System.out.println("Table TEACHING created.");
			
			s = "insert into TEACHING(TId, CourseId, ProfId) values ";
			String[] teachvals = {"(12, 4432, 1)",
								 "(23, 3013, 8)",
								 "(34, 1004, 4)",
								 "(45, 1334, 3)",
								 "(56, 3900, 5)"};
			for (int i=0; i<teachvals.length; i++)
				stmt.executeUpdate(s + teachvals[i]);
			System.out.println("TEACHING records inserted.");
			
			
			/** Run some Queries */
			
			System.out.println();
			//simple query to select all course names
			String qry = "select Title from COURSE";
			ResultSet rs = stmt.executeQuery(qry);
			System.out.println("Course Title\n===========");
			while (rs.next()) {
				String title = rs.getString("Title");
				System.out.println(title);
			}
			rs.close();
			
			System.out.println();
			//simple delete and then a select to ensure we can delete things
			//when it is run it should no longer include the course discrete math
			qry = "Delete from COURSE where Title='Discrete Math'";
			stmt.executeUpdate(qry);
			qry = "select Title from COURSE";
			rs = stmt.executeQuery(qry);
			System.out.println("Course Title\n===========");
			while (rs.next()) {
				String title = rs.getString("Title");
				System.out.println(title);
			}
			rs.close();
			
			System.out.println();
			//An update to test using update sql in simpledb
			//The title of the Inquiry Seminar Course should now be Humanities Practicum
			qry = "UPDATE COURSE set Title='Humanities Practicum' where Title='Inquiry Seminar'";
			stmt.executeUpdate(qry);
			qry = "select Title from COURSE";
			rs = stmt.executeQuery(qry);
			System.out.println("Course Title\n===========");
			while (rs.next()) {
				String title = rs.getString("Title");
				System.out.println(title);
			}
			rs.close();
			
			//Create a view
			qry = "CREATE VIEW courseTeachers "
					+ "as select PName, CourseId "
					+ "from PROFESSOR, TEACHING "
					+ "where PId = ProfId";
			stmt.executeUpdate(qry);
			qry = "SELECT PName from courseTeachers";
			rs=stmt.executeQuery(qry);
			System.out.println("\nThis is the view data");
			while(rs.next()) {
				String name = rs.getString("PName");
				System.out.println(name);
			}
			
			//A triple join, wild
			qry = "SELECT PName, Title "
					+ "FROM PROFESSOR, COURSE, TEACHING "
					+ "WHERE PId = ProfId and CId= CourseId";
			rs = stmt.executeQuery(qry);
			System.out.println("\nPName|\t Title\n============");
			while(rs.next()) {
				String PName = rs.getString("PName");
				String title = rs.getString("Title");
				System.out.println(PName + "\t "+title);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
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
