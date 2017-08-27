package htmldemo.wangpy.com.banner.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dell on 2017/8/27.
 * 数据库 存储广告下载路径
 */

public class DbHelper extends SQLiteOpenHelper {
    //数据库版本号
    private static final int VERSION = 1;
    //广告表
    public static final String TABLE_NAME_ADVER = "adverdb";
    private static final String SQL_CREATE_ADVER = "create table IF NOT EXISTS " +
            TABLE_NAME_ADVER + "(_id integer primary key autoincrement," + "ad_id varchar(8)," +
            "end varchar(32),title varchar(128),url varchar(128),typeid varchar(2),androidimg " +
            "varchar(128),flag varchar(2))";


    private static final String SQL_DROP_ADVER = "drop table if exists plistdb" + TABLE_NAME_ADVER;
    private static DbHelper sDbHelper = null;


    public DbHelper(Context context) {
        super(context, TABLE_NAME_ADVER, null, VERSION);
    }

    //单例模式拿到数据库实例
    public static DbHelper getInstance(Context context) {
        if (null == sDbHelper) {
            sDbHelper = new DbHelper(context);
        }

        return sDbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initAllTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 初始化所有表
     *
     * @param db
     */
    private void initAllTables(SQLiteDatabase db) {
        try {
            db.execSQL(SQL_CREATE_ADVER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除所有表
     *
     * @param db
     */
    private void dropAllTables(SQLiteDatabase db) {
        db.execSQL(SQL_DROP_ADVER);
        db.execSQL(SQL_CREATE_ADVER);
    }
}
