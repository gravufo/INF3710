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

--  Pour le point (b), on vous signale que plusieurs actions menacent la contrainte, pas seulement
--  la création ou la modification d’une séance…

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


-- C. Inventaire et analyse des contraintes d’intégrité
-- On souhaite garantir le respect de certaines propriétés (contraintes d´intégrité)
-- concernant la base de données. On vous demande d’énumérer l’ensemble des propriétés qui
-- vous semblent pertinentes (y compris les propriétés évoquées ci-dessus aux questions A et B).
-- Vous énoncerez chaque propriété d´une manière aussi précise et formelle que possible.
-- Pour chacune des propriétés énoncées, on vous demande de proposer une technique
-- permettant de garantir son respect. Cette technique pourrait être une contrainte SQL (clé
-- primaire, clé externe, unicité, différent de nul, contrainte de vérification) ou un trigger. Notez
-- qu’on ne vous impose pas d’implanter la technique que vous aurez proposée.

-- 1. Vérifier que les prérequis et corequis ne forment pas une boucle ce qui rendrait un cours
-- impossible à prendre. Ceci pourrait être fait avec un déclencheur, puisqu'il serait plus simple
-- de vérifier lors de l'ajout ou de la modification d'une entrée dans la table Prerequis. Il faut
-- donc, dans le déclancheur, comparer les prérequis du cours à modifier avec les prérequis de tous
-- ces prérequis afin de s'assurer que notre modification ne va pas créer une boucle.

-- 2. Vérifier que la salle de chaque cours est disponible aux heures du cours, c'est à dire qu'il n'y a pas
-- deux cours dans une salle en même temps. Il est encore une fois très faisable d'implanter cette contrainte
-- par l'entremise d'un déclencheur en analysant les locaux de tous les cours enregistrés aux heures qui nous
-- intéressent pour s'assurer que la salle voulue n'est pas dans cette liste. Il serait aussi possible de le
-- faire directement en JDBC, mais ce n'est pas aussi optimal, puisque si une autre méthode de modification
-- de base de donnée est utilisée, on ne peut pas avoir une garantie que la contrainte est respectée.

-- 3. Il ne faut pas qu'un enseignant soit requis à deux séances simultanément. Il faut donc vérifier que
-- l'enseignant est disponible lors de l'ajout ou de la modification de la place horaire d'un cours, d'une
-- section ou d'une séance. Pour ce faire, il faut utiliser un déclencheur qui utilisera une requête interne
-- afin de vérifier qu'une requête UPDATE n'apporte pas un conflit d'horaire des enseignants.

-- 4. Il ne faut pas pouvoir rentrer une heure non valide ou une journée non valis dans la table Jour et Heure.
-- Ainsi, puisque ceci est une contrainte assez simple, il est possible de le faire directement dans la base
-- de données grâce aux contraintes SQL. Par contre, alternativement, on peut aussi entrer les données initiales
-- qui ne changeront pas (puisque les jours de la semaine et les heures de la journée ne changent pas) et ensuite
-- empêcher (annuler) toute modification, ajout ou retrait grâce à un déclencheur.

-- 5. Il faut empêcher la modification du sigle et du titre d'un cours. Ceci est important, afin de garder une liste
-- de cours stable et retraçable. Pour remplir cette contrainte, il est possible d'utiliser un déclencheur qui va
-- empêcher toute modification de ces deux champs en remplaçant dans la requête la nouvelle valeur par l'ancienne.
-- Par contre, ceci n'est pas optimal, car il est possible qu'on veuille donner la permission à certaines personnes
-- (ex.: un département, un professeur ou autre) de modifier ces champs. Il serait donc plus optimal d'utiliser un
-- système de permissions afin de contrôler les droits d'accès à certaines tables ou champs.