package com.kevinhqf.mapdemo

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.kevinhqf.mapdemo.navi.AMapNavViewFactory
import com.kevinhqf.mapdemo.navi.LifecycleProvider
import com.kevinhqf.mapdemo.navi.ProxyLifecycleProvider
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.embedding.engine.plugins.lifecycle.HiddenLifecycleReference
import io.flutter.plugin.common.PluginRegistry


open class MapdemoPlugin : FlutterPlugin, ActivityAware {
    var pluginBinding: FlutterPlugin.FlutterPluginBinding? = null
    var lifecycle: Lifecycle? = null

    companion object {

        @JvmStatic
        fun registerWith(registrar: PluginRegistry.Registrar) {
            val activity = registrar.activity() ?: return

            if (activity is LifecycleOwner) {
                registrar.platformViewRegistry().registerViewFactory(
                        NAV_VIEW_TYPE,
                        AMapNavViewFactory(registrar.messenger(), object : LifecycleProvider {
                            override fun getLifecycle(): Lifecycle {
                                return activity.lifecycle
                            }
                        })
                )
            }else{
                registrar.platformViewRegistry().registerViewFactory(
                        NAV_VIEW_TYPE,
                        AMapNavViewFactory(registrar.messenger(), ProxyLifecycleProvider(activity))
                )
            }

        }

    }

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        pluginBinding = binding
        binding.platformViewRegistry.registerViewFactory(NAV_VIEW_TYPE,
                AMapNavViewFactory(binding.binaryMessenger, object : LifecycleProvider {
                    override fun getLifecycle(): Lifecycle {
                        return lifecycle!!
                    }
                }))
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        pluginBinding = null
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        val reference:HiddenLifecycleReference = binding.getLifecycle() as HiddenLifecycleReference
        lifecycle = reference.lifecycle
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivity() {
        lifecycle = null
    }
}