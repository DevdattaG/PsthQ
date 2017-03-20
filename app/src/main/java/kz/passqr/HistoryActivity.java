package kz.passqr;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    static TextView srNo;
    static TextView venueNum;
    static TextView venueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_screen);
        tl=(TableLayout)findViewById(R.id.TableLayout01);
        tr=new TableRow(this);
        srNo= new TextView(this);
        venueNum= new TextView(this);
        venueName = new TextView(this);
        AsyncCallWS task = new AsyncCallWS();
        task.execute();
        Log.d("Done"," Done with logging asych");
       // fetchData();
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
            try
            {
                JSONArray jr = new JSONArray(result.toString());
                Log.d("Array Length : ", jr.toString());
                for(int i = 0; i< jr.length(); i++)
                {
                    String s = jr.getString(i);
                    JSONObject js = (JSONObject)jr.getJSONObject(i);
                    srNo.setText(String.valueOf(i));
                    venueNum.setText(js.getString("VenueId"));
                    venueName.setText(js.getString("Venue"));
                    tr.addView(srNo);
                    tr.addView(venueNum);
                    tr.addView(venueName);
                    tl.addView(tr,new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    Log.d("asdf"," Json Fetched");
                }
            }catch(Exception ex)
            {
                Log.d("JSONException",ex.toString());
            }
        }
    }

    public void addNewRow(View view)
    {


    }

    public String fetchData() {
        String SOAP_ACTION = "http://tempuri.org/GetVenueDetails";
        String METHOD_NAME = "GetVenueDetails";
        String NAMESPACE = "http://tempuri.org/";
        String URL = "http://54.149.90.101/KzWebservice/barcodescanner.asmx";
        String response = "";
        try {
            // String abc = "4444";
            SoapObject Request = new SoapObject(NAMESPACE, METHOD_NAME);
            // Toast.makeText(ValidationPage.this, "Response" + code, Toast.LENGTH_LONG).show();
            //Request.addProperty("barcode", "9876543");
            SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            soapEnvelope.dotNet = true;
            soapEnvelope.setOutputSoapObject(Request);
            HttpTransportSE transport = new HttpTransportSE(URL);
            transport.call(SOAP_ACTION, soapEnvelope);
            SoapPrimitive resultString = (SoapPrimitive) soapEnvelope.getResponse();
            response = resultString.toString();
//            final Pattern acknowledgementPattern = Pattern.compile("<Acknowledgement>(.+?)</Acknowledgement>");
//            final Pattern countAckPattern = Pattern.compile("<CountAck>(.+?)</CountAck>");
//            final Matcher acknowledgementMatcher = acknowledgementPattern.matcher(response);
//            final Matcher countAckMatcher = countAckPattern.matcher(response);
//            acknowledgementMatcher.find();
//            countAckMatcher.find();
//            Log.i("Response", "Response caught : " + response);
//            System.out.println("Acknowledgement Token  " +acknowledgementMatcher.group(1));
//            System.out.println("CountAck Token  " +countAckMatcher.group(1));
//            if(acknowledgementMatcher.group(1).toString().equals("True") && countAckMatcher.group(1).toString().equals("True"))
//            {
//                Log.d("Response","Valid User... User not checked in yet");
//                //status = "Valid Code";
//
//            }else if(acknowledgementMatcher.group(1).toString().equals("True") && countAckMatcher.group(1).toString().equals("False"))
//            {
//                Log.d("Response","Valid User... User has checked in already");
//               // status = "Checked In";
//            }else
//            {
//                Log.d("Response","Invalid User !!!");
//               // status = "Invalid Code";
//
//            }
//            statusView.setText(status);
        } catch (Exception ex) {
            Log.e("Response", "Error: " + ex.getMessage());
        }
        return response;
    }
}
