package android.zeroh729.com.ecggrapher.ui.main.activities;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.presenters.DataListPresenter;
import android.zeroh729.com.ecggrapher.ui.base.BaseActivity;

import com.androidplot.util.PixelUtils;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.AdvancedLineAndPointRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import java.lang.ref.WeakReference;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;


@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity{

    @ViewById
    XYPlot plot;

    /**
     * Uses a separate thread to modulate redraw frequency.
     */
    private Redrawer redrawer;
    private static final int WIDTH_X = 33;
    private static final int BLIPS = 3;
    private static final int UPDATE_FREQHRTZ = 10;

    @AfterViews
    public void afterviews(){
        ECGModel ecgSeries = new ECGModel(WIDTH_X, UPDATE_FREQHRTZ);

        // add a new series' to the xyplot:
        MyFadeFormatter formatter =new MyFadeFormatter(WIDTH_X);
        formatter.setLegendIconEnabled(false);
        plot.addSeries(ecgSeries, formatter);
        plot.setRangeBoundaries(-5, 10, BoundaryMode.FIXED);
        plot.setDomainBoundaries(0, WIDTH_X, BoundaryMode.FIXED);

        // reduce the number of range labels
        plot.setLinesPerRangeLabel(3);

        // start generating ecg data in the background:
        ecgSeries.start(new WeakReference<>(plot.getRenderer(AdvancedLineAndPointRenderer.class)));

        // set a redraw rate of 30hz and start immediately:
        redrawer = new Redrawer(plot, 30, true);
    }

    /**
     * Special {@link AdvancedLineAndPointRenderer.Formatter} that draws a line
     * that fades over time.  Designed to be used in conjunction with a circular buffer model.
     */
    public static class MyFadeFormatter extends AdvancedLineAndPointRenderer.Formatter {

        private int trailSize;

        public MyFadeFormatter(int trailSize) {
            this.trailSize = trailSize;
        }

        @Override
        public Paint getLinePaint(int thisIndex, int latestIndex, int seriesSize) {
            // offset from the latest index:
            int offset;
            if(thisIndex > latestIndex) {
                offset = latestIndex + (seriesSize - thisIndex);
            } else {
                offset =  latestIndex - thisIndex;
            }

            float scale = 255f / trailSize;
            int alpha = (int) (255 - (offset * scale));
            getLinePaint().setAlpha(alpha > 0 ? alpha : 0);
            return getLinePaint();
        }
    }

    /**
     * Primitive simulation of some kind of signal.  For this example,
     * we'll pretend its an ecg.  This class represents the data as a circular buffer;
     * data is added sequentially from left to right.  When the end of the buffer is reached,
     * i is reset back to 0 and simulated sampling continues.
     */
    public static class ECGModel implements XYSeries {

        private final Number[] data;
        private final long delayMs;
        private final int blipInteral;
        private final Thread thread;
        private boolean keepRunning;
        private int latestIndex;

        private WeakReference<AdvancedLineAndPointRenderer> rendererRef;

        /**
         *
         * @param size Sample size contained within this model
         * @param updateFreqHz Frequency at which new samples are added to the model
         */
        public ECGModel(int size, int updateFreqHz) {
            data = new Number[size];
            for(int i = 0; i < data.length; i++) {
                data[i] = 0;
            }

            // translate hz into delay (ms):
            delayMs = 1000 / updateFreqHz;

            blipInteral = size / BLIPS;

            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (keepRunning) {
                            if (latestIndex >= data.length) {
                                latestIndex = 0;
                            }

                            // generate some random data:
                            int ecgIndex = latestIndex % blipInteral;
                            switch (ecgIndex){
                                case 2 : //P signal
                                    data[latestIndex] = Math.random() * 3;
                                    break;
                                case 3 : //Q signal
                                    data[latestIndex] = -(Math.random() * 2);
                                    break;
                                case 4 : //R signal
                                    data[latestIndex] = (Math.random() * 5) + 2;
                                    break;
                                case 5 : //S signal
                                    data[latestIndex] = -(Math.random() * 2);
                                    break;
                                case 8 : //T signal
                                    data[latestIndex] = Math.random() * 2;
                                    break;
                                default: //baseline
                                    data[latestIndex] = 0;
                            }

                            if(latestIndex < data.length - 1) {
                                // null out the point immediately following i, to disable
                                // connecting i and i+1 with a line:
                                data[latestIndex +1] = null;
                            }

                            if(rendererRef.get() != null) {
                                rendererRef.get().setLatestIndex(latestIndex);
                                Thread.sleep(delayMs);
                            } else {
                                keepRunning = false;
                            }
                            latestIndex++;
                        }
                    } catch (InterruptedException e) {
                        keepRunning = false;
                    }
                }
            });
        }

        public void start(final WeakReference<AdvancedLineAndPointRenderer> rendererRef) {
            this.rendererRef = rendererRef;
            keepRunning = true;
            thread.start();
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

    @Override
    public void onStop() {
        super.onStop();
        redrawer.finish();
    }
}