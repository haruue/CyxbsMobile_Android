package com.mredrock.cyxbs.network.service;

import com.mredrock.cyxbs.config.Const;
import com.mredrock.cyxbs.model.UpdateInfo;
import com.mredrock.cyxbs.network.setting.annotation.XmlApi;

import retrofit2.http.GET;
import rx.Observable;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */

public interface HaruueApiService {

    @GET(Const.MaterialSpecialVersion.HARUUE_API_BASE_URL + Const.MaterialSpecialVersion.API_UPDATE)
    @XmlApi
    Observable<UpdateInfo> update();

}
