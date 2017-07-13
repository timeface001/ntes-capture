package com.fs.dreams;

import com.alibaba.fastjson.JSON;
import com.fs.dreams.utils.FStringUtil;
import com.fs.dreams.utils.GeneralUtils;
import com.fs.dreams.utils.OkHttpUtil;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fengsong
 * @description:
 * @time 2017-07-12 20:55
 **/
public class CaptureUtils {

    private static final String APPVER = "1.5.2";
    private static final String REFERER = "http://music.163.com/";

    public static Song songDetail(Long id) {

        try {
            String response = OkHttpUtil.get(NtesApi.SONG_DETAIL.replaceAll("\\[id\\]", String.valueOf(id)));
            if (FStringUtil.isNoneBlank(response)) {
                System.out.println(JSON.toJSONString(JSON.parseObject(response), true));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Artist> searchArtist(String artistName, int pageNum, int pageSize) {
        List<Artist> resultList = new LinkedList<Artist>();
        try {
            String response = OkHttpUtil.post(NtesApi.SEARCH_URL, GeneralUtils.generateMap(
                    "s", artistName, "offset", getOffset(pageNum, pageSize), "limit", pageSize, "type", NtesApi.SEARCH_TYPE_ARTIST));
            if (FStringUtil.isNoneBlank(response)) {
                System.out.println(JSON.toJSONString(JSON.parseObject(response), true));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static List<Artist> getTopArtist(int pageNum, int pageSize) {
        try {
            Interceptor e = new Interceptor() {
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .addHeader("appver", APPVER)
                            .addHeader("Referer", REFERER)
                            .build();
                    return chain.proceed(request);
                }
            };
            OkHttpUtil.addInterceptor(e);

            System.out.println(OkHttpUtil.get("http://music.163.com/api/artist/top?limit=10&offset=0"));

            OkHttpUtil.removeInterceptor(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static int getOffset(int pageNum, int pageSize) {
        return pageNum == 0 ? 0 : (pageNum - 1) * pageSize;
    }

    public static void main(String[] args) {
        songDetail(37196629L);
    }
}
