package com.example.bmarket02;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class ShowBooksCamera extends SurfaceView implements SurfaceHolder.Callback {

    Camera camera;
    SurfaceHolder surfaceHolder;

    public ShowBooksCamera(Context context, Camera camera) {
        super(context);
        this.camera = camera;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Camera.Parameters parameters = this.camera.getParameters();
        parameters.set("jpeg-quality", 100);
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();

        Camera.Size msize = null;
        for(Camera.Size size : sizes)
        {
            msize = size;
        }

        for(Camera.Size size : sizes)
        {
            if(size.height >= 1024 && size.width >= 724)
            msize = size;
            break;
        }
        parameters.setPictureSize(msize.width, msize.height);

        if(this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
        {
            parameters.set("orientation", "portrait");
            this.camera.setDisplayOrientation(90);
            parameters.setRotation(90);
        }
        else
        {
            parameters.set("orientation", "landscape");
            camera.setDisplayOrientation(0);
            parameters.setRotation(0);
        }

        this.camera.setParameters(parameters);
        try{
            this.camera.setPreviewDisplay(surfaceHolder);
            this.camera.startPreview();
        }catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
