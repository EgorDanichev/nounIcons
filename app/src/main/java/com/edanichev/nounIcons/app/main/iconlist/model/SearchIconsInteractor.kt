package com.edanichev.nounIcons.app.main.iconlist.model

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.EmptyListException
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.GetIconsCommand
import com.edanichev.nounIcons.app.main.Utils.Network.rx.SimpleSingleObserver
import com.edanichev.nounIcons.app.main.base.data.BaseInteractor
import io.reactivex.disposables.CompositeDisposable
import ru.alfabank.mobile.android.core.domain.network.callback.RequestCallback
import javax.inject.Inject

open class SearchIconsInteractor
@Inject constructor(
    private val getIconsCommand: GetIconsCommand,
    compositeDisposable: CompositeDisposable
) : BaseInteractor(compositeDisposable) {

    //    TranslateCommand translateCommand = new TranslateCommand(this);

    open fun getIcons(term: String, callback: RequestCallback<List<IconDetails>>) {
        val observer = SimpleSingleObserver<Icons> {
            onComplete = {
                if (it.icons.isEmpty()) {
                    callback.onRequestFailure(EmptyListException())
                } else {
                    callback.onRequestSuccess(it.icons)
                }
            }
            onSubscribe = { compositeDisposable.add(it) }
            onError = {
                callback.onRequestFailure(it)
            }
        }

        if (!compositeDisposable.isDisposed) {
            getIconsCommand.term = term
            getIconsCommand.execute().subscribe(observer)
        }
    }
}