import 'dart:async';

import 'package:flutter/services.dart';

class Mapdemo {
  static const MethodChannel _channel =
      const MethodChannel('com.kevinhqf.mapdemo/navi');

  static Future<void> startNavi() async {
    await _channel.invokeMethod("startNav");
  }
}
