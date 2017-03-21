package kz.passqr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by KZ-Tech on 3/18/2017.
 */
public class HistoryActivity extends Activity {
    static TableLayout tl;
    static TableRow tr;
    //static TextView title;
   // static TextView srNo;
    static TextView barcode;
    static TextView lastScanned;
    static TextView gate;
    static TextView name;
    String troubleshootCode ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_screen);
        Button allowButton = (Button)findViewById(R.id.troubleshootScan);
        TextView title = (TextView)findViewById(R.id.textView2);
        title.setText("Troubleshoot");
        allowButton.setVisibility(View.GONE);
        tl=(TableLayout)findViewById(R.id.TableLayout01);
        troubleshootCode = getIntent().getStringExtra("troubleshootCode");
        AsyncCallWS task = new AsyncCallWS();
        task.execute();
    }

    public void showTable(String tableData)
    {
        TextView titleText = (TextView)findViewById(R.id.statusMessage);
        try
        {
            JSONArray jr = new JSONArray(tableData.toString());
            Log.d("Array Length : ", jr.toString());
            if(jr.length()>0)
            {
                //TableRow.LayoutParams params = new TableRow.LayoutParams();
//                tr=new TableRow(this);
//                //tr.setGravity(Gravity.CENTER_HORIZONTAL);
//                tr.setLayoutParams(params);
//                title=new TextView(this);
//                title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
//                title.setTypeface(null, Typeface.BOLD);
//                title.setGravity(Gravity.CENTER);
//                title.setText("Troubleshoot");
//                tl.addView(tr,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
       //         tr.addView(title);
                tr=new TableRow(this);
                //srNo= new TextView(this);
                barcode= new TextView(this);
                lastScanned = new TextView(this);
                gate= new TextView(this);
                name = new TextView(this);
//                srNo.setText("Sr. No.");
//                srNo.setTypeface(null, Typeface.BOLD);
//                srNo.setWidth(50);
//                srNo.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT));
                //srNo.setGravity(Gravity.CENTER);
                barcode.setText("Barcode");
                barcode.setTypeface(null, Typeface.BOLD);
                //barcode.setGravity(Gravity.CENTER);
                lastScanned.setText("Last Scanned");
                lastScanned.setTypeface(null, Typeface.BOLD);
                //lastScanned.setGravity(Gravity.CENTER);
                gate.setText("Gate");
                gate.setTypeface(null, Typeface.BOLD);
                //gate.setGravity(Gravity.CENTER);
                name.setText("Customer Name");
                name.setTypeface(null, Typeface.BOLD);
                //name.setGravity(Gravity.CENTER);
               // tr.addView(srNo);
                tr.addView(barcode);
                tr.addView(lastScanned);
                tr.addView(gate);
                tr.addView(name);
                tl.addView(tr,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                Button allowButton = (Button)findViewById(R.id.troubleshootScan);
                allowButton.setVisibility(View.VISIBLE);
                String latestBarcode = "";
                for(int i = 0; i< jr.length(); i++)
                {
                    tr=new TableRow(this);
                 //   srNo= new TextView(this);
                 //   srNo.setTypeface(null, Typeface.BOLD);
                 //   srNo.setGravity(Gravity.CENTER);
                    barcode= new TextView(this);
                  //  barcode.setTypeface(null, Typeface.BOLD);
                   // barcode.setGravity(Gravity.CENTER);
                    lastScanned = new TextView(this);
                   // lastScanned.setTypeface(null, Typeface.BOLD);
                    //lastScanned.setGravity(Gravity.CENTER);
                    gate= new TextView(this);
                    //gate.setTypeface(null, Typeface.BOLD);
                    //gate.setGravity(Gravity.CENTER);
                    name = new TextView(this);
                    //name.setTypeface(null, Typeface.BOLD);
                    //name.setGravity(Gravity.CENTER);

                    String s = jr.getString(i);
                    JSONObject js = (JSONObject)jr.getJSONObject(i);
                   // srNo.setText(String.valueOf(i));
                    //srNo.setWidth(50);
                    barcode.setText(js.getString("Barcode"));
                    lastScanned.setText(js.getString("LastScanned"));
                    gate.setText(js.getString("Gate"));
                    name.setText(js.getString("Name"));
                    //tr.addView(srNo);
                    tr.addView(barcode);
                    tr.addView(lastScanned);
                    tr.addView(gate);
                    tr.addView(name);
                    tl.addView(tr,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    if( i == jr.length()-1)
                    {
                        latestBarcode = js.getString("Barcode");
                    }
                }
                if(!latestBarcode.equals(""))
                titleText.setText("Allow " + troubleshootCode + " to be mapped to " + latestBarcode);
            }else
            {
                titleText.setText("No records found associated with this barcode");
            }

        }catch(Exception ex)
        {
            Log.d("JSONException",ex.toString());
        }

    }

    private class AsyncCallWS extends AsyncTask<Void, String, String> {

        @Override
        protected void onPreExecute() {
            Log.i("hgfds", "onPreExecute");
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.i("hgfds", "doInBackground");
            return fetchData();
            //return null;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("onPostExecute", result.toString());
            showTable(result.toString());
        }
    }

    public String fetchData() {
        String SOAP_ACTION = "http://tempuri.org/GetBarcodeInfo";
        String METHOD_NAME = "GetBarcodeInfo";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://54.149.90.101/KzWebservice1/BarCodeScanner.asmx";
        String response = "";
        try {
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            // Toast.makeText(ValidationPage.this, "Response" + code, Toast.LENGTH_LONG).show();
            Request.addProperty("Barcode", troubleshootCode);
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();
            response = resultString.toString();
            Log.d("Response catched", response);
        } catch (Exception ex) {
            Log.e("Response", "Error: " + ex.getMessage());
        }
        return response;
    }

    public void troubleshootScan(View view)
    {

    }

    public void cancelTroubleshoot(View view)
    {
        startActivity(new Intent("android.intent.action.QRReader"));
    }
}
