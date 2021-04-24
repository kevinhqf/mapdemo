package com.kevinhqf.mapdemo.navi

import android.content.Context
import com.amap.api.navi.AMapNaviViewOptions
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory

class AMapNavViewFactory(val binaryMessenger: BinaryMessenger,val lifecycleProvider: LifecycleProvider): PlatformViewFactory(StandardMessageCodec.INSTANCE) {



    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        val options = AMapNaviViewOptions()
        options.isSettingMenuEnabled = false
        options.isAfterRouteAutoGray = true
        options.isAutoLockCar=true
        return AMapNavView(viewId,context,binaryMessenger,lifecycleProvider, options)
    }
}