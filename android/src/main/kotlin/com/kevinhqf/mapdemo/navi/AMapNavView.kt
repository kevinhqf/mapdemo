package com.kevinhqf.mapdemo.navi

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.amap.api.navi.AMapNavi
import com.amap.api.navi.AMapNaviView
import com.amap.api.navi.AMapNaviViewOptions
import com.amap.api.navi.enums.NaviType
import com.amap.api.navi.model.AMapCalcRouteResult
import com.amap.api.navi.model.NaviLatLng
import com.kevinhqf.mapdemo.NAVI_CHANNEL
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView

public class AMapNavView(id: Int, val context: Context, binaryMessenger: BinaryMessenger, lifecycleProvider: LifecycleProvider, options: AMapNaviViewOptions) : DefaultLifecycleObserver, ActivityPluginBinding.OnSaveInstanceStateListener, MethodChannel.MethodCallHandler, PlatformView {

    var methodChannel: MethodChannel? = null
    var mapView: AMapNaviView? = null
    var mapNavi: AMapNavi? = null
    var disposed = false


    protected var mEndLatlng = NaviLatLng(22.819884, 113.284958)

    protected val sList: ArrayList<NaviLatLng> = ArrayList()
    protected val eList: ArrayList<NaviLatLng> = ArrayList()
    protected var mWayPointList: ArrayList<NaviLatLng> = ArrayList()

    init {
        //todo
        methodChannel = MethodChannel(binaryMessenger, NAVI_CHANNEL)
        methodChannel?.setMethodCallHandler(this)
        mapView = AMapNaviView(context,options)

        mapNavi = AMapNavi.getInstance(context)
        lifecycleProvider.getLifecycle().addObserver(this)

        mapNavi?.setEmulatorNaviSpeed(75)

        eList.add(mEndLatlng)
        mapView?.setAMapNaviViewListener(object : MapNaviViewListener() {})
        mapNavi?.addAMapNaviListener(object : MapNaviListener() {
            override fun onInitNaviSuccess() {
                super.onInitNaviSuccess()
                //Toast.makeText(context,"initnav",Toast.LENGTH_SHORT).show()
            }

            override fun onCalculateRouteSuccess(p0: AMapCalcRouteResult?) {
                super.onCalculateRouteSuccess(p0)
                mapNavi?.startNavi(NaviType.EMULATOR)
            }

            override fun onStartNavi(p0: Int) {
                super.onStartNavi(p0)
                Toast.makeText(context,"start",Toast.LENGTH_SHORT).show()
            }

            override fun onCalculateRouteFailure(p0: AMapCalcRouteResult?) {
                super.onCalculateRouteFailure(p0)
                Toast.makeText(context,"failure:${p0?.errorDescription}",Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun initNav(){
        //Toast.makeText(context,"press",Toast.LENGTH_SHORT).show()

        var strategy = 0
        try {
            //再次强调，最后一个参数为true时代表多路径，否则代表单路径
            strategy = mapNavi?.strategyConvert(true, false, false, false, false) ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mapNavi?.calculateDriveRoute(eList, null, strategy)
    }

    override fun onCreate(owner: LifecycleOwner) {
        try {
            if (disposed) return
            mapView?.onCreate(null)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onStart(owner: LifecycleOwner) {

    }

    override fun onResume(owner: LifecycleOwner) {
        try {
            if (disposed) return
            mapView?.onResume()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        try {
            if (disposed) return
            mapView?.onPause()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
    }

    override fun onDestroy(owner: LifecycleOwner) {
        try {
            if (disposed) return
            destroyMapViewIfNecessary()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun destroyMapViewIfNecessary() {
        mapView?.onDestroy()
        mapNavi?.stopNavi()
        AMapNavi.destroy()
    }

    override fun onSaveInstanceState(bundle: Bundle) {
        try {
            if (disposed) return
            mapView?.onSaveInstanceState(bundle)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onRestoreInstanceState(bundle: Bundle?) {
        try {
            if (disposed) return
            mapView?.onCreate(bundle)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "startNav") {
            initNav()
        }
    }

    override fun getView(): View {
        return mapView!!
    }

    override fun dispose() {
        try {
            if (disposed) return
            methodChannel?.setMethodCallHandler(null)
            destroyMapViewIfNecessary()
            disposed = true
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}