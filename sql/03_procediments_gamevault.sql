-- =====================================================================
-- 0486 Accés a dades · DAM2 · PratFP 2026-27
-- 03_procediments_gamevault.sql · Procediments i funcions de la sessió
-- de transaccions i procediments emmagatzemats (AA7)
-- S'executa connectat com a dam2 (o root) DESPRÉS de 01_gamevault_proves.sql.
-- Es pot tornar a executar: primer esborra i després crea.
-- =====================================================================
USE gamevault_proves;

DROP PROCEDURE IF EXISTS jocs_per_genere;
DROP PROCEDURE IF EXISTS total_gastat;
DROP PROCEDURE IF EXISTS registra_compra;
DROP FUNCTION  IF EXISTS preu_amb_iva;

DELIMITER //

-- 1) Torna un resultat (com un SELECT): els jocs d'un gènere
CREATE PROCEDURE jocs_per_genere(IN p_genere VARCHAR(30))
BEGIN
  SELECT titol, pegi
  FROM joc
  WHERE genere = p_genere
  ORDER BY titol;
END //

-- 2) Torna dades per paràmetres de sortida (OUT)
CREATE PROCEDURE total_gastat(IN  p_jugador INT,
                              OUT p_compres INT,
                              OUT p_total   DECIMAL(8,2))
BEGIN
  SELECT COUNT(*), IFNULL(SUM(preu_pagat), 0)
  INTO p_compres, p_total
  FROM compra
  WHERE jugador_id = p_jugador;
END //

-- 3) Fa una compra sencera: comprova l'estoc, el resta i afegeix la compra.
--    Si no hi ha estoc, llança un error propi amb SIGNAL.
CREATE PROCEDURE registra_compra(IN  p_jugador INT,
                                 IN  p_joc     INT,
                                 IN  p_plat    INT,
                                 OUT p_compra  INT)
BEGIN
  DECLARE v_estoc INT;
  DECLARE v_preu  DECIMAL(6,2);

  SELECT estoc, preu INTO v_estoc, v_preu
  FROM joc_plataforma
  WHERE joc_id = p_joc AND plataforma_id = p_plat
  FOR UPDATE;

  IF v_estoc IS NULL THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'Aquesta versio del joc no existeix';
  ELSEIF v_estoc = 0 THEN
    SIGNAL SQLSTATE '45000'
      SET MESSAGE_TEXT = 'No queda estoc d''aquesta versio';
  END IF;

  UPDATE joc_plataforma SET estoc = estoc - 1
  WHERE joc_id = p_joc AND plataforma_id = p_plat;

  INSERT INTO compra (jugador_id, joc_id, plataforma_id, data, preu_pagat)
  VALUES (p_jugador, p_joc, p_plat, CURDATE(), v_preu);

  SET p_compra = LAST_INSERT_ID();
END //

-- 4) Una funció: torna un sol valor i es pot fer servir dins d'un SELECT
CREATE FUNCTION preu_amb_iva(p_preu DECIMAL(6,2))
RETURNS DECIMAL(6,2)
DETERMINISTIC
BEGIN
  RETURN ROUND(p_preu * 1.21, 2);
END //

DELIMITER ;
