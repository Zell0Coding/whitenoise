package com.meathammerstudio.whitenoise.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.meathammerstudio.whitenoise.Utills.ItemTouchHelperAdapter;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    public static final float ALPHA_FULL = 1.0f;
    private final ItemTouchHelperAdapter mAdapter;

    public SwipeToDeleteCallback(ItemTouchHelperAdapter adapter) {
        super(0,ItemTouchHelper.LEFT);
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if(viewHolder instanceof musicAdapter.labelView){
            return 0;
        }else{
            return makeMovementFlags(0,ItemTouchHelper.LEFT);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
