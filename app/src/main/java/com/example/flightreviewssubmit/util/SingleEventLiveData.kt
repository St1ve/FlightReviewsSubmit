package com.example.flightreviewssubmit.util

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Class for LiveData, which prevent several showing data.
 */
open class SingleEventLiveData<T> : MutableLiveData<T>() {

    companion object{
        private const val TAG: String = "SingleEventLiveData"
    }

    private val mPending: AtomicBoolean = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            // TODO Create MainHelper
            // MainHelper.log(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        super.observe(owner, {
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    @MainThread
    override fun setValue(value: T?) {
        mPending.set(true)
        super.setValue(value)
    }

    /**
     * Used for cases where T is Void, to make calls cleaner.
     */
    @MainThread
    fun call() {
        value = null
    }

    /**
     * Used for cases where T is Void from bg thread, to make calls cleaner.
     */
    @MainThread
    fun postCall() {
        postValue(null)
    }
}