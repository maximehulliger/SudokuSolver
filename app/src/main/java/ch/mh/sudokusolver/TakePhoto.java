package ch.mh.sudokusolver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.io.IOException;

public class TakePhoto extends Activity {

    @Override
    protected void onCreate(Bundle instance) {
        super.onCreate(instance);

        try {
            final Camera  c = Camera.open(); // attempt to get a Camera instance

            // cache height from device metrics & status bar height
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int statusBarHeight = getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android"));
            int cacheHeight = (metrics.heightPixels - metrics.widthPixels - statusBarHeight) / 2;

            RelativeLayout rl = new RelativeLayout(this);
            // cam preview
            rl.addView(new Preview(this, c));
            // upper and down caches
            SurfaceView vUp = new SurfaceView(this);
            SurfaceView vDown = new SurfaceView(this);
            vUp.setBackgroundColor(Color.BLACK);
            vDown.setBackgroundColor(Color.BLACK);
            RelativeLayout.LayoutParams linLayoutParamUp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, cacheHeight);
            rl.addView(vUp, linLayoutParamUp);
            RelativeLayout.LayoutParams linLayoutParamDown = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, cacheHeight);
            linLayoutParamDown.topMargin = cacheHeight + metrics.widthPixels;
            rl.addView(vDown, linLayoutParamDown);

            // takePhotoButton
            Button takePhotoButton = new Button(this);
            takePhotoButton.setText("Take Sudoku");
            takePhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    c.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            Intent output = new Intent();
                            output.putExtra("picture", data);
                            setResult(RESULT_OK, output);
                            finish();
                        }
                    });
                }
            });
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            //params.bottomMargin = 100;
            takePhotoButton.setLayoutParams(params);
            rl.addView(takePhotoButton);

            setContentView(rl);

        } catch (Exception e) {
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

            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            this.camera = camera;
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                camera.setPreviewDisplay(mHolder);
                camera.startPreview();
            } catch (IOException e) {
                Log.e("surfaceCreated", "Error setting camera preview: " + e.getMessage());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            camera.stopPreview();
            camera.release();
        }
    }
}
