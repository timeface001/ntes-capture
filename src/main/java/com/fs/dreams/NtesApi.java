package com.fs.dreams;

/**
 * @author fengsong
 * @description:一句话描述下类的功能
 * @time 2017-07-13 9:26
 **/
public interface NtesApi {

    /**
     * 歌曲
     */
    Integer SEARCH_TYPE_SONG = 1;

    /**
     * 专辑
     */
    Integer SEARCH_TYPE_ALBUM = 10;

    /**
     * 歌手
     */
    Integer SEARCH_TYPE_ARTIST = 100;

    /**
     * 歌单
     */
    Integer SEARCH_TYPE_PLAYLIST = 1000;

    /**
     * 用户
     */
    Integer SEARCH_TYPE_USER = 1002;

    /**
     * MV
     */
    Integer SEARCH_TYPE_MV = 1004;

    /**
     * 歌词
     */
    Integer SEARCH_TYPE_LYRIC = 1006;

    /**
     * 电台主播
     */
    Integer SEARCH_TYPE_RADIO = 1009;

    /**
     * POST请求
     * s：搜索的内容  offset：偏移量（分页用） limit：获取的数量  type：搜索的类型
     * 歌曲 1
     * 专辑 10
     * 歌手 100
     * 歌单 1000
     * 用户 1002
     * mv 1004
     * 歌词 1006
     * 主播电台 1009
     */
    String SEARCH_URL = "http://music.163.com/api/search/pc";

    /**
     * GET
     * 歌曲信息
     * id：歌曲ID  ids：不知道干什么用的，用[]括起来的歌曲ID
     */
    String SONG_DETAIL = "http://music.163.com/api/song/detail/?id=[id]&ids=[id]";

    /**
     * GET
     * 歌词
     * id：歌曲ID
     * lv：值为-1，我猜测应该是判断是否搜索lyric格式
     * kv：值为-1，这个值貌似并不影响结果，意义不明
     * tv：值为-1，是否搜索tlyric格式
     */
    String SONG_LYRIC = "http://music.163.com/api/song/lyric?os=pc&id=[id]&lv=-1&kv=-1&tv=-1";

    /**
     * GET
     * <p>
     * 专辑信息
     */
    String ALBUM_DETAIL = "http://music.163.com/api/album/[id]?ext=true&id=[id]&offset=[offset]&total=true&limit=[limit]";

    /**
     * GET
     * 歌单信息
     */
    String PLAYLIST_DETAIL = "http://music.163.com/api/playlist/detail?id=[id]&updateTime=-1";

    /**
     * GET
     * MV信息
     */
    String MV_DETAIL = "http://music.163.com/api/mv/detail?id=[id]&type=mp4";

    /**
     * top歌曲
     */
    String SONG_TOP="http://music.163.com/api/artist/top?limit=[limit]&offset=[offset]";

    /**
     * 歌手下面歌曲
     */
    String ARTIST_SONG="http://music.163.com/api/cloudsearch/get/web?type=[type]&s=[s]&limit=[limit]&offset=[offset]";
}
