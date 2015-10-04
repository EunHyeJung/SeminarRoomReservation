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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import kookmin.cs.capstone2.var.StaticVariables;

/*
 * 등록된 회원의 아이디를 전송한다
 */
public class UserList extends HttpServlet {

	/*
	 * response : 가입된 회원들의 아이디
	 */
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		String jocl = "jdbc:apache:commons:dbcp:/pool1"; // 커넥션 풀을 위한 DBCP 설정 파일
		Connection conn = null; // DB 연결을 위한 Connection 객체
		Statement stmt = null; // ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; // SQL Query 결과를 담을 테이블 형식의 객체

		// for Json
		JSONObject jsonObject = new JSONObject(); // 최종 완성될 JSONObject 선언
		JSONArray statusArray = new JSONArray(); // 아이디가 들어갈 배열
		JSONObject statusInfo = new JSONObject(); // 배열 정보 한 개가 들어갈 JSONObject
		
		
		try {
			conn = DriverManager.getConnection(jocl); // 커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); // DB에 SQL문을 보내기 위한 Statement를 생성
			String sql = "select text_id from user;"; // 가입된 회원들의 아이디를 프로젝션
			rs = stmt.executeQuery(sql); // SQL Query Result

			// 가입된 회원들의 아이디를 jsonArray에 담는다
			while (rs.next()) {
				statusInfo = new JSONObject();
				statusInfo.put("textId", rs.getString("text_id"));
				statusArray.add(statusInfo); // Array에 Object 추가
			}

			// 전체의 JSONObejct에 status란 이름으로 JSON정보로 구성된 Array value 입력
			jsonObject.put("status", statusArray);
			pw.println(jsonObject.toString());
		} catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null) 
					stmt.close();
				if (rs != null)
					rs.close();
			} catch (SQLException se) {
				System.out.println(se.getMessage());
			}
		}
	}

}
