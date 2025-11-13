package com.example.openglcamera;

import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class TextureUtils {
    private static int program = -1;
    private static int positionHandle, texCoordHandle, samplerHandle;
    private static final String VERT = "attribute vec4 aPosition;attribute vec2 aTexCoord;varying vec2 vTexCoord;void main(){ gl_Position = aPosition; vTexCoord = aTexCoord; }";
    private static final String FRAG = "precision mediump float;varying vec2 vTexCoord;uniform sampler2D uTexture;void main(){ vec4 c = texture2D(uTexture, vTexCoord); gl_FragColor = c; }";

    public static int createTexture(){ int[] tex = new int[1]; GLES20.glGenTextures(1, tex, 0); GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex[0]); GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST); GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST); GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE); GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE); initProgram(); return tex[0]; }
    private static void initProgram(){ if (program != -1) return; int v = loadShader(GLES20.GL_VERTEX_SHADER, VERT); int f = loadShader(GLES20.GL_FRAGMENT_SHADER, FRAG); program = GLES20.glCreateProgram(); GLES20.glAttachShader(program, v); GLES20.glAttachShader(program, f); GLES20.glLinkProgram(program); positionHandle = GLES20.glGetAttribLocation(program, "aPosition"); texCoordHandle = GLES20.glGetAttribLocation(program, "aTexCoord"); samplerHandle = GLES20.glGetUniformLocation(program, "uTexture"); }
    private static int loadShader(int type, String src){ int s = GLES20.glCreateShader(type); GLES20.glShaderSource(s, src); GLES20.glCompileShader(s); return s; }
    public static void updateTexture(int texId, int w, int h, ByteBuffer data){ GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId); GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, w, h, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, data); }
    public static void drawTexture(FloatBuffer vbuf, FloatBuffer tbuf, int texId){ GLES20.glUseProgram(program); vbuf.position(0); GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, vbuf); GLES20.glEnableVertexAttribArray(positionHandle); tbuf.position(0); GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, tbuf); GLES20.glEnableVertexAttribArray(texCoordHandle); GLES20.glActiveTexture(GLES20.GL_TEXTURE0); GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texId); GLES20.glUniform1i(samplerHandle, 0); GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4); GLES20.glDisableVertexAttribArray(positionHandle); GLES20.glDisableVertexAttribArray(texCoordHandle); }
}
