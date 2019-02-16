package com.example.bluepea.ringingflashlight;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.lang.reflect.Parameter;

public class FlashLight {
    private static Camera camera;
    public static Camera.Parameters params;
    public static String mCameraId;
    public static CameraManager mCameraManager;



    public static void init(Context context){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mCameraManager= (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            
            try {
                mCameraId = mCameraManager.getCameraIdList()[0];
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        else{
            camera = Camera.open();
            params = camera.getParameters();
        }
    }

    public static void onTorch() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
                mCameraManager.setTorchMode(mCameraId, true);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        else{
            onTorchCam();
        }
    }

    public static void offTorch() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            try {
                mCameraManager.setTorchMode(mCameraId, false);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
        else{
            camera.stopPreview();
        }
    }

    private static void onTorchCam(){
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();

    }
    static class ThreadBlink extends Thread{
        private long delayms;
        public boolean isRunning;
        private int times;

        @Override
        public void start() {
            super.start();
            isRunning =true;
        }

        public ThreadBlink(long delayms, int times) {
            this.delayms = delayms;
            this.times = times;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                mCameraManager= (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
//                try {
//                    mCameraId = mCameraManager.getCameraIdList()[0];
//                } catch (CameraAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//            else{
//                camera = Camera.open();
//                params = camera.getParameters();
//            }
        }

        @Override
        public void run() {
            super.run();
            Log.d("time "+times,"myservice");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                try {
                    if (times == 0)
                        while (true) {
                            FlashLight.mCameraManager.setTorchMode(FlashLight.mCameraId, true);
                            Thread.sleep(delayms);
                            FlashLight.mCameraManager.setTorchMode(FlashLight.mCameraId, false);
                            Thread.sleep(delayms);
                        }
                    else
                        for (int time = 0; time < times; time++) {
                            FlashLight.mCameraManager.setTorchMode(FlashLight.mCameraId, true);
                            Thread.sleep(delayms);
                            FlashLight.mCameraManager.setTorchMode(FlashLight.mCameraId, false);
                            Thread.sleep(delayms);
                        }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (times == 0)
                    while (true)
                        try {
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            camera.setParameters(params);
                            camera.startPreview();
                            Thread.sleep(delayms);
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            camera.setParameters(params);
                            camera.stopPreview();
                            Thread.sleep(delayms);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                else
                    for (int time = 0; time < times; time++) {
                        try {
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            camera.setParameters(params);
                            camera.startPreview();
                            Thread.sleep(delayms);
                            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            camera.setParameters(params);
                            camera.stopPreview();
                            Thread.sleep(delayms);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            }
            Log.d("end loop","myservice");
        }


        @Override
        public void interrupt() {
            super.interrupt();
            Log.d("function interupt","myservice");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                try {
                    FlashLight.mCameraManager.setTorchMode(FlashLight.mCameraId, false);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }
            else
                camera.stopPreview();
            isRunning =false;
        }

        public int getTimes() {
            return times;
        }
    }
}
