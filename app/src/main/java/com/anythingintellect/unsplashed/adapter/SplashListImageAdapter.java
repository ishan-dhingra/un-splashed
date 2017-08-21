package com.anythingintellect.unsplashed.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.anythingintellect.networklib.RequestManager;
import com.anythingintellect.unsplashed.R;
import com.anythingintellect.unsplashed.databinding.ItemSplashImageBinding;
import com.anythingintellect.unsplashed.model.SplashImage;
import com.anythingintellect.unsplashed.view.SplashImageListFragment;
import com.anythingintellect.unsplashed.viewmodel.ItemSplashImageViewModel;

/**
 * Created by ishan.dhingra on 21/08/17.
 */

public class SplashListImageAdapter extends RecyclerView.Adapter<SplashListImageAdapter.SplashItemViewHolder> {

    private final RequestManager requestManager;
    private final ObservableList<SplashImage> imageList;

    public SplashListImageAdapter(RequestManager requestManager, ObservableList<SplashImage> imageList) {
        this.requestManager = requestManager;
        this.imageList = imageList;
        this.imageList.addOnListChangedCallback(itemChangedListener);
    }

    @Override
    public SplashItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_splash_image, parent, false);
        return new SplashItemViewHolder(binding, new ItemSplashImageViewModel(requestManager));
    }

    @Override
    public void onBindViewHolder(SplashItemViewHolder holder, int position) {
        holder.bind(imageList.get(position));
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class SplashItemViewHolder extends RecyclerView.ViewHolder {

        private final ViewDataBinding binding;
        private final ItemSplashImageViewModel viewModel;

        SplashItemViewHolder(ViewDataBinding binding, ItemSplashImageViewModel viewModel) {
            super(binding.getRoot());
            this.binding = binding;
            this.viewModel = viewModel;
        }

        void bind(SplashImage image) {
            viewModel.setSplashImg(image);
            binding.setVariable(BR.vm, viewModel);
            binding.executePendingBindings();
        }

    }

    private final ObservableList.OnListChangedCallback<ObservableList<SplashImage>> itemChangedListener = new ObservableList.OnListChangedCallback<ObservableList<SplashImage>>() {

        @Override
        public void onChanged(ObservableList<SplashImage> news) {

        }

        @Override
        public void onItemRangeChanged(ObservableList<SplashImage> news, int start, int count) {
            notifyItemRangeChanged(start, count);
        }

        @Override
        public void onItemRangeInserted(ObservableList<SplashImage> news, int start, int count) {
            notifyItemRangeInserted(start, count);
        }

        @Override
        public void onItemRangeMoved(ObservableList<SplashImage> news, int i, int i1, int i2) {
        }

        @Override
        public void onItemRangeRemoved(ObservableList<SplashImage> news, int start, int count) {
            notifyItemRangeRemoved(start, count);
        }
    };

}
