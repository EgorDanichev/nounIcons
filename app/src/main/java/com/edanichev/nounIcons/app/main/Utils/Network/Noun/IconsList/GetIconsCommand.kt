package com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList

import com.edanichev.nounIcons.app.main.NounIconDetails.Model.Icons
import com.edanichev.nounIcons.app.main.base.data.AbsReactiveCommand

import javax.inject.Inject

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit

open class GetIconsCommand
@Inject constructor(
    retrofit: Retrofit
) : AbsReactiveCommand<Icons>(Icons::class.java) {
    private val service: NounIconListService = retrofit.create(NounIconListService::class.java)

    lateinit var term: String

    override fun execute(): Single<Icons> {
        return service
            .icons(term)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}