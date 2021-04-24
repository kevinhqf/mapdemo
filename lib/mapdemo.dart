import 'dart:async';

import 'package:flutter/services.dart';

class MapDemo {
  static const MethodChannel methodChannel =
      const MethodChannel('com.kevinhqf.mapdemo/navi');
  static Future<void> startNavi(double lat,double lng) async {
    await methodChannel
        .invokeMethod("startNav", {'lat':lat, 'lng':lng });
  }


}
