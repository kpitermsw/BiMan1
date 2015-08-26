package biman.middle.kokin.ru;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SetSpinner {
	Spinner s1;
	String[] arr;
	String[] arrID;

	public SetSpinner(SQLiteDatabase database, String sql, BiManActivity ba, int spinner) {
		Cursor cursor = database.rawQuery(sql, new String [] {});
		
		int nnn=cursor.getCount();
		int ix_Cursor=0;
		arr   = new String[nnn];
		arrID = new String[nnn];
		cursor.moveToFirst();
		if(!cursor.isAfterLast()) {
			do {
				arrID[ix_Cursor]=cursor.getString(0);
				arr[ix_Cursor]=cursor.getString(1);
				
				ix_Cursor++;
				
			} while (cursor.moveToNext());
		}
		cursor.close();							
		
		s1 = (Spinner) ba.findViewById(spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ba,
	                android.R.layout.simple_spinner_item, arr);
        
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);		
		
	}
	
}
