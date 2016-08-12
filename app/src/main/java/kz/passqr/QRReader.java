package kz.passqr;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
                            //System.exit(0);
                            startActivity(validationPage);
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
}

