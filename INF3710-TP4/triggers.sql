-- A.  D�crivez et implantez un m�canisme afin d�imposer la contrainte suivante :  dans la table
--     Cours, on ne peut pas modifier le sigle ni le nom du cours.

-- Afin d'�viter qu'on puisse modifier le sigle et le titre d'un cours, lorsque
-- l'une de ces deux colonnes est modifi�e, on remplace la nouvelle valeur dans
-- la commande UPDATE, avant que celle-ci ne soit ex�cut�e, par la valeur actuelle.
-- Ainsi, la valeur ne peut �tre modifi�e.
-- Il serait toutefois peut-�tre plus pr�f�rable d'utiliser des permissions sur les
-- colonnes sp�cifiques � la place, si cela est possible.

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

-- B. Conflits d�horaire pour un enseignant
--    On veut interdire les conflits d�horaires pour un enseignant, c�est-�-dire la situation dans
--    laquelle un enseignant serait affect� � deux s�ances en m�me temps. On vous demande :

--    a. de caract�riser formellement ce type de conflit ;

--       Il s'agit d'un conflit entre deux s�ances de cours qui se donnent
--       int�gralement ou partiellement en m�me temps et dont la section est
--       enseign�e par au moins une m�me personne.

--    b. de pr�ciser les actions sur la base de donn�es qui pourraient engendrer un tel conflit ;

--       Modifier ou ajouter une ligne dans s�ance.
--       Modifier ou ajouter une ligne dans Enseigner.
--       Modifier Jour ou Heure
      
--    c.  d�implanter un ou plusieurs triggers qui garantissent la contrainte .

--  Pour le point (b), on vous signale que plusieurs actions menacent la contrainte, pas seulement
--  la cr�ation ou la modification d�une s�ance�

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


-- C. Inventaire et analyse des contraintes d�int�grit�
-- On souhaite garantir le respect de certaines propri�t�s (contraintes d�int�grit�)
-- concernant la base de donn�es. On vous demande d��num�rer l�ensemble des propri�t�s qui
-- vous semblent pertinentes (y compris les propri�t�s �voqu�es ci-dessus aux questions A et B).
-- Vous �noncerez chaque propri�t� d�une mani�re aussi pr�cise et formelle que possible.
-- Pour chacune des propri�t�s �nonc�es, on vous demande de proposer une technique
-- permettant de garantir son respect. Cette technique pourrait �tre une contrainte SQL (cl�
-- primaire, cl� externe, unicit�, diff�rent de nul, contrainte de v�rification) ou un trigger. Notez
-- qu�on ne vous impose pas d�implanter la technique que vous aurez propos�e.

-- 1. V�rifier que les pr�requis et corequis ne forment pas une boucle ce qui rendrait un cours
-- impossible � prendre. Ceci pourrait �tre fait avec un d�clencheur, puisqu'il serait plus simple
-- de v�rifier lors de l'ajout ou de la modification d'une entr�e dans la table Prerequis. Il faut
-- donc, dans le d�clancheur, comparer les pr�requis du cours � modifier avec les pr�requis de tous
-- ces pr�requis afin de s'assurer que notre modification ne va pas cr�er une boucle.

-- 2. V�rifier que la salle de chaque cours est disponible aux heures du cours, c'est � dire qu'il n'y a pas
-- deux cours dans une salle en m�me temps. Il est encore une fois tr�s faisable d'implanter cette contrainte
-- par l'entremise d'un d�clencheur en analysant les locaux de tous les cours enregistr�s aux heures qui nous
-- int�ressent pour s'assurer que la salle voulue n'est pas dans cette liste. Il serait aussi possible de le
-- faire directement en JDBC, mais ce n'est pas aussi optimal, puisque si une autre m�thode de modification
-- de base de donn�e est utilis�e, on ne peut pas avoir une garantie que la contrainte est respect�e.

-- 3. Il ne faut pas qu'un enseignant soit requis � deux s�ances simultan�ment. Il faut donc v�rifier que
-- l'enseignant est disponible lors de l'ajout ou de la modification de la place horaire d'un cours, d'une
-- section ou d'une s�ance. Pour ce faire, il faut utiliser un d�clencheur qui utilisera une requ�te interne
-- afin de v�rifier qu'une requ�te UPDATE n'apporte pas un conflit d'horaire des enseignants.

-- 4. Il ne faut pas pouvoir rentrer une heure non valide ou une journ�e non valis dans la table Jour et Heure.
-- Ainsi, puisque ceci est une contrainte assez simple, il est possible de le faire directement dans la base
-- de donn�es gr�ce aux contraintes SQL. Par contre, alternativement, on peut aussi entrer les donn�es initiales
-- qui ne changeront pas (puisque les jours de la semaine et les heures de la journ�e ne changent pas) et ensuite
-- emp�cher (annuler) toute modification, ajout ou retrait gr�ce � un d�clencheur.

-- 5. Il faut emp�cher la modification du sigle et du titre d'un cours. Ceci est important, afin de garder une liste
-- de cours stable et retra�able. Pour remplir cette contrainte, il est possible d'utiliser un d�clencheur qui va
-- emp�cher toute modification de ces deux champs en rempla�ant dans la requ�te la nouvelle valeur par l'ancienne.
-- Par contre, ceci n'est pas optimal, car il est possible qu'on veuille donner la permission � certaines personnes
-- (ex.: un d�partement, un professeur ou autre) de modifier ces champs. Il serait donc plus optimal d'utiliser un
-- syst�me de permissions afin de contr�ler les droits d'acc�s � certaines tables ou champs.