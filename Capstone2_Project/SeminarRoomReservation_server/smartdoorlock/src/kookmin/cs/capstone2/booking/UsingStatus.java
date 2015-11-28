package kookmin.cs.capstone2.booking;

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

import kookmin.cs.capstone2.common.MyHttpServlet;
import kookmin.cs.capstone2.common.StaticMethods;
import kookmin.cs.capstone2.common.StaticVariables;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UsingStatus extends MyHttpServlet {
	/*
	 * request : date(yyyy-MM-dd) 
	 * response : 예약 고유 id(id), 세미나실 id(roomId), 예약시작 시간(startTime), 예약 끝 시간(endTime), 예약 상태(status)
	 */
	
	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		super.service(request, response);
		
		//RequestBody to String
		String requestString = StaticMethods.getBody(request);
				
		// request 파라미터에서 json 파싱
		JSONObject requestObject = (JSONObject)JSONValue.parse(requestString);
		JSONArray requestJSONArray = (JSONArray)requestObject.get("roomIds"); //get room Id JSONArray
		String date = requestObject.get("date").toString(); // get date
		System.out.println(requestObject);
		
		PrintWriter pw = response.getWriter();

		try {
			conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
			stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
			  
			//status!=0, 즉 거절상태가 아닌 예약 내역 정보를 가져온다
			String sql = "select reservationinfo.id, room.id as room_id, reservationinfo.start_time, reservationinfo.end_time, reservationinfo.status "
					+ "from reservationinfo, room "
					+ "where (reservationinfo.room_id=room.id) "
					+ "and date='" + date + "'" 
					+ "and reservationinfo.status != " + StaticVariables.REJECTION;
			
			String tempSql = "";
			String roomId;
			for(int i = 0 ; i < requestJSONArray.size(); i++){
				JSONObject roomIdJSONObject = (JSONObject)requestJSONArray.get(i);
				roomId = roomIdJSONObject.get("roomId").toString();
				tempSql += roomId;
				if( i != requestJSONArray.size()-1 )
					tempSql += ", ";
			}
			if ( requestJSONArray.size() != 0 )
				sql += " and room.id in (" + tempSql +") order by room.id asc;"; //room id가 없을 때 
			System.out.println("usingStatus sql문 : " + sql);
			
			rs = stmt.executeQuery(sql);
			if(!rs.next()){
				responseJsonObj.put("responseData", null);
			}else {
				rs.beforeFirst(); // sql결과의 커서를 맨 앞으로 옮긴다
				while (rs.next()) {
					jsonArrayInfo = new JSONObject();
					jsonArrayInfo.put("reservationId", rs.getInt("id"));
					jsonArrayInfo.put("roomId", rs.getInt("room_id"));
					jsonArrayInfo.put("startTime", rs.getString("start_time"));
					jsonArrayInfo.put("endTime", rs.getString("end_time"));
					jsonArrayInfo.put("reservationStatus", rs.getString("status"));
					
					jsonArray.add(jsonArrayInfo); //Array에 Object 추가
				}
				subJsonObj.put("reservation", jsonArray);
				
				//전체의 JSONObejct에 status란 이름으로 JSON정보로 구성된 Array value 입력
				responseJsonObj.put("responseData", subJsonObj); 
			}
			pw.println(responseJsonObj);
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
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				System.out.println(se.getMessage());
			}
		}
	}
}