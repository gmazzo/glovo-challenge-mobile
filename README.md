# glovo-challenge-mobile

## Setup notes:
1. This project **must be open from root folder** with *Android Studio*, where the `settings.gradle` is located.
1. You need to use your own GoogleMaps API key to actually see a map.
1. It has built-in cache support, which can be disabled by changing `buildConfigField USE_CACHE` value to `false`.
1. Polygons are merged by default, it can be disabled by switching `buildConfigField MERGE_POLYGONS` to `false`.
1. The `buildConfigField API_ENDPOINT` points by default to a *fantasy* server. There is a `device` buildType which points to the same host machine where an emulator is running. It should also work with real devices on the same WiFi network.
1. There is a `mock` buildType, which preloads all data from the server and stores as assets. [okhttp-client-mock](https://github.com/gmazzo/okhttp-client-mock) is used to intercept HTTPs requests.
1. The server can be started by running `./gradlew server:run`
