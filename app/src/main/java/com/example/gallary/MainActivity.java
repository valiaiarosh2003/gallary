package com.example.gallary;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Image> allFiles;
    ArrayList<String> allFilesPaths = new ArrayList<>();

    private void showImages() {
        Uri uriImages = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID };

        Cursor cursor = this.getContentResolver().query(uriImages, projection, null, null, null);

        try {
            int count = 0;
            allFiles = new ArrayList<>();
            if (cursor != null) {
                cursor.moveToFirst(); // Ставит курсор на первый ряд
            } do {
                Image image = new Image();
                image.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
                image.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                count++;
                allFiles.add(image);
            }
            while (cursor.moveToNext() && count < 18);
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();}

        for (int i = 0; i < allFiles.size(); i++) {
            allFilesPaths.add(allFiles.get(i).getPath());
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.gallery);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        MyAdapter adapter = new MyAdapter(getApplicationContext(), allFiles, allFilesPaths);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        } else {showImages();}}

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImages();
            } else {
                Toast.makeText(this, "Разрешения на чтение нет", Toast.LENGTH_SHORT).show();
                finish();
            }}}}