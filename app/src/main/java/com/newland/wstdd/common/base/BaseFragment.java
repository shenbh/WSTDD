package com.newland.wstdd.common.base;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.newland.wstdd.common.dialog.CustomProgressDialog;
import com.newland.wstdd.common.view.LoadingDialog;

public abstract class BaseFragment extends Fragment implements OnClickListener {
    private MainFrameTask mMainFrameTask = null;
    public CustomProgressDialog progressDialog = null;
    protected FragmentActivity fragmentActivity = null;

    public BaseFragment() {
    }

    // 相当于无参构造函数一样
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.err.println("执行顺序是++++++++++++++++++1");
        fragmentActivity = getActivity();

    }

    /**
     * 当该类的子类创建对象的时候 会执行这个方法，好好理解一下类似于：
     * 子类调用构造函数的时候，会调用父类的无参构造函数一样，而这个类比较特殊，相当于Activtiy 会先去执行OnCreateView这个方法
     * 必须使用onCreateView()定义他的layout布局文件
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 调用子类的这个方法
        System.err.println("执行顺序是++++++++++++++++++2");
        View view = createAndInitView(inflater, container, savedInstanceState);

        return view;
    }

    // 子类会重载这个方法
    protected abstract View createAndInitView(LayoutInflater inflater,
                                              ViewGroup container, Bundle savedInstanceState);

    public void refresh() {
//		showDialog();
    }


    public LoadingDialog dialog;

    public void showDialog() {
        if (dialog == null) {
            initDialog(fragmentActivity);
        } else {
            dialog.dismiss();
            initDialog(fragmentActivity);
        }
    }

    public void initDialog(FragmentActivity fragmentActivity) {
        dialog = new LoadingDialog(fragmentActivity);
//		dialog.setTvMessage("正在加载...");
        if (!fragmentActivity.isFinishing()) {
            dialog.show(true);
        } else {
            dialog.show(false);
        }
    }

    @Override
    public void onDestroy() {
        stopProgressDialog();

        if (mMainFrameTask != null && !mMainFrameTask.isCancelled()) {
            mMainFrameTask.cancel(true);
        }

        super.onDestroy();

    }


    private void startProgressDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.createDialog(getActivity());
//	    	progressDialog.setMessage("正在加载中...");
        }

        progressDialog.show();
    }

    private void stopProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public class MainFrameTask extends AsyncTask<Integer, String, Integer> {
        private BaseFragment BaseFragmentActivity = null;

        public MainFrameTask(BaseFragment BaseFragmentActivity) {
            this.BaseFragmentActivity = BaseFragmentActivity;
        }

        @Override
        protected void onCancelled() {
            stopProgressDialog();
            super.onCancelled();
        }

        @Override
        protected Integer doInBackground(Integer... params) {

            try {
                if (progressDialog != null) {
                    while (progressDialog.isContinueDialog()) {
                        Thread.sleep(1000);//睡一秒
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPreExecute() {
            startProgressDialog();
        }

        @Override
        protected void onPostExecute(Integer result) {
            stopProgressDialog();
        }
    }

    public void refreshDialog() {
        mMainFrameTask = new MainFrameTask(this);
        mMainFrameTask.execute();
    }


}
