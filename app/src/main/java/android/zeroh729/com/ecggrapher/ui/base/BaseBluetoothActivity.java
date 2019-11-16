package android.zeroh729.com.ecggrapher.ui.base;

public abstract class BaseBluetoothActivity extends BaseActivity {
    public abstract void displayDisconnectedView();
    public abstract void connected();
    public abstract void disconnected();
    public abstract void receiveData(int data);
}
