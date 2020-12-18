package com.bigfortune.goldseven;

public class Wheel extends Thread {

    interface WheelListener {
        //void newImage(int img);
        void newImage(int imgTop, int imgCenter, int imgBottom, int index);
    }

    private static int[] drawables = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4};
    public int currentIndex;
    private WheelListener wheelListener;
    private long frameDuration;
    private long startIn;
    private boolean isStarted;

    public Wheel(WheelListener wheelListener, long frameDuration, long startIn) {
        this.wheelListener = wheelListener;
        this.frameDuration = frameDuration;
        this.startIn = startIn;
        currentIndex = 0;
        isStarted = true;
    }

    public int prevImg() {
        int localCurrentIndex = currentIndex;
        localCurrentIndex--;
        if (localCurrentIndex < 0) {
            localCurrentIndex = drawables.length - 1;
        }
        return localCurrentIndex;
    }

    public int nextImg() {
        currentIndex++;

        if (currentIndex == drawables.length) {
            currentIndex = 0;
        }
        return currentIndex;
    }

    public int currImg() {
        int localCurrentIndex = currentIndex;
        localCurrentIndex--;
        if (localCurrentIndex < 0) {
            localCurrentIndex = drawables.length - 1;
        }
        return localCurrentIndex;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(startIn);
        } catch (InterruptedException e) {
        }

        while (isStarted) {
            int prevIndex = prevImg();
            int nextIndex = nextImg();
            int currIndex = currImg();
            try {
                Thread.sleep(frameDuration);
            } catch (InterruptedException e) {
            }


            if (wheelListener != null) {
                wheelListener.newImage(drawables[prevIndex],
                        drawables[nextIndex],
                        drawables[currIndex], currIndex);
            }
        }
    }

    public void stopWheel() {
        isStarted = false;
    }
}