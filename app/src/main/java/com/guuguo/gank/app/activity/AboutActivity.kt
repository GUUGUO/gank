package com.guuguo.gank.app.activity;

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.FrameLayout
import com.guuguo.gank.R
import com.guuguo.gank.base.BaseActivity
import com.tencent.bugly.beta.Beta
import com.vansuita.materialabout.builder.AboutBuilder
import kotlinx.android.synthetic.main.base_toolbar_common.*

class AboutActivity : BaseActivity() {
    override fun getLayoutResId() = R.layout.activity_simple_back
    override fun getHeaderTitle() = "关于"
    override fun getToolBar() = id_tool_bar

    companion object {
        val ACTIVITY_ABOUT = 12
        fun intentTo(activity: Activity) {
            val intent = Intent(activity, AboutActivity::class.java)
            activity.startActivityForResult(intent, ACTIVITY_ABOUT)
        }
    }

    override fun initView() {
        super.initView()
        val url = "mqqwpa://im/chat?chat_type=wpa&uin=1152168009"
        val view = AboutBuilder.with(this)
                .setPhoto(R.mipmap.avatar)
                .setCover(R.mipmap.profile_cover)
                .setName("gank")
                .setSubTitle("gank.io 第三方客户端")
                .setBrief("更多的，源源不断的程序员干货和妹子图")
                .setAppName(R.string.app_name)
                .addEmailLink("guuguo@qq.com")
                .addGitHubLink("guuguo/gank")
                .setVersionNameAsAppSubTitle()
                .setWrapScrollView(true)
                .setLinksAnimated(true)
                .setShowAsCard(true)
                .addAction(R.mipmap.update, "检查更新", { v ->
                    Beta.checkUpgrade()
                })
                .build()
        (findViewById<FrameLayout>(R.id.content)).addView(view)
    }
}
