CREATE TABLE IF NOT EXISTS `article_tag` (
    `id_article` BIGINT,
    `id_tag` BIGINT,
    PRIMARY KEY(`id_article`, `id_tag`),
    FOREIGN KEY (`id_article`) REFERENCES `article`(`id`),
    FOREIGN KEY (`id_tag`) REFERENCES `tag`(`id`)
);