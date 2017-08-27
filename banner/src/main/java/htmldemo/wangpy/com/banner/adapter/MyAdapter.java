package htmldemo.wangpy.com.banner.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by dell on 2017/8/27.
 */

public class MyAdapter extends PagerAdapter {
    private Context context;
    private List<View> list;

    public MyAdapter(Context context, List<View> list) {
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //销毁view的方法
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView(list.get(position));
    }

    //添加view的方法
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position));
        return list.get(position);
    }
}
