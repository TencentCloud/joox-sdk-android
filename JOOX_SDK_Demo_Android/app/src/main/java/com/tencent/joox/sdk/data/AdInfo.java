package com.tencent.joox.sdk.data;

import java.util.List;

public class AdInfo {
    /*
    {
    "error": {
        "code": 0,
        "message": "success"
    },
    "head": {
        "timestamp": "1582796337",
        "nonce": "025e4f0336822612688071700",
        "signature": "fcf4d41e03630de9130454a19538a8586e04ddde"
    },
    "result": {
        "ad_infos": [
            {
                "ad_id": "109360793298866",
                "adcreative_template_id": "10001",
                "adcreative_elements": {
                    "adcreative_id": "109360793326142",
                    "jump_target": "wemusic: //www.joox.com?reportChannel=1&reportType=4&reportDetail=TIA&page=videoMessageBoard&id=123",
                    "update_time": "1535369924",
                    "image_url": "http: //pic-1252507790.file.myqcloud.com/109360782850621_0.jpg",
                    "title": "test",
                    "subtitle": "test",
                    "audio_url": "http: //audio-1252507790.file.myqcloud.com/109360784601394.mp3",
                    "button_text": "Learnmore",
                    "jump_type": "IN_APP_WEBPAGE",
                    "show_type": 1,
                    "audio_duration": 18573,
                    "skip_type": 1
                }
            }
        ]
    }
}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean{
        private List<AdBean> ad_infos;

        public List<AdBean> getAd_infos() {
            return ad_infos;
        }

        public void setAd_infos(List<AdBean> ad_infos) {
            this.ad_infos = ad_infos;
        }
    }

    public static class AdBean{
        private String ad_id;
        private AdElementsBean adcreative_elements;

        public String getAd_id() {
            return ad_id;
        }

        public void setAd_id(String ad_id) {
            this.ad_id = ad_id;
        }

        public AdElementsBean getAdcreative_elements() {
            return adcreative_elements;
        }

        public void setAdcreative_elements(AdElementsBean adcreative_elements) {
            this.adcreative_elements = adcreative_elements;
        }
    }

    public static class AdElementsBean{
        private String audio_url;

        public String getAudio_url() {
            return audio_url;
        }

        public void setAudio_url(String audio_url) {
            this.audio_url = audio_url;
        }
    }

}
