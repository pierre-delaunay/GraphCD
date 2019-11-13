package fr.istic.mob.graphcd;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import java.io.FileInputStream;

/**
 * Zoom Activity - with PhotoView library (https://github.com/chrisbanes/PhotoView)
 * @version 1.0.1
 * @author Charly C, Pierre D
 */
public class ZoomActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        PhotoView zoomView = findViewById(R.id.photo_view);

        if(getIntent().hasExtra("image")) {
            Bitmap bitmap = null;
            String filename = getIntent().getStringExtra("image");
            try {
                FileInputStream is = this.openFileInput(filename);
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
                zoomView.setImageBitmap(bitmap);
                this.setTitle(getResources().getString(R.string.app_name) + " - " + getResources().getString(R.string.zoom_mode));
                Toast.makeText(this, getResources().getString(R.string.zoom_mode_message), Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
