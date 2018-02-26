package com.newland.wstdd.mine.myinterest;

import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.newland.wstdd.R;
import com.newland.wstdd.common.common.AppContext;
import com.newland.wstdd.mine.myinterest.beanrequest.TddUserTagQuery;

/**

 * 
 */
public class DragAdapter extends BaseAdapter implements DragGridBaseAdapter {
	private List<TddUserTagQuery> pluInfoList;
	private LayoutInflater mInflater;
	private int mHidePosition = -1;
	
	private Context context;
	private boolean isShowDelete=false;// 是否可以删除
	/** 控制的postion */
	private int holdPosition;
	/** 是否改变 */
	private boolean isChanged = false;

	public DragAdapter(Context context, List<TddUserTagQuery> pluInfoList) {
		this.pluInfoList = pluInfoList;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pluInfoList == null ? 0 : pluInfoList.size();
	}

	@Override
	public TddUserTagQuery getItem(int position) {
		// TODO Auto-generated method stub
		if (pluInfoList != null && pluInfoList.size() != 0) {
			return pluInfoList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 由于复用convertView导致某些item消失了，所以这里不复用item，
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {

		convertView = mInflater.inflate(R.layout.item_plu_setting, null);	
		LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);

		TextView fruitNameTextView = (TextView) convertView
				.findViewById(R.id.interest_name);
		ImageView fruitPriceTextView = (ImageView) convertView
				.findViewById(R.id.interest_select_icon);
		
		// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag

		if (position == mHidePosition) {
			convertView.setVisibility(View.INVISIBLE);
		}
		TddUserTagQuery tddUserTag = getItem(position);
		fruitNameTextView.setText(tddUserTag.getTagName());
	
		fruitPriceTextView.setVisibility(View.VISIBLE);
		
		if (isChanged && (position == holdPosition)) {
			isChanged = false;
		}
		if (isChanged && (position == holdPosition)) {
			fruitNameTextView.setText("");
			fruitNameTextView.setSelected(true);
			fruitNameTextView.setEnabled(true);
			fruitPriceTextView.setVisibility(View.GONE);
			fruitPriceTextView.setSelected(true);
			fruitPriceTextView.setEnabled(true);

			isChanged = false;
		}
		fruitPriceTextView.setVisibility(isShowDelete ? View.VISIBLE : View.INVISIBLE);// 设置删除按钮是否显示
		if(isShowDelete){
			ll.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(((MyInterestActivity)context).isCanDeltet){
						deleteTipDialog(position,isShowDelete);
					}else{
						((MyInterestActivity)context).isCanDeltet=true;
						DragAdapter.this.notifyDataSetChanged();
					}
					
				}
			});
			ll.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					((MyInterestActivity)context).isCanDeltet=true;
					DragAdapter.this.notifyDataSetChanged();
					return true;
				}
			});
		
			
	}	
		//这里是设置每个gridview分的高度   但是记住可不是item.xml中的高度
		convertView.setLayoutParams(new GridView.LayoutParams((int) (AppContext.getAppContext().getScreenWidth()/4-10 ) , (int)(AppContext.getAppContext().getScreenHeight()/15)));// 动态设置item的高度		
		
		return convertView;
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		return null;
		
	}
	//确认是否要被删除
		private void deleteTipDialog(final int p, boolean isShowDelete2){
			Dialog dialog = new AlertDialog.Builder(context)
			.setTitle("温馨提示")		// 创建标题
				.setMessage("您确认要删除吗？") // 表示对话框中的内容
//				.setIcon(R.drawable.app_icon_del) // 设置LOGO
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						delete(p);
						setIsShowDelete(isShowDelete);
						notifyDataSetChanged();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					//不做任何操作就是消失的意思
						DragAdapter.this.notifyDataSetChanged();
					}
				}).create(); // 创建了一个对话框
			dialog.setCanceledOnTouchOutside(false);
			dialog.show() ;	// 显示对话框
		}

	@Override
	public void reorderItems(int oldPosition, int newPosition) {
		TddUserTagQuery temp = pluInfoList.get(oldPosition);
		if (oldPosition < newPosition) {
			for (int i = oldPosition; i < newPosition; i++) {
				Collections.swap(pluInfoList, i, i + 1);
			}
		} else if (oldPosition > newPosition) {
			for (int i = oldPosition; i > newPosition; i--) {
				Collections.swap(pluInfoList, i, i - 1);
			}
		}

		pluInfoList.set(newPosition, (TddUserTagQuery) temp);
	}

	@Override
	public void setHideItem(int hidePosition) {
		this.mHidePosition = hidePosition;
		notifyDataSetChanged();
	}

	public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	// 标记删除item
	public void setIsShowDelete(boolean isShowDelete) {
		this.isShowDelete = isShowDelete;
		notifyDataSetChanged();
	}

	// 删除item
	public void delete(int position) {
		TddUserTagQuery appinfo = pluInfoList.get(position);
		// appinfo.setState(false);
		pluInfoList.remove(position);
		notifyDataSetChanged();

	}

	/** 设置列表 */
	public void setListData(List<TddUserTagQuery> list) {
		pluInfoList = list;
	}
}
