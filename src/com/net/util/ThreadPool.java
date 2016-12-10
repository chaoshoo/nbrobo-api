package com.net.util;

import java.util.LinkedList;
/**
 * 工作线程池
 * 
 * huilet 2013-4-17
 * @author yuanc
 */
public class ThreadPool extends ThreadGroup {
	/** 线程池是否关闭*/
	private boolean isClosed = false; 
	/** 工作队列 */
	private LinkedList workQueue; 
	/** 线程池的id */
	private static int threadPoolID = 1; 

	private static ThreadPool instance;

	public static ThreadPool getInstance(int poolSize) {
		if (instance == null) {
			instance = new ThreadPool(poolSize);
		}
		return instance;
	}
	/**
	 * 
	 * @param poolSize
	 * 			表示线程池中的工作线程的数量
	 * huilet 2013-4-17
	 * @author yuanc
	 */
	private ThreadPool(int poolSize) {
		// 指定ThreadGroup的名称
		super(threadPoolID + ""); 
		// 继承到的方法，设置是否守护线程池
		setDaemon(true); 
		// 创建工作队列
		workQueue = new LinkedList(); 
		for (int i = 0; i < poolSize; i++) {
			// 创建并启动工作线程,线程池数量是多少就创建多少个工作线程
			new WorkThread(i).start(); 
		}
	}

	/**
	 * 向工作队列中加入一个新任务,由工作线程去执行该任务
	 * @param task
	 * huilet 2013-4-17
	 * @author yuanc
	 */
	public synchronized void execute(Runnable task) {
		if (isClosed) {
			throw new IllegalStateException();
		}
		/*
		 * 此处可以考虑设置线程池中等待线程大小、时间机制 if(workQueue.size()>30) {
		 * //Thread.sleep(3000); }
		 */
//		if(workQueue.size()>30) {    //线程数量超过30则休眠5秒
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
		if (task != null) {
			// 向队列中加入一个任务
			workQueue.add(task);
			// 唤醒一个正在getTask()方法中待任务的工作线程
			notify();
		}
	}

	/**
	 * 从工作队列中取出一个任务,工作线程会调用此方法
	 * @param threadid
	 * @return
	 * @throws InterruptedException
	 * huilet 2013-4-17
	 * @author yuanc
	 */
	private synchronized Runnable getTask(int threadid)
			throws InterruptedException {
		while (workQueue.size() == 0) {
			if (isClosed)
				return null;
			System.out.println("OPT Thread" + threadid + "Waiting Task...");
			// 如果工作队列中没有任务,就等待任务
			wait(); 
		}
		System.out.println("OPT Thread " + threadid + " Excute Task...");
		// 返回队列中第一个元素,并从队列中删除
		return (Runnable) workQueue.removeFirst(); 
	}

	/**
	 * 关闭线程池
	 * 
	 * huilet 2013-4-17
	 * @author yuanc
	 */
	public synchronized void closePool() {
		if (!isClosed) {
			// 等待工作线程执行完毕
			waitFinish(); 
			isClosed = true;
			// 清空工作队列
			workQueue.clear(); 
			// 中断线程池中的所有的工作线程,此方法继承自ThreadGroup类
			interrupt(); 
		}
	}

	/**
	 * 等待工作线程把所有任务执行完毕 
	 * 
	 * huilet 2013-4-17
	 * @author yuanc
	 */
	public void waitFinish() {
		synchronized (this) {
			isClosed = true;
			// 唤醒所有还在getTask()方法中等待任务的工作线程
			notifyAll();
		}
		// activeCount()
		// 返回该线程组中活动线程的估计值。
		Thread[] threads = new Thread[activeCount()];
		// enumerate()方法继承自ThreadGroup类，根据活动线程的估计值获得线程组中当前所有活动的工作线程
		int count = enumerate(threads);
		// 等待所有工作线程结束
		for (int i = 0; i < count; i++) {
			try {
				// 等待工作线程结束
				threads[i].join();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 内部类,工作线程,负责从工作队列中取出任务,并执行
	 * 
	 * huilet 2013-4-17
	 * 
	 * @author yuanc
	 */
	private class WorkThread extends Thread {
		private int id;

		public WorkThread(int id) {
			// 父类构造方法,将线程加入到当前ThreadPool线程组中
			super(ThreadPool.this, id + "");
			this.id = id;
		}

		public void run() {
			// isInterrupted()方法继承自Thread类，判断线程是否被中断
			while (!isInterrupted()) {
				Runnable task = null;
				try {
					// 取出任务
					task = getTask(id);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
				// 如果getTask()返回null或者线程执行getTask()时被中断，则结束此线程
				if (task == null)
					return;
				try {
					// 运行任务
					task.run();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
	}
}