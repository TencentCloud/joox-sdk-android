package com.tencent.joox.sdk

import com.joox.sdklibrary.SDKInstance


class RequestParamBuilder {

    protected var URLBuilder: StringBuilder = StringBuilder()

    val urlString: String
        get() = this.URLBuilder.toString()

    fun RequestParamBuilder() {
        URLBuilder = StringBuilder()

    }

    fun setCommonParam(): RequestParamBuilder {
        URLBuilder.append(JOOXRequestCommonParam.CLIENT_TAG).append(SDKInstance.getmInstance().getmAppInfo().getKey())
        setConnector()
        URLBuilder.append(JOOXRequestCommonParam.bundle_id).append(SDKInstance.getmInstance().getmAppInfo().packageName)
        return this
    }

    fun setConnector(): RequestParamBuilder {
        URLBuilder.append("&")
        return this
    }

    fun setTokenParam(): RequestParamBuilder {
        URLBuilder.append(JOOXRequestCommonParam.RESPONSE_TYPE).append("token")
        return this
    }

    fun setParam(param: String): RequestParamBuilder {
        URLBuilder.append(param)
        return this
    }

    fun setCountryParam(): RequestParamBuilder {
        URLBuilder.append(JOOXRequestCommonParam.country).append("hk")
        return this
    }

    fun setCountryParam(country: String): RequestParamBuilder {
        URLBuilder.append(JOOXRequestCommonParam.country).append(country)
        return this
    }


    fun setIndexParam(index: Int): RequestParamBuilder {
        URLBuilder.append(JOOXRequestCommonParam.index).append(index).toString()
        return this
    }

    fun setLanguageParam(): RequestParamBuilder {
        URLBuilder.append(JOOXRequestCommonParam.language).append("en").toString()
        return this
    }

    fun setLanguageParam(lan: String): RequestParamBuilder {
        URLBuilder.append(JOOXRequestCommonParam.language).append(lan).toString()
        return this
    }


    fun setNumParam(): RequestParamBuilder {
        URLBuilder.append(JOOXRequestCommonParam.number).append(50).toString()
        return this
    }

    fun getResultString():String{
        return URLBuilder.toString()
    }

    companion object {
        val TAG = "BaseRequestComposer"
    }

}
