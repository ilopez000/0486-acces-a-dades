-- =====================================================================
-- 0486 Accés a dades · DAM2 · PratFP 2026-27
-- 02_institut.sql · Base de dades de proves «institut»
-- Alumnat, mòduls i notes. Totes les persones són inventades.
-- Es pot executar tantes vegades com calgui: esborra i torna a crear.
-- =====================================================================

-- Perquè els accents arribin bé encara que el client no sigui UTF-8
SET NAMES utf8mb4;

DROP DATABASE IF EXISTS institut;
CREATE DATABASE institut CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE institut;

-- ---------------------------------------------------------------------
-- Estructura
-- ---------------------------------------------------------------------
CREATE TABLE cicle (
  codi   VARCHAR(5)   PRIMARY KEY,
  nom    VARCHAR(80)  NOT NULL,
  grau   ENUM('mitjà', 'superior') NOT NULL
) ENGINE=InnoDB;

CREATE TABLE modul (
  id          INT AUTO_INCREMENT PRIMARY KEY,
  codi        VARCHAR(5)   NOT NULL,
  nom         VARCHAR(80)  NOT NULL,
  hores       SMALLINT     NOT NULL,
  curs        TINYINT      NOT NULL CHECK (curs IN (1, 2)),
  cicle_codi  VARCHAR(5)   NOT NULL,
  CONSTRAINT uq_modul UNIQUE (codi, cicle_codi),
  CONSTRAINT fk_modul_cicle FOREIGN KEY (cicle_codi) REFERENCES cicle(codi)
) ENGINE=InnoDB;

CREATE TABLE alumne (
  id               INT AUTO_INCREMENT PRIMARY KEY,
  nom              VARCHAR(40)  NOT NULL,
  cognoms          VARCHAR(80)  NOT NULL,
  data_naixement   DATE         NOT NULL,
  email            VARCHAR(100) NOT NULL UNIQUE,
  ciutat           VARCHAR(50)  NULL,
  cicle_codi       VARCHAR(5)   NOT NULL,
  CONSTRAINT fk_alumne_cicle FOREIGN KEY (cicle_codi) REFERENCES cicle(codi)
) ENGINE=InnoDB;

-- La nota és NULL mentre el mòdul no està avaluat
CREATE TABLE matricula (
  alumne_id  INT           NOT NULL,
  modul_id   INT           NOT NULL,
  curs_acad  CHAR(7)       NOT NULL,
  nota       DECIMAL(4,2)  NULL CHECK (nota BETWEEN 0 AND 10),
  PRIMARY KEY (alumne_id, modul_id, curs_acad),
  CONSTRAINT fk_mat_alumne FOREIGN KEY (alumne_id) REFERENCES alumne(id) ON DELETE CASCADE,
  CONSTRAINT fk_mat_modul  FOREIGN KEY (modul_id)  REFERENCES modul(id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Dades
-- ---------------------------------------------------------------------
INSERT INTO cicle (codi, nom, grau) VALUES
 ('DAM',  'Desenvolupament d''Aplicacions Multiplataforma', 'superior'),
 ('DAW',  'Desenvolupament d''Aplicacions Web',             'superior'),
 ('ASIX', 'Administració de Sistemes Informàtics en Xarxa', 'superior'),
 ('SMX',  'Sistemes Microinformàtics i Xarxes',             'mitjà');

INSERT INTO modul (id, codi, nom, hores, curs, cicle_codi) VALUES
 (1,  '0484', 'Bases de dades',                          198, 1, 'DAM'),
 (2,  '0485', 'Programació',                             264, 1, 'DAM'),
 (3,  '0487', 'Entorns de desenvolupament',               99, 1, 'DAM'),
 (4,  '0486', 'Accés a dades',                           132, 2, 'DAM'),
 (5,  '0488', 'Desenvolupament d''interfícies',           132, 2, 'DAM'),
 (6,  '0490', 'Programació de serveis i processos',        99, 2, 'DAM'),
 (7,  '0484', 'Bases de dades',                          198, 1, 'DAW'),
 (8,  '0485', 'Programació',                             264, 1, 'DAW'),
 (9,  '0612', 'Desenvolupament web en entorn client',    132, 2, 'DAW'),
 (10, '0613', 'Desenvolupament web en entorn servidor',  165, 2, 'DAW'),
 (11, '0369', 'Implantació de sistemes operatius',       198, 1, 'ASIX'),
 (12, '0374', 'Administració de sistemes operatius',     132, 2, 'ASIX');
-- Cap alumne està matriculat de l'ASIX 0374 ni hi ha mòduls de SMX: serveix per als LEFT JOIN

INSERT INTO alumne (id, nom, cognoms, data_naixement, email, ciutat, cicle_codi) VALUES
 (1,  'Aina',    'Puig Serra',        '2004-03-12', 'aina.puig@example.com',     'El Prat de Llobregat',      'DAM'),
 (2,  'Biel',    'Martí Soler',       '2003-11-02', 'biel.marti@example.com',    'Barcelona',                 'DAM'),
 (3,  'Carla',   'Vidal Roca',        '2005-06-25', 'carla.vidal@example.com',   'Viladecans',                'DAM'),
 (4,  'David',   'Ruiz Ortega',       '2002-01-30', 'david.ruiz@example.com',    'L''Hospitalet de Llobregat', 'DAM'),
 (5,  'Elena',   'Ferrer Mas',        '2004-09-14', 'elena.ferrer@example.com',  'Sant Boi de Llobregat',     'DAM'),
 (6,  'Ferran',  'Costa Ribas',       '2005-02-08', 'ferran.costa@example.com',  'El Prat de Llobregat',      'DAM'),
 (7,  'Gemma',   'López Navarro',     '2003-07-19', 'gemma.lopez@example.com',   'Cornellà de Llobregat',     'DAM'),
 (8,  'Hugo',    'Sánchez Pérez',     '2004-12-01', 'hugo.sanchez@example.com',  NULL,                        'DAM'),
 (9,  'Irene',   'Font Casals',       '2003-04-22', 'irene.font@example.com',    'Gavà',                      'DAW'),
 (10, 'Jordi',   'Bosch Prats',       '2004-08-05', 'jordi.bosch@example.com',   'Barcelona',                 'DAW'),
 (11, 'Laia',    'Romero Gil',        '2005-10-17', 'laia.romero@example.com',   'Castelldefels',             'DAW'),
 (12, 'Marc',    'Torres Vila',       '2002-05-28', 'marc.torres@example.com',   'El Prat de Llobregat',      'DAW'),
 (13, 'Núria',   'Garcia Pons',       '2004-01-09', 'nuria.garcia@example.com',  'Barcelona',                 'DAW'),
 (14, 'Oriol',   'Camps Rius',        '2003-03-03', 'oriol.camps@example.com',   'Viladecans',                'ASIX'),
 (15, 'Paula',   'Molina Sanz',       '2005-11-11', 'paula.molina@example.com',  'Sant Boi de Llobregat',     'ASIX'),
 (16, 'Quim',    'Esteve Lluch',      '2004-06-16', 'quim.esteve@example.com',   'El Prat de Llobregat',      'ASIX'),
 (17, 'Rita',    'Oliva Duran',       '2003-09-27', 'rita.oliva@example.com',    'Gavà',                      'ASIX'),
 (18, 'Sergi',   'Pujol Carbó',       '2005-12-30', 'sergi.pujol@example.com',   'Barcelona',                 'DAM'),
 (19, 'Txell',   'Badia Fuster',      '2004-02-18', 'txell.badia@example.com',   'Cornellà de Llobregat',     'DAW'),
 (20, 'Unai',    'Iglesias Mora',     '2005-05-05', 'unai.iglesias@example.com', 'El Prat de Llobregat',      'SMX');
-- L'alumne 20 (SMX) no té cap matrícula: serveix per als LEFT JOIN

INSERT INTO matricula (alumne_id, modul_id, curs_acad, nota) VALUES
 -- DAM, primer curs (2025-26)
 (1, 1, '2025-26', 8.50), (1, 2, '2025-26', 9.25), (1, 3, '2025-26', 7.75),
 (2, 1, '2025-26', 6.00), (2, 2, '2025-26', 5.50), (2, 3, '2025-26', 6.25),
 (3, 1, '2025-26', 4.25), (3, 2, '2025-26', 7.00), (3, 3, '2025-26', 8.00),
 (4, 1, '2025-26', 9.75), (4, 2, '2025-26', 9.50), (4, 3, '2025-26', 9.00),
 (5, 1, '2025-26', 5.00), (5, 2, '2025-26', 3.75), (5, 3, '2025-26', 6.50),
 (6, 1, '2025-26', 7.25), (6, 2, '2025-26', 8.00), (6, 3, '2025-26', 7.50),
 (7, 1, '2025-26', 6.75), (7, 2, '2025-26', 6.00), (7, 3, '2025-26', 5.25),
 (8, 1, '2025-26', 2.50), (8, 2, '2025-26', 4.00), (8, 3, '2025-26', 5.00),
 (18, 1, '2025-26', 8.00), (18, 2, '2025-26', 7.25), (18, 3, '2025-26', 8.75),
 -- DAM, segon curs (2026-27, encara sense avaluar: nota NULL)
 (1, 4, '2026-27', NULL), (1, 5, '2026-27', NULL), (1, 6, '2026-27', NULL),
 (4, 4, '2026-27', NULL), (4, 5, '2026-27', NULL), (4, 6, '2026-27', NULL),
 (6, 4, '2026-27', NULL), (6, 5, '2026-27', NULL),
 -- Repetidors de DAM (2026-27)
 (3, 1, '2026-27', NULL), (5, 2, '2026-27', NULL), (8, 1, '2026-27', NULL), (8, 2, '2026-27', NULL),
 -- DAW (2025-26)
 (9, 7, '2025-26', 7.00),  (9, 8, '2025-26', 8.25),
 (10, 7, '2025-26', 5.75), (10, 8, '2025-26', 6.50),
 (11, 7, '2025-26', 9.00), (11, 8, '2025-26', 8.75),
 (12, 7, '2025-26', 3.50), (12, 8, '2025-26', 5.00),
 (13, 7, '2025-26', 6.25), (13, 8, '2025-26', 7.75),
 (19, 7, '2025-26', 8.50), (19, 8, '2025-26', 9.00),
 -- DAW, segon curs (2026-27)
 (9, 9, '2026-27', NULL), (9, 10, '2026-27', NULL),
 (11, 9, '2026-27', NULL), (11, 10, '2026-27', NULL),
 -- ASIX (2025-26)
 (14, 11, '2025-26', 6.50), (15, 11, '2025-26', 8.00),
 (16, 11, '2025-26', 4.50), (17, 11, '2025-26', 7.25);
