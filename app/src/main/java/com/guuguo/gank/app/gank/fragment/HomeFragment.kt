package com.guuguo.gank.app.gank.fragment

import android.animation.Animator
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.view.ViewAnimationUtils
import androidx.annotation.ColorInt
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import com.google.android.material.tabs.TabLayout
import com.guuguo.android.lib.extension.dpToPx
import com.guuguo.android.lib.extension.getColorCompat
import com.guuguo.android.lib.extension.safe
import com.guuguo.android.lib.systembar.SystemBarHelper
import com.guuguo.android.lib.utils.DisplayUtil
import com.guuguo.gank.R
import com.guuguo.gank.app.gank.activity.AboutActivity
import com.guuguo.gank.app.gank.activity.GankActivity
import com.guuguo.gank.base.BaseFragment
import com.guuguo.gank.databinding.FragmentHomeBinding
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.proguard.t
import kotlinx.android.synthetic.main.base_toolbar_common.*
import kotlinx.android.synthetic.main.base_toolbar_common.view.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.toolbar_search.view.*

class HomeFragment : BaseFragment<FragmentHomeBinding>(), Toolbar.OnMenuItemClickListener {
    override fun isNavigationBack() = false

    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun getHeaderTitle() = "gank"
    override fun getMenuResId() = R.menu.main_menu
    override fun getToolBar() = contentView?.findViewById<Toolbar>(R.id.id_tool_bar)
    override fun getLayoutResId() = R.layout.fragment_home

    override fun setTitle(title: String) {
        tv_title.text = title
    }

    lateinit var mNavHostFragment: NavHostFragment

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_check_up -> Beta.checkUpgrade()
            R.id.menu_search -> {
                val nav = NavOptions.Builder().setPopUpTo(R.id.searchFragment, true).build()
                findNavController().navigate(R.id.action_to_search, null, nav)
            }
            R.id.menu_about -> AboutActivity.intentTo(activity)
            else -> return false
        }
        return true
    }

    override fun initView() {
        super.initView()
//        SystemBarHelper.setPadding(activity, binding.toolbar.ll_bar)
        mNavHostFragment = childFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
//        NavigationUI.setupWithNavController(binding.navigation, mNavHostFragment.navController)
//        ViewCompat.setElevation(binding.toolbar, 8.dpToPx().toFloat())
        binding.toolbar.tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(t: TabLayout.Tab?) {
                revealColor(t)
                val id = when (t?.position) {
                    0 -> R.id.dailyFragment
                    1 -> R.id.gankCategoryFragment
                    2 -> R.id.gankCategoryContentFragment
                    else -> R.id.dailyFragment
                }
//                when (t?.position) {
//                    1 -> ViewCompat.setElevation(binding.toolbar, 0f)
//                    else -> ViewCompat.setElevation(binding.toolbar, 8.dpToPx().toFloat())
//                }
                onNavDestinationSelected(id, mNavHostFragment.navController, true)
            }
        })
        getToolBar()?.inflateMenu(getMenuResId())
        getToolBar()?.setOnMenuItemClickListener(this)
        binding.toolbar.search_card.setOnClickListener {
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, binding.toolbar.search_card, "share_search")
            findNavController().navigate(R.id.action_to_search)
//            FragmentNavigator(activity,childFragmentManager,)
        }
    }

    /**执行 reveal 动画*/
    private fun revealColor(t: TabLayout.Tab?) {
        val color = when (t?.position) {
            0 -> activity.getColorCompat(R.color.colorPrimary)
            1 -> activity.getColorCompat(R.color.color_red_ccfa3c55)
            2 -> activity.getColorCompat(R.color.colorPrimaryBlue)
            else -> activity.getColorCompat(R.color.colorPrimary)
        }
        //reveal动画
        val location = IntArray(2) //view的位置
        val view = t?.view as View
        view?.getLocationInWindow(location)
        val radius = if (DisplayUtil.getScreenWidth() / 2 > location[0]) {
            DisplayUtil.getScreenWidth() - location[0]
        } else {
            location[0]
        }
        binding.toolbar.v_bar_reveal_color.setBackgroundColor(color)
        val sysbar = activity.findViewById<View?>(R.id.systembar_statusbar_view)
        val sysbarForeground = activity.findViewById<View?>(R.id.systembar_foreground_view)
        sysbar?.setBackgroundColor(color)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val anim = ViewAnimationUtils.createCircularReveal(binding.toolbar.v_bar_reveal_color, location[0] + view.width.safe() / 2, location[1] + view.height.safe() + 20.dpToPx(), 20.dpToPx().toFloat(), radius.toFloat())
            val anim2 = ViewAnimationUtils.createCircularReveal(sysbar, location[0] + view.width.safe() / 2, location[1] + view.height.safe() + 20.dpToPx(), 20.dpToPx().toFloat(), radius.toFloat())
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    binding.toolbar.setBackgroundColor(color)
                    sysbarForeground?.setBackgroundColor(color)
                }
            })
            anim.start()
            anim2.start()
        } else {
        }
    }

    internal fun onNavDestinationSelected(id: Int, navController: NavController, popUp: Boolean): Boolean {
        val builder = NavOptions.Builder().setClearTask(true)
//        builder.setPopUpTo(R.id.nav_host, true)
        val options = builder.build()
        try {
            navController.navigate(id, null as Bundle?, options)
            return true
        } catch (var6: IllegalArgumentException) {
            return false
        }

    }
}
