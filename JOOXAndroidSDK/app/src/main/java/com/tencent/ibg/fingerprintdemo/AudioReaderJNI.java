package com.tencent.ibg.fingerprintdemo;

public class AudioReaderJNI {
    static {
        System.loadLibrary("audio_reader");
        System.loadLibrary("c++_shared");
    }

    public static native byte[] getTestBytes(String path);
}