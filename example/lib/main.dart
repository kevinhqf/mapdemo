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
  int _time=0;
  int _distance=0;
  @override
  void initState() {
    super.initState();
    _checkPermissions();
    MapDemo.methodChannel.setMethodCallHandler(platformCallHandler);
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      MapDemo.startNavi( 22.819884,113.284958);
    });
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

  Future<dynamic> platformCallHandler(MethodCall call)async{
    switch(call.method){
      case "onNaviInfoUpdate":
       setState(() {
         _distance = call.arguments['distance'];
         _time = call.arguments['time'];
       });
        break;
    }
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
          children: [topInfo(), map, bottom],
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
    onTap: (){},
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

  Widget topInfo(){
    return Container(
        child: Container(
          color: Colors.black,
          height: 45,
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              Container(
                child: Center(
                  child: Text(
                    '距离终点$_distance公里',
                    style: TextStyle(color: Colors.white),
                  ),
                ),
              ),
              Container(
                child: Center(
                  child: Text(
                    "预计用时$_time分钟",
                    style: TextStyle(color: Colors.white),
                  ),
                ),
              ),
            ],
          ),
        ));
  }

}
