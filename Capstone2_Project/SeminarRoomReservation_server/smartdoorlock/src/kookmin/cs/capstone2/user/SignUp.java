package kookmin.cs.capstone2.user;

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

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import kookmin.cs.capstone2.common.MyHttpServlet;
import kookmin.cs.capstone2.common.StaticMethods;
import kookmin.cs.capstone2.common.StaticVariables;
public class SignUp extends MyHttpServlet {
	
	/*
	 * request : 아이디, 비밀번호, 이름, 전화번호 
	 * response : 회원 등록 성공 여부
	 */

	//JSON으로 변경
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		super.service(request, response);
		
		//RequestBody to String
		System.out.println("SignUp : " + requestString);
						
		// request 파라미터에서 json 파싱
		JSONObject requestObject = (JSONObject)JSONValue.parse(requestString);
				
		// request 파라미터로 전송된 값 얻기
		String text_id = requestObject.get("id").toString();
		String password = requestObject.get("password").toString();
		String name = requestObject.get("name").toString();
		String phone = requestObject.get("phone").toString();

		PrintWriter pw = response.getWriter(); 
		
		try {
			
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "insert into user (text_id, password, name, phone) values ('"+ text_id + "', '" + password + "', '" + name + "', '" + phone + "');";
			int n = stmt.executeUpdate(sql);// return the row count for SQL DML statements 
			
			//response to client
			if (n == 1) {
				pw.println(StaticVariables.SUCCESS);
			} else
				pw.println(StaticVariables.FAIL);
			
		} catch (SQLException se) {
			
			System.out.println(se.getMessage());
			pw.println(StaticVariables.ERROR_MYSQL);
		
		} finally {
			pw.close();
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				System.out.println(se.getMessage());
			}
		}
	}
}