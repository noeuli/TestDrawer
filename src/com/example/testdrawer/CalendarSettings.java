package com.example.testdrawer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class CalendarSettings extends Activity {
    private static final String TAG = "CalendarSettings";

    PlaceholderFragment mFragmentBody;
    private ImageView mImageBefore;
    private ImageView mImageAfter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_settings);

        if (savedInstanceState == null) {
            mFragmentBody = new PlaceholderFragment(); 
            getFragmentManager().beginTransaction()
                    .add(R.id.container, mFragmentBody).commit();
        }

        checkBodyInitialized();
    }
    
    private void checkBodyInitialized() {
        Log.d(TAG, "checkBodyInitialized()");
        
        if (mFragmentBody != null) {
            if(mFragmentBody.getRootView() != null) {
                setupImageViews();
            } else {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        checkBodyInitialized();
                    }
                });
            }
        }
    }
    
    private void setupImageViews() {
        mImageBefore = (ImageView) mFragmentBody.getViewById(R.id.image_input);
        mImageAfter = (ImageView) mFragmentBody.getViewById(R.id.image_output);

        if (mImageBefore != null) {
            Log.d(TAG, "setupImageViews(): ImageView before is not NULL!");
        } else {
            Log.e(TAG, "setupImageViews(): Error: ImageView before is NULL!");
        }

        if (mImageAfter != null) {
            Log.d(TAG, "setupImageViews(): ImageView after is not NULL!");
        } else {
            Log.e(TAG, "setupImageViews(): Error: ImageView after is NULL!");
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String TAG = "PlaceholderFragment";
        
        private View mRootView;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            mRootView = inflater.inflate(
                    R.layout.fragment_calendar_settings, container, false);
            Log.i(TAG, "onCreateView()");
            return mRootView;
        }
        
        public View getViewById(int resId) {
            if (mRootView != null) {
                return mRootView.findViewById(resId);
            } else {
                return null;
            }
        }
        
        public View getRootView() {
            return mRootView;
        }
    }
    
    public void onClick(View v) {
        int id = v.getId();
        Log.d(TAG, "onClick() id=" + id);
        
        if (id == R.id.ok) {
            test();
            if (mPackageList != null && mPackageList.size() > 0) {
                String pkgName = mPackageList.get(0);
                /*
                int resId = getWallpaperResid(pkgName);
                Uri imageUri = Uri.parse("android.resource://" + pkgName + "/" + resId);
                Uri imageUri = getWallpaperUri(pkgName);
                */
                File imageFile = getWallpaper(pkgName);
                Uri imageUri = getImageContentUri(getBaseContext(), imageFile);
//                Uri imageUri = Uri.fromFile(imageFile);
                
                requestCrop(imageUri);
//                viewImage(imageUri);
            } else {
                
            }
        } else if (id == R.id.cancel){
            test2();
        }
    }
    
    private PackageManager mPackageManager;
/*
    private Uri getWallpaperUri2(File file) {
        return Uri.fromFile(file);
    }
    private Uri getWallpaperUri (String packageName) {
        Uri imageUri = null;
        
        if (mPackageManager == null) {
            mPackageManager = getPackageManager();
        }
        
        try {
            Resources res = mPackageManager.getResourcesForApplication(packageName);
            int resId = res.getIdentifier("wallpaper",  "drawable",  packageName);
            Bitmap bmp = BitmapFactory.decodeResource(res, resId);
            String filename = "/sdcard/temp.png";
            FileOutputStream out = null;
            try {
               out = new FileOutputStream(filename);
               bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
               try{
                   if (out != null) out.close();
               } catch(Throwable ignore) {}
            }
            File f = new File(filename);
            String fullPath = f.toString();
            Log.d(TAG, "path=" + fullPath);
            imageUri = Uri.parse(f.toString());
        } catch (Exception e) {
            
        }
        
        return imageUri;
    }
*/
    
    private File getWallpaper(String packageName) {
        File file = null;
        
        if (mPackageManager == null) {
            mPackageManager = getPackageManager();
        }
        
        try {
            Resources res = mPackageManager.getResourcesForApplication(packageName);
            int resId = res.getIdentifier("wallpaper",  "drawable",  packageName);
            file = resToFile(res, resId, "temp.png");
        } catch (Exception e) {
            
        }
        
        return file;
    }
    
    private File resToFile(Resources res, int resourceID, String filename) {
        File file = getApplicationContext().getFileStreamPath(filename);
        if(file.exists()) {
            return file;
        }

        InputStream is;
        FileOutputStream fos;
        try {
            is = res.openRawResource(resourceID);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            fos = openFileOutput(filename, MODE_PRIVATE);
            fos.write(buffer);
            fos.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    
    public Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
    /*
    private int getWallpaperResid(String packageName) {
        int resId = -1;
        
        if (mPackageManager == null) {
            mPackageManager = getPackageManager();
        }
        
        try {
            Resources res = mPackageManager.getResourcesForApplication(packageName);
            resId = res.getIdentifier("wallpaper",  "drawable",  packageName);
        } catch (Exception e) {
            
        }
        
        return resId;
    }
    
    private Uri getWallpaperDrawable(String packageName) {
        Uri picUri = null;
        
        if (mPackageManager == null) {
            mPackageManager = getPackageManager();
        }
        
        try {
            Resources res = mPackageManager.getResourcesForApplication(packageName);
            int resId = res.getIdentifier("wallpaper",  "drawable",  packageName);
            picUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" 
                        + res.getResourcePackageName(resId) + '/' 
                        + res.getResourceTypeName(resId) + '/' 
                        + res.getResourceEntryName(resId) );
        } catch (Exception e) {
            
        }
        
        return picUri;
    }
    */
    
    private final String ICON_STYLE_URI = "content://com.pantech.app.iconstyleagent.IconStyleProvider/iconstyles";
    private ArrayList<String> mPackageList;
    
    private void test() {
        ContentResolver cr = getContentResolver();
        Uri contentUri = Uri.parse(ICON_STYLE_URI);
        
        if (mPackageList==null) {
            mPackageList = new ArrayList<String>();
        }
        mPackageList.clear();
        
        int index = 0;
        try {
            Cursor c = cr.query(contentUri, null, null, null, null);
            if (c==null) return;
            while (c.moveToNext()) {
                int field_package = c.getColumnIndexOrThrow("package");
                String packageName = c.getString(field_package);
                mPackageList.add(packageName);
                Log.d(TAG, "test() [" + index + "] " + packageName);
                index++;
            }
        } catch (Exception e) {
            
        }
    }
    
    private void test2() {
        try {
            //use standard intent to capture an image
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //we will handle the returned data in onActivityResult
            startActivityForResult(captureIntent, CAMERA_CAPTURE);
        } catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support capturing images!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    
    private void viewImage(Uri imageUri) {
        Log.d(TAG, "viewImage(" + imageUri + ")");
        
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(imageUri);
            startActivity(intent);
        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Whoops - your device doesn't support to view image action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
            Log.e(TAG, "requestCrop()", anfe);
        }
    }
    
    private void requestCrop(Uri imageUri) {
        Log.d(TAG, "requestCrop(" + imageUri + ")");
        
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(imageUri,  "image/*");
            cropIntent.putExtra("crop",  "true");
            cropIntent.putExtra("aspectX",  1);
            cropIntent.putExtra("aspectY",  1);
            cropIntent.putExtra("outputX",  200);
            cropIntent.putExtra("outputY",  200);
            cropIntent.putExtra("return-data",  true);
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
            Log.e(TAG, "requestCrop()", anfe);
        }
        
    }
    
    private final int PIC_CROP = 1;
    private final int CAMERA_CAPTURE = 2;
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult(" + requestCode + ", " + resultCode + ", " + data + ")");
        
        if (requestCode == CAMERA_CAPTURE) {
            if (resultCode == RESULT_OK && data != null) {
                //get the Uri for the captured image
                Uri picUri = data.getData();
//                requestCrop(picUri);
                viewImage(picUri);
            }
        } else
        if (requestCode == PIC_CROP) {
            if (resultCode == RESULT_CANCELED) {
                Log.w(TAG, "onActivityResult(): PIC_CROP - Result Canceled!");
            } else if (resultCode == RESULT_OK) {
                //get the returned data
                Bundle extras = data.getExtras();
                if (extras != null) {
                    //get the cropped bitmap
                    Bitmap thePic = extras.getParcelable("data");
                    //display the returned cropped image
                    if (mImageAfter != null) mImageAfter.setImageBitmap(thePic);
                } else {
                    Log.e(TAG, "onActivityResult(): extras NULL!");
                }
            } else {
                Log.w(TAG, "onActivityResult(): PIC_CROP - result " + resultCode);
            }
        }
    }
    
    

}
