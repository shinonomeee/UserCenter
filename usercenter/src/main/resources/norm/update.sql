# 2022.08.14 update
alter table usercenter.user
    modify gender tinyint default 2 null comment '性别 0-男 1-女 2-未设置';

