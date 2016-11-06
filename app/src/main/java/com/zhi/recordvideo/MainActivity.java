package com.zhi.recordvideo;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener {

    private SurfaceHolder surfaceHolder;
    private MediaRecorder mediaRecorder;

    private SurfaceView mSurfaceView;
    private RelativeLayout mRlRecord;
    private Button mBtnRecord;
    private Button mBtnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initEvents();

        surfaceHolder = mSurfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setFixedSize(176, 144);
    }

    private void initEvents() {
        mBtnRecord.setOnClickListener(this);
        mBtnStop.setOnClickListener(this);
    }

    private void initViews() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mRlRecord = (RelativeLayout) findViewById(R.id.rl_record);
        mBtnRecord = (Button) findViewById(R.id.btn_record);
        mBtnStop = (Button) findViewById(R.id.btn_stop);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_record:

                record();
                mRlRecord.setEnabled(false);
                mBtnStop.setEnabled(true);
                break;
            case R.id.btn_stop:
                if(null != mediaRecorder){
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder = null;
                }
                mRlRecord.setEnabled(true);
                mBtnStop.setEnabled(false);
                break;
        }
    }

    private void record() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".3gp");
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            if (null != file) {
                mediaRecorder.setOutputFile(file.getAbsolutePath());
            }
            mediaRecorder.setVideoSize(480, 320);
            mediaRecorder.setVideoFrameRate(8);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
            mediaRecorder.prepare();
            mediaRecorder.start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mRlRecord.setVisibility(View.VISIBLE);
                return true;
        }
        return super.onTouchEvent(event);
    }
}