create table user
(
    id            bigint auto_increment comment 'id 主键'
        primary key,
    username      varchar(256)                        null comment '用户昵称',
    user_account  varchar(256)                        null comment '账户名',
    avatar_url    varchar(1024)                       null comment '头像地址',
    gender        tinyint   default 2                 null comment '性别 0-男 1-女 2-未设置',
    user_password varchar(512)                        not null comment '密码',
    phone         varchar(128)                        null comment '电话',
    email         varchar(512)                        null comment '邮箱',
    user_status   int       default 0                 not null comment '用户状态
0 正常',
    user_role     int       default 0                 not null comment '用户角色：0-普通用户 1-管理员',
    create_time   timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint   default 0                 not null comment '是否删除
0 表示未删除'
)
    comment '用户表';