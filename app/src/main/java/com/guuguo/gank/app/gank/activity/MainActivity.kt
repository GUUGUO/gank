package com.guuguo.gank.app.gank.activity

import android.graphics.Color
import com.guuguo.android.lib.extension.getColorCompat
import com.guuguo.android.lib.systembar.SystemBarHelper
import com.guuguo.gank.R
import com.guuguo.gank.base.BaseActivity
import android.view.ViewGroup
import android.os.Build.VERSION
import android.os.Bundle
import android.view.View
import com.guuguo.gank.base.MBaseActivity
import com.guuguo.gank.databinding.ActivityMainBinding
import com.guuguo.gank.util.ThemeUtils
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import kotlinx.android.synthetic.main.fragment_home.*


class MainActivity : MBaseActivity<ActivityMainBinding>() {
    override fun getLayoutResId() = R.layout.activity_main
    override fun initStatusBar() {
        val decorView = window.decorView as ViewGroup
        if (VERSION.SDK_INT >= 19) {
            var translucentView = decorView.findViewById<View>(R.id.systembar_foreground_view)
            if (translucentView == null) {
                translucentView = View(decorView.context)
                translucentView.id = R.id.systembar_foreground_view
                val lp = ViewGroup.LayoutParams(-1, SystemBarHelper.getStatusBarHeight(decorView.context))
                decorView.addView(translucentView, lp)
            }

            translucentView.setBackgroundColor(getColorCompat(R.color.colorPrimary))
        }
        SystemBarHelper.tintStatusBar(activity, getColorCompat(R.color.colorPrimary), 0.2f)
    }

}
