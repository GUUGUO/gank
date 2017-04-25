package com.guuguo.gank.app

import android.app.Application
import com.guuguo.android.lib.BaseApplication
import com.guuguo.gank.R
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta

/**
 * Created by guodeqing on 16/3/7.
 */
class MyApplication : BaseApplication() {
    override fun init() {
        instance = INSTANCE as MyApplication?;
        initBugly()
    }

    companion object {
        var instance: MyApplication? = null
    }


    private fun initBugly() {
        Beta.upgradeDialogLayoutId = R.layout.dialog_upgrade
        Beta.tipsDialogLayoutId = R.layout.dialog_tips
        Bugly.init(this, "40950003e9", false);
    }
}