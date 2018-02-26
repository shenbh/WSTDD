package com.newland.wstdd.common.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.newland.wstdd.R;
import com.newland.wstdd.common.adapter.HomeFragmentAdapter;
import com.newland.wstdd.common.common.ActivityType;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.common.tools.UiHelper;
import com.newland.wstdd.common.tools.UiToUiHelper;
import com.newland.wstdd.common.viewpager.HomeViewPager;
import com.newland.wstdd.common.widget.PengRadioButton;
import com.newland.wstdd.find.find.FindFragment;
import com.newland.wstdd.message.MessageFragment;
import com.newland.wstdd.mine.MineFragment;
import com.newland.wstdd.originate.OriginateFragment;
import com.newland.wstdd.originate.origateactivity.OriginateChairActivity;
import com.newland.wstdd.shortcut.ShortCutActivity;

/**
 * 主界面 包括 底部的五个按钮的监听事件
 * 
 * @author Administrator
 * 
 */
public class MainFragment extends Fragment implements OnCheckedChangeListener {
	
	public class MyClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.shortcut_botton_icon://快捷界面
				if("false".equals(AppContext.getAppContext().getIsLogin())){
//					UiHelper.ShowOneToast(getActivity(), "该操作需要登录后进行！");
					UiToUiHelper.showLogin(getActivity());
				}else{
//					Intent intent = new Intent(getActivity(), ShortCutActivity.class);
//					startActivity(intent);
					if("true".equals(AppContext.getAppContext().getIsLogin())){
						Intent intent = new Intent(getActivity(), OriginateChairActivity.class);
						Bundle bundle = new Bundle();
						bundle.putInt("activity_type", ActivityType.typeShortCut);
						bundle.putString("activity_action", "publish");
						intent.putExtras(bundle);
						getActivity().startActivity(intent);
					}else{
						UiToUiHelper.showLogin(getActivity());
					}
					break;
				}
				
				break;

			default:
				break;
			}
		}

	}

	private View view;
	private HomeViewPager mViewPager;
	private RadioGroup mRadioGroup;
	private PengRadioButton mRadioButton1, mRadioButton2, mRadioButton3, mRadioButton4;
	private ImageView shortCutIconImageView;
	private ImageView mImageView;
	private float mCurrentCheckedRadioLeft;
	private List<Fragment> mFragmentList;
	private HomeFragmentAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_main, null);
		initView();
		initListener();
		return view;
	}

	public void initView() {
		mFragmentList = new ArrayList<Fragment>();

		mFragmentList.add(new OriginateFragment());
		mFragmentList.add(new FindFragment());
		mFragmentList.add(new MessageFragment());
		mFragmentList.add(new MineFragment());

		mViewPager = (HomeViewPager) view.findViewById(R.id.pager);
		initViewPagerScroll();
		mRadioGroup = (RadioGroup) view.findViewById(R.id.radio);
		mRadioButton1 = (PengRadioButton) view.findViewById(R.id.btn1);
		mRadioButton2 = (PengRadioButton) view.findViewById(R.id.btn2);
		mRadioButton3 = (PengRadioButton) view.findViewById(R.id.btn3);
		mRadioButton4 = (PengRadioButton) view.findViewById(R.id.btn4);
		shortCutIconImageView = (ImageView) view.findViewById(R.id.shortcut_botton_icon);
		shortCutIconImageView.setOnClickListener(new MyClick());
		adapter = new HomeFragmentAdapter(getChildFragmentManager(), mFragmentList);
		adapter.setFragments((ArrayList<Fragment>) mFragmentList);	
		mViewPager.setAdapter(adapter);
		mViewPager.setOffscreenPageLimit(3);//太重要了，可以秒滑动
		mViewPager.setSlipping(false);
		mRadioButton1.setChecked(true);
		mRadioButton3.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				UiHelper.ShowOneToast(getActivity(), "该功能正在开发中,敬请期待");
				return true;
			}
		});
	}

	private void initListener() {
		mRadioGroup.setOnCheckedChangeListener(this);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				
				
				if (position >= 2) {
					
						((RadioButton) mRadioGroup.getChildAt(position + 1)).setChecked(true);// 这句话的作用是滑动的时，底部的按钮颜色也会改变
					
					

				} else {
					((RadioButton) mRadioGroup.getChildAt(position)).setChecked(true);// 这句话的作用是滑动的时，底部的按钮颜色也会改变

				}
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.btn1:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.btn2:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.btn3:
//			mViewPager.setCurrentItem(2);
			UiHelper.ShowOneToast(getActivity(), "该功能正在开发中,敬请期待");
			break;
		case R.id.btn4:
			mViewPager.setCurrentItem(3);
			break;

		default:
			break;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();

	try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}

	public static MainFragment newInstance(String string) {
		MainFragment newFragment = new MainFragment();
		Bundle bundle = new Bundle();
		bundle.putString("hello", string);
		newFragment.setArguments(bundle);
		return newFragment;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

	}
	  private void initViewPagerScroll( ){

		    try {

		            Field mScroller = null;

		            mScroller = ViewPager.class.getDeclaredField("mScroller");

		            mScroller.setAccessible(true); 

		            ViewPagerScroller scroller = new ViewPagerScroller( mViewPager.getContext( ) );

		            mScroller.set( mViewPager, scroller);

		        }catch(NoSuchFieldException e){

		        

		        }catch (IllegalArgumentException e){

		        

		        }catch (IllegalAccessException e){

		        

		        }

		    }
}
