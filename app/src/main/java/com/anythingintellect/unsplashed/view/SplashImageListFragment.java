package com.anythingintellect.unsplashed.view;


import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anythingintellect.networklib.RequestManager;
import com.anythingintellect.unsplashed.R;
import com.anythingintellect.unsplashed.UnSplashedApp;
import com.anythingintellect.unsplashed.adapter.SplashListImageAdapter;
import com.anythingintellect.unsplashed.databinding.FragmentSplashImageListBinding;
import com.anythingintellect.unsplashed.model.SplashImage;
import com.anythingintellect.unsplashed.network.UnSplashedAPI;
import com.anythingintellect.unsplashed.viewmodel.SplashImageListViewModel;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashImageListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    SplashImageListViewModel viewModel;
    @Inject
    RequestManager requestManager;
    SplashListImageAdapter adapter;

    public SplashImageListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((UnSplashedApp)getActivity().getApplication())
                .getAppComponent().inject(this);
        // Setup Adapter
        viewModel.loadSplashImageList(false);
        adapter = new SplashListImageAdapter(requestManager, viewModel.getSplashImages());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSplashImageListBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_splash_image_list, container, false);
        binding.setVm(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRV(view);
    }

    private void setupRV(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rvImageList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
    }


    @Override
    public void onRefresh() {
        viewModel.loadSplashImageList(true);
    }

}
