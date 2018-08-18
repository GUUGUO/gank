package com.guuguo.gank.app.gank.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.databinding.BaseObservable
import com.google.gson.reflect.TypeToken
import com.guuguo.android.lib.extension.safe
import com.guuguo.gank.R.id.swiper
import com.guuguo.gank.app.gank.fragment.GankDailyFragment
import com.guuguo.gank.base.BaseListViewModel
import com.guuguo.gank.constant.LocalData
import com.guuguo.gank.constant.MEIZI_COUNT
import com.guuguo.gank.constant.myGson
import com.guuguo.gank.model.Ganks
import com.guuguo.gank.model.entity.GankModel
import com.guuguo.gank.model.other.RefreshListModel
import com.guuguo.gank.net.ApiServer
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.view_refresh_recycler.*
import java.util.*


/**
 * mimi 创造于 2017-05-22.
 * 项目 pika
 */
class GankDailyViewModel : BaseListViewModel() {
    override fun refreshData(refresh: Boolean) {
        Flowable.zip(ApiServer.getGankData(ApiServer.TYPE_FULI, MEIZI_COUNT, page), ApiServer.getGankData(ApiServer.TYPE_REST, MEIZI_COUNT, page),
                BiFunction<Ganks<ArrayList<GankModel>>, Ganks<ArrayList<GankModel>>, List<GankModel>> { t1, t2 ->
                    t1.results?.zip(t2.results!!) { a: GankModel, b: GankModel ->
                        a.desc = b.desc
                        a.who = b.who
                        a
                    }.safe()
                })
                .subscribe({
                    it.let {
                        if (page == 1)
                            LocalData.gankDaily = myGson.toJson(it)
                        setUpMeiziList(it)
                    }
                }, {
                    isLoading.value = false
                    isError.value = it
                }).isDisposed
    }

    private val refreshListModel = RefreshListModel<GankModel>()
    val ganksListLiveData = MutableLiveData<RefreshListModel<GankModel>>()

    fun getMeiziData() {
        val tempStr = LocalData.gankDaily
        if (tempStr.isNotEmpty())
            setUpMeiziList(myGson.fromJson(tempStr, object : TypeToken<ArrayList<GankModel>>() {}.type))
    }

    fun setUpMeiziList(lMeiziList: List<GankModel>) {
        isLoading.value = false
        if (lMeiziList.size < MEIZI_COUNT) {
            refreshListModel.isEnd = true
        }
        if (page == 1) {
            refreshListModel.setRefresh(lMeiziList)
        } else {
            refreshListModel.setUpdate(lMeiziList)
        }
        ganksListLiveData.value = refreshListModel
    }
}

