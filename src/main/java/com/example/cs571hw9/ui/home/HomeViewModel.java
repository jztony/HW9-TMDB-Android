package com.example.cs571hw9.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> titleText;
    private MutableLiveData<String> footerText;

    public HomeViewModel() {
        titleText = new MutableLiveData<>();
        footerText = new MutableLiveData<>();

        titleText.setValue(("USC Films"));
        footerText.setValue(("\n\n Powered by TMDB\nDeveloped by Zhengxie Hu\n"));
    }

    public LiveData<String> getTitle() {
        return titleText;
    }
    public LiveData<String> getFooter() {
        return footerText;
    }
}