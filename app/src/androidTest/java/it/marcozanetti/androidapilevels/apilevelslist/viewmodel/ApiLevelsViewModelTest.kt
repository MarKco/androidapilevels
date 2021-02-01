package it.marcozanetti.androidapilevels.apilevelslist.viewmodel

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.marcozanetti.androidapilevels.apilevelslist.view.ApiLevelsFragment
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
@RunWith(AndroidJUnit4::class)
class ApiLevelsViewModelTest {

    @Test
    fun getAPILevels_retrieve_checkNumber() {
        val fragment = ApiLevelsFragment()

        val application = ApplicationProvider.getApplicationContext() as Application

        val viewModelFactory =
            ApiLevelsViewModelFactory(application)


        val viewModel =
            viewModelFactory.create((ApiLevelsViewModel::class.java))
        viewModel.retrieveApiLevelData()

        val listToCheck = viewModel.apiLevelItems.blockingObserve()
        assertEquals(listToCheck?.size, 29)

    }

    //Function that helps testing observers without passing a lifecycleowner
    fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
        val observer = OneTimeObserver(handler = onChangeHandler)
        observe(observer, observer)
    }

    //Helper class in order to implement Observer/Observable protocol with a default lifecycleowner
    class OneTimeObserver<T>(private val handler: (T) -> Unit) : Observer<T>, LifecycleOwner {
        private val lifecycle = LifecycleRegistry(this)

        init {
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }

        override fun getLifecycle(): Lifecycle = lifecycle

        override fun onChanged(t: T) {
            handler(t)
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        }
    }

    fun <T> LiveData<T>.blockingObserve(): T? {
        var value: T? = null
        val latch = CountDownLatch(1)

        val observer = Observer<T> { t ->
            value = t
            latch.countDown()
        }

        observeForever(observer)

        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}
**/