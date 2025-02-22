# 会员表
drop table if exists `member`;
create table `member`
(
    `id`     bigint not null comment 'id',
    `mobile` varchar(11) comment '手机号',
    primary key (`id`),
    unique key `idx_mobile` (`mobile`)
)
    engine = innodb
    default charset = utf8mb4 comment ='会员';

insert into `member` (id, mobile) values (1, '18943191561');

# 乘车人表
drop table if exists `passenger`;
create table `passenger`
(
    `id`          bigint      not null comment 'id',
    `member_id`   bigint      not null comment '会员id',
    `name`        varchar(20) not null comment '姓名',
    `id_card`     varchar(18) not null comment '身份证',
    `type`        char(1)     not null comment '旅客类型|枚举[PassengerTypeEnum]',
    `create_time` datetime(3) comment '新增时间',
    `update_time` datetime(3) comment '修改时间',
    primary key (`id`),
    index `member_id_index` (`member_id`)
) engine = innodb
  default charset = utf8mb4 comment ='乘车人';