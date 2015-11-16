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

import kookmin.cs.capstone2.GCM.GcmSender;
import kookmin.cs.capstone2.common.StaticVariables;

public class Login extends HttpServlet {
	/*
	 * request : 아이디, 비밀번호 response : 로그인 성공 여부
	 */

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");

		// request 파라미터로 전송된 값 얻기
		String text_id = request.getParameter("id");
		String password = request.getParameter("password");
		String gcmRegId = request.getParameter("instanceId");

		Connection conn = null; //DB 연결을 위한 Connection 객체
		Statement stmt = null; //ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체

		//for Json
		JSONObject jsonObject = new JSONObject(); //최종 완성될 JSONObject 선언
		JSONObject secondObject = new JSONObject();
		JSONArray roomArray = new JSONArray(); //방 정보를 담을 JSONArray
		JSONObject roomInfo = new JSONObject(); //방 정보 한 개의 정보가 들어갈 JSONObject
		JSONArray userArray = new JSONArray(); //회원 정보를 담을 JSONArray
		JSONObject userInfo = new JSONObject(); //한 회원 정보가 들어갈 JSONObject
		
		try {
			//gs.sendPush();
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			
			//request로 들어온 id와 password가 일치하는 회원을 찾는 질의문
			String sql = "select * from user where text_id='" + text_id + "' && password='" + password + "'"; 
			
			rs = stmt.executeQuery(sql); //SQL Query Result
			
			//id와 password가 일치하는 user가 있을 경우 b_admin column을 확인하여 관리자 여부를 가린다
			if (!rs.next()) {
				secondObject.put("result", StaticVariables.FAIL); // 로그인 실패
			} else{ // 로그인 성공
				int userId = rs.getInt("id");
				//secondObject.put("result", StaticVariables.FAIL);
				secondObject.put("id", userId);
				Boolean bAdmin = rs.getBoolean("b_admin");
				if (bAdmin)
					secondObject.put("result", StaticVariables.ADMIN_SUCCESS); // 관리자 
				else 
					secondObject.put("result", StaticVariables.SUCCESS); // 일반 사용자
				
				// gcm register id를 등록 또는 갱신한다
				sql = "select * from gcmid where id=" + userId;
				rs = stmt.executeQuery(sql); // SQL Query Result
				if(!rs.next()){
					sql = "insert into gcmid values(" + userId + ",'" + gcmRegId + "');";
					stmt.executeUpdate(sql);
					System.out.println("registered the new regId");
				} else {
					sql = "update gcmid set reg_id='" + gcmRegId +"' where id=" + userId;
					System.out.println("update the regId");
				}
				
				sql = "select id, room_id from room;";
				rs=stmt.executeQuery(sql);
				while(rs.next()) {
					roomInfo.put("roomId", rs.getString("id"));
					roomInfo.put("roomName", rs.getString("room_id"));
					roomArray.add(roomInfo);
					roomInfo = new JSONObject();
				}
				
				sql = "select id, text_id, name from user;";
				rs = stmt.executeQuery(sql);
				// 가입된 회원들의 아이디를 jsonArray에 담는다
				while (rs.next()) {
					userInfo = new JSONObject();
					userInfo.put("id", rs.getString("id"));
					userInfo.put("userId", rs.getString("text_id"));
					userInfo.put("name", rs.getString("name"));
					userArray.add(userInfo); // Array에 Object 추가
				}
				
			}
			secondObject.put("room", roomArray);
			secondObject.put("user", userArray);
			jsonObject.put("responseData", secondObject);
			System.out.println(jsonObject);
			pw.println(jsonObject);
			
		} catch (SQLException e) {
			System.err.print("SQLException: ");
			System.err.println(e.getMessage());
			e.printStackTrace();
			pw.println(StaticVariables.ERROR_MYSQL);
		} finally {
			try {
				pw.close();
				if (stmt != null)
					stmt.close();
				if (rs != null)
					rs.close();
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				System.out.println(se.getMessage());
				pw.println(StaticVariables.ERROR_MYSQL);
			}
		}
	}
}
