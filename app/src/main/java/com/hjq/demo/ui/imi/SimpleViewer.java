package com.hjq.demo.ui.imi;

import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.ImiFrameMode;
import com.hjimi.api.iminect.Utils;

import java.nio.ByteBuffer;


public class SimpleViewer extends Thread {

    private boolean mShouldRun = false;

    private ImiDevice.ImiStreamType mStreamType;
    private GLPanel mGLPanel;
    private DecodePanel mDecodePanel;
    private ImiDevice mDevice;
    private ImiFrameMode mCurrentMode;

    public SimpleViewer(ImiDevice device, ImiDevice.ImiStreamType streamType) {
        mDevice = device;
        mStreamType = streamType;
    }

    public void setGLPanel(GLPanel GLPanel) {
        this.mGLPanel = GLPanel;
    }

    public void setDecodePanel(DecodePanel decodePanel) {
        this.mDecodePanel = decodePanel;
    }

    @Override
    public void run() {
        super.run();
        //get current framemode.
        mCurrentMode = mDevice.getCurrentFrameMode(mStreamType);

        //start read frame.
        while (mShouldRun) {
            ImiDevice.ImiFrame nextFrame = mDevice.readNextFrame(mStreamType, 25);

            //frame maybe null, if null, continue.
            if(nextFrame == null){
                continue;
            }

            switch (mStreamType)
            {
                case COLOR:
                    //draw color.
                    drawColor(nextFrame);
                    break;
                case DEPTH:
                    //draw depth.
                    drawDepth(nextFrame);
                    break;
            }
        }
    }

    private void drawDepth(ImiDevice.ImiFrame nextFrame) {
        ByteBuffer frameData = nextFrame.getData();
        int width = nextFrame.getWidth();
        int height = nextFrame.getHeight();

        frameData = Utils.depth2RGB888(nextFrame, true, false);

        mGLPanel.paint(null, frameData, width, height);
    }

    private void drawColor(ImiDevice.ImiFrame nextFrame) {
        ByteBuffer frameData = nextFrame.getData();
        int width = nextFrame.getWidth();
        int height = nextFrame.getHeight();

        //draw color image.
        switch (mCurrentMode.getFormat())
        {
            case IMI_PIXEL_FORMAT_IMAGE_H264:
                if(mDecodePanel != null){
                    mDecodePanel.paint(frameData, nextFrame.getTimeStamp());
                }
                break;
            case IMI_PIXEL_FORMAT_IMAGE_YUV420SP:
                frameData = Utils.yuv420sp2RGB(nextFrame);
                if(mGLPanel != null){
                    mGLPanel.paint(null, frameData, width, height);
                }
                break;
            case IMI_PIXEL_FORMAT_IMAGE_RGB24:
                if(mGLPanel != null){
                    mGLPanel.paint(null, frameData, width, height);
                }
                break;
            default:
                break;
        }
    }

    public void onPause(){
        if(mGLPanel != null){
            mGLPanel.onPause();
        }
    }

    public void onResume(){
        if(mGLPanel != null){
            mGLPanel.onResume();
        }
    }

    public void onStart(){
        if(!mShouldRun){
            mShouldRun = true;

            //start read thread
            this.start();
        }
    }

    public void onDestroy(){
        mShouldRun = false;
    }
}
