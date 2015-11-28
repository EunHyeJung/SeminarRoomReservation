package kookmin.cs.capstone2.common;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MyHttpServlet extends HttpServlet{
	
    
    public Connection conn = null; // DB 연결을 위한 Connection 객체
    public Statement stmt = null; // ready for DB Query result
    public ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체

    //for JSON
    public JSONObject responseJsonObj = new JSONObject(); // 최종 전달 객체
    public JSONObject subJsonObj = new JSONObject(); // 결과 저장 객체
    public JSONArray jsonArray = new JSONArray(); // 배열 저장 객체
    public JSONObject jsonArrayInfo = new JSONObject(); // 배열 원소 하나

    @Override
    protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    
    	// request, response 인코딩 방식 지정
    			request.setCharacterEncoding("utf-8");
    			response.setContentType("text/html;chamv.rset=utf-8");
    }
}
