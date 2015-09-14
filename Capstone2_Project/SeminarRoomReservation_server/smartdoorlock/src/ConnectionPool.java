import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class ConnectionPool {
	// DB Connection Pool 이용한 DB 연결

	private Connection con;

	ConnectionPool() {
		try {
			Context init = new InitialContext();
			DataSource ds = (DataSource) init
					.lookup("java:comp/env/jdbc/connection");
			con = ds.getConnection();

			System.out.println("Connection Success!");

		} catch (Exception e) {
			System.out.println("Connection Fail!");
			e.printStackTrace();
		}
	}
	
	public Connection returnCon(){
		return con;
	}
	
	public void termination(){
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
