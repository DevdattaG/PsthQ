package kz.passqr;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    static String status = "fetching...";
    static String details ="";

    static TextView codeView;
    static TextView statusView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.validation_page);
       // Intent myIntent = getIntent(); // gets the previously created intent
        code = getIntent().getStringExtra("Code");
        //Toast.makeText(ValidationPage.this, "Response" + code.toString(), Toast.LENGTH_LONG).show();
        AsyncCallWS task = new AsyncCallWS();
        task.execute();

       // Toast.makeText(ValidationPage.this, "Setting values to texts", Toast.LENGTH_SHORT).show();
        // displays final values. (Delete this after)
        codeView = (TextView) findViewById(R.id.codeDisplay);
        statusView = (TextView) findViewById(R.id.statusDisplay);
        statusView.setText(status);
        codeView.setText(code);
    }

    public void clickOK(View view){
        //Toast.makeText(ValidationPage.this, "Redirecting", Toast.LENGTH_SHORT).show();
        Intent mainPage = new Intent("android.intent.action.QRReader");
        startActivity(mainPage);
        //System.exit(0);
    }

    public void clickDetails(View view){
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.qr_dialog);
                           // dialog.setTitle("PassThru");
                            dialog.setTitle(status);

                            // set the custom dialog components - text, image and button
                            TextView text = (TextView) dialog.findViewById(R.id.text);
                            text.setText(details);
                           // ImageView image = (ImageView) dialog.findViewById(R.id.image);
                            //image.setImageResource(R.mipmap.ic_launcher);

                            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                            // if button is clicked, close the custom dialog
                            dialogButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    //System.exit(0);
                                }
                            });
                            dialog.show();
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
        //    Toast.makeText(ValidationPage.this, "Responce for status : " + status, Toast.LENGTH_LONG).show();
            statusView.setText(status);
            details = resultString.toString();
            statusView.invalidate();
        //    Toast.makeText(ValidationPage.this, "Response" + resultString.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void calculate() {
        String SOAP_ACTION = "http://tempuri.org/BarcodeStatus";
        String METHOD_NAME = "BarcodeStatus";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://54.149.90.101/KzWebservice/barcodescanner.asmx";

        try {
           // String abc = "4444";
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
           // Toast.makeText(ValidationPage.this, "Response" + code, Toast.LENGTH_LONG).show();
            Request.addProperty("barcode", code);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            resultString = (SoapPrimitive) soapEnvelope.getResponse();
            String response = resultString.toString();
            final Pattern acknowledgementPattern = Pattern.compile("<Acknowledgement>(.+?)</Acknowledgement>");
            final Pattern countAckPattern = Pattern.compile("<CountAck>(.+?)</CountAck>");
            final Matcher acknowledgementMatcher = acknowledgementPattern.matcher(response);
            final Matcher countAckMatcher = countAckPattern.matcher(response);
            acknowledgementMatcher.find();
            countAckMatcher.find();
            Log.i(TAG, "Response caught : " + response);
            System.out.println("Acknowledgement Token  " +acknowledgementMatcher.group(1));
            System.out.println("CountAck Token  " +countAckMatcher.group(1));
            if(acknowledgementMatcher.group(1).toString().equals("True") && countAckMatcher.group(1).toString().equals("True"))
            {
                Log.d(TAG,"Valid User... User not checked in yet");
                status = "Valid Code";

            }else if(acknowledgementMatcher.group(1).toString().equals("True") && countAckMatcher.group(1).toString().equals("False"))
            {
                Log.d(TAG,"Valid User... User has checked in already");
                status = "Checked In";
            }else
            {
                Log.d(TAG,"Invalid User !!!");
                status = "Invalid Code";

            }
//            statusView.setText(status);
        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
}
