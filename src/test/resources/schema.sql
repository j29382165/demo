-- 一定要加IF NOT EXISTS,避免重複創建table product --
CREATE TABLE IF NOT EXISTS product
(
    product_id         INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_name       VARCHAR(128)  NOT NULL,
    category           VARCHAR(32)  NOT NULL,
    image_url          VARCHAR(256) NOT NULL,
    price              INT          NOT NULL,
    stock              INT          NOT NULL,
    description        VARCHAR(1024),
    created_date       TIMESTAMP    NOT NULL,
    last_modified_date TIMESTAMP    NOT NULL
    );

-- 一定要加IF NOT EXISTS,避免重複創建table user,帳號測試 --
CREATE TABLE IF NOT EXISTS user
(
    user_id            INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email              VARCHAR(256) NOT NULL UNIQUE,  -- 是UNIQUE, 不是UNIQUE KEY
    password           VARCHAR(256) NOT NULL,
    created_date       TIMESTAMP    NOT NULL,
    last_modified_date TIMESTAMP    NOT NULL
    );

-- 一定要加IF NOT EXISTS,避免重複創建table order,訂單測試 --
CREATE TABLE IF NOT EXISTS `order`
(
    order_id           INT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    user_id            INT       NOT NULL,
    total_amount       INT       NOT NULL,
    created_date       TIMESTAMP NOT NULL,
    last_modified_date TIMESTAMP NOT NULL
);

-- 一定要加IF NOT EXISTS,避免重複創建table order_item
CREATE TABLE IF NOT EXISTS order_item
(
    order_item_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_id      INT NOT NULL,
    product_id    INT NOT NULL,
    quantity      INT NOT NULL,
    amount        INT NOT NULL
);

