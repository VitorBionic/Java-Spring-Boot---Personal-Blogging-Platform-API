CREATE TABLE IF NOT EXISTS `article` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `title` VARCHAR(80) NOT NULL,
    `content` TEXT NOT NULL,
    `publication_date` TIMESTAMP NOT NULL,
    PRIMARY KEY (`id`)
);