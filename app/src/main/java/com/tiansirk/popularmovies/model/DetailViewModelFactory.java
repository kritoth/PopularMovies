package com.tiansirk.popularmovies.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Custom ViewModelFactory to let the possibility to use a parameter in the ViewModel for querying
 * the database.
 */
public class DetailViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private int mId;

    public DetailViewModelFactory(Application application, int mId) {
        this.mApplication = application;
        this.mId = mId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailViewModel(mApplication, mId);
    }
}
