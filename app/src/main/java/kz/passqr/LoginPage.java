package kz.passqr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by KZ-Tech on 7/20/2016.
 */
public class LoginPage extends Activity {
    Button submitButton;
    Button clearButton;
    EditText userName;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        Button submitButton = (Button)findViewById(R.id.button4);
        Button clearButton = (Button)findViewById(R.id.button3);
        final EditText userName = (EditText)findViewById(R.id.editText);
        userName.requestFocus();
        final EditText password = (EditText)findViewById(R.id.editText2);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().equals("Kyazoonga") &&

                        password.getText().toString().equals("Kyazoonga")) {
                    Toast.makeText(getApplicationContext(), "Redirecting...",Toast.LENGTH_SHORT).show();
                    Intent nextPage = new Intent(
                            "android.intent.action.MAINACTIVITY");

                    startActivity(nextPage);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
                }
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                userName.setText("");
                password.setText("");
                userName.requestFocus();
            }


        });
    }
}
