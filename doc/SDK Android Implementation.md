# SDK Android Implementation


## 1.Configuration

1. Add maven warehouse configuration
```
maven {
    url 'https://g-ioun5297-maven.pkg.coding.net/repository/joox/sdk/'
    credentials {
        username = codingArtifactsGradleUsername
        password = codingArtifactsGradlePassword
    }
}

```

2. implementation sdk in build.gradle.

```
implementation("com.tme.joox:sdklibrary:2.4.0.14")

```


3.please modify repositories in build.gradle to ensure that all the 3-rd party packages can be implemented.

## 3.Implementation

1.Please do init JOOX SDK inside an Application.If no specific deviceId to set,deviceId can be null and androidId will be set instead.

```
override fun onCreate() {
    super.onCreate()
    Log.d("SDKApplication", "onCreate called!!!!");
    var scopeList = ArrayList<String>()
    scopeList.add("public+user_profile+playmusic") 
    SDKGlobal.isDebug = BuildConfig.DEBUG
    SDKGlobal.networkConfig = NetworkConfig(true)
    val appKey = "input app key"
    val appPkg = "input app package name"
    SDKInstance.getIns().init(instance, appKey, appPkg, scopeList)
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
     * @param context better to be application context
     * @param appId please following instructions in doc to get APPID
     * @param appName package name of your application
     * @param scopeList please following instructions in OpenAPI Document
     */
    @Override
    public void init(Context context, String appId, String appName,  ArrayList<String> scopeList)
```

4.If any callback from JOOX need to be implemented, please do register listener to get updated with status inside Activity:

```
SDKInstance.getIns().registerSDKListener(SDKListener listener)
```

and listeners can be unregistered by:

```
public void unregisterListener(SDKListener listener)
```

5.Call JOOX SDK to authoirize

```
SDKInstance.getIns().auth()
```

Please be noticed that if AuthType.AUTH_OPENID has been set to authtype, following value need also to be set before auth() is called:

```
SDKInstance.mSessionKey = openIdInfo.session_key
SDKInstance.mOpenId = openIdInfo.wmid
SDKInstance.getIns().auth()
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
/joox_for_third/log
```

Please do send files under this directory along with your ***JOOX ID*** to us if any error occurs.

