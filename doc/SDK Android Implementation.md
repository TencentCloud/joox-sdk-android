# SDK Android Implementation

## 1.Apply For APPID

Please apply for an APPId (Only support our partnter now, please contact us if you need).Please do fulfil all the neccessary info and then copy the assigned APPID and store it to the project.

For example:

```
<string name="app_id">xxxxxxxx</string>
```



## 2.Configuration

1.please copy our library under "../libs/ " and then add the implementation in build.gradle.

```
implementation(name:'JOOX_SDK_2.1.0.1',ext:'aar')
implementation(name:'OpenSDK-release',ext:'aar')
implementation(name:'fingerprint_pure_lib-1.0.2-release',ext:'aar')

```



2.please add all the dependencies that will be used in build.gradle.

```
    api (name:'OpenSDK-release',ext:'aar')
    api(name:'sdklibrary-release',ext:'aar')
    implementation 'com.squareup.okhttp3:okhttp:3.12.4'
    implementation 'com.tencent.mars:mars-xlog:1.2.3'
    implementation "com.google.android.exoplayer:exoplayer-core:r2.5.1"
    implementation "com.google.android.exoplayer:exoplayer-hls:r2.5.1"
    implementation 'com.tencent.mars:mars-xlog:1.2.3'
    implementation "com.google.code.gson:gson:2.8.5"
    implementation 'com.tencent:mmkv-static:1.1.0'
```

3.please modify repositories in build.gradle to ensure that all the 3-rd party packages can be implemented.

## 3.Implementation

1.Please do init JOOX SDK inside an Application.If no specific deviceId to set,deviceId can be null and androidId will be set instead. 

```
override fun onCreate() {
    super.onCreate()
    instance = this
    var scopeList = ArrayList<String>()
    var deviceId = "";
    //scopeLists depends on rights applied on the platform,if you are not sure what to put 
    scopeList.add("public+user_profile+playmusic_free") 		           SDKInstance.getmInstance().init(instance, deviceId,getString(R.string.app_id), packageName, scopeList)
}
```

2.Please do declare our entry point Activity in AndroidManifest.xml of application module so that auth status can be passed and handled.

```
<activity android:name="com.joox.sdklibrary.kernel.jxapi.JXEntryActivity"
    android:screenOrientation="portrait"
    android:exported="true"
    />
```

3.please do init SDK with Application context.

```
    /**
     *
     * @param context better to be application context
     * @param deviceId  unique identifier  for per devices,if null, will use android id instead
     * @param appId please following instructions in doc to get APPID
     * @param appName package name of your application
     * @param authType please select authtype defined in AuthType.java
     * @param scopeList please following instructions in OpenAPI Document 
     */
    @Override
    public void init(Context context, String deviceId, String appId, String appName, int authType, ArrayList<String> scopeList) 
```

4.If any callback from JOOX need to be implemented, please do register listener to get updated with status inside Activity:

```
SDKInstance.getmInstance().registerSDKListener(SDKListener listener)
```

and listeners can be unregistered by:

```
public void unregisterListener(SDKListener listener)
```

5.Call JOOX SDK to authoirize

```
SDKInstance.getmInstance().auth()
```

Please be noticed that if AuthType.AUTH_OPENID has been set to authtype,following value need also to be set before auth() is called:

```
SDKInstance.mSessionKey = openIdInfo.session_key
SDKInstance.mOpenId = openIdInfo.wmid
SDKInstance.getmInstance().auth()
```

6.If need to logout JOOX, please call logout() to clean all login info of SDK:

```
public void logout();
```

7.If you want to disconnect with JOOX SDK,please call:

```
public void exitJOOX();
```

SDK will clean all the memories used by JOOX.



## 4.For FeedBack Tips

please authorize "storage" in the setting page for demo,It will generate Log under directory:

```
Android/data/{package name}/files/joox_for_third/log
```

Please do send files under this directory along with your ***JOOX ID*** to us if any error occurs.

