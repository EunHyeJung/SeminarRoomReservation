import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/* 
 * A Servlet for providing reservation status
 * 1) HttpServlet클래스 상속
 * 2) service 메소드 오버라이딩
 */
 
 public class Schedule extends HttpServlet{
	/* 
	 * request : 요청에 관련된 정보를 갖는 객체 (클라이언트가 보낸 정보)
	 *           
	 * response : 응답에 관련된 정보를 갖는 객체
	 *            오늘 날짜부터의 예약 스케줄
	 */
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//request 파라미터로 전송된 값 얻기
		request.setCharacterEncoding("euc-kr");
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		PrintWriter pw = response.getWriter();
		
		try{
			Class.forName("com.mysql.jdbc.Driver"); //jdbc Driver loading
			String url = "jdbc:mysql://127.0.0.1:3306/smartdoorlock"; //mysql jdbc url
			con = DriverManager.getConnection(url, "root", "root");
			
			String sql = "select *from schedule where date >= CURRENT_DATE()"; //CURRENT_DATE() : MYSQL에서 제공하는 함수
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			System.out.println(rs.toString());
			while(rs.next()){
				String date = rs.getString("date");
				String start_time = rs.getString("start_time");
				String end_time = rs.getString("end_time");
				pw.println("date="+date+"&start_time="+start_time+"&end_time="+end_time);
			}
		}catch(ClassNotFoundException e){
			System.err.print("ClassnotFoundException: ");
			System.err.println(e.getMessage());
		}catch(SQLException e){
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
				e.printStackTrace();
		}finally{
			try{
				if(stmt != null) stmt.close();
				if(con != null) con.close();
				if(rs != null) rs.close();
 			}catch(SQLException se){
				System.out.println(se.getMessage());
			}
		}
	}
}