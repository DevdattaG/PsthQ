package kz.passqr;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

/**
 * Created by KZ-Tech on 7/20/2016.
 */
public class QRReader extends Activity{
    final Context context = this;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private TextView barcodeInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_reader);
        findViewById(R.id.manualCheck).setVisibility(View.GONE);

        cameraView = (SurfaceView)findViewById(R.id.camera_view);
        barcodeInfo = (TextView)findViewById(R.id.code_info);

        barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.ALL_FORMATS)
                        .build();

        cameraSource = new CameraSource
                .Builder(this, barcodeDetector).setAutoFocusEnabled(true)
                .setRequestedPreviewSize(640, 480)
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override

            public void surfaceCreated(SurfaceHolder holder)  {

                int hasPermission = ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA);
                if (hasPermission == PackageManager.PERMISSION_GRANTED) {
                    //Do smthng
                    try {
                        cameraSource.start(cameraView.getHolder());
                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if (barcodes.size() != 0) {
                    barcodeInfo.post(new Runnable() {    // Use the post method of the TextView
                        public void run() {
                            //cameraSource.release();
                            Intent validationPage = new Intent(
                                    "android.intent.action.ValidationPage");
                            validationPage.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            validationPage.putExtra("Code",barcodes.valueAt(0).displayValue);

                            startActivity(validationPage);
                            System.exit(0);
//                            final Dialog dialog = new Dialog(context);
//                            dialog.setContentView(R.layout.qr_dialog);
//                            dialog.setTitle("PassThru");
//
//                            // set the custom dialog components - text, image and button
//                            TextView text = (TextView) dialog.findViewById(R.id.text);
//                            text.setText(barcodes.valueAt(0).displayValue);
//                            ImageView image = (ImageView) dialog.findViewById(R.id.image);
//                            image.setImageResource(R.mipmap.ic_launcher);
//
//                            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
//                            // if button is clicked, close the custom dialog
//                            dialogButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    dialog.dismiss();
//                                    System.exit(0);
//                                }
//                            });
//
//                            dialog.show();

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSource.release();
        barcodeDetector.release();
    }

    public void showBarcodeCameraView(View view){
        findViewById(R.id.manualCheck).setVisibility(View.GONE);
        findViewById(R.id.camera_view).setVisibility(View.VISIBLE);
        Button scanButton = (Button)findViewById(R.id.barcodeButton);
        Button manualButton = (Button)findViewById(R.id.manualButton);
        scanButton.setBackgroundColor(Color.parseColor("#ED1651"));
        scanButton.setTextColor(Color.parseColor("#ffffff"));
        manualButton.setBackgroundColor(Color.parseColor("#cccccc"));
        manualButton.setTextColor(Color.parseColor("#000000"));
        EditText barcodeVal = (EditText)findViewById(R.id.barcodeNum);
        barcodeVal.setText("");

        // For closing the keyboard if appears. Found to generate bugs while the executing. Found : 17th March 2017 6:54 PM
//        InputMethodManager inputManager = (InputMethodManager)
//                getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showManualView(View view){
        findViewById(R.id.camera_view).setVisibility(View.GONE);
        findViewById(R.id.manualCheck).setVisibility(View.VISIBLE);
        Button scanButton = (Button)findViewById(R.id.barcodeButton);
        Button manualButton = (Button)findViewById(R.id.manualButton);
        manualButton.setBackgroundColor(Color.parseColor("#ED1651"));
        manualButton.setTextColor(Color.parseColor("#ffffff"));
        scanButton.setBackgroundColor(Color.parseColor("#cccccc"));
        scanButton.setTextColor(Color.parseColor("#000000"));
    }

    public void scanManual(View view){
        final EditText barcodeVal = (EditText)findViewById(R.id.barcodeNum);
        if(barcodeVal.getText().toString().equals(""))
        {
            Log.d("Scan Code : ","Invalid");
            Toast.makeText(QRReader.this, "Invalid Input", Toast.LENGTH_SHORT).show();
        }else{
            Log.d("Scan Code",barcodeVal.getText().toString());
                    Intent validationPage = new Intent("android.intent.action.ValidationPage");
                    validationPage.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    validationPage.putExtra("Code",barcodeVal.getText().toString());
                    startActivity(validationPage);
                   // System.exit(0);
        }
    }

    public void clearManual(View view){
        EditText barcodeVal = (EditText)findViewById(R.id.barcodeNum);
        barcodeVal.setText("");
    }

    public void showHistoryView(View view)
    {
        startActivity(new Intent("android.intent.action.HistoryActivity"));
    }
}

