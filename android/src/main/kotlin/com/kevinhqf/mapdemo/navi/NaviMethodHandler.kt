package com.kevinhqf.mapdemo.navi

import com.amap.api.navi.model.*
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class NaviMethodHandler : MethodChannel.MethodCallHandler {

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "startNavi" -> {
                val startLat = call.argument<Double>("startLat") ?: 39.825934
                val startLng = call.argument<Double>("startLng") ?: 116.342972
                val endLat = call.argument<Double>("endLat") ?: 40.084894
                val endLng = call.argument<Double>("endLng") ?: 116.603039
                var endLatlng = NaviLatLng(endLat, endLng)
                var startLatlng = NaviLatLng(startLat, startLng)

            }
        }

    }


}
