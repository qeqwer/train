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

