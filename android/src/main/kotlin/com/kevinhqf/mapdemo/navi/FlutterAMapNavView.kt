package com.kevinhqf.mapdemo.navi

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.amap.api.navi.*
import com.amap.api.navi.enums.AMapNaviParallelRoadStatus
import com.amap.api.navi.enums.NaviType
import com.amap.api.navi.model.*
import com.google.gson.Gson
import com.kevinhqf.mapdemo.*
import com.kevinhqf.mapdemo.R
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.platform.PlatformView
import java.lang.Exception
import java.util.concurrent.atomic.AtomicInteger


class FlutterAMapNavView(val context: Context, val atomicInteger: AtomicInteger, val registrar:PluginRegistry.Registrar , id: Int, var activity: Activity, val mOptions: AMapNavModel?) : PlatformView, MethodChannel.MethodCallHandler, Application.ActivityLifecycleCallbacks, AMapNaviListener, AMapNaviViewListener {

    var navChannel: MethodChannel = MethodChannel(registrar.messenger(), NAVI_CHANNEL)


    var navView: AMapNaviView
    var aMapNav: AMapNavi
    var rootView: View
    lateinit var latlon: Coordinate


    var disposed = false

    init {
        navChannel.setMethodCallHandler(this)
        aMapNav = AMapNavi.getInstance(activity)

        rootView = View.inflate(activity, R.layout.amap_nav, null)
        navView = view.findViewById(R.id.navi_view)
        registrar.activity().application.registerActivityLifecycleCallbacks(this)
    }

    fun initNav() {
        Toast.makeText(activity,"nav",Toast.LENGTH_SHORT).show()
        val options = configOptions()
        options.isLayoutVisible = true
        navView.onCreate(null)
        navView.viewOptions = options
        navView.setAMapNaviViewListener(this)
        aMapNav.addAMapNaviListener(this)
    }

    private fun configOptions(): AMapNaviViewOptions {
        val options = navView.viewOptions
        options.isScreenAlwaysBright = true
        options.isTrafficInfoUpdateEnabled = true
        mOptions?.let {
            options.isLayoutVisible = it.showUIElements
            options.setModeCrossDisplayShow(it.showCrossImage)
            options.isTrafficLayerEnabled = it.showTrafficButton
            options.isTrafficBarEnabled = it.showTrafficBar
            options.isRouteListButtonShow = it.showBrowseRouteButton
            options.isSettingMenuEnabled = it.showMoreButton
        }
        return options
    }

    fun setup() {
        when (atomicInteger.get()) {
            STOPPED -> {
                navView.onCreate(null)
                navView.onResume()
                navView.onPause()
            }
            RESUMED -> {
                navView.onCreate(null)
                navView.onResume()

            }
            CREATED -> {
                navView.onCreate(null)

            }
            DESTROYED -> {
                navView.setAMapNaviViewListener(null)
                navView.onDestroy()
                aMapNav.removeAMapNaviListener(this)
                aMapNav.stopNavi()
                AMapNavi.destroy()
            }
        }
        registrar.activity().application.registerActivityLifecycleCallbacks(this)
    }

    override fun getView(): View {
        return rootView
    }

    override fun dispose() {
        if (disposed) return

        disposed = true
        navView.setAMapNaviViewListener(null)
        navView.onDestroy()
        aMapNav.removeAMapNaviListener(this)
        aMapNav.stopNavi()
        AMapNavi.destroy()

        navChannel.setMethodCallHandler(null)
        registrar.activity().application.unregisterActivityLifecycleCallbacks(this)

    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        if (call.method == "startNav") {

            var model = Coordinate()
            model.latitude = 22.933251
            model.longitude = 113.216944
            latlon = model
            initNav()

        }
    }

    override fun onActivityCreated(p0: Activity, savedInstanceState: Bundle?) {
        if (disposed || activity.hashCode() != registrar.activity().hashCode()) {
            return;
        }
        navView.onCreate(savedInstanceState);
    }

    override fun onActivityStarted(p0: Activity) {
        if (disposed || activity.hashCode() != registrar.activity().hashCode()) {
            return;
        }
    }

    override fun onActivityResumed(p0: Activity) {
        if (disposed || activity.hashCode() != registrar.activity().hashCode()) {
            return;
        }
        navView.onResume()
    }

    override fun onActivityPaused(p0: Activity) {
        if (disposed || activity.hashCode() != registrar.activity().hashCode()) {
            return;
        }
        navView.onPause()
    }

    override fun onActivityStopped(p0: Activity) {
        if (disposed || activity.hashCode() != registrar.activity().hashCode()) {
            return;
        }
    }

    override fun onActivitySaveInstanceState(p0: Activity, outState: Bundle) {
        if (disposed || activity.hashCode() != registrar.activity().hashCode()) {
            return;
        }
        navView.onSaveInstanceState(outState)
    }

    override fun onActivityDestroyed(p0: Activity) {
        if (disposed || activity.hashCode() != registrar.activity().hashCode()) {
            return;
        }
        navView.onDestroy()
    }

    override fun onInitNaviFailure() {

    }
    protected var mEndLatlng = NaviLatLng(40.084894, 116.603039)
    protected var mStartLatlng = NaviLatLng(39.825934, 116.342972)
    protected val sList: ArrayList<NaviLatLng> = ArrayList()
    protected val eList: ArrayList<NaviLatLng> = ArrayList()
    protected var mWayPointList: ArrayList<NaviLatLng> = ArrayList()
    override fun onInitNaviSuccess() {
        latlon?.let {
            var strategy = 0
            try {
                strategy = aMapNav.strategyConvert(true, false, false, false, false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //设置模拟导航的行车速度
            aMapNav.setEmulatorNaviSpeed(75)

            sList.add(mStartLatlng)
            eList.add(mEndLatlng)
            aMapNav.calculateDriveRoute(sList, eList, mWayPointList, strategy)
        }
    }

    override fun onStartNavi(p0: Int) {

    }

    override fun onTrafficStatusUpdate() {

    }

    override fun onLocationChange(p0: AMapNaviLocation?) {

    }

    override fun onGetNavigationText(p0: Int, p1: String?) {

    }

    override fun onGetNavigationText(p0: String?) {

    }

    override fun onEndEmulatorNavi() {

    }

    override fun onArriveDestination() {

    }

    override fun onCalculateRouteFailure(p0: Int) {

    }

    override fun onCalculateRouteFailure(p0: AMapCalcRouteResult?) {

    }

    override fun onReCalculateRouteForYaw() {

    }

    override fun onReCalculateRouteForTrafficJam() {

    }

    override fun onArrivedWayPoint(p0: Int) {

    }

    override fun onGpsOpenStatus(p0: Boolean) {

    }

    override fun onNaviInfoUpdate(p0: NaviInfo?) {

    }

    override fun updateCameraInfo(p0: Array<out AMapNaviCameraInfo>?) {

    }

    override fun updateIntervalCameraInfo(p0: AMapNaviCameraInfo?, p1: AMapNaviCameraInfo?, p2: Int) {

    }

    override fun onServiceAreaUpdate(p0: Array<out AMapServiceAreaInfo>?) {

    }

    override fun showCross(p0: AMapNaviCross?) {

    }

    override fun hideCross() {

    }

    override fun showModeCross(p0: AMapModelCross?) {

    }

    override fun hideModeCross() {

    }

    override fun showLaneInfo(p0: Array<out AMapLaneInfo>?, p1: ByteArray?, p2: ByteArray?) {

    }

    override fun showLaneInfo(p0: AMapLaneInfo?) {

    }

    override fun hideLaneInfo() {

    }

    override fun onCalculateRouteSuccess(p0: IntArray?) {

    }

    override fun onCalculateRouteSuccess(p0: AMapCalcRouteResult?) {
        //todo
        aMapNav.startNavi(NaviType.EMULATOR)
    }

    override fun notifyParallelRoad(p0: Int) {

    }

    override fun OnUpdateTrafficFacility(p0: Array<out AMapNaviTrafficFacilityInfo>?) {

    }

    override fun OnUpdateTrafficFacility(p0: AMapNaviTrafficFacilityInfo?) {

    }

    override fun updateAimlessModeStatistics(p0: AimLessModeStat?) {

    }

    override fun updateAimlessModeCongestionInfo(p0: AimLessModeCongestionInfo?) {

    }

    override fun onPlayRing(p0: Int) {

    }

    override fun onNaviRouteNotify(p0: AMapNaviRouteNotifyData?) {

    }

    override fun onGpsSignalWeak(p0: Boolean) {

    }

    override fun onNaviSetting() {

    }

    override fun onNaviCancel() {

    }

    override fun onNaviBackClick(): Boolean {
        navChannel.invokeMethod("close_nav", null)
        return false
    }

    override fun onNaviMapMode(p0: Int) {

    }

    override fun onNaviTurnClick() {

    }

    override fun onNextRoadClick() {

    }

    override fun onScanViewButtonClick() {

    }

    override fun onLockMap(p0: Boolean) {

    }

    override fun onNaviViewLoaded() {

    }

    override fun onMapTypeChanged(p0: Int) {

    }

    override fun onNaviViewShowMode(p0: Int) {

    }


}