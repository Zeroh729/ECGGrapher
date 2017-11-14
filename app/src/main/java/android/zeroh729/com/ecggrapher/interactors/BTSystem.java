package android.zeroh729.com.ecggrapher.interactors;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.zeroh729.com.ecggrapher.ECGGrapher_;
import android.zeroh729.com.ecggrapher.data.events.BTDataReceiveEvent;
import android.zeroh729.com.ecggrapher.data.local.BTState;
import android.zeroh729.com.ecggrapher.interactors.interfaces.DataCallback;
import android.zeroh729.com.ecggrapher.services.MockSensorService_;
import android.zeroh729.com.ecggrapher.utils.OttoBus;
import android.zeroh729.com.ecggrapher.utils._;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import static android.zeroh729.com.ecggrapher.data.events.BTConstants.KEY_DEVICE_ADDRESS;
import static android.zeroh729.com.ecggrapher.data.events.BTConstants.KEY_DEVICE_NAME;
import static android.zeroh729.com.ecggrapher.data.events.BTConstants.KEY_STATE;

@EBean(scope = EBean.Scope.Singleton)
public class BTSystem {
    DataCallback<Bundle> viewCallback;
    static BTState state;

    @Bean
    OttoBus bus;

    public void init(){
    }

    public void subscribe(DataCallback<Bundle> viewCallback){
        this.viewCallback = viewCallback;
        if(BluetoothAdapter.getDefaultAdapter().isEnabled())
            updateState(BTState.BT_ON_DOING_NOTHING);
        else
            updateState(BTState.BT_OFF);
    }

    public void unsubscribe(){
        viewCallback = null;
    }

    public static BTState getBTState() {
        return state;
    }

    private void updateState(BTState state){
        updateState(state, new Bundle());
    }

    private void updateState(BTState state, Bundle bundle){
        this.state = state;
        if(viewCallback!=null) {
            bundle.putString(KEY_STATE, state.toString());
            viewCallback.onUpdate(bundle);
        }
        if(state == BTState.CONNECTED){
            //start Service
            MockSensorService_.intent(ECGGrapher_.getInstance()).startReadingSensor().start();
        }else if(state == BTState.DISCONNECTED){
            MockSensorService_.intent(ECGGrapher_.getInstance()).stopReadingSensor().start();
        }
    }

    public void stateEnabled(boolean isOn){
        if(isOn)
            updateState(BTState.BT_ON_DOING_NOTHING);
        else
            updateState(BTState.BT_OFF);
    }

    public void stateSettingToEnable(boolean isEnabling){
        if(isEnabling)
            updateState(BTState.BT_TURNING_ON);
        else
            updateState(BTState.BT_TURNING_OFF);
    }
}
