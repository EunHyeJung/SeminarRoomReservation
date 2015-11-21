package kr.ac.kookmin.cs.capstone2.seminarroomreservation.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Member;

import kr.ac.kookmin.cs.capstone2.seminarroomreservation.Reservation.ItemUser;

/**
 * Created by eunhye on 2015-11-16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASENAME = "members.sqlite";
    private static final int VERSION = 1;

    private static final String TABLE_MEMBER = "member";

/*    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/
    public DatabaseHelper(Context context) {
        super(context, "members", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // member 테이블을 생성
        database.execSQL("create table member (id integer primary key, user_id text, name text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // 데이터베이스 스키마 변경 시 필요한 코드 여기에 넣음
    }

    public void insertMember(ItemUser itemUser) {
        SQLiteDatabase database = getWritableDatabase();
        //String query = "INSERT INTO `WordbookInfo`(`_id`,`name`) VALUES ("+bookId+",'"+name+"');";
        String query = "insert into " + TABLE_MEMBER
                + " values(" + itemUser.getId() + ", '" + itemUser.getUserId()
                + "', '" + itemUser.getUserName() + "');";
        try {
            database.execSQL(query);
        } catch (Exception ex) {
            Log.e("error", "Excpetion in Drop member table  SQL" + ex.toString());
        }
    }

}
