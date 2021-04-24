package com.kevinhqf.mapdemo.navi

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory
import java.util.concurrent.atomic.AtomicInteger

class FlutterAMapNavFactory(var state:AtomicInteger,val registry: PluginRegistry.Registrar):PlatformViewFactory(StandardMessageCodec.INSTANCE) {



    override fun create(context: Context, viewId: Int, args: Any?): PlatformView {
        val gson = Gson()
        var model :AMapNavModel?=null
        if (args is String){
            model = gson.fromJson(args.toString(),AMapNavModel::class.java)
        }
        val aMapNavView = FlutterAMapNavView(context,state,registry,viewId,registry.activity(),model)
      //  aMapNavView.setup()
        return aMapNavView
    }
}