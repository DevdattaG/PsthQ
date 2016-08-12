package kz.passqr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KZ-Tech on 8/11/2016.
 */
public class ValidationPage extends ActionBarActivity {
    final Context context = this;
    String TAG = "Response";
    SoapPrimitive resultString;
    String code ="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validation_page);
       // Intent myIntent = getIntent(); // gets the previously created intent
        code = getIntent().getStringExtra("Code");
        AsyncCallWS task = new AsyncCallWS();
        task.execute();


        final TextView codeView = (TextView) findViewById(R.id.codeDisplay);
        codeView.setText(code);
    }
    public void clickOK(View view){
        Toast.makeText(ValidationPage.this, "Redirecting", Toast.LENGTH_SHORT).show();
        Intent mainPage = new Intent("android.intent.action.MAINACTIVITY");

        startActivity(mainPage);
        System.exit(0);
    }
    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            calculate();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
            Toast.makeText(ValidationPage.this, "Response" + resultString.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public void calculate() {
        String SOAP_ACTION = "http://tempuri.org/BarcodeStatus";
        String METHOD_NAME = "BarcodeStatus";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://54.149.90.101/KzWebservice/barcodescanner.asmx";

        try {
            String abc = "4444";
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            Request.addProperty("barcode", Integer.parseInt(abc));
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);

            HttpTransportSE transport = new HttpTransportSE(URL);

            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            String response = resultString.toString();
            final Pattern acknowledgementPattern = Pattern.compile("<Acknowledgement>(.+?)</Acknowledgement>");
            final Pattern countAckPattern = Pattern.compile("<Acknowledgement>(.+?)</Acknowledgement>");
            final Matcher acknowledgementMatcher = acknowledgementPattern.matcher(response);
            final Matcher countAckMatcher = acknowledgementPattern.matcher(response);
            acknowledgementMatcher.find();
            countAckMatcher.find();
            Log.i(TAG, "Response caught : " + response);
            System.out.println("Acknowledgement Token  " +acknowledgementMatcher.group(1));
            System.out.println("CountAck Token  " +countAckMatcher.group(1));
            if(acknowledgementMatcher.group(1) == "True" && countAckMatcher.group(1) == "True")
            {
                Log.d(TAG,"Valid User... User not checked in yet");
            }else if(acknowledgementMatcher.group(1) == "True" && countAckMatcher.group(1) == "False")
            {
                Log.d(TAG,"Valid User... User has checked in already");
            }else
            {
                Log.d(TAG,"Invalid User !!!");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
}
