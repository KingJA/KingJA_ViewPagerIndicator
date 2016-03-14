package kingja.com.kingja_viewpagerindicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<BaseFragment> fragmentList = new ArrayList<>();
    private BaseFragment baseFragment;
    private ViewPager vp;
    private IndicatorLayout indicator;
    private List<String> mTitleList= Arrays.asList("标题1","标题2","标题3","标题4","标题5","标题6","标题7");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        fillData();
    }


    private void initView() {
        vp = (ViewPager) findViewById(R.id.vp);
        indicator = (IndicatorLayout) findViewById(R.id.indicator);

    }

    private void initData() {
        for (int i = 0; i <mTitleList.size(); i++) {
            baseFragment = BaseFragment.newInstance(mTitleList.get(i));
            fragmentList.add(baseFragment);
        }
    }

    private void fillData() {
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragmentList));
        indicator.setTitles(mTitleList);
        indicator.setUpWithViewPager(vp,0);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        private List<BaseFragment> list;

        public MyPagerAdapter(FragmentManager fm, List<BaseFragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
