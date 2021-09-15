package com.example.cs571hw9.ui.watchlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WatchlistViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WatchlistViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}