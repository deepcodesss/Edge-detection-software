package com.example.openglcamera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import java.nio.ByteBuffer;
import android.hardware.camera2.*;
import androidx.annotation.NonNull;

public class Camera2Helper {
    public interface FrameListener { void onFrame(byte[] nv21, int width, int height); }

    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSession;
    private final Context ctx;
    private final FrameListener listener;
    private ImageReader imageReader;
    private Handler backgroundHandler;
    private HandlerThread backgroundThread;

    public Camera2Helper(Context ctx, FrameListener listener){
        this.ctx = ctx; this.listener = listener;
    }

    public void start(){
        startBackgroundThread();
        CameraManager manager = (CameraManager) ctx.getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = manager.getCameraIdList()[0];
            CameraCharacteristics chars = manager.getCameraCharacteristics(cameraId);
            android.util.Size[] sizes = chars.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                    .getOutputSizes(ImageFormat.YUV_420_888);
            android.util.Size chosen = sizes[0];
            imageReader = ImageReader.newInstance(chosen.getWidth(), chosen.getHeight(), ImageFormat.YUV_420_888, 2);
            imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override public void onImageAvailable(ImageReader reader) {
                    Image img = null;
                    try {
                        img = reader.acquireLatestImage();
                        if (img == null) return;
                        ByteBuffer yBuf = img.getPlanes()[0].getBuffer();
                        ByteBuffer uBuf = img.getPlanes()[1].getBuffer();
                        ByteBuffer vBuf = img.getPlanes()[2].getBuffer();
                        int ySize = yBuf.remaining();
                        int uSize = uBuf.remaining();
                        int vSize = vBuf.remaining();
                        byte[] nv21 = new byte[ySize + uSize + vSize];
                        yBuf.get(nv21, 0, ySize);
                        byte[] u = new byte[uSize];
                        byte[] v = new byte[vSize];
                        uBuf.get(u); vBuf.get(v);
                        for (int i = 0; i < vSize; i++) {
                            nv21[ySize + i*2] = v[i];
                            nv21[ySize + i*2 + 1] = u[i];
                        }
                        listener.onFrame(nv21, img.getWidth(), img.getHeight());
                    } catch (Exception e){ e.printStackTrace(); }
                    finally { if (img != null) img.close(); }
                }
            }, backgroundHandler);

            manager.openCamera(cameraId, new CameraDevice.StateCallback() {
                @Override public void onOpened(@NonNull CameraDevice camera) {
                    cameraDevice = camera;
                    try {
                        android.view.Surface surface = imageReader.getSurface();
                        final CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        builder.addTarget(surface);
                        cameraDevice.createCaptureSession(java.util.Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                            @Override public void onConfigured(@NonNull CameraCaptureSession session) {
                                captureSession = session;
                                try { captureSession.setRepeatingRequest(builder.build(), null, backgroundHandler); } catch (CameraAccessException e){ e.printStackTrace(); }
                            }
                            @Override public void onConfigureFailed(@NonNull CameraCaptureSession session) {}
                        }, backgroundHandler);
                    } catch (CameraAccessException e){ e.printStackTrace(); }
                }
                @Override public void onDisconnected(@NonNull CameraDevice camera) { camera.close(); cameraDevice = null; }
                @Override public void onError(@NonNull CameraDevice camera, int error) { camera.close(); cameraDevice = null; }
            }, backgroundHandler);

        } catch (Exception e){ e.printStackTrace(); }
    }

    public void stop(){ try { if (captureSession != null) captureSession.close(); if (cameraDevice != null) cameraDevice.close(); if (imageReader != null) imageReader.close(); stopBackgroundThread(); } catch (Exception e){ e.printStackTrace(); } }

    private void startBackgroundThread(){ backgroundThread = new HandlerThread("CamBg"); backgroundThread.start(); backgroundHandler = new Handler(backgroundThread.getLooper()); }
    private void stopBackgroundThread(){ if (backgroundThread != null) { backgroundThread.quitSafely(); try { backgroundThread.join(); backgroundThread = null; backgroundHandler = null; } catch (InterruptedException ignored) {} } }
}
