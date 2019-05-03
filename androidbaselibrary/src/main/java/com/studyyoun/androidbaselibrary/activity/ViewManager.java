package com.studyyoun.androidbaselibrary.activity;

import android.app.Application;
import android.content.Context;


import com.studyyoun.androidbaselibrary.base.IBaseInterface;
import com.studyyoun.androidbaselibrary.utils.ObjectUtil;

import java.util.List;
import java.util.Stack;

public class ViewManager {
	private static Stack<IBaseInterface> activityStack = new Stack<IBaseInterface>();

	private static Context context;
	private static Application application;


	private ViewManager() {
	}

	/**
	 * 初始化工具类
	 *
	 * @param app 上下文
	 */
	public static void init(Application app) {
		ViewManager.context = app.getApplicationContext();
		ViewManager.application = app;
	}

	/**
	 * 获取ApplicationContext
	 *
	 * @return ApplicationContext
	 */
	public static Context getContext() {
		if (context != null) return context;
		throw new NullPointerException("u should init first");
	}

	/**
	 * 获取Application
	 *
	 * @return ApplicationContext
	 */
	public static Application getApplication() {
		if (context != null) return application;
		throw new NullPointerException("u should init first");
	}


	private static class ManagerHolder {
		private static final ViewManager instance = new ViewManager();
	}

	public static ViewManager getInstance() {
		return ManagerHolder.instance;
	}

	/**
	 * 获取当前View栈中元素个数
	 */
	public int getCount() {
		return activityStack.size();
	}
	
	/**
	 * 获取当前View栈中元素个数
	 */
	public void removeLast() {
		activityStack.remove(getCount() - 1);
	}

	/**
	 * 添加View到栈
	 */
	public void addView(IBaseInterface view) {
		if (activityStack == null) {
			activityStack = new Stack<IBaseInterface>();
		}
		if (activityStack.contains(view)) {
			finishView(view.getClass());
		}
		activityStack.add(view);
	}

	/**
	 * 获取当前View（栈顶View）
	 */
	public IBaseInterface topView() {
		if (activityStack == null) {
			throw new NullPointerException(
					"Activity stack is Null,your Activity must extend BaseActivity");
		}
		if (activityStack.isEmpty()) {
			return null;
		}
		IBaseInterface view = activityStack.lastElement();
		return (IBaseInterface) view;
	}

	/**
	 * 获取当前View（栈顶View） 没有找到则返回null
	 */
	public IBaseInterface findView(Class<?> cls) {
		IBaseInterface view = null;
		for (IBaseInterface aty : activityStack) {
			if (aty.getClass().equals(cls)) {
				view = aty;
				break;
			}
		}
		return (IBaseInterface) view;
	}

	/**
	 * 结束当前view（栈顶view）
	 */
	public void finishView() {
		IBaseInterface activity = activityStack.lastElement();
		finishView((CommonBaseActivity) activity);
	}

	/**
	 * 结束指定的view(重载)
	 */
	public void finishView(CommonBaseActivity view) {
		if (view != null) {
			activityStack.remove(view);
			view.finish();
			view = null;
		}
	}

	/**
	 * 结束指定的view(重载)
	 */
	public void finishView(Class<?> cls) {
		for (int i = activityStack.size() - 1; i >= 0; i--) {
			if (activityStack.get(i).getClass().equals(cls)) {
				finishView((CommonBaseActivity) activityStack.get(i));
			}
		}
	}
	
	/**
	 * 关闭除了指定View以外的全部view 如果cls不存在于栈中，则栈全部清空
	 * 
	 * @param cls
	 */
	public void finishOthersView(Class<?> cls) {
		Stack<IBaseInterface> tmp = ObjectUtil.CloneObject(activityStack);
		for (int i = tmp.size() - 1; i >= 0; i--) {
			if (!tmp.get(i).getClass().equals(cls)) {
				finishView((CommonBaseActivity) tmp.get(i));
			}
		}
	}
	
	/**
	 * 关闭列表中的view
	 * 
	 * @param cls
	 */
	public void finishViews(List<Class<?>> cls) {
		for (Class<?> class1 : cls) {
			finishView(class1);
		}
	}

	/**
	 * 关闭列表中的view
	 *
	 * @param cls
	 */
	public void finishViews(Class<?>[] cls) {
		for (Class<?> class1 : cls) {
			finishView(class1);
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllView() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				finishView((CommonBaseActivity) activityStack.get(i));
			}
		}
	}

	/**
	 * 应用程序退出
	 * 
	 */
	public void AppExit() {
		try {
			finishAllView();
			Runtime.getRuntime().exit(0);
		} catch (Exception e) {
			Runtime.getRuntime().exit(-1);
		}
	}
}
