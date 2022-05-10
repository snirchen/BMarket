package com.example.bmarket02;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class BooksCamera extends AppCompatActivity implements Camera.PictureCallback {

    public static Bitmap[] images = new Bitmap[3];
    public static int current_image_index = 0;
    private static final int IMAGE_HEIGHT = 724;
    private static final int IMAGE_WIDTH = 1024;
    private static final float IMAGE_ORIENTATION = 90;
    public static Boolean can_take_picture = true;
    Camera camera;
    FrameLayout camera_preview;
    ShowBooksCamera showBooksCamera;
    ImageButton back_btn;
    ImageView take_picture_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_books_camera);
        camera_preview = findViewById(R.id.camera_preview);
        back_btn = findViewById(R.id.back_btn);
        take_picture_btn = findViewById(R.id.take_picture_btn);
        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        parameters.set("jpeg-quality", 100);
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();

        Camera.Size msize = null;
        for (Camera.Size size : sizes) {
            msize = size;
        }

        int i = 0;
        for (Camera.Size size : sizes) {
            if (size.height >= IMAGE_HEIGHT && size.width >= IMAGE_WIDTH) {
                msize = size;
                //break;
                i++;
            }
        }
        //Toast.makeText(BooksCamera.this, "i: " + i, Toast.LENGTH_SHORT).show();


        parameters.setPictureSize(msize.width, msize.height);
        camera.setParameters(parameters);
        showBooksCamera = new ShowBooksCamera(this, camera);
        camera_preview.addView(showBooksCamera);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        take_picture_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(can_take_picture)
                {
                    can_take_picture = false;
                    camera.takePicture(null, null, mPicture);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            can_take_picture = true;
                        }
                    }, 3000);
                }
            }
        });
    }

    public void onPictureTaken(byte[] bytes, Camera camera) {
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        camera.stopPreview();
        camera.release();
        camera_preview.removeAllViews();
        camera = Camera.open();
        showBooksCamera = new ShowBooksCamera(this, camera);
        camera_preview.addView(showBooksCamera);
    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // Determine the width/height of the image
            int width = camera.getParameters().getPictureSize().width;
            int height = camera.getParameters().getPictureSize().height;

            // Load the bitmap from the byte array
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);

            // Rotate and crop the image into A4 ratio
            bitmap = RotateBitmap(bitmap, IMAGE_ORIENTATION);
            bitmap = Bitmap.createBitmap(bitmap, (int) Math.round(0.13 * bitmap.getWidth()), 0, (int) Math.round(0.74 * bitmap.getWidth()), (int) Math.round(0.785 * bitmap.getHeight())); // CHANGE IN THE FUTURE
            bitmap = ScaleBitmap(bitmap, (float) IMAGE_WIDTH, true);
            images[current_image_index] = bitmap;
            Intent it = new Intent(BooksCamera.this, ConfirmBookPictureActivity.class);
            startActivity(it);
        }
    };

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static Bitmap ScaleBitmap(Bitmap realImage, float maxImageSize, boolean filter) {
        float ratio = Math.min((float) maxImageSize / realImage.getWidth(), (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }
}
