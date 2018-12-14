package com.glovo.challenge.cities.details

import androidx.annotation.VisibleForTesting
import com.glovo.challenge.data.cities.CitiesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import javax.inject.Inject

internal class CityDetailsPresenter @Inject constructor(
    private val view: CityDetailsContract.View,
    private val citiesRepository: CitiesRepository
) : CityDetailsContract.Presenter {

    @VisibleForTesting
    internal var getDetailsDisposable = Disposables.disposed()

    override fun onStart() {
        getDetailsDisposable.dispose()
        getDetailsDisposable = citiesRepository.getDetails("TODO")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::showDetails)
    }

    override fun onStop() {
        getDetailsDisposable.dispose()
    }

}
