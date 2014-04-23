package com.energysistem.energylauncher.tvboxlauncher.ui;

/**
 * Created by emg on 22/04/2014.
 */

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.energysistem.energylauncher.tvboxlauncher.R;

import java.io.IOException;

/**
 * Example of loading an image into an image view using the image picker.
 *
 * Created by Rex St. John (on behalf of AirPair.com) on 3/4/14.
 */
public class ImagePickerActivity extends Activity implements Button.OnClickListener {

    // Code for our image picker select action.
    private static final int IMAGE_PICKER_SELECT = 999;
    public static final String ARG_SECTION_NUMBER = "ARG_SECTION_NUMBER";

    // Reference to our image view we will use
    private ImageView mSelectedImage;

    // Reference to picker button.
    private Button mPickPhotoButton;
    private Button mCancelButton;
    private Button mGuardarButton;

    private Bitmap selectedBitmapTemp;

    /**
     * Default empty constructor.
     */
    public ImagePickerActivity(){
        super();
    }

//    /**
//     * Static factory method
//     * @param sectionNumber
//     * @return
//     */
//    public static ImagePickerActivity newInstance(int sectionNumber) {
//        ImagePickerActivity activity = new ImagePickerActivity();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        activity.setArguments(args);
//        return activity;
//    }

//    /**
//     * OnCreateView fragment override
//     * @param inflater
//     * @param container
//     * @param savedInstanceState
//     * @return
//     */
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view =  null;
//        view = inflater.inflate(R.layout.activity_photo_picker, container, false);
//
//        // Set the image view
//        mSelectedImage = (ImageView)view.findViewById(R.id.imageViewFullSized);
//        mPickPhotoButton = (Button)view.findViewById(R.id.button);
//
//        // Set OnItemClickListener so we can be notified on button clicks
//        mPickPhotoButton.setOnClickListener(this);
//
//        return view;
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);


        // Set the image view
        mSelectedImage = (ImageView) findViewById(R.id.imageViewFullSized);
        mPickPhotoButton = (Button) findViewById(R.id.busca_photo_button);
        mGuardarButton = (Button) findViewById(R.id.guarda_foto);
        mCancelButton = (Button) findViewById(R.id.cancel_button);


        // Set OnItemClickListener so we can be notified on button clicks
        mPickPhotoButton.setOnClickListener(this);
        mGuardarButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        mGuardarButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        int idView = view.getId();

        switch (idView){
            case R.id.busca_photo_button:
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, IMAGE_PICKER_SELECT);
                break;
            case R.id.guarda_foto:
                finish();
                WallpaperManager wp = WallpaperManager.getInstance(this);
                try {
                    wp.setBitmap(selectedBitmapTemp);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.cancel_button:
                finish();
                break;
        }
    }


    /**
     * Photo Selection result
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICKER_SELECT  && resultCode == Activity.RESULT_OK) {

            setFullImageFromFilePath(getBitmapPath(data, this));

//            Bitmap bitmap = getBitmapFromCameraData(data, this);
//            mSelectedImage.setImageBitmap(bitmap);

            mGuardarButton.setVisibility(View.VISIBLE);
        }
    }


    /**
     * Scale the photo down and fit it to our image views.
     *
     * "Drastically increases performance" to set images using this technique.
     * Read more:http://developer.android.com/training/camera/photobasics.html
     */
    private void setFullImageFromFilePath(String imagePath) {
        // Get the dimensions of the View
        int targetW = mSelectedImage.getWidth();
        int targetH = mSelectedImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        selectedBitmapTemp = BitmapFactory.decodeFile(imagePath, bmOptions);
        mSelectedImage.setImageBitmap(selectedBitmapTemp);
    }

    /**
     * Use for decoding camera response data.
     *
     * @param data
     * @param context
     * @return
     */
    public static Bitmap getBitmapFromCameraData(Intent data, Context context){
        String picturePath = getBitmapPath(data, context);
        return BitmapFactory.decodeFile(picturePath);
    }

    public static String getBitmapPath(Intent data, Context context){
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        return cursor.getString(columnIndex);
    }
}



