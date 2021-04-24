#import "MapdemoPlugin.h"
#if __has_include(<mapdemo/mapdemo-Swift.h>)
#import <mapdemo/mapdemo-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "mapdemo-Swift.h"
#endif

@implementation MapdemoPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftMapdemoPlugin registerWithRegistrar:registrar];
}
@end
