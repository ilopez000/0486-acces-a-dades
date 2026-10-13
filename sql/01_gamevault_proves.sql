-- =====================================================================
-- 0486 Accés a dades · DAM2 · PratFP 2026-27
-- 01_gamevault_proves.sql · Base de dades de proves del catàleg GameVault
-- (no és la base de dades «gamevault» que construireu vosaltres a l'AEA2)
-- Dades de prova inspirades en jocs reals (preus, estocs, jugadors,
-- ressenyes i compres són inventats).
-- Es pot executar tantes vegades com calgui: esborra i torna a crear.
-- =====================================================================

-- Perquè els accents arribin bé encara que el client no sigui UTF-8
SET NAMES utf8mb4;

DROP DATABASE IF EXISTS gamevault_proves;
CREATE DATABASE gamevault_proves CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gamevault_proves;

-- ---------------------------------------------------------------------
-- Estructura
-- ---------------------------------------------------------------------
CREATE TABLE estudi (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  nom           VARCHAR(80)  NOT NULL UNIQUE,
  pais          VARCHAR(40)  NOT NULL,
  any_fundacio  SMALLINT     NULL
) ENGINE=InnoDB;

CREATE TABLE plataforma (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  codi       VARCHAR(10)  NOT NULL UNIQUE,
  nom        VARCHAR(60)  NOT NULL,
  fabricant  VARCHAR(40)  NOT NULL
) ENGINE=InnoDB;

CREATE TABLE joc (
  id               INT AUTO_INCREMENT PRIMARY KEY,
  titol            VARCHAR(120) NOT NULL,
  genere           VARCHAR(30)  NOT NULL,
  data_llancament  DATE         NULL,
  pegi             TINYINT      NULL CHECK (pegi IN (3, 7, 12, 16, 18)),
  estudi_id        INT          NOT NULL,
  CONSTRAINT fk_joc_estudi FOREIGN KEY (estudi_id) REFERENCES estudi(id)
) ENGINE=InnoDB;

-- Relació N:M entre jocs i plataformes, amb el preu i l'estoc de cada versió
CREATE TABLE joc_plataforma (
  joc_id         INT          NOT NULL,
  plataforma_id  INT          NOT NULL,
  preu           DECIMAL(6,2) NOT NULL CHECK (preu >= 0),
  estoc          INT          NOT NULL DEFAULT 0 CHECK (estoc >= 0),
  PRIMARY KEY (joc_id, plataforma_id),
  CONSTRAINT fk_jp_joc        FOREIGN KEY (joc_id)        REFERENCES joc(id) ON DELETE CASCADE,
  CONSTRAINT fk_jp_plataforma FOREIGN KEY (plataforma_id) REFERENCES plataforma(id)
) ENGINE=InnoDB;

CREATE TABLE jugador (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  nick       VARCHAR(30)  NOT NULL UNIQUE,
  email      VARCHAR(100) NOT NULL UNIQUE,
  data_alta  DATE         NOT NULL,
  ciutat     VARCHAR(50)  NULL
) ENGINE=InnoDB;

CREATE TABLE ressenya (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  joc_id      INT          NOT NULL,
  jugador_id  INT          NOT NULL,
  nota        TINYINT      NOT NULL CHECK (nota BETWEEN 1 AND 10),
  comentari   VARCHAR(255) NULL,
  data        DATE         NOT NULL,
  CONSTRAINT uq_ressenya UNIQUE (joc_id, jugador_id),
  CONSTRAINT fk_res_joc     FOREIGN KEY (joc_id)     REFERENCES joc(id) ON DELETE CASCADE,
  CONSTRAINT fk_res_jugador FOREIGN KEY (jugador_id) REFERENCES jugador(id)
) ENGINE=InnoDB;

-- Cada compra és d'una versió concreta (joc + plataforma) que ha d'existir
CREATE TABLE compra (
  id             INT AUTO_INCREMENT PRIMARY KEY,
  jugador_id     INT          NOT NULL,
  joc_id         INT          NOT NULL,
  plataforma_id  INT          NOT NULL,
  data           DATE         NOT NULL,
  preu_pagat     DECIMAL(6,2) NOT NULL,
  CONSTRAINT fk_com_jugador FOREIGN KEY (jugador_id) REFERENCES jugador(id),
  CONSTRAINT fk_com_versio  FOREIGN KEY (joc_id, plataforma_id)
                            REFERENCES joc_plataforma(joc_id, plataforma_id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Dades
-- ---------------------------------------------------------------------
INSERT INTO estudi (id, nom, pais, any_fundacio) VALUES
 (1,  'Nintendo EPD',        'Japó',          2015),
 (2,  'Rockstar Games',      'Estats Units',  1998),
 (3,  'CD Projekt Red',      'Polònia',       2002),
 (4,  'FromSoftware',        'Japó',          1986),
 (5,  'Team Cherry',         'Austràlia',     2014),
 (6,  'Maddy Makes Games',   'Canadà',        NULL),
 (7,  'Supergiant Games',    'Estats Units',  2009),
 (8,  'Nomada Studio',       'Espanya',       2016),
 (9,  'Tequila Works',       'Espanya',       2009),
 (10, 'Larian Studios',      'Bèlgica',       1996),
 (11, 'ConcernedApe',        'Estats Units',  2012),
 (12, 'Mojang Studios',      'Suècia',        2009),
 (13, 'Digital Legends',     'Espanya',       2001);

INSERT INTO plataforma (id, codi, nom, fabricant) VALUES
 (1, 'PC',      'PC (Windows)',          'Diversos'),
 (2, 'PS5',     'PlayStation 5',         'Sony'),
 (3, 'PS4',     'PlayStation 4',         'Sony'),
 (4, 'XSX',     'Xbox Series X|S',       'Microsoft'),
 (5, 'SWITCH',  'Nintendo Switch',       'Nintendo'),
 (6, 'SWITCH2', 'Nintendo Switch 2',     'Nintendo'),
 (7, 'MOBIL',   'Mòbil (Android i iOS)', 'Diversos');

INSERT INTO joc (id, titol, genere, data_llancament, pegi, estudi_id) VALUES
 (1,  'The Legend of Zelda: Breath of the Wild',  'Aventura',     '2017-03-03', 12, 1),
 (2,  'The Legend of Zelda: Tears of the Kingdom', 'Aventura',    '2023-05-12', 12, 1),
 (3,  'Super Mario Odyssey',      'Plataformes',  '2017-10-27',  7, 1),
 (4,  'Red Dead Redemption 2',    'Acció',        '2018-10-26', 18, 2),
 (5,  'Grand Theft Auto V',       'Acció',        '2013-09-17', 18, 2),
 (6,  'The Witcher 3: Wild Hunt', 'RPG',          '2015-05-19', 18, 3),
 (7,  'Cyberpunk 2077',           'RPG',          '2020-12-10', 18, 3),
 (8,  'Elden Ring',               'RPG',          '2022-02-25', 16, 4),
 (9,  'Dark Souls III',           'RPG',          '2016-04-12', 16, 4),
 (10, 'Hollow Knight',            'Metroidvania', '2017-02-24',  7, 5),
 (11, 'Hollow Knight: Silksong',  'Metroidvania', '2025-09-04',  7, 5),
 (12, 'Celeste',                  'Plataformes',  '2018-01-25',  7, 6),
 (13, 'Hades',                    'Roguelike',    '2020-09-17', 12, 7),
 (14, 'Hades II',                 'Roguelike',    '2025-09-25', 12, 7),
 (15, 'Gris',                     'Plataformes',  '2018-12-13',  3, 8),
 (16, 'Neva',                     'Plataformes',  '2024-10-15',  7, 8),
 (17, 'Rime',                     'Aventura',     '2017-05-26',  7, 9),
 (18, 'Baldur''s Gate 3',         'RPG',          '2023-08-03', 18, 10),
 (19, 'Stardew Valley',           'Simulació',    '2016-02-26',  7, 11),
 (20, 'Minecraft',                'Sandbox',      '2011-11-18',  7, 12),
 (21, 'Pikmin 4',                 'Estratègia',   '2023-07-21',  3, 1),
 (22, 'Mario Kart World',         'Curses',       '2025-06-05',  3, 1),
 (23, 'Grand Theft Auto VI',      'Acció',        NULL,        NULL, 2),
 (24, 'Elden Ring Nightreign',    'RPG',          '2025-05-30', 16, 4);

INSERT INTO joc_plataforma (joc_id, plataforma_id, preu, estoc) VALUES
 (1, 5, 59.99, 12), (1, 6, 69.99, 5),
 (2, 5, 69.99, 8),  (2, 6, 79.99, 4),
 (3, 5, 49.99, 10),
 (4, 1, 39.99, 20), (4, 3, 29.99, 6), (4, 4, 39.99, 3),
 (5, 1, 29.99, 15), (5, 2, 39.99, 9), (5, 4, 39.99, 7), (5, 3, 19.99, 0),
 (6, 1, 29.99, 11), (6, 2, 39.99, 5), (6, 5, 39.99, 2),
 (7, 1, 59.99, 14), (7, 2, 59.99, 6), (7, 4, 59.99, 4), (7, 6, 69.99, 3),
 (8, 1, 59.99, 18), (8, 2, 59.99, 10), (8, 4, 59.99, 5), (8, 6, 69.99, 7),
 (9, 1, 59.99, 3),  (9, 3, 29.99, 2),
 (10, 1, 14.99, 30), (10, 5, 14.99, 12), (10, 3, 14.99, 4),
 (11, 1, 19.50, 25), (11, 5, 19.50, 10), (11, 6, 19.50, 9), (11, 2, 19.50, 8),
 (12, 1, 19.99, 20), (12, 5, 19.99, 6),
 (13, 1, 24.50, 15), (13, 5, 24.50, 8), (13, 2, 24.99, 3),
 (14, 1, 29.50, 22), (14, 6, 29.50, 6),
 (15, 1, 16.99, 9),  (15, 5, 16.99, 5), (15, 7, 4.99, 0),
 (16, 1, 19.99, 7),  (16, 2, 19.99, 3), (16, 5, 19.99, 2),
 (17, 1, 29.99, 1),  (17, 3, 19.99, 0),
 (18, 1, 59.99, 16), (18, 2, 69.99, 7), (18, 4, 69.99, 2),
 (19, 1, 13.99, 40), (19, 5, 13.99, 15), (19, 7, 4.99, 0),
 (20, 1, 29.99, 50), (20, 5, 29.99, 20), (20, 7, 6.99, 0), (20, 2, 19.99, 10),
 (21, 5, 59.99, 6),
 (22, 6, 79.99, 15),
 (24, 1, 39.99, 8),  (24, 2, 39.99, 6), (24, 4, 39.99, 2);

INSERT INTO jugador (id, nick, email, data_alta, ciutat) VALUES
 (1,  'pixelmarta',  'marta.pixel@example.com',  '2024-01-15', 'El Prat de Llobregat'),
 (2,  'noobmaster',  'joan.noob@example.com',    '2024-02-03', 'Barcelona'),
 (3,  'laia_gg',     'laia.gg@example.com',      '2024-03-21', 'L''Hospitalet de Llobregat'),
 (4,  'kratos_bcn',  'pau.kratos@example.com',   '2024-05-10', 'Barcelona'),
 (5,  'zeldafan',    'nuria.zelda@example.com',  '2024-06-30', 'Viladecans'),
 (6,  'speedrunner', 'arnau.speed@example.com',  '2024-09-01', 'Sant Boi de Llobregat'),
 (7,  'cozygamer',   'emma.cozy@example.com',    '2025-01-12', 'El Prat de Llobregat'),
 (8,  'darksoul88',  'marc.dark@example.com',    '2025-02-28', 'Cornellà de Llobregat'),
 (9,  'nintendera',  'julia.nin@example.com',    '2025-04-04', 'Castelldefels'),
 (10, 'retroadri',   'adria.retro@example.com',  '2025-07-19', NULL),
 (11, 'hadesfan',    'irene.hades@example.com',  '2025-10-02', 'Gavà'),
 (12, 'newbie26',    'oriol.new@example.com',    '2026-09-14', 'El Prat de Llobregat');

INSERT INTO ressenya (joc_id, jugador_id, nota, comentari, data) VALUES
 (1,  5, 10, 'El millor món obert que he jugat mai',            '2024-07-02'),
 (1,  9,  9, 'Llibertat total per explorar',                     '2025-04-10'),
 (1,  2,  8, NULL,                                                '2024-03-11'),
 (2,  5, 10, 'Ultramà és una genialitat',                        '2024-08-15'),
 (2,  9,  9, 'Més gran que el primer',                           '2025-05-01'),
 (3,  6,  9, 'Perfecte per fer speedruns',                       '2024-09-20'),
 (3,  9,  8, 'Divertit i molt variat',                           '2025-04-20'),
 (4,  4, 10, 'Una història que no oblidaré',                     '2024-06-01'),
 (4,  2,  7, 'Massa lent al principi',                           '2024-04-02'),
 (5,  2,  8, 'Encara aguanta',                                   '2024-02-20'),
 (5,  4,  6, NULL,                                                '2024-05-30'),
 (6,  1,  9, 'Les missions secundàries són de 10',               '2024-02-14'),
 (6,  8, 10, 'Obra mestra',                                      '2025-03-15'),
 (7,  1,  7, 'Ha millorat molt amb els pegats',                  '2024-11-05'),
 (7,  4,  8, 'Night City és impressionant',                      '2024-12-01'),
 (8,  8, 10, 'Difícil però just',                                '2025-03-01'),
 (8,  4,  9, 'M''ha costat 60 hores però val la pena',           '2024-07-21'),
 (8,  3,  6, 'Massa difícil per a mi',                           '2024-04-18'),
 (9,  8,  9, 'El Souls més rodó',                                '2025-03-20'),
 (10, 6, 10, 'Imprescindible',                                   '2024-10-02'),
 (10, 3,  9, 'Preciós i difícil',                                '2024-03-30'),
 (10, 10, 9, NULL,                                                '2025-08-01'),
 (11, 6,  9, 'Ha valgut la pena esperar',                        '2025-09-15'),
 (11, 11, 8, 'Més difícil que el primer',                        '2025-10-10'),
 (12, 6, 10, 'El millor plataformes indie',                      '2024-09-10'),
 (12, 7,  8, 'M''ha fet plorar',                                 '2025-01-20'),
 (13, 11, 10, 'No en tinc mai prou',                             '2025-10-05'),
 (13, 1,  9, 'Música i art espectaculars',                       '2024-01-30'),
 (14, 11, 9, 'Encara millor que el primer',                      '2025-10-12'),
 (15, 7,  9, 'Una obra d''art jugable',                          '2025-01-25'),
 (15, 3,  8, 'Curt però preciós',                                '2024-04-01'),
 (16, 7,  8, 'Fet a Barcelona i es nota el talent',              '2025-02-10'),
 (18, 1, 10, 'El millor RPG de la dècada',                       '2024-03-03'),
 (18, 3,  9, 'Juga-ho en cooperatiu',                            '2024-05-05'),
 (19, 7, 10, 'El joc més relaxant del món',                      '2025-01-13'),
 (19, 10, 8, 'Enganxa massa',                                    '2025-07-25'),
 (20, 10, 7, 'Un clàssic per a totes les edats',                 '2025-07-30'),
 (20, 2,  9, NULL,                                                '2024-02-10'),
 (21, 9,  8, 'Molt bonic per jugar en família',                  '2025-04-25'),
 (22, 9,  7, 'Divertit, però prefereixo el 8',                   '2025-06-20');

INSERT INTO compra (jugador_id, joc_id, plataforma_id, data, preu_pagat) VALUES
 (1, 6, 1,  '2024-02-01', 19.99),
 (1, 7, 1,  '2024-10-30', 59.99),
 (1, 13, 1, '2024-01-20', 24.50),
 (1, 18, 1, '2024-02-25', 59.99),
 (2, 1, 5,  '2024-03-01', 59.99),
 (2, 4, 1,  '2024-03-15', 39.99),
 (2, 5, 1,  '2024-02-05', 14.99),
 (2, 20, 1, '2024-02-05', 29.99),
 (3, 8, 2,  '2024-04-01', 59.99),
 (3, 10, 1, '2024-03-22', 14.99),
 (3, 15, 5, '2024-03-28', 16.99),
 (3, 18, 2, '2024-05-01', 69.99),
 (4, 4, 4,  '2024-05-12', 39.99),
 (4, 5, 2,  '2024-05-12', 39.99),
 (4, 7, 2,  '2024-11-20', 49.99),
 (4, 8, 2,  '2024-06-01', 59.99),
 (5, 1, 5,  '2024-06-30', 59.99),
 (5, 2, 5,  '2024-07-30', 69.99),
 (6, 3, 5,  '2024-09-02', 49.99),
 (6, 10, 5, '2024-09-05', 14.99),
 (6, 11, 5, '2025-09-04', 19.50),
 (6, 12, 1, '2024-09-02', 19.99),
 (7, 15, 1, '2025-01-14', 16.99),
 (7, 16, 1, '2025-02-01', 19.99),
 (7, 19, 5, '2025-01-12', 13.99),
 (7, 12, 5, '2025-01-18', 19.99),
 (8, 6, 2,  '2025-03-01', 39.99),
 (8, 8, 1,  '2025-02-28', 49.99),
 (8, 9, 1,  '2025-03-10', 59.99),
 (9, 1, 6,  '2025-06-05', 69.99),
 (9, 2, 6,  '2025-06-05', 79.99),
 (9, 3, 5,  '2025-04-15', 49.99),
 (9, 21, 5, '2025-04-22', 59.99),
 (9, 22, 6, '2025-06-05', 79.99),
 (10, 10, 1, '2025-07-20', 14.99),
 (10, 19, 1, '2025-07-21', 13.99),
 (10, 20, 1, '2025-07-25', 29.99),
 (11, 13, 5, '2025-10-02', 24.50),
 (11, 14, 1, '2025-10-02', 29.50),
 (11, 11, 1, '2025-10-03', 19.50);
