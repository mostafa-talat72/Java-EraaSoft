CREATE TABLE player (
                        id NUMBER NOT NULL,
                        name VARCHAR2(100),
                        age NUMBER,
                        CONSTRAINT player_id_uk UNIQUE (id),
                        CONSTRAINT player_name_uk UNIQUE (name)
);