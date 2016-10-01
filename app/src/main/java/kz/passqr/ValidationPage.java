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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by KZ-Tech on 8/11/2016.
 */
public class ValidationPage extends ActionBarActivity {
    final Context context = this;
    String TAG = "Response";
    String code ="";
    String status = "fetching...";
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
        codeView = (TextView) findViewById(R.id.codeDisplay);
        statusView = (TextView) findViewById(R.id.statusDisplay);
        statusView.setText(status);
        codeView.setText(code);
    }

    public void clickOK(View view){
        Toast.makeText(ValidationPage.this, "Redirecting", Toast.LENGTH_SHORT).show();
        Intent mainPage = new Intent("android.intent.action.MAINACTIVITY");

        startActivity(mainPage);
        System.exit(0);
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
                                    System.exit(0);
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
            //Toast.makeText(ValidationPage.this, "Responce for status : " + status, Toast.LENGTH_LONG).show();

        //    Toast.makeText(ValidationPage.this, "Response" + resultString.toString(), Toast.LENGTH_LONG).show();

        }

    }
    public void setStatus(String stat){
        this.status = stat;
        statusView.setText(this.status);
        statusView.invalidate();
    }

    public void calculate() {
        String url = "http://192.168.2.11:80/PassT_WebService/api/Scan/CheckIsQRCodeValid";
        final JSONObject body = new JSONObject();
        try {
            body.put("TransId",code);
            body.put("TollPlazaId",2);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            status = response;
                            //Toast.makeText(ValidationPage.this, "The Response : " + response, Toast.LENGTH_LONG).show();
                            setStatus(status);
                        }

                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    // Error handling
                    System.out.println("Something went wrong!");
                    Log.d(TAG, "Something went wrong!");
                    status = "Error Encountered" + error.getMessage();
                    setStatus(status);
                    Log.d(TAG, error.getMessage());
                    error.printStackTrace();

                }


            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    //  params.put("Content-Type", "application/json");
                    params.put("Basic-Authorization", "AppUser:5222c123-936e-4d20-86d3-11354093bfdd");

                    return params;
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return body.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }

            };

            Volley.newRequestQueue(this).add(stringRequest);

        } catch (Exception ex) {
            Log.e(TAG, "Error: " + ex.getMessage());
        }
    }
}
