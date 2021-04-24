import 'dart:io';

import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:mapdemo/mapdemo.dart';
import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(MyApp());
}

final List<Permission> needPermissionList = [
  Permission.location,
  Permission.storage,
  Permission.phone,
];

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
    _checkPermissions();
  }

  @override
  void reassemble() {
    super.reassemble();
    _checkPermissions();
  }

  void _checkPermissions() async {
    Map<Permission, PermissionStatus> statuses =
        await needPermissionList.request();
    statuses.forEach((key, value) {
      print('$key permissionStatus is $value');
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = "0"; //await Mapdemo.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }


  @override
  Widget build(BuildContext context) {
    if (Platform.isAndroid) {
      SystemChrome.setEnabledSystemUIOverlays([SystemUiOverlay.bottom]);
    }

    return MaterialApp(
      home: Scaffold(
          body: Center(
        child: Column(
          children: [topInfo, map, bottom],
        ),
      )),
    );
  }

  Widget map = Container(
    child: Expanded(
      child: AndroidView(viewType: "com.kevinhqf.mapdemo/FlutterAMapNavView"),
    ),
  );

  Widget bottom = GestureDetector(
    onTap: Mapdemo.startNavi,
    child: Container(
      color: Colors.lightBlueAccent,
      child: Center(
        child: Text(
          "乘客上车确认",
          style: TextStyle(color: Colors.white),
        ),
      ),
      height: 65,
    ),
  );

  Widget topInfo = Container(
      child: Container(
    color: Colors.black,
    height: 45,
    child: Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: [
        Container(
          child: Center(
            child: Text(
              "距离起点0公里",
              style: TextStyle(color: Colors.white),
            ),
          ),
        ),
        Container(
          child: Center(
            child: Text(
              "预计用时0分钟",
              style: TextStyle(color: Colors.white),
            ),
          ),
        ),
      ],
    ),
  ));
}
