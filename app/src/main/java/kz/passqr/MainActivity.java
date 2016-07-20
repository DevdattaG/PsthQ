package kz.passqr;

import android.app.Activity;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

   public void clickFuncShowQR(View view){
       Toast.makeText(MainActivity.this, "ShowQR Clicked", Toast.LENGTH_SHORT).show();
   }

    public void clickFuncExit(View view){
        Toast.makeText(MainActivity.this,"Exit clicked", Toast.LENGTH_SHORT).show();
        System.exit(0);

    }
}
