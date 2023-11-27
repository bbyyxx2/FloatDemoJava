package com.bbyyxx2.floatdemojava;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnInvokeView;

public class SecondActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> resultLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

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

        button.setOnClickListener(v -> {
            toChoosePhoto();
        });

        button2.setOnClickListener(v -> {
            if (EasyFloat.getFloatView("test2") != null && EasyFloat.getFloatView("test2").isShown()){
                EasyFloat.dismiss("test2");
            }
        });
    }

    private void toChoosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncher.launch(intent);
    }

    private void openFloatView(Uri uri){
        EasyFloat.with(SecondActivity.this)
                .setLayout(R.layout.float_layout, new OnInvokeView() {
                    @Override
                    public void invoke(View view) {
                        ImageView iv = view.findViewById(R.id.iv);
                        if (uri != null){
                            iv.setImageURI(uri);
                        }
                    }
                })
//                .setShowPattern(ShowPattern.CURRENT_ACTIVITY)
                .setShowPattern(ShowPattern.FOREGROUND)
                .setSidePattern(SidePattern.DEFAULT)
                .setDragEnable(true)
                .setTag("test2")
                .show();
    }
}
