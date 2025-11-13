package com.example.openglcamera;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GLSurface extends GLSurfaceView {
    private GLRenderer renderer;

    public GLSurface(Context context, AttributeSet attrs){
        super(context, attrs);
        setEGLContextClientVersion(2);
        renderer = new GLRenderer();
        setRenderer(renderer);
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }

    public void updateFrame(byte[] nv21, int width, int height){
        renderer.updateFrame(nv21, width, height);
        requestRender();
    }

    public void setShowEdges(boolean show){ renderer.setShowEdges(show); requestRender(); }
}
