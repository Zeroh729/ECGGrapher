package android.zeroh729.com.ecggrapher.data.model;

import android.zeroh729.com.ecggrapher.data.local.Constants;

import com.androidplot.xy.AdvancedLineAndPointRenderer;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

public class MockECGSeries implements XYSeries {
    private final Number[] data;
    private final long delayMs;
    private final Thread thread;
    private int latestIndex = 0;
    private boolean keepRunning;
    private XYPlot plot;

    public MockECGSeries(final XYPlot plot) {
        this.plot = plot;
        data = new Number[Constants.COUNT_X];
        delayMs = 10;
        keepRunning = true;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (keepRunning) {
                        if (latestIndex >= data.length) {
                            latestIndex = 0;
                        }

                        data[latestIndex] = Math.random() * 1023;

                        if (latestIndex < data.length - 1) {
                            // null out the point immediately following i, to disable
                            // connecting i and i+1 with a line:
                            data[latestIndex + 1] = null;
                        }

                        plot.getRenderer(AdvancedLineAndPointRenderer.class).setLatestIndex(latestIndex);
                        Thread.sleep(delayMs);
                        latestIndex++;
                    }
                } catch (InterruptedException e) {
                    keepRunning = false;
                }
            }
        });
    }

    public void addData(int y){
        if (latestIndex >= data.length) {
            latestIndex = 0;
        }

        data[latestIndex] = y;
        if (latestIndex < data.length - 1) {
            // null out the point immediately following i, to disable
            // connecting i and i+1 with a line:
            data[latestIndex + 1] = null;
        }
        plot.getRenderer(AdvancedLineAndPointRenderer.class).setLatestIndex(latestIndex);
        latestIndex++;
    }

    public void start(){
        thread.start();
    }
    public void stop(){
        keepRunning = false;
    }

    @Override
    public int size() {
        return data.length;
    }

    @Override
    public Number getX(int index) {
        return index;
    }

    @Override
    public Number getY(int index) {
        return data[index];
    }

    @Override
    public String getTitle() {
        return "Signal";
    }
}
