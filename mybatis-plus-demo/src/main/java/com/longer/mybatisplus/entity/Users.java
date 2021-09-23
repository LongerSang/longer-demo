package com.longer.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "TB_USER")
public class Users implements Serializable {
    /* 说明主键的几种不同策略
     * ID_WORKER_STR：mybatis-plus 自带的主键生成，_STR 是字符串类型
     * ID_WORKER：mybatis-plus 自带的主键生成，long 长整数类型，两种生成都是 19 位
     * AUTO：对应数据库自增
     * INPUT：请自己输入 id
     * NONE：这个也是自己输入，不过一般用上面的
     * UUID：一段不重复的字符串，很长还是混搭类型 */
    /**
     * 用户 id 主键，采用 mybatis-plus 的自动生成
     */
    @TableId(value = "USERID", type = IdType.ID_WORKER_STR)
    private String userID;

    /**
     * 用户名 and 账号 唯一
     */
    @TableField(value = "USERNAME")
    private String userName;

    /**
     * 密码
     */
    @TableField(value = "PWD")
    private String password;

    /**
     * 身份
     */
    @TableField(value = "STATUS")
    private String status;

    /**
     * 创建时间，添加的时候调用
     */
    @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 修改时间，添加和修改的时候调用
     */
    @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 版本号，乐观锁
     */
    @Version
    @TableField(value = "VERSION", fill = FieldFill.INSERT)
    private Integer version;

    /**
     * 逻辑删除
     */
    @TableLogic
    @TableField(value = "DELETED")
    private Integer deleted;
}
