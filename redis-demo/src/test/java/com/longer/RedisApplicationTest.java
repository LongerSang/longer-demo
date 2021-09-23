package com.longer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.longer.redis.entity.Goods;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class RedisApplicationTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 测试 StringRedisTemplate 存储字符串
    @Test
    public void testStringRedisTemplate() {
        String name = "小糸侑";// 定义变量
        stringRedisTemplate.opsForValue().set("name", name);// 将变量存入 redis 缓存数据库
        name = stringRedisTemplate.opsForValue().get("name");// 从缓存数据库中取出数据
        System.out.println(name);// 打印
        /* 以上测试为正常的添加取出字符串 */
    }

    @Autowired
    private RedisTemplate redisTemplate;

    // 测试 RedisTemplate 存储对象
    @Test
    public void testRedisTemplate() {
        Goods goods = new Goods("1", "娃哈哈", 1);// 一个商品
        redisTemplate.opsForValue().set("goods", goods);// 将商品存入 redis 缓存数据库
        goods = (Goods) redisTemplate.opsForValue().get("goods");// 从缓存数据库中取出数据
        System.out.println(goods);// 打印
        /* 以上测试为正常的添加和取出对象 */
    }

    // redis 主机
    @Value("${spring.redis.host}")
    private String redis_host;

    // redis 端口号
    @Value("${spring.redis.port}")
    private int redis_port;

    private ObjectMapper mapper = new ObjectMapper();// 将对象转换成字符串类型的对象

    // 测试将对象转换成 json 格式的字符串在存入 redis 缓存数据库
    @Test
    public void testObject_String() {
        // jedis 连接缓冲数据库
        Jedis jedis = new Jedis(redis_host, redis_port);
        Goods goods = new Goods("1", "娃哈哈", 1);// 一个商品
        try {
            String string_goods = mapper.writeValueAsString(goods);// 将对象转换成字符串
            // redisTemplate.opsForValue().set("string_goods", string_goods);// 将转换成字符串的对象存入缓存数据库
            jedis.set("string_goods", string_goods);// 将转换成字符串的对象存入缓存数据库
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // String string_goods = stringRedisTemplate.opsForValue().get("string_goods");// 从缓存数据库中取出数据
        String string_goods = jedis.get("string_goods");// 从缓存数据库中取出数据
        System.out.println(string_goods);// 打印从中取出的字符串对象
        try {
            goods = mapper.readValue(string_goods, Goods.class);// 将取出的字符串转换对象
            System.out.println(goods);// 打印对象
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            jedis.close();// 关流
        }
        /* 上面测试用 jedis 为正常数据，用 stringRedisTemplate 存入对象转换的字符串会有奇怪的符号在里面 */
    }

    // 测试 redis 存储集合 List
    @Test
    public void testRedisList() {
        // jedis 连接缓冲数据库
        Jedis jedis = new Jedis(redis_host, redis_port);
        List<Goods> goodsList = new ArrayList<>();// 定义一个集合
        for (int i = 0; i < 3; i++) {// 循环三次，new 三个对象将放入集合
            Goods goods = new Goods("" + i, "娃哈哈" + i, 1);
            goodsList.add(goods);
        }
        try {
            String goodsList_str = mapper.writeValueAsString(goodsList);// 将集合转换成字符串
            stringRedisTemplate.opsForValue().set("goodsList_str", goodsList_str);// 将转换成字符串的集合存入 redis 缓存数据库
            // jedis.set("goodsList_str", goodsList_str);// 将转换成字符串的集合存入 redis 缓存数据库
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String goodsList_str = stringRedisTemplate.opsForValue().get("goodsList_str");// 从缓存数据库中取出数据
        // String goodsList_str = jedis.get("goodsList_str");// 从缓存数据库中取出数据
        System.out.println(goodsList_str);// 打印从中取出的字符串对象
        try {
            goodsList = mapper.readValue(goodsList_str, List.class);// 将字符串转换成集合
            System.out.println(goodsList);// 直接将集合打印出来
            for (int i = 0; i < goodsList.size(); i++) {
                System.out.println(goodsList.get(i));
            }// 循环集合的大小取出数据
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            jedis.close();// 关流
        }
        /* 以上测试使用 jedis 和 stringRedisTemplate 均成功，使用 for 循环为正常结果，但是使用 foreach 报错：java.util.LinkedHashMap cannot be cast to com.longer.redis.entity.Goods */
    }

    // 测试 redis 存储集合 Map
    @Test
    public void testRedisMap() {
        // jedis 连接缓冲数据库
        Jedis jedis = new Jedis(redis_host, redis_port);
        Map<String, Goods> goodsMap = new HashMap<>();// 定义一个集合
        for (int i = 0; i < 3; i++) {// 循环三次，new 三个对象将放入集合
            Goods goods = new Goods("" + i, "娃哈哈" + i, 1);
            goodsMap.put(goods.getGoodsId(), goods);
        }
        try {
            String goodsMap_str = mapper.writeValueAsString(goodsMap);// 将集合转换成字符串
            stringRedisTemplate.opsForValue().set("goodsMap_str", goodsMap_str);// 将转换成字符串的集合存入 redis 缓存数据库
            // jedis.set("goodsMap_str", goodsMap_str);// 将转换成字符串的集合存入 redis 缓存数据库
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String goodsMap_str = stringRedisTemplate.opsForValue().get("goodsMap_str");// 从缓存数据库中取出数据
        // String goodsMap_str = jedis.get("goodsMap_str");// 从缓存数据库中取出数据
        System.out.println(goodsMap_str);// 打印从中取出的字符串对象
        try {
            goodsMap = mapper.readValue(goodsMap_str, Map.class);// 将字符串转换成集合
            System.out.println(goodsMap);// 直接将集合打印出来
            for (String goodsId : goodsMap.keySet()) {// 取出 map 集合的 key 值
                // Goods goods = goodsMap.get(goodsId);// 将集合中的对象取出失败了
                // System.out.println(goods);
                System.out.println(goodsMap.get(goodsId));// 通过 key 值取出一个对象数据
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } finally {
            jedis.close();// 关流
        }
        /* 以上测试使用 jedis 和 stringRedisTemplate 均成功，但是将集合中的对象单个取出来转换报错：java.util.LinkedHashMap cannot be cast to com.longer.redis.entity.Goods */
    }

    /* 上面两个存储集合的方法都是将集合转换成 json 格式的字符串存入 redis 缓存数据库，取出来也是将 json 字符串转换成集合，下面测试之间存入集合和取出集合 */

    // 测试 redis 存取集合
    @Test
    public void testJedis() {
        // jedis 连接缓存数据库
        Jedis jedis = new Jedis(redis_host, redis_port);
        Map<String, String> map = new HashMap<>();// 创建一个 map 集合
        for (int i = 0; i < 3; i++) {
            map.put("" + i, "小糸侑");// 在集合中添加三条数据
        }
        jedis.hmset("map", map);// 将当前集合存入 redis 缓存数据库
        // 从缓冲数据库中取出集合
        map = jedis.hgetAll("map");
        for (String key : map.keySet()) {// 取出键 key
            System.out.println(map.get(key));// 通过 键 key 得到 值 value
        }
    }

    // 测试 redis 删除数据
    @Test
    public void testDel() {
        // jedis 连接缓存数据库
        Jedis jedis = new Jedis(redis_host, redis_port);
        jedis.del("map");// 删掉，给老子滚
    }

}
