package biman.middle.kokin.ru;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class BiManActivity extends Activity { 
	//extends  TabActivity implements TabHost.TabContentFactory {
	// Проверка коммита  111 222 333
	// 111
	static SharedPreferences preferences;
    SharedPreferences.Editor editor;
    DatabaseHelper dh;
    
    private static final String DB_NAME = "biman1.sqlite3";
	private static final String TABLE_NAME = "goodstree";

	private SQLiteDatabase database;
	AlertDialog.Builder builder;

	BiManActivity _this = this;

    GoodsProcess goodProc;
    KontrProcess kontrProc;
    DocProcess docProc;
	// -----------------
    
    TextView eGoodArt;
    TextView eGoodName;
    TextView eKontrName;
    TextView eDocName;    

    LinearLayout ll;
    TabWidget tw;        
    TextView tv;
    View tabView;
    
    
    /*
	@Override
	public View createTabContent(String tag) {
		// TODO Auto-generated method stub
        final TextView tv = new TextView(this);
        //dh = new DatabaseHelper(this);
        //String aaa=dh.getCol2();
        //tv.setText("from table aaa "+aaa);
        return tv;
	}
	*/
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ExternalDbOpenHelper dbOpenHelper = new ExternalDbOpenHelper(this, DB_NAME);
        database = dbOpenHelper.openDataBase();
        //���, ���� �������!
        
        setContentView(R.layout.main);

        
        //final TabHost tabHost = getTabHost();
        final TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        
        LayoutInflater.from(this).inflate(R.layout.goodtab, tabHost.getTabContentView(), true);
       	TabSpec tc1 = tabHost.newTabSpec("tab1");      
       	tc1.setIndicator("������� �������");
       	tc1.setContent(R.id.v1);
        tabHost.addTab(tc1);

        LayoutInflater.from(this).inflate(R.layout.kontrtab, tabHost.getTabContentView(), true);        
       	TabSpec tc2 = tabHost.newTabSpec("tab2");
       	tc2.setIndicator("�������");
       	tc2.setContent(R.id.ktab);
        tabHost.addTab(tc2);

        LayoutInflater.from(this).inflate(R.layout.doctab, tabHost.getTabContentView(), true);        
       	TabSpec tc3 = tabHost.newTabSpec("tab3");
       	tc3.setIndicator("���������");
       	tc3.setContent(R.id.dtab);
        tabHost.addTab(tc3);

        tw = (TabWidget)tabHost.findViewById(android.R.id.tabs);        
       
        tabView = tw.getChildTabViewAt(0);
        tv = (TextView)tabView.findViewById(android.R.id.title);
        tv.setTextSize(30);        
        	
        tabView = tw.getChildTabViewAt(1);
        tv = (TextView)tabView.findViewById(android.R.id.title);
        tv.setTextSize(30);        
        
        tabView = tw.getChildTabViewAt(2);
        tv = (TextView)tabView.findViewById(android.R.id.title);
        tv.setTextSize(30);        
        
        
        // �������� ������� Goods
        goodProc = new GoodsProcess();
        goodProc.goodsStart(database, this);

        kontrProc = new KontrProcess();
        kontrProc.kontrStart(database, this);
        
        docProc = new DocProcess();
        docProc.docStart(database, this);
        
        
       	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
       	imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //		    android:focusable="false"
        
    }    
    
    
    protected Dialog onCreateDialog(int id) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	if (id==0) {
    		View v = LayoutInflater.from(this).inflate(R.layout.listgoods, null, true);
    		builder.setView(v);
    		
    		eGoodArt   = (TextView) v.findViewById(R.id.cnm_goodart);
    		eGoodName  = (TextView) v.findViewById(R.id.cnm_good);    		
    		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int id) {
    				int kkk=0;
    			}
    		});
    	}	
    	
    	if (id==1) {
    		View v = LayoutInflater.from(this).inflate(R.layout.listkontrs, null, true);
    		builder.setView(v);
    		
    		eKontrName   = (TextView) v.findViewById(R.id.cnm_kontr);
    		
    		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int id) {
    				int kkk=0;
    			}
    		});
    	}
    	
    	if (id==2) {
    		View v = LayoutInflater.from(this).inflate(R.layout.listdocs, null, true);
    		builder.setView(v);
    		
    		eDocName   = (TextView) v.findViewById(R.id.cnumasis);
    		
    		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    			public void onClick(DialogInterface dialog, int id) {
    				int kkk=0;
    			}
    		});
    	}
    	
    	return builder.create();
   	
    }

}