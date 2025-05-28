package com.aioveu.service.impl;

import com.aioveu.service.DistributedLock;

/**
 * @author fuwei.deng
 * @date 2017年6月14日 下午3:10:57
 * @version 1.0.0
 * @desc AbstractDistributedLock.java 抽象类，实现基本的方法，关键方法由子类去实现
 */
public abstract class AbstractDistributedLock implements DistributedLock {

	@Override
	public boolean lock(String key) {
		return lock(key, TIMEOUT_MILLIS, RETRY_TIMES, SLEEP_MILLIS, true);
	}

	@Override
	public boolean lockNotTry(String key) {
		return lock(key, TIMEOUT_MILLIS, RETRY_TIMES, SLEEP_MILLIS, false);
	}

	@Override
	public boolean lock(String key, int retryTimes) {
		return lock(key, TIMEOUT_MILLIS, retryTimes, SLEEP_MILLIS, true);
	}

	@Override
	public boolean lock(String key, int retryTimes, long sleepMillis) {
		return lock(key, TIMEOUT_MILLIS, retryTimes, sleepMillis, true);
	}

	@Override
	public boolean lock(String key, long expire) {
		return lock(key, expire, RETRY_TIMES, SLEEP_MILLIS, true);
	}

	@Override
	public boolean lock(String key, long expire, int retryTimes) {
		return lock(key, expire, retryTimes, SLEEP_MILLIS, true);
	}

}