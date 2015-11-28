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

import org.json.simple.JSONObject;

import kookmin.cs.capstone2.GCM.GcmSender;
import kookmin.cs.capstone2.common.MyHttpServlet;
import kookmin.cs.capstone2.common.StaticVariables;

//관리자가 예약 내역을 승인 또는 거절 할 때 DB 업데이트 해준다

public class BookingFilter extends MyHttpServlet {
    
    /*
     * request : reservationId(id), command
     * response : DB update 성공 여부
     */
    
    @Override
    protected void service(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    	
    	super.service(request, response);
        
        // request 파라미터로 전송된 값 얻기
        String reservationId = request.getParameter("id");
        String command = request.getParameter("command");
        String message = "";
        if(command.equals("1"))
            message = "예약이 승인되었습니다.";
        else
            message = "예약이 거절되었습니다.";
        
        PrintWriter pw = response.getWriter();

        try {
            
            conn = DriverManager.getConnection(StaticVariables.JOCL); //커넥션 풀에서 대기 상태인 커넥션을 얻는다
            stmt = conn.createStatement(); //DB에 SQL문을 보내기 위한 Statement를 생성
            
            String sql = "update reservationinfo set status=" + command + " where id=" + reservationId +";";
            int result = stmt.executeUpdate(sql);// return the row count for SQL DML statements
            if(result > 0){
                responseJsonObj.put("result", StaticVariables.SUCCESS);
                sql = " select reg_id from gcmid, reservationinfo "
                        + "where gcmid.id=reservationinfo.user_id "
                        + "and reservationinfo.id=" + reservationId;
                rs = stmt.executeQuery(sql);
                if(rs.next()){
                    GcmSender gs = new GcmSender();
                    gs.sendPush(rs.getString("reg_id"), message);
                }
            }
            else
                responseJsonObj.put("result", StaticVariables.FAIL);
        }catch (SQLException e) {
            System.err.print("SQLException: ");
            System.err.println(e.getMessage());
            e.printStackTrace();
            responseJsonObj.put("result", StaticVariables.ERROR_MYSQL);
        } finally {
            pw.println(responseJsonObj);
            try {
                if (stmt != null) 
                    stmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                System.out.println(se.getMessage());
                // Statement와 Connection을 닫는 과정에서 SQLException이 발생할 수 는 있지만,
                // 이미 위에 작성된 과정은 실행 된 후다.
                //responseJsonObj.put("result", StaticVariables.ERROR_MYSQL);
            }
        }
    }
}
