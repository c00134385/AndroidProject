package com.hjq.demo.mananger;

import android.content.Context;
import android.text.TextUtils;

import com.hjq.demo.BuildConfig;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class OrthManager extends BaseManager{
    private static OrthManager ourInstance;

    private String url = "https://www.jianshu.com/p/aa3b9184fed6";
    private boolean orthValid;

    private long mInterval = TimeUnit.MINUTES.toMillis(10);
    private Runnable mAction = new Runnable() {

        @Override
        public void run() {
            Timber.d("tid:%d \"%s\" is running", Thread.currentThread().getId(), Thread.currentThread().getName());

            Observable.just(NetworkManager.getInstance().isNetworkConnected())
                    .map(new Function<Boolean, String>() {
                        @Override
                        public String apply(Boolean aBoolean) throws Exception {
                            if(aBoolean) {
                                return url;
                            } else {
                                throw new RuntimeException("netnotavailable");
                            }
                        }
                    })
                    .map(new Function<String, Boolean>() {
                        @Override
                        public Boolean apply(String s) throws Exception {
                            return parseUrl(s);
                        }
                    })
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Timber.e(throwable, throwable.getMessage());
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean b) throws Exception {
                            orthValid = b;
                        }
                    });
            mHandler.postDelayed(mAction, mInterval);
        }
    };


    public static OrthManager getInstance() {
        if(null == ourInstance) {
            throw new RuntimeException("OrthManager not intialized.");
        }
        return ourInstance;
    }

    private OrthManager(Context context) {
        super(context);
        orthValid = true;
        if(BuildConfig.DEBUG) {
            mInterval = TimeUnit.MINUTES.toMillis(1);
        }
    }

    public static void init(Context context) {
        ourInstance = new OrthManager(context);
    }

    public void start() {
        mHandler.removeCallbacks(mAction);
        mHandler.post(mAction);
    }

    private Boolean parseUrl(String url) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        Document document = Jsoup.connect(url).sslSocketFactory(SsX509TrustManager.getSSLContext().getSocketFactory()).get();
        if(null != document) {
            Elements elements = document.getElementsByClass("show-content");
            if(null != elements) {
                Iterator it = elements.iterator();
                while (it.hasNext()) {
                    Element element = (Element) it.next();
                    String html = element.html();
                    if(!TextUtils.isEmpty(html) && html.contains("rock-rock-rock")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isOrthValid() {
        return orthValid;
    }
}
