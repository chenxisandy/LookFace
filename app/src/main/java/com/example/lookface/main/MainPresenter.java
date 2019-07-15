package com.example.lookface.main;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.example.lookface.model.DataRepo;

import java.io.File;

public class MainPresenter<T extends Context & MainContract.View> implements MainContract.Presenter {

    private T view;

    private DataRepo repo;

    public MainPresenter(T view) {
        this.repo = DataRepo.getInstance();
        this.view = view;
    }


    @Override
    public void setData() {
        repo.fetchListFromLitePal();
        view.setRecyclerView(repo.getBeanList());
    }


}
