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
               "android.intent.action.ValidateCode");

       startActivity(nextPage);
   }

    public void clickFuncExit(View view){
        Toast.makeText(MainActivity.this,"Exit clicked", Toast.LENGTH_SHORT).show();
       // System.exit(0);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.qr_dialog);
        dialog.setTitle("Logout");

        // set the custom dialog components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.text);
        text.setText("Logging you out...");
        ImageView image = (ImageView) dialog.findViewById(R.id.image);
        image.setImageResource(R.mipmap.ic_launcher);

        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                System.exit(0);
            }
        });

        dialog.show();

    }
}
