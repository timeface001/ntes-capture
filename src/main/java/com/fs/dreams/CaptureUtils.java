package com.fs.dreams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fs.dreams.utils.*;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author fengsong
 * @description:
 * @time 2017-07-12 20:55
 **/
public class CaptureUtils {

    private static final String APPVER = "1.5.2";
    private static final String REFERER = "http://music.163.com/";

    private static final String REQUEST_CODE_SUCCESS = "200";

    private static String getBracket(String s) {
        return "\\[" + s + "\\]";
    }



    public static Song songDetail(Long id) {

        Song song = null;
        try {
            String response = OkHttpUtil.get(NtesApi.SONG_DETAIL.replaceAll(getBracket("id"), String.valueOf(id)));
            if (FStringUtil.isNoneBlank(response)) {
                JSONObject jsonObject = JSON.parseObject(response);

                if (!jsonObject.getJSONArray("songs").isEmpty()) {
                    JSONObject jo = jsonObject.getJSONArray("songs").getJSONObject(0);
                    song = new Song();
                    song.setName(jo.getString("name"));
                    song.setSrcId(jo.getLong("id"));
                    song.setCreateTime(new Date());
                    song.setAlbumSrcId(jo.getJSONObject("album").getLong("id"));
                    song.setDuration(jo.getInteger("duration"));
                    song.setCommentThreadId(jo.getString("commentThreadId"));
                    song.setMvId(jo.getLong("mvid"));
                    song.setScore(jo.getInteger("score"));
                    song.setPopularity(jo.getInteger("popularity"));
                    song.setLyric(getLyric(jo.getLong("id")));

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return song;
    }

    public static String getLyric(Long id) {
        try {
            String response = OkHttpUtil.get(NtesApi.SONG_LYRIC.replace("[id]", String.valueOf(id)));
            JSONObject jsonObject = JSON.parseObject(response);
            if (REQUEST_CODE_SUCCESS.equals(jsonObject.getString("code"))) {
                return jsonObject.getJSONObject("lrc").getString("lyric");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return FStringUtil.EMPTY;
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
        List<Artist> artists = new ArrayList<Artist>();
        try {
            Interceptor e = new Interceptor() {
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .addHeader("Cookie", "appver=" + APPVER)
                            .addHeader("Referer", REFERER)
                            .build();
                    return chain.proceed(request);
                }
            };
            OkHttpUtil.addInterceptor(e);

            String response = OkHttpUtil.get(NtesApi.ARTIST_TOP.replace("[limit]", String.valueOf(pageSize)).replace("[offset]", getOffset(pageNum, pageSize)));
            if (FStringUtil.isNoneBlank(response)) {
                JSONArray array = JSON.parseObject(response).getJSONArray("artists");
                if (Objects.nonNull(array) && !array.isEmpty()) {
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject jb = array.getJSONObject(i);
                        Artist artist = new Artist();
                        artist.setSrcId(jb.getLong("id"));
                        artist.setAlbumSize(jb.getInteger("albumSize"));
                        artist.setAlias(FJSONUtil.joinJSONArray(jb.getJSONArray("alias"), ","));
                        artist.setAvatar(jb.getString("picUrl"));
                        artist.setMusicSize(jb.getInteger("musicSize"));
                        artist.setCreateTime(new Date());
                        artist.setName(jb.getString("name"));

                        artists.add(artist);
                    }
                }
            }

            OkHttpUtil.removeInterceptor(e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return artists;
    }

    public static List<Song> searchSongs(String artistName, int pageNum, int pageSize) {
        List<Song> songs = new ArrayList<Song>();
        try {
            String response = OkHttpUtil.post(NtesApi.ARTIST_SONG, GeneralUtil.generateMap("s", artistName, "offset", getOffset(pageNum, pageSize), "limit", pageSize, "type", NtesApi.SEARCH_TYPE_SONG));
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return songs;
    }

    public static List<Song> searchSongsByAlbumName(String albumName, Long albumId, int pageNum, int pageSize) {
        List<Song> songs = new ArrayList<Song>();
        try {
            String response = OkHttpUtil.post(NtesApi.ARTIST_SONG, GeneralUtil.generateMap("s", albumName, "offset", getOffset(pageNum, pageSize), "limit", pageSize, "type", NtesApi.SEARCH_TYPE_SONG));
            if (StringUtils.isNotBlank(response)) {
                JSONObject jsonObject = JSON.parseObject(response);
                if (jsonObject.getInteger("songCount") != 0) {
                    JSONArray jsonArray = jsonObject.getJSONArray("songs");

                    for (int i = 0; i < jsonArray.size(); i++) {
                        Song song = new Song();
                        JSONObject jb = jsonArray.getJSONObject(i);

                        //验证albumID
                        if (albumId.equals(jb.getJSONObject("al").getLong("id"))) {
                            songs.add(songDetail(jb.getLong("id")));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return songs;
    }


    private static String getOffset(int pageNum, int pageSize) {
        return String.valueOf(pageNum == 0 ? 0 : (pageNum - 1) * pageSize);
    }

    public static void main(String[] args) {
        //System.out.println(JSON.toJSONString(getTopArtist(1,100),true));;
        //searchSongs("周杰伦", 1, 10);
        System.out.println(JSON.toJSONString(songDetail(418602084L)));
    }
}
