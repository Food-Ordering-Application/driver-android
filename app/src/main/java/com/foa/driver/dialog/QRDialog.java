package com.foa.driver.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foa.driver.R;
import com.foa.driver.api.UserService;
import com.foa.driver.session.LoginSession;
import com.google.zxing.WriterException;
import com.paypal.checkout.paymentbutton.PaymentButton;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

import static android.content.Context.WINDOW_SERVICE;
import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;

public class QRDialog extends Dialog {

    private Context context;
    private ImageView orderIdQRCodeImageView;
    private TextView orderIdTextView;
    private String orderId;

    String TAG = "GenerateQRCode";
    Button start, save;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    public QRDialog(Context context, String orderId) {
        super(context);
        this.context = context;
        this.orderId = orderId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_qr);
        init();
        createQRCode();
    }


    private void init(){
        orderIdQRCodeImageView = findViewById(R.id.orderIdQRCodeTextView);
        orderIdTextView = findViewById(R.id.orderIdTextView);
        orderIdTextView.setText(orderId);
    }
    
    private void createQRCode(){
        if (orderId!=null) {
            WindowManager manager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int smallerDimension = width < height ? width : height;
            smallerDimension = smallerDimension * 3 / 4;

            qrgEncoder = new QRGEncoder(
                    orderId, null,
                    QRGContents.Type.TEXT,
                    smallerDimension);
            try {
                bitmap = qrgEncoder.encodeAsBitmap();
                orderIdQRCodeImageView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                Log.v(TAG, e.toString());
            }
        }

//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean save;
//                String result;
//                try {
//                    save = QRGSaver.save(savePath,orderId, bitmap, QRGContents.ImageType.IMAGE_JPEG);
//                    result = save ? "Image Saved" : "Image Not Saved";
//                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }
    
}
