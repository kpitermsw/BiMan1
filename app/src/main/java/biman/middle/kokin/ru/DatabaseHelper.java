package biman.middle.kokin.ru;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	 
	 static final String dbName="demoDB";
	 static final String employeeTable="Employees";
	 static final String colID="EmployeeID";
	 static final String colName="EmployeeName";
	 static final String colAge="Age";
	 static final String colDept="Dept";
	 
	 static final String deptTable="Dept";
	 static final String colDeptID="DeptID";
	 static final String colDeptName="DeptName";
	 
	 static final String viewEmps="ViewEmps";
	 
	 //� �������� �����������
	 
	 public DatabaseHelper(Context context) {
		 super(context, dbName, null,35);
	 }

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		  db.execSQL(	"CREATE TABLE aaa ("+
			        	"col1 TEXT, "+
			        	"col2 TEXT)");		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS aaa");
		onCreate(db);
	}
	
	public void insert1() {
		SQLiteDatabase db=this.getWritableDatabase();
		//for (int i=0; i<100000;i++) {
		//	db.execSQL("insert into aaa values(\"1\", \"abcde\")");
		//}
	
	}
// 111
	public String getCol2() {
		String res=""; 
		SQLiteDatabase db=this.getReadableDatabase();

		/*
		Cursor cur=db.rawQuery("select * from aaa",new String [] {});
		cur.moveToFirst();
		res = cur.getString(cur.getColumnIndex("col2"));
		*/
		Cursor cur=db.rawQuery("select count(*) co from aaa",new String [] {});
		cur.moveToFirst();
		res = Integer.toString( cur.getInt(0));
		
		return (res);
	
	}
	
	
}