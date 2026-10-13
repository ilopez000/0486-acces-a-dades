-- =====================================================================
-- 0486 Accés a dades · DAM2 · PratFP 2026-27
-- 00_usuari_dam2.sql · Crea l'usuari de treball del mòdul
-- S'executa UNA vegada, connectat com a root.
-- =====================================================================

-- L'usuari dam2 només es pot connectar des del mateix ordinador.
-- Es crea per a 'localhost' i per a '127.0.0.1' perquè, segons com
-- es connecti el client, MariaDB veu un nom o l'altre.
CREATE USER IF NOT EXISTS 'dam2'@'localhost' IDENTIFIED BY 'dam2pass';
CREATE USER IF NOT EXISTS 'dam2'@'127.0.0.1' IDENTIFIED BY 'dam2pass';

-- Té tots els permisos, però només sobre les dues bases de dades de proves.
GRANT ALL PRIVILEGES ON gamevault_proves.* TO 'dam2'@'localhost';
GRANT ALL PRIVILEGES ON gamevault_proves.* TO 'dam2'@'127.0.0.1';
GRANT ALL PRIVILEGES ON institut.*  TO 'dam2'@'localhost';
GRANT ALL PRIVILEGES ON institut.*  TO 'dam2'@'127.0.0.1';

FLUSH PRIVILEGES;
