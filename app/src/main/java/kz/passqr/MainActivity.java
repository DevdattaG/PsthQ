package kz.passqr;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

   public void clickFuncShowQR(View view){
       Toast.makeText(MainActivity.this, "ShowQR Clicked", Toast.LENGTH_SHORT).show();
       Intent nextPage = new Intent(
               "android.intent.action.QRReader");

       startActivity(nextPage);
   }

    public void clickFuncExit(View view){
        Toast.makeText(MainActivity.this,"Logging Out", Toast.LENGTH_SHORT).show();
        System.exit(0);
    }
}
