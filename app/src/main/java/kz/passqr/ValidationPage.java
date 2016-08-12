package kz.passqr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by KZ-Tech on 8/11/2016.
 */
public class ValidationPage extends Activity {
    final Context context = this;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validation_page);
       // Intent myIntent = getIntent(); // gets the previously created intent
        String code = getIntent().getStringExtra("Code");
        final TextView codeView = (TextView) findViewById(R.id.codeDisplay);
        codeView.setText(code);
    }
    public void clickOK(View view){
        Toast.makeText(ValidationPage.this, "Redirecting", Toast.LENGTH_SHORT).show();
        Intent mainPage = new Intent("android.intent.action.MAINACTIVITY");

        startActivity(mainPage);
        System.exit(0);
    }
}
