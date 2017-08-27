package htmldemo.wangpy.com.banner.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import htmldemo.wangpy.com.banner.bean.Ad;
import htmldemo.wangpy.com.banner.db.AdvertisementDao;

/**
 * Created by dell on 2017/8/27.
 */

public class AdvertiseUtil {
    /**
     * 应用pack名称
     */
    public static final String APP_PACKAGE = "htmldemo.wangpy.com.banner";

    /**
     * 检查后台是否有广告配置
     *
     * @param context
     */
    public static void getLoadingAd(final Context context, List<Ad> list) {
        //  这里模拟网络请求，假如后台配置了广告信息
        DealWithLoadingAdverResponse(context, list);
    }

    /**
     * 处理广告
     *
     * @param con
     * @param list
     */
    public static void DealWithLoadingAdverResponse(Context con, List<Ad> list) {
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                getAdverImgFromServer(con, list.get(i));
            }
        }

    }

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static long getCurrentDateMil() {
        String t = sdf.format(new Date());
        try {
            return sdf.parse(t).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 联网下载文件
     *
     * @return 文件
     * @throws Exception
     */
    public static void getAdverImgFromServer(final Context context, final Ad adver) {
        new Thread() {

            @Override
            public void run() {
                HttpURLConnection conn;
                File file = null;
                File tmpDownFile = null;
                InputStream is = null;
                FileOutputStream fos = null;
                BufferedInputStream bis = null;
                //PreferencesManager.setAdverCacheStatus(context, false,isTopAdver);
                boolean isToDeleteTmpFinally = true;
                try {
                    Log.i("TAG", "run: " + adver.getAndroidimg());
                    conn = (HttpURLConnection) new URL(adver.getAndroidimg())
                            .openConnection();
                    conn.setConnectTimeout(5000);
                    // 获取到文件的大小
                    int max = conn.getContentLength();
                    is = conn.getInputStream();
                    file = generateAdverImgSaveFile(context, adver);
                    tmpDownFile = new File(file.getAbsolutePath() + ".bak");
                    long nowTime = System.currentTimeMillis();
                    long lastTime = 0;
                    if (tmpDownFile.exists()) {
                        lastTime = tmpDownFile.lastModified();
                        // 如果发现临时文件在3分钟内都没有写入,则表明网络请求出现异常,删除损坏的临时文件,重新下载
                        if ((nowTime - lastTime) > (3 * 60 * 1000)) {
                            tmpDownFile.delete();
                        } else {
                            isToDeleteTmpFinally = false;
                            return;
                        }
                    }
                    tmpDownFile.createNewFile();
                    fos = new FileOutputStream(tmpDownFile);
                    bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    int total = 0;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        total += len;
                    }
                    if (total == max) {
                        tmpDownFile.renameTo(file);
                        if (file.exists()) {
                            adver.setAndroidimg(file.getPath());
                            AdvertisementDao.insertAd(adver);
                        }
                    }
                } catch (Exception e) {
                    tmpDownFile.delete();
                } finally {
                    if (tmpDownFile != null && isToDeleteTmpFinally) {
                        tmpDownFile.delete();
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                        if (bis != null) {
                            bis.close();
                        }
                        if (is != null) {
                            is.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                super.run();
            }
        }.start();
    }

    /**
     * 生成广告图片缓存路径
     *
     * @param context
     * @param adver
     * @return
     */
    public static File generateAdverImgSaveFile(Context context,
                                                Ad adver) {
        File saveDir = new File(getRootFilePath() + "save/ad_cache/");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        String[] imgUrlPeices = adver.getAndroidimg().split("/");
        String saveFileName = adver.getId() + "_"
                + imgUrlPeices[imgUrlPeices.length - 1];
        File result = new File(saveDir, saveFileName);
        return result;
    }

    /**
     * 获得文件根目录
     *
     * @return
     */
    public static String getRootFilePath() {
        String sdCardPath = getSDPath();
        if (TextUtils.isEmpty(sdCardPath)) {
            return "";
        } else {
            return sdCardPath + File.separator + APP_PACKAGE
                    + File.separator;
        }
    }

    public static String getSDPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            // 获取跟目录
            return Environment.getExternalStorageDirectory().toString();
        } else {
            return "";
        }
    }

    private static long lastClickTime;

    /**
     * 删除缓存的广告图片
     *
     * @param context
     * @param adver
     */
    public static void deleteCachedAdverImg(Context context,
                                            Ad adver) {
        File cachedImagFile = new File(adver.getAndroidimg());
        if (cachedImagFile.exists()) {
            cachedImagFile.delete();
        }
    }

    public static void deleteCachedAdverImg(Context context,
                                            File cachedImagFile) {
        if (cachedImagFile.exists()) {
            cachedImagFile.delete();
        }
    }

    /**
     * 检查广告是否存在缓存图片
     *
     * @param context
     * @param adver
     * @return
     */
    public static boolean isAdverImgExist(Context context,
                                          Ad adver) {
        return generateAdverImgSaveFile(context, adver).exists();
    }

    public static Bitmap scaleImgSize(File imgPath) {
        double maxSize = 1024 * 1024;
        double fileSize = imgPath.length();
        int scale = 1;
        double fileSizeAfterScale = fileSize;
        int scaleTime = 1;
        while (fileSizeAfterScale > maxSize) {
            scale = (int) Math.pow(2, scaleTime);
            fileSizeAfterScale = fileSize * ((1.0 / scale) * (1.0 / scale));
            scaleTime++;
        }
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = false;
        opt.inSampleSize = scale;
        Bitmap btp = BitmapFactory.decodeFile(imgPath.getAbsolutePath(), opt);
        return btp;
    }
}
