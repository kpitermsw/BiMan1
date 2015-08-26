package biman.middle.kokin.ru;

import biman.middle.kokin.ru.SetSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;

public class DocProcess {
    private static final String NAME = "NAME";
    private static final String IS_EVEN = "IS_EVEN";
    
	EditText eFind;
    Button bFind;
    SQLiteDatabase database;
    BiManActivity ba;
    List<Map<String, String>> groupData;
    List<List<Map<String, String>>> childData;
    
    ExpandableListAdapter mAdapter;
    List<Map<String, String>> docs;
    Map<String, String> docMap;
    
    String[] usrs;
    
    Spinner s1;					// Комбобокс для выбора
    SetSpinner spUsers;
    SetSpinner spTypeDoc;
    SetSpinner spInterval;
    
    String whereUsers;
    String whereTypeDoc;
    String whereInterval;    
    
	public void docStart(SQLiteDatabase db, BiManActivity pba ) {
		ba = pba;
		database = db;
		whereUsers = "";
		whereTypeDoc = "";

		docs = new ArrayList<Map<String, String>>();
		
		
        eFind = (EditText) ba.findViewById(R.id.findText);
        bFind = (Button) ba.findViewById(R.id.bFind);
        bFind.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		setdocView("", eFind.getText().toString());
        	}
        });

        
        spUsers = new SetSpinner(
				database,
				"select -1, 'Все'" +
				"union "+
				"select nid_user, cnm_user " +
				"  from users ",
				ba,
				R.id.spinner_mngr
		);

        spUsers.s1.setOnItemSelectedListener(
        		new OnItemSelectedListener(){

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
        				if (spUsers.arrID[arg2].equals("-1")) {
        					whereUsers = "";        					
        				} else {
        					whereUsers = " and nID_Author="+spUsers.arrID[arg2];
        				}	
        				setdocView("0","");
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
        			
        		});
        
        
        spTypeDoc = new SetSpinner(
				database,
				"select -1, 'Все'" +
				"union "+
				"select nid_typedoc, cnm_typedoc " +
				"  from type_documents " +
				" where nshow=1 and nshow2=1  ",
				ba,
				R.id.spinner_typedoc
		);
		
        spTypeDoc.s1.setOnItemSelectedListener(
        		new OnItemSelectedListener(){

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
        				if (spTypeDoc.arrID[arg2].equals("-1")) {
        					whereTypeDoc = "";        					
        				} else {
        					whereTypeDoc = " and root_documents.nID_typedoc="+spTypeDoc.arrID[arg2];
        				}	
        				setdocView("0","");
						
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
        			
        		});
        
        
        
        
        
        spInterval = new SetSpinner(
				database,
				"select 1, 'Сегодня'" +
				"union "+
				"select 2, '3 дня'"+
				"union "+
				"select 3, 'неделя'"+
				"union "+
				"select 4, 'месяц'"+
				
				"",
				ba,
				R.id.spinner_interval
		);
        
        
        setdocView("0","");
        
        
	}

    public void setdocView(String sidParent, String AddedWhere) {
    	
    	docs.clear();

		String sqlDocs;
   		sqlDocs = 
   			"select nid_root, cnm_typedoc, cnumasis, ddateasis, cnm_kontragent " +
   			"  from root_documents, type_documents, kontragents " +
   			" where root_documents.nid_typedoc=type_documents.nid_typedoc" +
   			"   and root_documents.nid_kontragent=kontragents.nid_kontragent" +
   			"   and ddateasis>='2012.01.01'" +
   			whereUsers +
   			whereTypeDoc +   			
   			" order by nid_root" +
   			""
   			
   			;
    	
		Cursor docCursor = database.rawQuery(sqlDocs, new String [] {});
		docCursor.moveToFirst();
		if(!docCursor.isAfterLast()) {
			do {
		    	docMap = new HashMap<String, String>();
		    	docs.add(docMap);
		    	
		    	String ddateasis=docCursor.getString(3).substring(0,10);
		    	
		    	docMap.put("cnumasis", docCursor.getString(2));// + " (" + docCursor.getString(0).toString()+")");    	
		    	docMap.put("ddateasis", ddateasis);
		    	docMap.put("cnm_typedoc", docCursor.getString(1));
		    	docMap.put("cnm_kontragent", docCursor.getString(4));		    	
			} while (docCursor.moveToNext());
		}
		docCursor.close();							
		
				
		
		ListView lv = (ListView)ba.findViewById(R.id.listDocs);
		ListAdapter la = new SimpleAdapter(
					ba, 
					docs, 
					R.layout.listdocs, 
					new String[] {"cnumasis","ddateasis","cnm_typedoc", "cnm_kontragent"}, 
					new int[]{R.id.cnumasis, R.id.ddateasis, R.id.cnm_typedoc, R.id.cnm_kontr}
					);
		lv.setAdapter(la);
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				ba.showDialog(2);

				docMap = docs.get(pos);
				//String s1=goodsMap.get("cnm_goodart").toString();
				ba.eDocName.setText(docMap.get("cnumasis").toString());
				
			}});    	
    	
    }	
	
}
