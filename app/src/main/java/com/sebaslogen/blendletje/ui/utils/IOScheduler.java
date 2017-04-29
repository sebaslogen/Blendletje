package com.sebaslogen.blendletje.ui.utils;


import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/**
 * Class to provide RxJava IO scheduler
 */
public class IOScheduler {
    public Scheduler get() {
        return Schedulers.io();
    }
}
