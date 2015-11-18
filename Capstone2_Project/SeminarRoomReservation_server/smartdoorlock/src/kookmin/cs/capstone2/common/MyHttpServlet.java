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
	
	protected Connection conn = null; // DB 연결을 위한 Connection 객체
	protected Statement stmt = null; // ready for DB Query result
	protected ResultSet rs = null; //SQL Query 결과를 담을 테이블 형식의 객체

	//for JSON
	protected JSONObject responseJsonObj = new JSONObject(); // 최종 전달 객체
	protected JSONObject subJsonObj = new JSONObject(); // 결과 저장 객체
	protected JSONArray jsonArray = new JSONArray(); // 배열 저장 객체
	protected JSONObject jsonArrayInfo = new JSONObject(); // 배열 원소 하나
}
