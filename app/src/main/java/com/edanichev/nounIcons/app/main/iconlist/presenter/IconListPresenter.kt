package com.edanichev.nounIcons.app.main.iconlist.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.edanichev.nounIcons.app.R
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails
import com.edanichev.nounIcons.app.main.Utils.Analytics.NounFirebaseAnalytics
import com.edanichev.nounIcons.app.main.Utils.Network.InternetStatus.InternetStatus
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.EmptyListException
import com.edanichev.nounIcons.app.main.iconlist.model.SearchIconsInteractor
import com.edanichev.nounIcons.app.main.iconlist.view.MainView
import ru.alfabank.mobile.android.core.domain.network.callback.RequestCallback
import javax.inject.Inject

@InjectViewState
class IconListPresenter @Inject
constructor(
    private var searchIconsInteractor: SearchIconsInteractor,
    private val internetStatus: InternetStatus,
    private val analytics: NounFirebaseAnalytics
) : MvpPresenter<MainView>(), IIconListPresenter {

    override fun onDestroy() {
        searchIconsInteractor.onDestroy()
    }

    override fun getIconsList(term: String) {
        checkInternet()

        if (term.isNotEmpty()) {
            viewState.showProgress()
        } else {
            viewState.emptyQueryError()
            return
        }

        val callback = RequestCallback<List<IconDetails>>()
        callback.apply {
            setSuccess { viewState.showIconsList(it) }
            setError {
                if (it is EmptyListException)
                    viewState.onEmptyIconsList()
            }
        }

        searchIconsInteractor.getIcons(term, callback)
        analytics.registerSearchEvent(term)
    }

    private fun checkInternet() {
        if (!internetStatus.isNetworkConnected())
            viewState.showSnack(R.string.no_internet)
        else
            viewState.hideSnack()
    }
}