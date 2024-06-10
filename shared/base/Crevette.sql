
--last 08/06
CREATE DATABASE crevettes;
ALTER DATABASE crevettes OWNER TO postgres;
\c crevettes

-- Admin
CREATE TABLE IF NOT EXISTS Admin (
    idAdmin SERIAL PRIMARY KEY,
    Pseudo VARCHAR(255) NOT NULL,
    MotDePasse VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Bassin(
    idBassin SERIAL PRIMARY KEY,
    nom VARCHAR(255)
);

-- Type de commande
CREATE TABLE IF NOT EXISTS Type(
    idType SERIAL PRIMARY KEY,
    nom VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Region(
    idRegion SERIAL PRIMARY KEY,
    nom VARCHAR(255)
);

-- Etat d'une commande
CREATE TABLE IF NOT EXISTS Etat(
    idEtat SERIAL PRIMARY KEY,
    Nom VARCHAR(255),
    pourcentage DECIMAL(6,2) 
);

CREATE TABLE Equipement(
    idEquipement SERIAL PRIMARY KEY,
    Nom VARCHAR(255),
    prixUnitaire DECIMAL(15,3) NOT NULL
);

CREATE TABLE IF NOT EXISTS Depense(
    idDepense SERIAL PRIMARY KEY,
    idAdmin INT,
    depense VARCHAR(100),
    quantite DECIMAL(15,3)  NOT NULL,
    prixUnitaire DECIMAL(15,3) NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (idAdmin) REFERENCES Admin(idAdmin)
);

CREATE TABLE IF NOT EXISTS Saison(
    idSaison SERIAL PRIMARY KEY,
    dateDebut DATE NOT NULL,
    dateFin DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS ActionCaisse(
    idAction SERIAL PRIMARY KEY,
    nom VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS NormeEau(
    idNorme SERIAL PRIMARY KEY,
    ph DECIMAL(6,2) NOT NULL,
    Ammonia DECIMAL(6,2) NOT NULL,
    H20 DECIMAL(6,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS EtatCrevette(
    idEtatCrevette SERIAL PRIMARY KEY,
    idAdmin INT,
    poids DECIMAL(10,2), -- par kg
    idBassin INT NOT NULL,
    date DATE NOT NULL,  -- par semaine
    etat INT NOT NULL,
    FOREIGN KEY (idBassin) REFERENCES Bassin(idBassin),
    FOREIGN KEY (etat) REFERENCES Etat(idEtat),
    FOREIGN KEY (idAdmin) REFERENCES Admin(idAdmin)
);

CREATE TABLE IF NOT EXISTS AchatEquipement(
    idAchatEquipement SERIAL PRIMARY KEY,
    idEquipement INT NOT NULL,
    idAdmin INT,
    quantite DECIMAL(10,2) NOT NULL,
    date DATE NOT NULL,  -- par semaine
    FOREIGN KEY (idEquipement) REFERENCES Equipement(idEquipement),
    FOREIGN KEY (idAdmin) REFERENCES Admin(idAdmin)
);

CREATE TABLE IF NOT EXISTS commande(
    idCommande SERIAL PRIMARY KEY,
    poids DECIMAL(12,3) NOT NULL,
    idUser INT NOT NULL,
    date DATE,
    type INT NOT NULL,
    idRegion INT NOT NULL,
    statue INT NOT NULL,
    dateFin DATE,
    FOREIGN KEY (idRegion) REFERENCES Region(idRegion),
    FOREIGN KEY (type) REFERENCES Type(idType),
    FOREIGN KEY (idUser) REFERENCES Admin(idAdmin),
    FOREIGN KEY (statue) REFERENCES Etat(idEtat)
);

CREATE TABLE IF NOT EXISTS Cours(
    idCours SERIAL PRIMARY KEY,
    idType INT NOT NULL,
    region INT NOT NULL,
    prixUnitaire DECIMAL(15,3) NOT NULL,
    FOREIGN KEY (idType) REFERENCES Type(idType),
    FOREIGN KEY (region) REFERENCES Region(idRegion)
);

CREATE TABLE IF NOT EXISTS BesoinBassin(
    idBesoin SERIAL PRIMARY KEY,
    idBassin INT NOT NULL,
    idEquipement INT NOT NULL,
    qteEquipement INT NOT NULL,
    depense VARCHAR(100),
    qteDepense INT NOT NULL,
    frequence DECIMAL(6,2) NOT NULL,
    FOREIGN KEY (idBassin) REFERENCES Bassin(idBassin),
    FOREIGN KEY (idEquipement) REFERENCES Equipement(idEquipement)
);

CREATE TABLE IF NOT EXISTS BesoinCommande(
    idBesoin SERIAL PRIMARY KEY,
    idEquipement INT NOT NULL,
    qteEquipement INT NOT NULL,
    depense VARCHAR(100),
    qteDepense INT NOT NULL,
    frequence DECIMAL(6,2) NOT NULL,
    FOREIGN KEY (idEquipement) REFERENCES Equipement(idEquipement)
);

CREATE TABLE Frais(
    idFrais SERIAL PRIMARY KEY,
    type INT NOT NULL,
    region INT NOT NULL,
    frais DECIMAL(15,3) NOT NULL,
    FOREIGN KEY (type) REFERENCES Type(idType),
    FOREIGN KEY (region) REFERENCES Region(idRegion)
);

CREATE TABLE IF NOT EXISTS Caisse(
    idAction SERIAL PRIMARY KEY,
    idAdmin INT,
    montant DECIMAL (16,3) NOT NULL,
    action INT NOT NULL,
    libelle VARCHAR(255),
    date DATE NOT NULL,
    FOREIGN KEY (action) REFERENCES ActionCaisse(idAction),
    FOREIGN KEY (idAdmin) REFERENCES Admin(idAdmin)
);

CREATE TABLE IF NOT EXISTS Eau(
    idEau SERIAL PRIMARY KEY,
    idBassin INT NOT NULL,
    ph DECIMAL(6,2) NOT NULL,
    Ammonia DECIMAL(6,2) NOT NULL,
    H20 DECIMAL(6,2) NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (idBassin) REFERENCES Bassin(idBassin)
);

CREATE TABLE IF NOT EXISTS StockEquipement(
    idStock SERIAL PRIMARY KEY,
    idAdmin INT,
    idEquipement INT NOT NULL,
    quantite INT NOT NULL,
    date DATE NOT NULL,
    action INT NOT NULL,
    FOREIGN KEY (idEquipement) REFERENCES Equipement(idEquipement),
    FOREIGN KEY (action) REFERENCES ActionCaisse(idAction),
    FOREIGN KEY (idAdmin) REFERENCES Admin(idAdmin)
);

CREATE TABLE IF NOT EXISTS StockCrevette(
    idStock SERIAL PRIMARY KEY,
    idAdmin INT,
    idBassin INT NOT NULL,
    poids DECIMAL(10,2), -- Par kg
    date DATE NOT NULL,
    action INT NOT NULL,
    FOREIGN KEY (idBassin) REFERENCES Bassin(idBassin),
    FOREIGN KEY (action) REFERENCES ActionCaisse(idAction),
    FOREIGN KEY (idAdmin) REFERENCES Admin(idAdmin)
);

-- -1 not available for sale
-- 1 available for sales
CREATE TABLE SaisonBassin (
    idSB SERIAL PRIMARY KEY,
    idBassin INT NOT NULL,
    idSaison INT NOT NULL,
    statue INT NOT NULL CHECK (statue IN (-1, 1)),
    FOREIGN KEY (idBassin) REFERENCES Bassin(idBassin),
    FOREIGN KEY (idSaison) REFERENCES Saison(idSaison)
);
