package com.guuguo.gank.base

import android.view.View
import com.guuguo.android.lib.app.LBaseFragment
import com.guuguo.android.lib.app.LNBaseFragment
import com.guuguo.gank.app.MyApplication

/**
 * Created by guodeqing on 7/23/16.
 */

abstract class BaseFragment : LNBaseFragment() {
    protected var myApplication = MyApplication.instance

    abstract fun initPresenter()
    override fun initView() {
        initPresenter()
        super.initView()
    }
}
