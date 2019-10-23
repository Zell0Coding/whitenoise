package com.ponicamedia.android.whitenoise.Utills;
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}