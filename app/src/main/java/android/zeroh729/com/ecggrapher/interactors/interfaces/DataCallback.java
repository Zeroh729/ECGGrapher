package android.zeroh729.com.ecggrapher.interactors.interfaces;

import static android.R.attr.data;

public interface DataCallback<T> {
    void onUpdate(T data);
}
