package com.aioveu.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisUtil
 * @Description redis工具类
 * @Author xlfan10
 * @Datetime 2019/8/20 11:39
 */
@Component
@Slf4j
public class RedisUtil {

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 默认有效时间
     */
    public static long expireTime = 1800;

    // ====================== common ======================

    /**
     * 设置指定key的有效时间
     *
     * @param key  键
     * @param time 有效时间(秒)
     * @return true：成功；false：失败
     */
    public static boolean expire(String key, long time, RedisTemplate rt) {
        try {
            if (time > 0) {
                rt.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("expire：", e);
            return false;
        }
    }

    /**
     * 获取指定key的有效时间
     *
     * @param key 键，不能为null
     * @return 有效时间(秒)，返回0代表为永久有效
     */
    public long getExpire(String key) {
        Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire == null ? 0 : expire;
    }

    /**
     * 判断指定key是否存在
     *
     * @param key 键
     * @return true：存在；false：不存在
     */
    public boolean hasKey(String key) {
        try {
            Boolean hasKey = redisTemplate.hasKey(key);
            return hasKey == null ? false : hasKey;
        } catch (Exception e) {
            log.error("查询redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 删除指定key
     *
     * @param key 可以传一个或多个值
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                List<String> list = Arrays.asList(key);
                redisTemplate.delete(list);
            }
        }
    }

    // ====================== String ======================
    /**
     * 设置指定key的值
     *
     * @param key   键
     * @param value 值
     * @return true：成功；false：失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("插入redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 设置指定key的值并设置有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0；如果time小于等于0 将设置默认时间1800秒
     * @return true：成功；false：失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("插入redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 设置指定key的值并设置有效时间（如果不存在就设置）
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0；如果time小于等于0 将设置默认时长1800秒
     * @return true：成功；false：失败
     */
    public boolean setIfAbsent(String key, Object value, long time) {
        Boolean a;
        try {
            if (time > 0) {
                a = redisTemplate.opsForValue().setIfAbsent(key, value, time, TimeUnit.SECONDS);
            } else {
                a = redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.SECONDS);
            }
            return a == null ? false: a;
        } catch (Exception e) {
            log.error("插入redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 设置指定key的值并设置有效时间（如果存在就设置）
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0；如果time小于等于0 将设置默认时长1800秒
     * @return true：成功；false：失败
     */
    public boolean setIfPresent(String key, Object value, long time) {
        Boolean a;
        try {
            if (time > 0) {
                a = redisTemplate.opsForValue().setIfPresent(key, value, time, TimeUnit.SECONDS);
            } else {
                a = redisTemplate.opsForValue().setIfPresent(key, value, expireTime, TimeUnit.SECONDS);
            }
            return a == null ? false: a;
        } catch (Exception e) {
            log.error("插入redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 将指定key的值加上给定的增量值
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return 返回新增后的值
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("redis增量值必须大于0");
        }
        Long increment = redisTemplate.opsForValue().increment(key, delta);
        return increment == null ? 0 : increment;
    }

    /**
     * 将指定key的值减去给定的减量值
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return long
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("减量值必须大于0");
        }
        Long increment = redisTemplate.opsForValue().increment(key, -delta);
        return increment == null ? 0 : increment;
    }

    // ====================== Hash ======================

    /**
     * 获取哈希表中指定key的指定字段的值
     *
     * @param key   键
     * @param filed 字段名
     * @return 对应字段的值
     */
    public Object hGet(String key, String filed) {
        return redisTemplate.opsForHash().get(key, filed);
    }

    /**
     * 获取哈希表中指定key的所有字段和值
     *
     * @param key 键
     * @return 对应的所有字段和值
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 设置哈希表中指定key的字段和值
     *
     * @param key   键
     * @param filed 字段名
     * @param value 值
     * @return true：成功；false：失败
     */
    public boolean hSet(String key, String filed, Object value) {
        try {
            redisTemplate.opsForHash().put(key, filed, value);
            return true;
        } catch (Exception e) {
            log.error("插入redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 设置多个键值对到哈希表key中
     *
     * @param key 键
     * @param map 键值对
     *
     * @return true：成功；false：失败
     */
    public static boolean hSetMap(String key, Map<String, Object> map, RedisTemplate rt) {
        try {
            rt.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("插入redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 设置多个键值对到哈希表key中，并设置有效时间
     *
     * @param key  键
     * @param map  键值对
     * @param time 有效时间(秒)
     * @return true：成功；false：失败
     */
    public static boolean hSetMap(String key, Map<String, Object> map, long time, RedisTemplate rt) {
        try {
            rt.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time, rt);
            }
            return true;
        } catch (Exception e) {
            log.error("插入redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 获取整个Map
     * @param key
     * @param rt
     * @return
     */
    public static Map<String, Object> getEntireMap(String key, RedisTemplate rt) {
        return rt.opsForHash().entries(key);
    }

    /**
     * 添加或更新单个字段
     * @param key
     * @param hashKey
     * @param value
     * @param rt
     */
    public static void putToMap(String key, String hashKey, Object value, RedisTemplate rt) {
        rt.opsForHash().put(key, hashKey, value);
    }

    /**
     * 删除单个字段
     * @param key
     * @param hashKey
     * @param rt
     */
    public static void deleteFromMap(String key, String hashKey, RedisTemplate rt) {
        rt.opsForHash().delete(key, hashKey);
    }

    /**
     * 获取单个字段
     * @param key
     * @param hashKey
     * @param rt
     * @return
     */
    public static Object getFromMap(String key, String hashKey, RedisTemplate rt) {
        return rt.opsForHash().get(key, hashKey);
    }

    /**
     * 删除哈希表中对应key的字段
     *
     * @param key   键
     * @param filed 字段名
     */
    public void hDel(String key, Object... filed) {
        redisTemplate.opsForHash().delete(key, filed);
    }

    /**
     * 判断哈希表中对应key是否存在指定字段
     *
     * @param key   键
     * @param filed 字段名
     * @return true：存在；false：不存在
     */
    public boolean hHasKey(String key, String filed) {
        return redisTemplate.opsForHash().hasKey(key, filed);
    }

    /**
     * 将哈希表指定key的字段的值加上给定的增量值
     *
     * @param key   键
     * @param filed 字段名
     * @param delta 增量值
     * @return 返回新增后的值
     */
    public double hincr(String key, String filed, double delta) {
        return redisTemplate.opsForHash().increment(key, filed, delta);
    }

    /**
     * 将哈希表指定key的字段的值减去给定的减量值
     *
     * @param key   键
     * @param filed 字段名
     * @param delta 减量值
     * @return 返回减去后的值
     */
    public double hdecr(String key, String filed, double delta) {
        return redisTemplate.opsForHash().increment(key, filed, -delta);
    }

    // ====================== Set ======================

    /**
     * 获取Set中指定key的所有值
     *
     * @param key 键
     * @return 所有值
     */
    public Set<Object> sGet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断Set中指定key是否存在指定value
     *
     * @param key   键
     * @param value 值
     * @return true：存在；false：不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            Boolean member = redisTemplate.opsForSet().isMember(key, value);
            return member == null ? false : member;
        } catch (Exception e) {
            log.error("查询redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 设置Set中指定key的值
     *
     * @param key    键
     * @param values 值，可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            return count == null ? 0 : count;
        } catch (Exception e) {
            log.error("查询redis数据出错，错误信息为：", e);
            return 0;
        }
    }

    /**
     * 设置Set中指定key的值，并设置有效时间
     *
     * @param key    键
     * @param time   有效时间(秒)
     * @param values 值，可以是多个
     * @return 成功个数
     */
    public long sSet(String key, long time, RedisTemplate rt, Object... values) {
        try {
            Long count = rt.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time, rt);
            }
            return count == null ? 0 : count;
        } catch (Exception e) {
            log.error("查询redis数据出错，错误信息为：", e);
            return 0;
        }
    }

    /**
     * 获取Set中指定key的长度
     *
     * @param key 键
     * @return 长度
     */
    public long sSize(String key) {
        Long size = redisTemplate.opsForSet().size(key);
        return size == null ? 0 : size;
    }

    /**
     * 移除Set中指定key的value值
     *
     * @param key    键
     * @param values 值，可以是多个
     * @return 移除的个数
     */
    public long sRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count == null ? 0 : count;
        } catch (Exception e) {
            log.error("移除redis数据异常，错误信息为：", e);
            return 0;
        }
    }

    // ====================== List ======================

    /**
     * 获取List中指定key指定范围的值（0到-1代表所有值）
     *
     * @param key   键
     * @param start 开始
     * @param end   结束
     * @return 对应值
     */
    public List<Object> lGet(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取List中指定key的长度
     *
     * @param key 键
     * @return 长度
     */
    public long lSize(String key) {
        Long size = redisTemplate.opsForList().size(key);
        return size == null ? 0 : size;
    }

    /**
     * 获取List中指定key的指定索引的值
     *
     * @param key   键
     * @param index 索引
     * @return 值
     */
    public Object lIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 设置List中指定key的值
     *
     * @param key   键
     * @param value 值
     * @return true：成功；false：失败
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("插入redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 设置List中指定key的值，并设置有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间(秒)
     * @return true：成功；false：失败
     */
    public boolean lSet(String key, Object value, long time, RedisTemplate rt) {
        try {
            rt.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time, rt);
            }
            return true;
        } catch (Exception e) {
            log.error("插入redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 设置List中指定key的指定索引的值
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return true：成功；false：失败
     */
    public boolean lSet(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("插入redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 给List中指定的key后添加list
     *
     * @param key   键
     * @param value 值
     * @return true：成功；false：失败
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("插入redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 给List中指定的key后添加list，并设置有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  有效时间(秒)
     * @return true：成功；false：失败
     */
    public static boolean lSet(String key, List<Object> value, long time, RedisTemplate rt) {
        try {
            rt.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time, rt);
            }
            return true;
        } catch (Exception e) {
            log.error("插入redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 移除List中与value相等的元素
     *
     * @param key   键
     * @param count 个数，count=0则删除所有相等的元素；count>0则从表头开始向表尾搜索，移除count个；count<0则从表尾开始向表头搜索，移除count个。
     * @param value 值
     * @return 移除个数，列表不存在返回0
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove == null ? 0 : remove;
        } catch (Exception e) {
            log.error("移除redis数据异常，错误信息为：", e);
            return 0;
        }
    }

    public static <T> List<T> getList(String key, RedisTemplate rt) {
        ListOperations<String, T> listOperations =  rt.opsForList();
        return listOperations.range(key, 0, -1);
    }

    public static <T> boolean setList(String key, List<T> list, RedisTemplate rt) {
        return setList(key, list, 0, rt);
    }

    public static <T> boolean setList(String key, List<T> list, long time, RedisTemplate rt) {
        rt.opsForList().rightPushAll(key, list);
        if (time > 0) {
            expire(key, time, rt);
        }
        return true;
    }

    /**
     * 删除缓存
     * @param key
     * @param rt
     * @return
     */
    public static boolean deleteByKey(String key, RedisTemplate rt) {
        return rt.delete(key);
    }

    /**
     * 获取指定key的值
     *
     * @param key 键，不能为null
     * @return 值
     */
    public static <V> V get(String key, RedisTemplate rt) {
        ValueOperations<String, V> valueOperations = rt.opsForValue();
        try {
            return valueOperations.get(key);
        }catch (Exception e) {
            e.printStackTrace();
            log.error(key + "获取Redis缓存异常");
        }
        return null;
    }

    /**
     * 设置指定key的值并设置有效时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0；如果time小于等于0 将设置默认时间1800秒
     * @return true：成功；false：失败
     */
    public static boolean set(String key, Object value, long time, TimeUnit timeUnit, RedisTemplate rt) {
        try {
            if (time > 0) {
                rt.opsForValue().set(key, value, time, timeUnit);
            } else {
                rt.opsForValue().set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("插入redis数据出错，错误信息为：", e);
            return false;
        }
    }

    /**
     * 获取指定key的值
     *
     * @param key 键，不能为null
     * @return 值
     */
    public <V> V get(String key) {
        ValueOperations<String, V> valueOperations = redisTemplate.opsForValue();
        try {
            return valueOperations.get(key);
        }catch (Exception e) {
            e.printStackTrace();
            log.info(key + "获取Redis缓存异常");
            del(key);
        }
        return null;
    }
}
