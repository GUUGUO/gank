package com.guuguo.learnsave.view

import com.guuguo.learnsave.bean.entity.GankBean

/**
 * 主界面的接口
 * Created by panl on 15/12/22.
 */
interface IMainView : IBaseView {
    fun showProgress()
    fun hideProgress()
    fun showNoMoreData()
    fun showErrorView(e: Throwable)
    fun showMeiziList(lMeiziList: List<GankBean>)
}
