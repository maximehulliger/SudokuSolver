package ch.mh.sudokusolver;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

    ImageView sudokuViewer;
    private final static int requestTakePhoto = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sudokuViewer = (ImageView) findViewById(R.id.sudokuViewer);

        Button takePhotoButton = (Button) findViewById(R.id.takeNewPhotoButton);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TakePhoto.class);
                startActivityForResult(i, requestTakePhoto);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case requestTakePhoto:
                byte[] picture = data.getByteArrayExtra("picture");
                Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                sudokuViewer.setImageBitmap(bitmap);
                break;
        }
    }
}
