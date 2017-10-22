package com.tdyh.android.rx.retrofit;

/**
 * Created by dugang on 2017/4/12.RxLifecycle绑定工具类
 */
@SuppressWarnings("unused")
public class RxUtil {
  /*  private static <T> LifecycleTransformer<T> bindToLifecycle(LifecycleProvider provider) {
        if (provider instanceof RxAppCompatActivity) {
            return ((RxAppCompatActivity) provider).bindToLifecycle();
        } else if (provider instanceof RxFragment) {
            return ((RxFragment) provider).bindToLifecycle();
        } else {
            throw new IllegalArgumentException("class must extents RxAppCompatActivity or RxFragment");
        }
    }

    private static <T> LifecycleTransformer<T> bindToLifecycle(LifecycleProvider provider, ActivityEvent event) {
        if (provider instanceof RxAppCompatActivity) {
            return ((RxAppCompatActivity) provider).bindUntilEvent(event);
        } else {
            throw new IllegalArgumentException("class must extents RxAppCompatActivity");
        }
    }

    private static <T> LifecycleTransformer<T> bindToLifecycle(LifecycleProvider provider, FragmentEvent event) {
        if (provider instanceof RxFragment) {
            return ((RxFragment) provider).bindUntilEvent(event);
        } else {
            throw new IllegalArgumentException("class must extents RxFragment");
        }
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final LifecycleProvider provider) {
        return new ObservableTransformer<T, T>() {
            @Override public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .retryWhen(new RetryWithDelay())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxUtil.<T>bindToLifecycle(provider));

            }
        };
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final LifecycleProvider provider, final ActivityEvent event) {
        return new ObservableTransformer<T, T>() {
            @Override public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .retryWhen(new RetryWithDelay())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxUtil.<T>bindToLifecycle(provider, event));

            }
        };
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final LifecycleProvider provider, final FragmentEvent event) {
        return new ObservableTransformer<T, T>() {
            @Override public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .retryWhen(new RetryWithDelay())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(RxUtil.<T>bindToLifecycle(provider, event));

            }
        };
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final LifecycleProvider provider, @NonNull final Dialog dialog) {
        return new ObservableTransformer<T, T>() {
            @Override public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .delay(1, TimeUnit.SECONDS)
                        .retryWhen(new RetryWithDelay())
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull final Disposable disposable) throws Exception {
                                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override public void onCancel(DialogInterface dialog) {
                                        disposable.dispose();
                                    }
                                });
                                dialog.show();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnTerminate(new Action() {
                            @Override public void run() throws Exception {
                                dialog.dismiss();
                            }
                        })
                        .compose(RxUtil.<T>bindToLifecycle(provider));
            }
        };
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final LifecycleProvider provider, final ActivityEvent event, @NonNull final Dialog dialog) {
        return new ObservableTransformer<T, T>() {
            @Override public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .delay(1, TimeUnit.SECONDS)
                        .retryWhen(new RetryWithDelay())
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull final Disposable disposable) throws Exception {
                                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override public void onCancel(DialogInterface dialog) {
                                        disposable.dispose();
                                    }
                                });
                                dialog.show();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnTerminate(new Action() {
                            @Override public void run() throws Exception {
                                dialog.dismiss();
                            }
                        })
                        .compose(RxUtil.<T>bindToLifecycle(provider, event));
            }
        };
    }

    public static <T> ObservableTransformer<T, T> applySchedulers(final LifecycleProvider provider, final FragmentEvent event, @NonNull final Dialog dialog) {
        return new ObservableTransformer<T, T>() {
            @Override public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream
                        .delay(1, TimeUnit.SECONDS)
                        .retryWhen(new RetryWithDelay(2,5000))
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull final Disposable disposable) throws Exception {
                                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override public void onCancel(DialogInterface dialog) {
                                        disposable.dispose();
                                    }
                                });
                                dialog.show();
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnTerminate(new Action() {
                            @Override public void run() throws Exception {
                                dialog.dismiss();
                            }
                        })
                        .compose(RxUtil.<T>bindToLifecycle(provider, event));
            }
        };
    }*/
}
