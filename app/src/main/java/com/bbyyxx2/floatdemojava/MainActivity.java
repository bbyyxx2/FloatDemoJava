package com.bbyyxx2.floatdemojava;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnInvokeView;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //registerForActivityResult方法必须在Activity的onCreate方法或者onStart方法之前调用
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri imageUri = data.getData();
                // 你可以在这里处理用户选择的图片
                openFloatView(imageUri);
            }
        });

        Button button = findViewById(R.id.bt);
        Button button2 = findViewById(R.id.bt2);
        TextView tv = findViewById(R.id.tv);

        tv.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            startActivity(intent);
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, 1);
                } else {
                    toChoosePhoto();
                }

            }
        });

        button2.setOnClickListener(v -> {
            if (EasyFloat.getFloatView("test") != null && EasyFloat.getFloatView("test").isShown()){
                EasyFloat.dismiss("test");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1){
            if ((grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                toChoosePhoto();
            } else {
                Toast.makeText(MainActivity.this, "权限被拒绝", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void toChoosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
    }

    private void openFloatView(Uri uri){
        EasyFloat.with(MainActivity.this)
                .setLayout(R.layout.float_layout, new OnInvokeView() {
                    @Override
                    public void invoke(View view) {
                        ImageView iv = view.findViewById(R.id.iv);
                        if (uri != null){
                            iv.setImageURI(uri);
                        }
                    }
                })
                .setShowPattern(ShowPattern.CURRENT_ACTIVITY)
                .setSidePattern(SidePattern.DEFAULT)
                .setDragEnable(true)
                .setTag("test")
                .show();
    }
}