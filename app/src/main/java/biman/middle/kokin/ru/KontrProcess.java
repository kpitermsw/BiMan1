package biman.middle.kokin.ru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class KontrProcess {
    private static final String NAME = "NAME";
    private static final String IS_EVEN = "IS_EVEN";
    
	EditText eFind;
    Button bFind;
    SQLiteDatabase database;
    BiManActivity ba;
    List<Map<String, String>> groupData;
    List<List<Map<String, String>>> childData;
    
    ExpandableListAdapter mAdapter;
    List<Map<String, String>> kontrs;
    Map<String, String> kontrMap;
    
	public void kontrStart(SQLiteDatabase db, BiManActivity pba ) {
		ba = pba;
		database = db;

		kontrs = new ArrayList<Map<String, String>>();
		
		
        eFind = (EditText) ba.findViewById(R.id.findText);
        bFind = (Button) ba.findViewById(R.id.bFind);
        bFind.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		setKontrView("", eFind.getText().toString());
        	}
        });

        groupData = new ArrayList<Map<String, String>>();
        childData = new ArrayList<List<Map<String, String>>>();
        
        Map<String, String> curGroupMap = new HashMap<String, String>();
        groupData.add(curGroupMap);
        curGroupMap.put(NAME, "      "+"Контрагенты");

        List<Map<String, String>> children = new ArrayList<Map<String, String>>();          
        Map<String, String> curChildMap;
        curChildMap = new HashMap<String, String>();
        children.add(curChildMap);
        curChildMap.put(NAME, "Все");
        
        curChildMap = new HashMap<String, String>();        
        children.add(curChildMap);
        curChildMap.put(NAME, "Новые");
	
        childData.add(children);        
        
        
        ExpandableListView elv1;
        elv1 = (ExpandableListView) ba.findViewById(R.id.expKontrs);
        
        
        mAdapter = new SimpleExpandableListAdapter(
                ba,
                groupData,
                //android.R.layout.simple_expandable_list_item_1,
                R.layout.listadapter1,
                new String[] { NAME, IS_EVEN },
                //new int[] { android.R.id.text1, android.R.id.text2 },
                new int[] { R.id.textView1, android.R.id.text2 },
                childData,
                android.R.layout.simple_expandable_list_item_2,
                new String[] { NAME, IS_EVEN },
                new int[] { android.R.id.text1, android.R.id.text2 }
                );
        
        elv1.setAdapter(mAdapter);
        setKontrView("0","");
        
        
	}

    public void setKontrView(String sidParent, String AddedWhere) {
    	
    	kontrs.clear();

		String sqlKontrs;
    	if (!AddedWhere.equals("")) {
      	  sqlKontrs = "select cnm_kontragent from kontragents ";
    	} else {
       	  sqlKontrs = "select cnm_kontragent from kontragents ";    		
    	}
    	
    	
		Cursor kontrCursor = database.rawQuery(sqlKontrs, new String [] {});
		kontrCursor.moveToFirst();
			if(!kontrCursor.isAfterLast()) {
				do {
			    	kontrMap = new HashMap<String, String>();
			    	kontrs.add(kontrMap);
			    	kontrMap.put("cnm_kontragent", kontrCursor.getString(0));    	
			    	
					
				} while (kontrCursor.moveToNext());
			}
			kontrCursor.close();							
		
	
			/*
				nOSTTotal, nOSTReserv, nOSTFree
			*/
			
		ListView lv = (ListView)ba.findViewById(R.id.listKontrs);
		ListAdapter la = new SimpleAdapter(
					ba, 
					kontrs, 
					R.layout.listkontrs, 
					new String[] {"cnm_kontragent"}, 
					new int[]{R.id.cnm_kontr}
					);
		lv.setAdapter(la);
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				ba.showDialog(1);

				kontrMap = kontrs.get(pos);
				//String s1=goodsMap.get("cnm_goodart").toString();
				ba.eKontrName.setText(kontrMap.get("cnm_kontragent").toString());
				
			}});    	
    	
    }	
	
}
