package ch.mh.sudokusolver;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.IOException;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);

        try {
            Camera  c = Camera.open(); // attempt to get a Camera instance

            RelativeLayout rl = new RelativeLayout(this);
            
            rl.addView(new Preview(this, c));

            setContentView(rl);
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.e("start", "error while opening camera and preview:");
            e.printStackTrace();
        }


    }

    private class Preview extends SurfaceView implements SurfaceHolder.Callback {

        SurfaceHolder mHolder;

        Camera camera;

        Preview(Context context, Camera camera) {
            super(context);


            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            //SurfaceView s = new SurfaceView(context);

            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            //addView(s);

            this.camera = camera;

            try {
                camera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Important: Call startPreview() to start updating the preview
            // surface. Preview must be started before you can take a picture.
            camera.startPreview();
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                Log.e("surfaceCreated", ""+holder+" "+mHolder);
                camera.setPreviewDisplay(mHolder);
                camera.startPreview();
            } catch (IOException e) {
                Log.e("surfaceCreated", "Error setting camera preview: " + e.getMessage());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            if (holder.getSurface() == null){
                // preview surface does not exist
                return;
            }
/*
            // stop preview before making changes
            try {
                camera.stopPreview();
            } catch (Exception e){
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                camera.setPreviewDisplay(mHolder);
                camera.startPreview();

            } catch (Exception e){
                Log.e("restart preview", "Error starting camera preview: " + e.getMessage());
            }*/
            // Now that the size is known, set up the camera parameters and begin
            // the preview.
            /*Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(localSizes.get(currentSize).width, localSizes.get(currentSize).height);
            requestLayout();
            camera.setParameters(parameters);

            // Important: Call startPreview() to start updating the preview surface.
            // Preview must be started before you can take a picture.
            camera.startPreview();*/
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.e("surfaceDestroyed", "surfaceDestroyed");
            // Surface will be destroyed when we return, so stop the preview.
            stopPreviewAndFreeCamera();
        }

        /**
         * When this function returns, mCamera will be null.
         */
        private void stopPreviewAndFreeCamera() {

            if (camera != null) {
                // Call stopPreview() to stop updating the preview surface.
                camera.stopPreview();

                // Important: Call release() to release the camera for use by other
                // applications. Applications should release the camera immediately
                // during onPause() and re-open() it during onResume()).
                camera.release();

                camera = null;
            }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {

        }
    }
}
