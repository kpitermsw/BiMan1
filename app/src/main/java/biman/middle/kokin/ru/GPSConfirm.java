package biman.middle.kokin.ru;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import biman.middle.kokin.ru.R;

public class GPSConfirm extends Activity {
    int gpstype;
    String messtoact;
    Button btnOK;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gpsconfirm);

        Bundle extras = getIntent().getExtras();
        gpstype = extras.getInt("gpstype");
        messtoact = extras.getString("messtoact");
        
        TextView label1 = (TextView) findViewById(R.id.gps_text1);;
        TextView label2 = (TextView) findViewById(R.id.gps_text2);;        

        if (!messtoact.equals("")) {
        	label1.setText(messtoact);
        }
        
        //"Без включенного GPS работа невозможна";        
    	label2.setText("");
        if (gpstype==2) {
        	label2.setText("Без включенного GPS работа невозможна");
        }
        
        btnOK = (Button) findViewById(R.id.gps_bOK);
        btnOK.setOnClickListener(new View.OnClickListener() {         
            @Override
            public void onClick(View v) {
            	finish();
            }
        });        

        
    }

}
