package android.zeroh729.com.ecggrapher.ui.main.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.zeroh729.com.ecggrapher.R;
import android.zeroh729.com.ecggrapher.data.model.BluetoothDeviceItem;
import android.zeroh729.com.ecggrapher.ui.main.views.viewholders.DeviceViewHolder;
import android.zeroh729.com.ecggrapher.ui.main.views.viewholders.DeviceViewHolder_;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by da Ent on 28/11/2015.
 */
@EBean
public class BluetoothDevicesAdapter extends BaseAdapter {
    ArrayList<BluetoothDeviceItem> devices = new ArrayList<>();

    @RootContext
    Context context;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DeviceViewHolder deviceView;
        if (convertView == null) {
            deviceView = DeviceViewHolder_.build(context);
        } else {
            deviceView = (DeviceViewHolder) convertView;
        }

        deviceView.bind(getItem(position));

        return deviceView;
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public BluetoothDeviceItem getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getPosition(BluetoothDeviceItem device){
        return devices.indexOf(device);
    }

    public void add(BluetoothDeviceItem device) {
        boolean isUnique = true;
        for(BluetoothDeviceItem d : devices){
            if(device.getDeviceAddress().equals(d.getDeviceAddress())){
                isUnique = false;
                break;
            }
        }
        if(isUnique)
            devices.add(device);
    }

}
