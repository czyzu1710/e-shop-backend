-- !Ups
CREATE TABLE BRAND
(
    ID         INTEGER PRIMARY KEY AUTOINCREMENT,
    BRAND_NAME TEXT UNIQUE
);

-- !Downs
DROP TABLE BRAND;