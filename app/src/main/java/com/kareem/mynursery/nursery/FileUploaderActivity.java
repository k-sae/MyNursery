package com.kareem.mynursery.nursery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.kareem.mynursery.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.util.ArrayList;

public  abstract class FileUploaderActivity extends AppCompatActivity implements UploadStatusDelegate {

    private  static  final String TAG = FileUploaderActivity.class.getName();
    private final static int RESULT_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filer_uploader);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            Uri filePath = data.getData();
            uponImagePicked(filePath.toString());
        }
    }

    public void uploadMultipart(String fileLocation) {
        try {
                    new MultipartUploadRequest(this, "http://drhanadi.com/mynursery/mynursery.php")
                            // starting from 3.1+, you can also use content:// URI string instead of absolute file
                            .addFileToUpload(fileLocation, "image_file")
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .addParameter("key", "CodeCamp:authKey")
                            .addParameter("nursery_id", "test")
                            .setDelegate(this)
                            .startUpload();
        } catch (Exception exc) {
            Log.e("AndroidUploadService", exc.getMessage(), exc);
        }
    }
    private String getImageName(String respond){
        return respond.substring("{\"apiRespond\":\"".length() - 1, respond.indexOf("\"}"));
    }

    @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {

    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
        Toast.makeText(this, getString(R.string.error_occurred_will_uploading), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
        onUploadComplete(getImageName(serverResponse.getBodyAsString()));
    }

    @Override
    public void onCancelled(Context context, UploadInfo uploadInfo) {

    }
    protected void startImageChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),RESULT_CODE);

    }
    public abstract void onUploadComplete(String imageName);
    public abstract void uponImagePicked(String imageLocation);

}
