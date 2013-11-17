-- A.  Décrivez et implantez un mécanisme afin d’imposer la contrainte suivante :  dans la table
--     Cours, on ne peut pas modifier le sigle ni le nom du cours.

-- Afin d'éviter qu'on puisse modifier le sigle et le titre d'un cours, lorsque
-- l'une de ces deux colonnes est modifiée, on remplace la nouvelle valeur dans
-- la commande UPDATE, avant que celle-ci ne soit exécutée, par la valeur actuelle.
-- Ainsi, la valeur ne peut être modifiée.
-- Il serait toutefois peut-être plus préférable d'utiliser des permissions sur les
-- colonnes spécifiques à la place, si cela est possible.

CREATE OR REPLACE TRIGGER
	im_sigle_and_name
BEFORE
	UPDATE OF sigle, titre
ON
	Cours
FOR EACH ROW
BEGIN
	:NEW.sigle := :OLD.sigle;
	:NEW.titre := :OLD.titre;
END;

-- B. Conflits d’horaire pour un enseignant
--    On veut interdire les conflits d’horaires pour un enseignant, c’est-à-dire la situation dans
--    laquelle un enseignant serait affecté à deux séances en même temps. On vous demande :

--    a. de caractériser formellement ce type de conflit ;

--       Il s'agit d'un conflit entre deux séances de cours qui se donnent
--       intégralement ou partiellement en même temps et dont la section est
--       enseignée par au moins une même personne.

--    b. de préciser les actions sur la base de données qui pourraient engendrer un tel conflit ;

--       Modifier ou ajouter une ligne dans séance.
--       Modifier ou ajouter une ligne dans Enseigner.
--       Modifier Jour ou Heure
      
--    c.  d’implanter un ou plusieurs triggers qui garantissent la contrainte .

CREATE OR REPLACE TRIGGER
	bf_heure_seance
BEFORE
	UPDATE OF codHeure, codJour
ON
	Seance
FOR EACH ROW
BEGIN

	if(0 <	(SELECT COUNT(e.idPers, e.sigle, s.leType, s.groupe)
				FROM Enseigner e, Seance s, Cours c
				WHERE e.idPers = c.idPers
				AND e.sigle = s.sigle
				AND e.sigle = c.sigle
				AND s.codJour = :NEW.codJour
				AND s.codJour != :OLD.codJour
				AND s.codHeure ))
	THEN
		RAISE_APPLICATION_ERROR(-20101,'Il y a un conflit d\'horaire');

END;

--  Pour le point (b), on vous signale que plusieurs actions menacent la contrainte, pas seulement
--  la création ou la modification d’une séance…

-- C. Inventaire et analyse des contraintes d’intégrité
-- On souhaite garantir le respect de certaines propriétés (contraintes d´intégrité)
-- concernant la base de données. On vous demande d’énumérer l’ensemble des propriétés qui
-- vous semblent pertinentes (y compris les propriétés évoquées ci-dessus aux questions A et B).
-- Vous énoncerez chaque propriété d´une manière aussi précise et formelle que possible.
-- Pour chacune des propriétés énoncées, on vous demande de proposer une technique
-- permettant de garantir son respect. Cette technique pourrait être une contrainte SQL (clé
-- primaire, clé externe, unicité, différent de nul, contrainte de vérification) ou un trigger. Notez
-- qu’on ne vous impose pas d’implanter la technique que vous aurez proposée.