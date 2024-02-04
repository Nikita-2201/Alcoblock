create table if not exists alco
(
    id                  varchar(255) not null primary key,
    picture_id          varchar(255),
    strength            real not null,
    title               varchar(255) not null,
    user_id             varchar(255)
);

insert into alco values
        ('1', '1', 4, 'Пиво', null),
        ('2', '2', 16, 'Вино', null),
        ('3', '3', 80, 'Самогон', null),
        ('4', '4', 40, 'Водка', null),
        ('5', '5', 35, 'Ликёр', null);
