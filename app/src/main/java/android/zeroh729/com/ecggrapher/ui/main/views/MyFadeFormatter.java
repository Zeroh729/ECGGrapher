package android.zeroh729.com.ecggrapher.ui.main.views;

import android.graphics.Color;
import android.graphics.Paint;

import com.androidplot.xy.AdvancedLineAndPointRenderer;

public class MyFadeFormatter extends AdvancedLineAndPointRenderer.Formatter {

    private int trailSize;

    public MyFadeFormatter(int trailSize) {
        this.trailSize = trailSize;
        Paint p = new Paint();
        p.setColor(Color.parseColor("#da1713"));
        p.setStrokeWidth(2f);
        p.setAntiAlias(true);
        setLinePaint(p);
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
