package com.aioveu.service;
/**
 * @author fuwei.deng
 * @date 2017年6月14日 下午3:11:05
 * @version 1.0.0
 * @desc DistributedLock.java 顶级接口
 */
public interface DistributedLock {
	
	long TIMEOUT_MILLIS = 30000;
	
	int RETRY_TIMES = Integer.MAX_VALUE;
	
	long SLEEP_MILLIS = 500;

	/**
	 * 加锁
	 * @param key
	 * @return
	 */
	boolean lock(String key);

	/**
	 * 加锁 一旦失败直接返回结果，不进行重试
	 * @param key
	 * @return
	 */
	boolean lockNotTry(String key);


	/**
	 * 加锁
	 * @param key
	 * @param retryTimes
	 * @return
	 */
	boolean lock(String key, int retryTimes);

	/**
	 * 加锁
	 * @param key
	 * @param retryTimes
	 * @param sleepMillis
	 * @return
	 */
	boolean lock(String key, int retryTimes, long sleepMillis);

	/**
	 * 加锁
	 * @param key
	 * @param expire
	 * @return
	 */
	boolean lock(String key, long expire);

	/**
	 * 加锁
	 * @param key
	 * @param expire
	 * @param retryTimes
	 * @return
	 */
	boolean lock(String key, long expire, int retryTimes);

	/**
	 * 加锁
	 * @param key
	 * @param expire
	 * @param retryTimes
	 * @param sleepMillis
	 * @param tryLock
	 * @return
	 */
	boolean lock(String key, long expire, int retryTimes, long sleepMillis, boolean tryLock);

	/**
	 * 解锁
	 * @param key
	 * @return
	 */
	boolean releaseLock(String key);
}