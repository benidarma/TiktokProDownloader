package com.ameldev.tiktokprodownloader.api;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.ameldev.tiktokprodownloader.models.Tiktok;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CommonClassForAPI {

    @SuppressLint("StaticFieldLeak")
    private static Activity mActivity;
    @SuppressLint("StaticFieldLeak")
    private static CommonClassForAPI CommonClassForAPI;
    public static CommonClassForAPI getInstance(Activity activity) {
        if (CommonClassForAPI == null) {
            CommonClassForAPI = new CommonClassForAPI();
        }
        mActivity = activity;
        return CommonClassForAPI;
    }

    public void CallTiktokApi(final DisposableObserver observer, String URL, String link) {
        RestClient.getInstance(mActivity).getService().callTiktokApi(URL, link)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Tiktok>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Tiktok o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }
}
