package com.example.openglcamera;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

public class GLRenderer implements GLSurfaceView.Renderer {
    static { System.loadLibrary("native-lib"); }
    private int textureId = -1;
    private ByteBuffer lastFrame;
    private int frameW = 0, frameH = 0;
    private boolean showEdges = true;

    private FloatBuffer vertexBuffer;
    private FloatBuffer texBuffer;
    private final float[] vertices = { -1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f };
    private final float[] texCoords = { 0f,0f, 0f,1f, 1f,0f, 1f,1f };

    public GLRenderer(){
        vertexBuffer = ByteBuffer.allocateDirect(vertices.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexBuffer.put(vertices).position(0);
        texBuffer = ByteBuffer.allocateDirect(texCoords.length*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        texBuffer.put(texCoords).position(0);
    }

    public native byte[] processFrameNative(byte[] nv21, int width, int height, boolean edges);

    public void updateFrame(byte[] nv21, int width, int height){
        byte[] processed = processFrameNative(nv21, width, height, showEdges);
        if (processed == null) return;
        frameW = width; frameH = height;
        lastFrame = ByteBuffer.allocateDirect(processed.length);
        lastFrame.put(processed).position(0);
    }

    public void setShowEdges(boolean show){ this.showEdges = show; }

    @Override public void onSurfaceCreated(GL10 gl, EGLConfig config) { textureId = TextureUtils.createTexture(); GLES20.glClearColor(0f,0f,0f,1f); }
    @Override public void onSurfaceChanged(GL10 gl, int width, int height) { GLES20.glViewport(0,0,width,height); }
    @Override public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        if (lastFrame == null || frameW==0) return;
        lastFrame.position(0);
        TextureUtils.updateTexture(textureId, frameW, frameH, lastFrame);
        TextureUtils.drawTexture(vertexBuffer, texBuffer, textureId);
    }
}
