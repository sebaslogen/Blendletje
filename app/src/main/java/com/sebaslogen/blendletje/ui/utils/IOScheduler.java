package com.sebaslogen.blendletje.ui.utils;


import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * Class to provide RxJava IO scheduler
 */
public class IOScheduler {
    public Scheduler get() {
        return Schedulers.io();
    }
}
