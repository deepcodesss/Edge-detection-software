package com.example.openglcamera;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {
    static { System.loadLibrary("native-lib"); }

    private static final int REQ_CAMERA = 101;
    private Camera2Helper cameraHelper;
    private GLSurface cameraGLSurface;
    private Button toggleBtn;
    private boolean showEdges = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraGLSurface = findViewById(R.id.glSurface);
        toggleBtn = findViewById(R.id.btn_toggle);

        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                showEdges = !showEdges;
                cameraGLSurface.setShowEdges(showEdges);
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQ_CAMERA);
        } else {
            initCamera();
        }
    }

    private void initCamera(){
        cameraHelper = new Camera2Helper(this, new Camera2Helper.FrameListener() {
            @Override
            public void onFrame(byte[] nv21, int width, int height) {
                cameraGLSurface.updateFrame(nv21, width, height);
            }
        });
        cameraHelper.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraHelper != null) cameraHelper.stop();
    }

    public native byte[] processFrameNative(byte[] data, int width, int height, boolean edges);
}
