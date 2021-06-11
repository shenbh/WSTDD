package com.newland.wstdd.originate.search;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.SharePreferenceUtil.SharedPreferencesRefreshUtil;
import com.newland.wstdd.common.base.BaseFragment;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.resultlisterer.OnPostListenerInterface;
import com.newland.wstdd.common.tools.StringUtil;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.originate.search.OriginateSearchHistoryAdapter.onItemClickListener;

/**
 * 搜索历史列表界面 Fragment
 *
 * @author Administrator
 * 2015-12-10
 */
@SuppressLint("ValidFragment")
public class OriginateSearchHistoryFragment extends BaseFragment implements OnPostListenerInterface, OnClickListener {
    private TextView moriginate_interestcontent_tv;// 搜索感兴趣的内容
    private TextView moriginate_history_tv;// 历史记录
    private ListView moriginate_history_lv;// 历史记录列表
    private View view3;//历史列表底下横线
    private TextView moriginate_cleanhistory_tv;// 清除历史记录
    private OriginateSearchHistoryAdapter originateSearchAdapter;

    private Context context;

    SharedPreferencesRefreshUtil sharedPreferencesUtil = new SharedPreferencesRefreshUtil(context);
    List<HashMap<String, String>> datasList;
    /**
     * SharedPreferences中储存数据的路径
     **/
    public final static String DATA_URL = "/data/data/";
    public final static String SHARED_MAIN_XML = "search_info.xml";

    /**
     * 标签
     */
    List<String> tags = new ArrayList<String>();// 标签
    private TextView[][] tv_tags = new TextView[2][4];
    private LinearLayout linearlayout1;// 第一排4个tags数据
    private LinearLayout linearlayout2;// 第二排4个tags数据

    /**
     * 标签
     */


    @SuppressLint("ValidFragment")
    public OriginateSearchHistoryFragment(Context context) {
        this.context = context;
    }

    @Override
    protected View createAndInitView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_originate_searchhistory, container, false);
        if (AppContext.getAppContext().getTags() == null) {
            AppContext.getAppContext().setTags(StringUtil.appContextTagsStringToList(AppContext.getAppContext().getMyTags()));
        }
        tags = AppContext.getAppContext().getSearchTags();
        initView(view);
        return view;
    }

    private void initView(View view) {
        moriginate_interestcontent_tv = (TextView) view.findViewById(R.id.originate_interestcontent_tv);
        moriginate_history_tv = (TextView) view.findViewById(R.id.originate_history_tv);
        moriginate_history_lv = (ListView) view.findViewById(R.id.originate_history_lv);
        moriginate_cleanhistory_tv = (TextView) view.findViewById(R.id.originate_cleanhistory_tv);
        view3 = (View) view.findViewById(R.id.view3);
        datasList = new ArrayList<HashMap<String, String>>();
        datasList = sharedPreferencesUtil.getSearchInfo(context, "searchhistory");

        if (datasList != null && datasList.size() > 0) {
            view3.setVisibility(View.VISIBLE);
        } else {
            view3.setVisibility(View.GONE);
        }
        originateSearchAdapter = new OriginateSearchHistoryAdapter(context, datasList);
        moriginate_history_lv.setAdapter(originateSearchAdapter);
        originateSearchAdapter.setOnItemClickListener(new onItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                EditText searchEdt = (EditText) getActivity().findViewById(R.id.originate_search_edt);

                searchEdt.setText(((TextView) v).getText().toString());
                getActivity().findViewById(R.id.originate_search_tv).performClick();
            }
        });

        moriginate_cleanhistory_tv.setOnClickListener(this);

        tv_tags[0][0] = (TextView) view.findViewById(R.id.tags11_tv);
        tv_tags[0][1] = (TextView) view.findViewById(R.id.tags12_tv);
        tv_tags[0][2] = (TextView) view.findViewById(R.id.tags13_tv);
        tv_tags[0][3] = (TextView) view.findViewById(R.id.tags14_tv);
        tv_tags[1][0] = (TextView) view.findViewById(R.id.tags21_tv);
        tv_tags[1][1] = (TextView) view.findViewById(R.id.tags22_tv);
        tv_tags[1][2] = (TextView) view.findViewById(R.id.tags23_tv);
        tv_tags[1][3] = (TextView) view.findViewById(R.id.tags24_tv);
        linearlayout1 = (LinearLayout) view.findViewById(R.id.linearlayout1);
        linearlayout2 = (LinearLayout) view.findViewById(R.id.linearlayout2);
        //设置点击监听事件
        for (int i = 0; i < 8; i++) {
            if (i < 4) {
                tv_tags[0][i].setOnClickListener(this);
            } else {
                tv_tags[1][i - 4].setOnClickListener(this);
            }
        }
        if (tags != null && tags.size() > 0) {
            setTagsData(tags);
        } else if (tags != null && tags.size() == 0) {
            tags.add("艺术");
            tags.add("互联网");
            tags.add("演唱会");
            tags.add("音乐节");
            tags.add("摄影");
            tags.add("美食");
            setTagsData(tags);
        }
    }

    /**
     * 设置标签内容
     */
    private void setTagsData(List<String> tags) {
        int size = tags.size();
        if (size <= 4) {
            linearlayout2.setVisibility(View.GONE);
            for (int i = 0; i < size; i++) {
                tv_tags[0][i].setText(tags.get(i));
                tv_tags[0][i].setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.text_roundstyle));
                if (StringUtil.isEmpty(tv_tags[0][i].getText().toString())) {
                    tv_tags[0][i].setVisibility(View.INVISIBLE);
                }
            }
        } else {
            linearlayout2.setVisibility(View.VISIBLE);
            for (int i = 0; i < size; i++) {
                if (i < 4) {
                    tv_tags[0][i].setText(tags.get(i));
                    tv_tags[0][i].setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.text_roundstyle));
                } else {
                    tv_tags[1][i - 4].setText(tags.get(i));
                    tv_tags[1][i - 4].setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.text_roundstyle));
                    if (StringUtil.isEmpty(tv_tags[1][i - 4].getText().toString())) {
                        tv_tags[1][i - 4].setVisibility(View.INVISIBLE);
                    }
                }
            }
        }

    }

    private void deleteSharePreferenceFile() {
        /**删除SharePreferences文件*/
        File file = new File(DATA_URL + getActivity().getPackageName().toString()
                + "/shared_prefs", SHARED_MAIN_XML);
        if (file.exists()) {
            file.delete();
        }
        System.out.println("删除SharedPreferences文件成功");
    }

    @Override
    public void onClick(View v) {
        EditText searchEdt = (EditText) getActivity().findViewById(R.id.originate_search_edt);
        switch (v.getId()) {
            case R.id.originate_cleanhistory_tv:
                sharedPreferencesUtil.cleanInfo(context);
                deleteSharePreferenceFile();
                originateSearchAdapter.getList().clear();
                view3.setVisibility(View.GONE);
                originateSearchAdapter.setList(originateSearchAdapter.getList());
                originateSearchAdapter.notifyDataSetChanged();
                break;
            case R.id.tags11_tv:
                searchEdt.setText(tv_tags[0][0].getText().toString());
                getActivity().findViewById(R.id.originate_search_tv).performClick();
                break;
            case R.id.tags12_tv:
                searchEdt.setText(tv_tags[0][1].getText().toString());
                getActivity().findViewById(R.id.originate_search_tv).performClick();
                break;
            case R.id.tags13_tv:
                searchEdt.setText(tv_tags[0][2].getText().toString());
                getActivity().findViewById(R.id.originate_search_tv).performClick();
                break;
            case R.id.tags14_tv:
                searchEdt.setText(tv_tags[0][3].getText().toString());
                getActivity().findViewById(R.id.originate_search_tv).performClick();
                break;
            case R.id.tags21_tv:
                searchEdt.setText(tv_tags[1][0].getText().toString());
                getActivity().findViewById(R.id.originate_search_tv).performClick();
                break;
            case R.id.tags22_tv:
                searchEdt.setText(tv_tags[1][1].getText().toString());
                getActivity().findViewById(R.id.originate_search_tv).performClick();
                break;
            case R.id.tags23_tv:
                searchEdt.setText(tv_tags[1][2].getText().toString());
                getActivity().findViewById(R.id.originate_search_tv).performClick();
                break;
            case R.id.tags24_tv:
                searchEdt.setText(tv_tags[1][3].getText().toString());
                getActivity().findViewById(R.id.originate_search_tv).performClick();
                break;
            default:
                break;
        }
    }

    @Override
    public void OnHandleResultListener(Object obj, int responseId) {
        if (progressDialog != null) {
            progressDialog.setContinueDialog(false);
        }
    }

    @Override
    public void OnFailResultListener(String mess) {
        if (progressDialog != null) {
            progressDialog.setContinueDialog(false);
            UiHelper.ShowOneToast(getActivity(), mess);
        }
    }

    @Override
    public void refresh() {

    }

}
