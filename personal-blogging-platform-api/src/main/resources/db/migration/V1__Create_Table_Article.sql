CREATE TABLE IF NOT EXISTS `article` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `title` varchar(80) NOT NULL,
    `content` text NOT NULL,
    `publicationDate` timestamp NOT NULL,
    PRIMARY KEY (`id`)
);