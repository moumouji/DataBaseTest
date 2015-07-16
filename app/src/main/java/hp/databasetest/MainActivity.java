package hp.databasetest;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.Currency;


public class MainActivity extends Activity implements View.OnClickListener {

    TextView count,txt;
    Button b1,b2,b3;
    EditText et;
    SQLiteDatabase client;
    String sql;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString() + "DBTest.db3", null);
        try{
            sql = "create table mytest (names varchar(255))";
            client.execSQL(sql);
        }catch (SQLiteException se){
            se.printStackTrace();
        }

        count = (TextView) findViewById(R.id.count);
        txt = (TextView)findViewById(R.id.txt);
        b1 = (Button)findViewById(R.id.insert);
        b1.setOnClickListener(this);
        b2 = (Button) findViewById(R.id.update);
        b2.setOnClickListener(this);
        b3 = (Button) findViewById(R.id.delete);
        b3.setOnClickListener(this);
        et = (EditText) findViewById(R.id.etext);

        inittvs();
    }

    @Override
    protected void onDestroy() {
        if(client != null && client.isOpen()) client.close();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        String tmp = et.getText().toString();
        int type = 0;
        try{
            switch (v.getId()){
                case R.id.insert:
                    type = 1;
                    sql = "insert into mytest values(?)";
                    client.execSQL(sql, new String[]{tmp});
                    break;
                case R.id.update:
                    type = 2;
                    String[] gets = tmp.split(" ");
                    sql = "update mytest set names = ? where names = ?";
                    client.execSQL(sql, new String[]{gets[1], gets[0]});
                    break;
                case R.id.delete:
                    type = 3;
                    sql = "delete from mytest where names = ?";
                    client.execSQL(sql, new String[]{tmp});
                    break;
            }
            inittvs();
        }catch (SQLiteException se){
            se.printStackTrace();
            if(type == 1){
                Toast.makeText(getApplicationContext(), "Insert Error", Toast.LENGTH_SHORT).show();
            }else if(type == 2){
                Toast.makeText(getApplicationContext(), "Update Error", Toast.LENGTH_SHORT).show();
            }else if(type == 3){
                Toast.makeText(getApplicationContext(), "The one you input is not exist", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void inittvs(){
        sql = "select * from mytest";
        Cursor cursor = client.rawQuery(sql, null);
        if(cursor != null){
            count.setText("Current member : " + cursor.getCount());
            String txts = "";
            while(cursor.moveToNext()){
                txts += cursor.getString(cursor.getColumnIndex("names"));
                txts += "\n";
            }
            txt.setText(txts);
            et.setText("");
        }
    }
}
