create table if not exists images
(
    id                  varchar(255) not null primary key,
    path                varchar(255) not null,
    title               varchar(255) not null
);

insert into images values
        ('1', 'https://lh3.googleusercontent.com/u/0/drive-viewer/AAOQEOROTiKQSWmsUkIb45El3pXIXVxyKQGprCIY0mS9FWDrCbBG2Kl8_mskU5JsK3sGjGipOpXGo88ORkuc3xpYkxOQvjMusQ=w2880-h1600', 'Пиво'),
        ('2', 'https://lh3.googleusercontent.com/u/0/drive-viewer/AAOQEOR3vTeWaTw4WYc50oL7pmQAQyehmkXPiSgRp__RTBspGO-1sKQnQlviIQp-Qk5IQwxG8F4Z9liU_TnoBtRzAhpvXv8NaA=w2880-h1600', 'Вино'),
        ('3', 'https://lh3.googleusercontent.com/u/0/drive-viewer/AAOQEOTCDjVDwaRtvoJhPXJcX33oH-nQCDori4iAteoSe_p6-W1jwAl4xfLpOKGW3S3yVBmfWcRI-RGvxWNmaFzRA8kyzbFXMQ=w2880-h1600', 'Самогон'),
        ('4', 'https://lh3.googleusercontent.com/u/0/drive-viewer/AAOQEOSHKTmLEE3VOod1kfieWNcL1G2uklBkFgxiJ-UiC72CH1wD7t08uLltz04_JBBpfZNiWNAemMkL7rimzYGGQec20txopA=w2880-h1600', 'Водка'),
        ('5', 'https://lh3.googleusercontent.com/u/0/drive-viewer/AAOQEOQDM0nvZYr03gVH0RrS0AXuelyY0xbAIT5on2eAemSUYTwJ7hvbxYkKMXqpltz-GroKcpLXzdMt8JPwuUJlqUAiQbpWsA=w2880-h1600', 'Ликер'),
        ('6', 'https://lh3.googleusercontent.com/u/0/drive-viewer/AAOQEOQDM0nvZYr03gVH0RrS0AXuelyY0xbAIT5on2eAemSUYTwJ7hvbxYkKMXqpltz-GroKcpLXzdMt8JPwuUJlqUAiQbpWsA=w2880-h1600', 'Звездочка');