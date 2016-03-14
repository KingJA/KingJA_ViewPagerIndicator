package kingja.com.kingja_viewpagerindicator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Shinelon on 2016/3/13.
 */
public class BaseFragment extends Fragment {
    private static final String INFO_FRAGMEN="info";
    private String info;

    public static BaseFragment newInstance(String info){
        BaseFragment baseFragment = new BaseFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INFO_FRAGMEN,info);
        baseFragment.setArguments(bundle);
        return baseFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle!=null){
            info = bundle.getString(INFO_FRAGMEN);
        }
        TextView tv = new TextView(getActivity());
        tv.setText(info);
        tv.setGravity(Gravity.CENTER);
        return tv;
    }
}
