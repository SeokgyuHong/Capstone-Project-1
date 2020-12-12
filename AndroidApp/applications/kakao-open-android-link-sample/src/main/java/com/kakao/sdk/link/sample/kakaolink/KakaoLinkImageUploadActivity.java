package com.kakao.sdk.link.sample.kakaolink;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.network.storage.ImageUploadResponse;
import com.kakao.sdk.link.sample.R;
import com.kakao.util.helper.MediaUtils;
import com.kakao.util.helper.log.Logger;

import java.io.File;
import java.io.FileNotFoundException;

public class KakaoLinkImageUploadActivity extends Activity {
    private static final int GALLERY_REQUEST_CODE = 0;
    private String imageUrl;
    private TextView urlTextView;
    private ImageView selectedImageView;

    private String[] STORAGE_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private final int REQUEST_EXTERNAL_STORAGE = 1;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_link_upload);

        final String[] methods = new String[]{
                getString(R.string.title_image_upload),
        };

        ListView listView = findViewById(R.id.link40_method_list);
        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return methods.length;
            }

            @Override
            public Object getItem(int position) {
                return methods[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater mInflater = (LayoutInflater) KakaoLinkImageUploadActivity.this
                        .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.menu_item, null);
                }

                ImageView imageView = convertView.findViewById(R.id.method_image);
                TextView textView = convertView.findViewById(R.id.method_text);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_image));
                textView.setText(methods[position]);
                return convertView;
            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                uploadImageForLink();
            }
        });

        urlTextView = findViewById(R.id.tv_image_url);
        selectedImageView = findViewById(R.id.selected_image);
    }

    @SuppressWarnings("unused")
    private void uploadImageAfterScrap() {
        KakaoLinkService.getInstance().scrapImage(this, false, "http://www.kakaocorp.com/images/logo/og_daumkakao_151001.png", new ResponseCallback<ImageUploadResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(ImageUploadResponse result) {
                Logger.e(result.getOriginal().getUrl());
            }
        });
    }

    private void uploadImageForLink() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void uploadImage() {
        if (ActivityCompat.checkSelfPermission(KakaoLinkImageUploadActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, REQUEST_EXTERNAL_STORAGE);
            return;
        }

        try {
            File imageFile = new File(MediaUtils.getImageFilePathFromUri(uri, this));

            KakaoLinkService.getInstance().uploadImage(this, false, imageFile, new ResponseCallback<ImageUploadResponse>() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Logger.e(errorResult.toString());
                    Toast.makeText(getApplicationContext(), errorResult.toString(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(ImageUploadResponse result) {
                    imageUrl = result.getOriginal().getUrl();
                    urlTextView.setText(imageUrl);
                    Toast.makeText(KakaoLinkImageUploadActivity.this, "Successfully uploaded image at " + imageUrl, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (FileNotFoundException e) {
            Logger.e(e.toString());
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            selectedImageView.setImageURI(uri);
            this.uri = uri;
            uploadImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadImage();
                } else {
                    Toast.makeText(getApplicationContext(), "User did not agree to give storage permission.", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}
