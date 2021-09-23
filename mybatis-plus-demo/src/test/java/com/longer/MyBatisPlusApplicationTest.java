package com.longer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.longer.mybatisplus.mapper.UserMapper;
import com.longer.mybatisplus.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MyBatisPlusApplicationTest {

    // 注入持久层
    @Autowired
    private UserMapper userMapper;

    // 测试添加
    @Test
    public void testInsert() {
        Users user = new Users();
        user.setUserName("小糸侑");
        user.setPassword("789524");
        user.setStatus("S");

        userMapper.insert(user);
        /* 测试添加以后的说明，用到了 mybatis-plus 框架里面对主键的策略：自带生成 19 位 id，用到了自动填充 */
    }

    // 测试修改
    @Test
    public void testUpdate() {
        Users user = userMapper.selectById("1438071766336434178");

        userMapper.updateById(user);
        /* 测试修改以后的说明，用到了乐观锁和自动填充（先查询在修改） */
    }

    // 测试查询
    @Test
    public void testQuery() {
        // 不给条件就查询全部用
        userMapper.selectList(null);

        // 根据主键查询
        // userMapper.selectById("1430097170464104450");
        /* 正常的根据主键查询，返回唯一对象 */

        // 多个 id 批量查询
        // userMapper.selectBatchIds(Arrays.asList("1", "2"));
        /* 根据集合中的多个 id 查询，返回 list集合 */

        // map 条件查询
        // Map<String, Object> map = new HashMap<String, Object>();
        // map.put("USERNAME", "小糸侑");
        // map.put("PWD", "789524");
        // userMapper.selectByMap(map);
        /* 此处注意，使用 ByMap 的条件查询，里面的键对应的是数据库中的字段，比如这里，我 Users 对象中的密码属性是 password，数据库中的字段是 pwd，这里需要写上数据库的字段，map 集合的值是对应字段查询条件的值，返回的是 list 集合 */
    }

    // 测试分页查询
    @Test
    public void testPage() {
        // 创建 page 对象，传入两个参数，当前页和每页几条数据（此处为第一页，每页三条数据）
        Page<Users> page = new Page<Users>(1, 3);

        // 调用 mybatis-plus 分页查询，会将查询的结果封装到 page 对象里面
        userMapper.selectPage(page, null);

        // 通过 page 对象获得分页数据
        System.out.println(page.getCurrent());// 当前页
        System.out.println(page.getRecords());// 每页的数据
        System.out.println(page.getSize());// 每页显示的记录数
        System.out.println(page.getTotal());// 总记录条数
        System.out.println(page.getPages());// 总共分多少页

        System.out.println(page.hasNext());// 是否有下一页
        System.out.println(page.hasPrevious());// 是否有上一页
    }

    // 测试物理删除
    @Test
    public void testDelete() {
        // 根据主键删除
        userMapper.deleteById("1438071766336434178");

        // 批量删除
        // userMapper.deleteBatchIds(Arrays.asList("2", "1430070181233537026", "1430058192079687681", "1430070570871808002", "1430097170464104450"));
    }

    // 测试逻辑删除
    @Test
    public void testDeleted() {
        // userMapper.deleteById("1438071766336434178");
        /* 逻辑删除和物理删除方法一样，但是加上了 @TableLogic 注解和逻辑删除插件，这样以后不管是修改删除还是查询，都会默认加上 DELETED = 0 这个条件，如果想查询被逻辑删除的信息，那么就只能自己去写 xml 文件写 sql 查询了 */
    }

    // 条件查询
    @Test
    public void testQueryWrapper() {
        // 使用 QueryWrapper 来实现条件查询
        QueryWrapper<Users> wrapper = new QueryWrapper<>();

        // 比较运算符：ge >=, gt >, le <=, lt <
        // wrapper.ge("VERSION", 0);
        // userMapper.selectList(wrapper);

        // eq ==, ne != <>
        // wrapper.eq("STATUS", "S");
        // userMapper.selectList(wrapper);

        // like 模糊查询
        // wrapper.like("USERNAME", "糸");
        // userMapper.selectList(wrapper);

        // between 闭区间
        // wrapper.between("VERSION", 1, 3);
        // userMapper.selectList(wrapper);

        // 排序：orderByDesc 降序 orderByAsc 升序
        // wrapper.orderByDesc("VERSION");
        // userMapper.selectList(wrapper);

        // last sql 语句拼接
        // wrapper.last("AND USERNAME = '小糸侑'");
        // userMapper.selectList(wrapper);

        wrapper.eq("USERNAME", "小糸侑");

        // 选择要查询的字段
        // wrapper.select("USERID", "USERNAME", "PWD", "STATUS");
        userMapper.selectList(wrapper);
    }

}
