CREATE TABLE Departement (
    idDept INTEGER PRIMARY KEY,
    nom varchar(50) CONSTRAINT unique_nom UNIQUE);

CREATE TABLE Personne (
    idPers INTEGER PRIMARY KEY,
    nom VARCHAR(50),
    prenom VARCHAR(50));

CREATE TABLE Jour (
    codJour SMALLINT PRIMARY KEY,
    nom VARCHAR(8)
    );
    
CREATE TABLE Heure (
    codHre SMALLINT PRIMARY KEY,
    hre VARCHAR(5)
    );
	
CREATE TABLE Cours (
    sigle VARCHAR(8) PRIMARY KEY,
    titre VARCHAR(200),
    nbCredit SMALLINT,
    cycle CHAR(2) CONSTRAINT check_cycle CHECK(cycle = 'ES' OR cycle = 'BA' OR cycle = 'CE'),
    idPers INTEGER REFERENCES Personne,
    idDept INTEGER REFERENCES Departement);
    
CREATE TABLE Section (
    sigle VARCHAR(8) REFERENCES Cours,
    leType CHAR(1) CONSTRAINT check_leType CHECK(leType = 'C' OR leType = 'L'),
    groupe SMALLINT,
    PRIMARY KEY(sigle, leType, groupe)
    );
    
CREATE TABLE Seance (
    sigle VARCHAR(8),
    leType CHAR(1),
    groupe SMALLINT,
    codJour SMALLINT REFERENCES Jour,
    codHeure SMALLINT REFERENCES Heure(codHre),
    alternance CHAR(2) CONSTRAINT check_alternance CHECK(alternance = 'HE' OR alternance = 'B1' OR alternance = 'B2'),
    duree SMALLINT,
    lelocal VARCHAR(8),
    PRIMARY KEY (sigle, leType, groupe, codJour, codHeure, alternance),
	FOREIGN KEY (sigle, leType, groupe) REFERENCES Section(sigle, leType, groupe)
    );

CREATE TABLE Enseigner (
    idPers INTEGER REFERENCES Personne,
    sigle VARCHAR(8),
    leType CHAR(1),
    groupe SMALLINT,
    PRIMARY KEY (idPers, sigle, leType, groupe),
	FOREIGN KEY (sigle, leType, groupe) REFERENCES Section(sigle, leType, groupe)
    );
    
CREATE TABLE Prerequis (
    cours VARCHAR(8) REFERENCES Cours (sigle),
    preRequis VARCHAR(8) REFERENCES Cours (sigle),
    PRIMARY KEY (cours, preRequis)
    );
    