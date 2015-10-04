package kookmin.cs.capstone2.booking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kookmin.cs.capstone2.common.StaticVariables;
import kookmin.cs.capstone2.common.StaticMethods;

public class BookingRequest extends HttpServlet {
	
	/*
	 * request : 세미나실 id, 신청자 id, 날짜(yyyy-MM-dd), 시작시간, 끝시간, 예약 목적, 세미나 참석자 id
	 * response : 회원 등록 성공 여부
	 */
	
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// request, response 인코딩 방식 지정
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		//RequestBody to String
		String requestString = StaticMethods.getBody(request);
		
		// request 파라미터에서 json 파싱
		Object obj = JSONValue.parse(requestString);
		JSONObject jsonObject = (JSONObject)obj;
		JSONObject jsonObject2 = (JSONObject)jsonObject.get("request");
		
		String roomName = jsonObject2.get("roomId").toString();
		String userId = jsonObject2.get("userId").toString();
		String date = jsonObject2.get("date").toString();
		String startTime = jsonObject2.get("startTime").toString();
		String endTime = jsonObject2.get("endTime").toString();
		String context = jsonObject2.get("context").toString();
		JSONArray participantsArray = (JSONArray)jsonObject2.get("participants");
		
		//JSONObject for result
		JSONObject resultJSON = new JSONObject();

		String jocl = "jdbc:apache:commons:dbcp:/pool1"; //커넥션 풀을 위한 DBCP 설정 파일
		Connection conn = null; //DB 연결을 위한 Connection 객체
		Statement stmt = null; //ready for DB Query result
		PrintWriter pw = response.getWriter();
		ResultSet rs = null; // SQL Query 결과를 담을 테이블 형식의 객체
		
		try {
			
			conn = DriverManager.getConnection(jocl); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			
			//오토커밋을 false로 지정하여 트랜잭션 조건을 맞춘다
			conn.setAutoCommit(false);
			
			String sql;
			sql = "select id from room where room_id='" + roomName +"';"; //해당 roomName의 고유 id를 검색
			rs = stmt.executeQuery(sql); // SQL Query Result
	
			if(rs.next()){
				int roomId = rs.getInt("id"); //해당 roomName의 고유 id 저장
			
				sql = "insert into reservationinfo(room_id, user_id, date, start_time, end_time) "
						+ "values ("+ roomId + "," + userId + ", '" + date + "', '" + startTime + "', '" + endTime + "', '" + context + "');";
				int n = stmt.executeUpdate(sql);// return the row count for SQL DML statements 
			
				if (n == 1){ //table reservationinfo에 insert 성공
					
					//get 'id' from the last line of table 'reservationinfo' 
					sql = "select id from reservationinfo order by id desc limit 1;";
					
					rs = stmt.executeQuery(sql);
					if(rs.next()){
						
						int reservationId = rs.getInt("id"); //예약 고유 번호
						String participantStrId = null; //participantsArray에서 가져올 회의 참가자들의 text_id
						JSONObject object = new JSONObject(); //participantsArray의 객체 요소
						int participantId; //회의 참가자들의 고유 id
						
						//회의 참가자들의 고유 id를 검색할 질의
						sql = "select id from user where text_id=?;";
						
						//회의 참가자 정보를 seminarmember table에 insert
						String insertSql = "insert into seminarmember values(reservationId, ?);";
						
						PreparedStatement selectStmt = conn.prepareStatement(sql),
								insertStmt = conn.prepareStatement(insertSql);
						
						for(int i = 0 ; i < participantsArray.size() ; i++){
							object = (JSONObject)participantsArray.get(i); //JSONObject 배열 요소
							participantStrId = (String)object.get("id"); //전달된 회의 참석자 text id
							selectStmt.setString(1, participantStrId);
							rs = selectStmt.executeQuery();
							participantId = rs.getInt("id"); //participantStrId를 가진 유저의 고유 id
							insertStmt.setInt(1, participantId);
							insertStmt.executeUpdate(); //table 'seminarmember'에 insert
							
							//clear for preparedStatement close()
							selectStmt.clearParameters();
							insertStmt.clearParameters();
						}
						
						//preparedStatement close
						selectStmt.close();
						insertStmt.close();
						
						conn.commit(); //트랜젝션
						resultJSON.put("status", StaticVariables.SUCCESS);
					}
				} else {
					conn.rollback();
					resultJSON.put("status", StaticVariables.FAIL);
				}
			} else {
				conn.rollback();
				resultJSON.put("status", StaticVariables.FAIL);
			}
		} catch (SQLException se) {
			System.out.println(se.getMessage());
			try {
				conn.rollback();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resultJSON.put("status", StaticVariables.ERROR_MYSQL);
		} finally {
			pw.println(resultJSON);
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
