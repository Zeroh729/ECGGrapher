package android.whitewidget.com.boilerplateandroid.presenters;

/**
 * Created by Admin on 3/8/16.
 */
public class LivestreamPresenter implements BasePresenter {
    LivestreamSystemInterface system;
    LivestreamScreenInterface screen;

    @Override
    public void setup() {

    }

    @Override
    public void updateState() {

    }

    @Override
    public void setState(int state) {

    }

    public interface LivestreamSystemInterface extends SystemInterface{

    }

    public interface LivestreamScreenInterface extends ScreenInterface{

    }
}
