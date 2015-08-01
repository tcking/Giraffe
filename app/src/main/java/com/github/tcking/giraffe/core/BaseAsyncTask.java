package com.github.tcking.giraffe.core;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


/**
 * <pre>
 * 封装了ProgressDialog功能的AsycTask基类，所有的AsyncTask都应该继承此类
 * 1.如果要显示ProgressDialog，则在构造函数中调用setShowDialog(true)并重写getDialogMessage来设定显示的信息
 * 2.onPostExecute只执行与UI无关的逻辑，和UI相关的控制可以通过AsyncTaskListener在调用处编写实现
 * 3.如果一个AsyncTask实例执行的参数不变，AsyncTask要执行的参数通过构造函数传入，可以减少出现参数漏传或误传的错误
 * </pre>
 * @author tangchao
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	private AsyncTaskListener listener;
	protected FragmentActivity activity;
    protected boolean showDialog=false;
	private Fragment fragment;
	private boolean isCancelable;
    protected ProgressDialog progressDialog;


    public final void doTask(Params... params){
        if (Build.VERSION.SDK_INT >= 11) {
            executeOnExecutor(CoreApp.getThreadPool(), params);
        } else {
            execute(params);
        }
    };

    /**
	 * AsyncTask不同执行阶段的回调
	 * @author tangchao
	 *
	 */
	public class AsyncTaskListener{
		public void onPreExecute(){};
		public void onUpdateProgress(Progress... values){};
		public void onPostExecute(Result result){};
		public void onCancelled(){};
	}

	public BaseAsyncTask(FragmentActivity activity) {
		this.activity = activity;
	}
	
	public BaseAsyncTask(Fragment fragment) {
		this.fragment=fragment;
		if (fragment!=null) {
			this.activity = fragment.getActivity();
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (listener != null) {
			listener.onPreExecute();
		}
		if (showDialog) {
            this.progressDialog=new ProgressDialog(activity,ProgressDialog.THEME_HOLO_LIGHT);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage(getDialogMessage());
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (!isCancelled()) {
                        cancel(true);
                    }
                }
            });
            progressDialog.show();
		}
	}

	@Override
	protected void onProgressUpdate(Progress... values) {
		super.onProgressUpdate(values);
		if (listener != null) {
			listener.onUpdateProgress(values);
		}
	}

	/**
	 * ProgressDialog要显示的信息
	 * @return
	 */
	protected String getDialogMessage(){
		return "请稍后...";
	}

	@Override
	protected void onPostExecute(Result result) {
		dismissDialog();
		if (listener != null) {
			if (activity instanceof CoreBaseActivity) {
				CoreBaseActivity baseAcivity = (CoreBaseActivity)activity;
				if (baseAcivity.isFinished()) {
					return;
				}
				if (fragment!=null && fragment.isDetached()) {
					return;
				}
				listener.onPostExecute(result);
			}else {
				if (fragment!=null && fragment.isDetached()) {
					return;
				}
				listener.onPostExecute(result);
			}
		}
	}

	private void dismissDialog() {
		if (progressDialog!=null) {
			try {
                progressDialog.dismiss();
			} catch (Exception e) {
                Log.e("BaseAsyncTask.onPostExecute error", e);
			}
		}
	}

	/**
	 * @param showDialog
	 * @return
	 */
	public BaseAsyncTask<Params, Progress, Result> setShowDialog(boolean showDialog) {
		this.showDialog = showDialog;
		return this;
	}



	/**
	 * @param listener the listener to set
	 */
	public BaseAsyncTask<Params, Progress, Result> setListener(AsyncTaskListener listener) {
		this.listener = listener;
		return this;
	}

	@Override
	protected void onCancelled() {
		dismissDialog();
		if (listener != null) {
			listener.onCancelled();
		}
	}

	public void setIsCancelable(boolean isCancelable) {
		this.isCancelable = isCancelable;
	}
}
