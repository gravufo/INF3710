-- A.  Décrivez et implantez un mécanisme afin d’imposer la contrainte suivante :  dans la table
-- Cours, on ne peut pas modifier le sigle ni le nom du cours.

CREATE OR REPLACE TRIGGER
	im_sigle_and_name
BEFORE
	UPDATE OF sigle, titre
ON
	Cours
WHEN
	OLD.sigle <> NEW.sigle OR OLD.titre <> NEW.titre
BEGIN
	SET NEW.sigle = OLD.sigle;
	SET NEW.titre = OLD.titre;
END;
	
	


-- B. Conflits d’horaire pour un enseignant
-- On veut interdire les conflits d’horaires pour un enseignant, c’est-à-dire la situation dans
-- laquelle un enseignant serait affecté à deux séances en même temps. On vous demande :
-- a. de caractériser formellement ce type de conflit ;

-- b. de préciser les actions sur la base de données qui pourraient engendrer un tel conflit ;

-- c.  d’implanter un ou plusieurs triggers qui garantissent la contrainte .
-- Pour le point (b), on vous signale que plusieurs actions menacent la contrainte, pas seulement
-- la création ou la modification d’une séance…

-- C. Inventaire et analyse des contraintes d’intégrité
-- On souhaite garantir le respect de certaines propriétés (contraintes d´intégrité)
-- concernant la base de données. On vous demande d’énumérer l’ensemble des propriétés qui
-- vous semblent pertinentes (y compris les propriétés évoquées ci-dessus aux questions A et B).
-- Vous énoncerez chaque propriété d´une manière aussi précise et formelle que possible.
-- Pour chacune des propriétés énoncées, on vous demande de proposer une technique
-- permettant de garantir son respect. Cette technique pourrait être une contrainte SQL (clé
-- primaire, clé externe, unicité, différent de nul, contrainte de vérification) ou un trigger. Notez
-- qu’on ne vous impose pas d’implanter la technique que vous aurez proposée.