package htmldemo.wangpy.com.banner.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import htmldemo.wangpy.com.banner.MyApplication;
import htmldemo.wangpy.com.banner.bean.Ad;

/**
 * Created by dell on 2017/8/27.
 * 数据库辅助类
 */

public class AdvertisementDao {
    private static DbHelper mHelper = DbHelper.getInstance(MyApplication.getContext());

    //数据插入方法
    public synchronized static void insertAd(Ad adver) {
        try {
            //if(!isExists(adver)){
            // 某条广告如果存在，则不进行重复插入
            SQLiteDatabase db = mHelper.getWritableDatabase();
            db.execSQL(
                    "insert into "
                            + DbHelper.TABLE_NAME_ADVER
                            + "(ad_id,end,title,url,typeid,androidimg,flag) values(?,?,?,?,?,?,?)",
                    new Object[]{adver.getId(), adver.getEnd(), adver.getTitle(), adver.getUrl(), adver.getTypeid(), adver.getAndroidimg(), adver.getFlag()
                    });
            db.close();
            //}
        } catch (Exception e) {
        }
    }
    //数据库删除方法
    public synchronized static void deleteAd(Ad adver) {
        try {
            SQLiteDatabase db = mHelper.getWritableDatabase();
            db.delete(DbHelper.TABLE_NAME_ADVER, "ad_id=?", new String[]{adver.getId()});
            db.close();
        } catch (Exception e) {
        }
    }

    /**
     * 获得广告方法
     * @return
     */
    public synchronized static ArrayList<Ad> getAdDataList() {
        ArrayList<Ad> list = new ArrayList<Ad>();
        Cursor cursor = null;
        try {
            SQLiteDatabase db = mHelper.getReadableDatabase();
            cursor = db.rawQuery("select * from " + DbHelper.TABLE_NAME_ADVER
                    + " ", null);
            while (cursor.moveToNext()) {
                Ad info = new Ad();
                info.setId(cursor.getString(cursor.getColumnIndex("ad_id")));
                info.setEnd(cursor.getString(cursor.getColumnIndex("end")));
                info.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
                info.setTypeid(cursor.getString(cursor.getColumnIndex("typeid")));
                info.setAndroidimg(cursor.getString(cursor.getColumnIndex("androidimg")));
                info.setFlag(cursor.getString(cursor.getColumnIndex("flag")));
                list.add(info);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
        }
        return list;

    }

    /**
     * 查询某条广告信息是否存在
     * @return
     */
    public synchronized static boolean isExists(Ad adver) {
        boolean exists = false;
        try {
            SQLiteDatabase db = mHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from " + DbHelper.TABLE_NAME_ADVER
                    + " where ad_id = ? ", new String[] { adver.getId() });
            exists = cursor.moveToNext();
            cursor.close();
            db.close();
        } catch (Exception e) {
        }
        return exists;
    }
}
