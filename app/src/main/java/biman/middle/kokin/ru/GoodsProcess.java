package biman.middle.kokin.ru;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
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
import android.widget.ExpandableListView.OnChildClickListener;

public class GoodsProcess extends Activity {

    private static final String NAME = "NAME";
    private static final String IS_EVEN = "IS_EVEN";
    ExpandableListAdapter mAdapter;
	
    ArrayList aI = new ArrayList();
    ArrayList aJ = new ArrayList();
    ArrayList aID = new ArrayList();
    GoodsProcess _this = this;
	
    EditText eFind;
    Button bFind;
    SQLiteDatabase database;

    String sqlGoodsTree;
    String sqlGoodsTreeChild;    

    List<Map<String, String>> groupData;
    List<List<Map<String, String>>> childData;
    
    List<Map<String, String>> goods;
    Map<String, String> goodsMap;
    
    BiManActivity ba;

    String sss;
    
	public GoodsProcess() {
	}
	
	public void goodsStart(SQLiteDatabase db, BiManActivity pba ) {
		ba = pba;
		database = db;

    	goods = new ArrayList<Map<String, String>>();		
		
        eFind = (EditText) ba.findViewById(R.id.findText);
        bFind = (Button) ba.findViewById(R.id.bFind);
        bFind.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		setGoodView("", eFind.getText().toString());
        	}
        });
		
		   // ываывсывс
        groupData = new ArrayList<Map<String, String>>();
        childData = new ArrayList<List<Map<String, String>>>();
 
   		sqlGoodsTree = 
			//"select _nid_node, cnodetext from goodstree g1 "+
			//" where not exists (select _nid_node from goodstree gpar where gpar._nid_node = g1.nid_parent )"+
			//"   and nid_typenode=0"
   			
   			"select nid_node, cnodetext from goodstree g1 "+
			" where not exists (select nid_node from goodstree gpar where gpar.nid_node = g1.nid_parent )"+
			"   and nid_typenode=0"
			;
        
   		
    	Cursor friendCursor = database.rawQuery(sqlGoodsTree, new String [] {});
    	//goods_ids = new String[friendCursor.getCount()];
    	
    	friendCursor.moveToFirst();
    	int parentcount = friendCursor.getCount();
        
        for (int i = 0; i < parentcount; i++) {
        	
            Map<String, String> curGroupMap = new HashMap<String, String>();
            groupData.add(curGroupMap);

            curGroupMap.put(NAME, "      "+friendCursor.getString(1));

            List<Map<String, String>> children = new ArrayList<Map<String, String>>();          
       		sqlGoodsTreeChild = 
    			"select nid_node, cnodetext from goodstree g1 "+
    			" where nid_parent="+friendCursor.getString(0)+
    			"   and nid_typenode=0"
    			;

        	Cursor childCursor = database.rawQuery(sqlGoodsTreeChild, new String [] {});
        	//goods_ids = new String[childCursor.getCount()];
        	
        	childCursor.moveToFirst();
       		
       		int childcount = childCursor.getCount();
       		
            for (int j = 0; j < childcount; j++) {
                Map<String, String> curChildMap = new HashMap<String, String>();
                children.add(curChildMap);
                String goodName = childCursor.getString(1);
                curChildMap.put(NAME, ""+childCursor.getString(1));
                
                if (childCursor.isAfterLast()) {
                } else {
                	aI.add( i);
                	aJ.add( j);
                	aID.add( childCursor.getString(0));
                }
                
                int iii=0;
                iii=1;
                childCursor.moveToNext();                
            }
            childData.add(children);
            
            childCursor.close();
            friendCursor.moveToNext();
            
        }

        
        ExpandableListView elv1;
        elv1 = (ExpandableListView) ba.findViewById(R.id.expandableListView1);
        elv1.requestFocus();
        elv1.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int arg2, int arg3, long arg4) {
				// TODO Auto-generated method stub
				for (int i=0; i<aI.size();i++) {
					if (aI.get(i).equals(arg2)) { 
						if (aJ.get(i).equals(arg3))  {
							String sidParent = aID.get(i).toString();
							//Toast.makeText(ba, sidParent, Toast.LENGTH_LONG).show();
							setGoodView(sidParent, "");
						}
					}
				}
				
				return false;
			}});
        
        // Set up our adapter
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
        
        friendCursor.close();        
		
	}

    public void setGoodView(String sidParent, String AddedWhere) {
    	goods.clear();
    	//goods.

		//ArrayList<String> goods = new ArrayList<String>();
		String sqlGoods;
    	if (!AddedWhere.equals("")) {
      	  sqlGoods = 
      		  "select goods.nid_good, cnm_goodart, cnm_good "+
      		  " ,c1.nCost as cost1 "+
      		  " ,c3.nCost as cost3 "+
      		  " ,c9.nCost as cost9 "+
      		  " ,(select cnm_brand from brands where nid_brand=goods.nid_brand) as cnm_brand"+
      		  "  ,(select coalesce(sum(nqty),0) from accounts "+
      		  "     where nid_account=41 and nid_subaccount=1 and nDebCred=2 and nid_root=0 "+
      		  "       and nanalit1=goods.nid_good) as nOSTTotal "+
      		  "  ,(select coalesce(sum(quantity),0) from reserved where nid_good= goods.nid_good) as nOSTReserv "+
      		  "  ,(select coalesce(sum(nqty),0) from accounts where nid_account=41 and nid_subaccount=1 and nDebCred=2 and nid_root=0 and nanalit1=goods.nid_good) -  "+
      		  "   (select coalesce(sum(quantity),0) from reserved where nid_good= goods.nid_good)    as nOSTFree "+
      		  

      		  " from ((( goods "+
      		  "     left join costs c1 on (c1.nid_nomencl=goods.nid_good and c1.nid_typecost=1 and c1.dDT_Closed is null) )"+
      		  "     left join costs c3 on (c3.nid_nomencl=goods.nid_good and c3.nid_typecost=3 and c3.dDT_Closed is null) )"+
      		  "     left join costs c9 on (c9.nid_nomencl=goods.nid_good and c9.nid_typecost=3 and c9.dDT_Closed is null) )"+      		  
      		  " where 1=1 "+
      	      " and ((cNM_GoodArt like '%" + AddedWhere + "%') " +
      	      "  or  (cNM_Good like '%" + AddedWhere + "%') " +
      	      "  or  (cNM_Priznak1 like '%" + AddedWhere + "%') " +
      	      "      ) "+
      		  "   and nvisible=1"+
      		  " order by cnm_goodart"
      		  ;
      	  
    	} else {
    		sqlGoods = 
    			"select goodstree.nid_good, cnm_goodart, cnm_good "+ 
    			" ,c1.nCost as cost1 "+
          		" ,c3.nCost as cost3 "+
          		" ,c9.nCost as cost9 "+
          		" ,(select cnm_brand from brands where nid_brand=goods.nid_brand) as cnm_brand"+          		
        		"  ,(select coalesce(sum(nqty),0) from accounts "+
          		"     where nid_account=41 and nid_subaccount=1 and nDebCred=2 and nid_root=0 "+
          		"       and nanalit1=goods.nid_good) as nOSTTotal "+
          		"  ,(select coalesce(sum(quantity),0) from reserved where nid_good= goods.nid_good) as nOSTReserv "+
          		"  ,(select coalesce(sum(nqty),0) from accounts where nid_account=41 and nid_subaccount=1 and nDebCred=2 and nid_root=0 and nanalit1=goods.nid_good) -  "+
          		"   (select coalesce(sum(quantity),0) from reserved where nid_good= goods.nid_good)    as nOSTFree "+

          		"  from ((((goodstree inner join goods on (goods.nID_Good = goodstree.nID_Good) ) "+    			
    			"     left join costs c1 on (c1.nid_nomencl=goods.nid_good and c1.nid_typecost=1 and c1.dDT_Closed is null) )"+
          		"     left join costs c3 on (c3.nid_nomencl=goods.nid_good and c3.nid_typecost=3 and c3.dDT_Closed is null) )"+
          		"     left join costs c9 on (c9.nid_nomencl=goods.nid_good and c9.nid_typecost=3 and c9.dDT_Closed is null) )"+      		  

    			" where 1=1 "+
    			"   and nid_parent="+sidParent +
    			"   and nvisible=1"+
    			" order by cnm_goodart"
    			;
    	}
		
		Cursor goodsCursor = database.rawQuery(sqlGoods, new String [] {});
		goodsCursor.moveToFirst();
			if(!goodsCursor.isAfterLast()) {
				do {
			    	goodsMap = new HashMap<String, String>();
			    	goods.add(goodsMap);
			    	goodsMap.put("cnm_goodart", goodsCursor.getString(1));    	
			    	goodsMap.put("cnm_good", goodsCursor.getString(2));			    	
			    	goodsMap.put("cost1", goodsCursor.getString(3));
			    	goodsMap.put("cnm_brand", goodsCursor.getString(6));
			    	
			    	goodsMap.put("nOSTTotal", goodsCursor.getString(7));
			    	goodsMap.put("nOSTReserv", goodsCursor.getString(8));
			    	goodsMap.put("nOSTFree", goodsCursor.getString(9));			    	
			    	
			    	
					//String name = goodsCursor.getString(1) + " " + goodsCursor.getString(2);
					//goods.add(name, "");
			    	
				} while (goodsCursor.moveToNext());
			}
			goodsCursor.close();							
		
	
			/*
				nOSTTotal, nOSTReserv, nOSTFree
			*/
			
		ListView lv = (ListView)ba.findViewById(R.id.listGoods);
		ListAdapter la = new SimpleAdapter(
					ba, 
					goods, 
					R.layout.listgoods, 
					new String[] { "cnm_goodart", "cnm_good", "cost1", "cnm_brand", "nOSTTotal", "nOSTReserv", "nOSTFree" }, 
					new int[]{R.id.cnm_goodart, R.id.cnm_good, R.id.cost, R.id.brand, R.id.nOSTTotal , R.id.nOSTReserv, R.id.nOSTFree		
					}
					);
		lv.setAdapter(la);
		lv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				ba.showDialog(0);
				
				goodsMap = goods.get(pos);
				//String s1=goodsMap.get("cnm_goodart").toString();
				ba.eGoodArt.setText(goodsMap.get("cnm_goodart").toString());
				ba.eGoodName.setText(goodsMap.get("cnm_good").toString());				

				
			}});
    }

    protected Dialog onCreateDialog(int id) {
    	return ba.builder.create();
   	
    }
    
	
}
