# JOOX SDK Document

## API

```
public void init(Context context, String appId, String appName,  ArrayList<String> scopeList);
```

```
public void auth();
```

Login via ticket_token:
```
SDKInstance.ticketToken = {ticketToken};
public void auth();
```
```
public void refreshUserInfo();
```
```
public void registerListener(SDKListener listener);
```

```
public void unregisterListener(SDKListener listener);
```

Note: Implementing this API will replace the processing of Audio Focus inside the SDK
```
void addCustomAudioFocusListener(CustomAudioFocusChangeListener listener);
```

```
void removeCustomAudioFocusChangeListener(CustomAudioFocusChangeListener listener);
```

```
public void play(BaseSongInfo songinfo, PlayCallBack callback);
```

```
public void playAd(BaseAdInfo adInfo, PlayCallBack callback);
```

```
public void seekTo(long position);
```

```
public void resumePlay()
```

```
public void pausePlay();
```
```
public void stopPlay();
```
```
public void setEnableRequestAudioFocus(boolean isEnable, boolean abandonAudioFocus);
```

Note:**urlPathString** need to be API name,**urlQueryString** need to be matched query parameters defined in https://docs.google.com/document/d/12TEjEWYzMIWyDvozBFIKGjziQtuMCRS7yd94Itn85Lg.

```
public void doJooxRequest(String urlPathString,String urlQueryString, SceneBase.OnSceneBack callback);
```


Note:logout() can be called if you need to logout joox but it will still listening to status in SDK

```
public void logout();
```

Note:exitJOOXSDK() can be called to release all the listeners and memories used in SDK

```
public void exitJOOXSDK();
```

## AuthType

3 different ways can be used to login JOOX:

```
AUTH_WITH_MOBILE
AUTH_WITH_QRCODE
AUTH_OPENID
AUTH_TICKET_TOKEN
```

## SDKListener

get current auth state

```
    public void currentAuthState(int authState);
    
    public void updateCurrentPlayState(int playState)

```

AuthState：

```
public class AuthState {
    public static int INITED = 0;            
    public static int RUNNING= 1;            
    public static int CANCELLED = 2;         
    public static int  FAILED =3 ;           
    public static  int  SUCCESS = 4;         
}
```

PlayerState：

```
public interface PlayerState {

    int IDLE = 0;
    int FINISH = 1;
    int PREPARED = 2;
    int PREPARING = 3;
    int ERROR = 4;
    int LOADING = 5;
    int PAUSE = 6;
    int PLAYING = 7;
}
```

Example:

```
    override fun currentAuthState(authState: Int) {
        runOnUiThread{
            this.authState = authState
            if (authState == AuthState.SUCCESS) {
								//todo when login success
            } else {
								//todo when login fail
            }
        }
    }
```

## PlayCallBack

Callback for play function,will return error code if songs cannot be played.while song is playing,progress will be updated continuously.

```
public interface PlayCallBack {
    public void onPlayResult(int code);

    public void onPlayProgress(long progress, long during);
}
```

PlayErrState Result Code
```
public class PlayErrState {


    /**
     * Play Success
     */
    public static final int SUCCESS = 0;

    /**
     * Failed to obtain song playback information
     */
    public static final int UPDATE_SONG_INFO_ERROR = 1;

    /**
     * Playback frequency limit for non-VIP users (no more limit starting from SDK 2.4.0 version)
     */
    @Deprecated
    public static final int LIMIT_ERROR = 2;

    /**
     * 播放组件内部错误
     */
    public static final int PLAYER_SUPPORT_ERROR = 3;

    /**
     * Internal error in playback component
     */
    public static final int NO_RESOURCE_URL_IN_SERVER = 4;

    /**
     * Song permission restrictions (for non-VIP users)
     */
    public static final int NO_AUTHORITY_TO_PLAY = 5;

    /**
     * Account scope restrictions
     */
    public static final int TOKEN_SCOPE_LIMITED = 6;

    /**
     * Login expired (expired, playback without login will return: NO_AUTHORITY_TO_PLAY)
     */
    @Deprecated
    public static final int LOGIN_EXPIRED = 7;

    /**
     * Copyright restrictions
     */
    public static final int TOKEN_COPYRIGHT_LIMITED = 8;


    /**
     * unknown error
     */
    public static final int PLAYER_UNKNOW_ERROR = -1;
}
```

## SceneBase.onSceneBack

Callback for network connection.Definition of response code can be found in https://docs.google.com/document/d/12TEjEWYzMIWyDvozBFIKGjziQtuMCRS7yd94Itn85Lg.

```
public void onSuccess(int responseCode,String JsonResult);
public void onFail(int errCode);
```

## ErrorState

```
public class NetworkState {
    public static final int SUCCESS = 200;
    public static final int MAILFORMED_REQUEST = 400; //可能是请求拼错或者未部署，后台不认识相关请求
    public static final int UNAUTHORIZED_ERROR = 401;//授权问题
    public static final int ERROR_PARAM = 403;         //参数问题
    public static final int TOO_MANY_REQUEST = 429;        //请求太多
    public static final int INTERNAL_SERVER_ERROR = 500;   //后台问题
    public static final int SERVICE_UNAVAIL = 503;          //后台问题
    // 回包无法解析
    public static final int JSON_PARSE_ERROR = 10000;       //Server回包解析出错
    //回报无数据
    public static final int ERROR_RESPONSE = 10001;         //回包无法解析出相关数据
    public static final int NO_RESPONSE = 10002;            //没有设置TagId
}
```

```
public class UnauthorizedErrorCode {

    // token scope无效，需要第三方自行核对  SDK给出登录失败/授权失败的错误码
    public static final int ACCESS_DENIED_ERROR = -40101;
    // token 过期，客户端需要重新refresh
    public static final int TOKEN_EXPIRED_ERROR = -40102;
    // token无效，客户端踢出重新登录
    public static final int TOKEN_INVALID_ERROR = -40103;
    // token已经刷新过，不管
    public static final int NOT_READY_TO_REFRESH_ERROR = -40104;
    public static final int TOKEN_EMPTY_ERROR = -40105;
    // 授权问题 保底处理401
    public static final int DEFAULT_ERROR = 401;
}
```

## Report

```
/**
 * @param actionId: Int  
 * 1（曝光） 2（点击） 3（加入歌单） 4（从歌单中移除） 5（like）6（取消like） 7（关注）
 * 8（取消关注）9（下载）10（音频指纹生成-V4.17不上报）11（本地歌曲匹配-V4.17不上报）
 * @param bodyJson: String
 * json格式, 具体上报的字段信息参考该文档：https://docs.qq.com/doc/DRGFFRFZmZnVtVkdV
 */
val builder = f(1, "{body json}")
ReportManager.reportMLRecommend(builder)
```



