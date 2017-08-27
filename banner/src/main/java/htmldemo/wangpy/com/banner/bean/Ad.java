package htmldemo.wangpy.com.banner.bean;

import java.io.Serializable;

/**
 * Created by dell on 2017/8/27.
 */

public class Ad implements Serializable {
    private String androidimg;//广告图片的url
    private String end;// 过期时间的时间戳
    private String id;
    private String rank;//广告的排序
    private String shorturl;
    private String title;//广告的标题
    /**
     * 广告类型 1:文字 2:图片 3:图文
     */
    private String typeid;
    private String url;//点击广告跳转的url
    private String flag;//0：广告可点击


    public String getAndroidimg() {
        return androidimg;
    }

    public void setAndroidimg(String androidimg) {
        this.androidimg = androidimg;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getShorturl() {
        return shorturl;
    }

    public void setShorturl(String shorturl) {
        this.shorturl = shorturl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
