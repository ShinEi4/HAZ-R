
INSERT INTO Admin (Pseudo, MotDePasse) VALUES
    ('admin1', 'password1'),
    ('admin2', 'password2'),
    ('admin3', 'password3');

INSERT INTO Bassin (nom) VALUES 
    ('Bassin1'),
    ('Bassin2'),
    ('Bassin3'),
    ('Bassin4'),
    ('Bassin5'),
    ('Bassin6'),
    ('Bassin7'),
    ('Bassin8'),
    ('Bassin9'),
    ('Bassin10');                           

INSERT INTO Type (nom) VALUES   
    ('Local'),
    ('Export');

INSERT INTO Region (nom) VALUES 
    ('Analamanga'),
    ('Bongolava'),
    ('Itasy'),
    ('Vakinankaratra'),
    ('Diana'),
    ('Sava'),
    ('Amoron i Mania'),
    ('Atsimo-Atsinana'),
    ('Fitovinany'),
    ('Haute Matsiatra'),
    ('Ihorombe'),
    ('Vatovavy'),
    ('Betsiboka'),
    ('Boeny'),
    ('Melaky'),
    ('Sofia'),
    ('Alaotra-Mangoro'),
    ('Analanjirofo'),
    ('Antsinana'),
    ('Androy'),
    ('Anosy'),
    ('Atsimo-Andrefana'),
    ('Menabe');

INSERT INTO Etat (Nom, pourcentage) VALUES   
    ('Commander', 30),
    ('En cours de preparation', 40),
    ('Prete', 5),
    ('Livrer', 5),
    ('Payer', 20),
    ('Libre', 0);

INSERT INTO ActionCaisse (nom) VALUES   
    ('Depot'),
    ('Retrait');

INSERT INTO Equipement (Nom, prixUnitaire) VALUES
    ('Net', 20),
    ('Water Pump', 150),
    ('Filter', 100),
    ('Heater', 80),
    ('Air Pump', 70),
    ('Thermometer', 15),
    ('Feeding Ring', 10),
    ('Aquarium Light', 120),
    ('CO2 System', 200),
    ('Water Test Kit', 50);

-- Insert data into StockEquipement
INSERT INTO StockEquipement (idAdmin, idEquipement, quantite, date, action) VALUES
    (1, 1, 10, '2024-06-01', 1), -- Depot of 10 Nets by admin1
    (1, 2, 5, '2024-06-01', 1),  -- Depot of 5 Water Pumps by admin1
    (2, 3, 7, '2024-06-02', 1),  -- Depot of 7 Filters by admin2
    (2, 1, 3, '2024-06-03', 2),  -- Retrait of 3 Nets by admin2
    (3, 4, 2, '2024-06-03', 1),  -- Depot of 2 Heaters by admin3
    (3, 5, 4, '2024-06-04', 1),  -- Depot of 4 Air Pumps by admin3
    (1, 2, 1, '2024-06-04', 2),  -- Retrait of 1 Water Pump by admin1
    (1, 6, 10, '2024-06-05', 1), -- Depot of 10 Thermometers by admin1
    (2, 3, 2, '2024-06-05', 2),  -- Retrait of 2 Filters by admin2
    (3, 7, 5, '2024-06-06', 1);  -- Depot of 5 Feeding Rings by admin3

-- Insert data into StockCrevette
INSERT INTO StockCrevette (idAdmin, idBassin, poids, date, action) VALUES
    (1, 1, 50.2, '2024-06-01', 1),  -- Adding 50.2 kg to Bassin 1 by admin1
    (1, 1, 20.5, '2024-06-02', 1),  -- Adding 20.5 kg to Bassin 1 by admin1
    (2, 1, 10.8, '2024-06-03', 2),  -- Removing 10.8 kg from Bassin 1 by admin2
    (2, 2, 40.0, '2024-06-01', 1),  -- Adding 40.0 kg to Bassin 2 by admin2
    (3, 2, 15.0, '2024-06-02', 2);  -- Removing 15.0 kg from Bassin 2 by admin3

-- Insert data into the Caisse table
INSERT INTO Caisse (idAdmin, montant, action, date, libelle) VALUES 
    (1, 100, 1, '2024-06-06', 'vente'), -- Adding 100 to the cash register by admin1
    (2, 50, 1, '2024-06-06', 'vente'),  -- Adding 50 to the cash register by admin2
    (1, 20, 2, '2024-06-06', 'buy'),    -- Taking out 20 from the cash register by admin1
    (3, 30, 1, '2024-06-06', 'vente'),  -- Adding 30 to the cash register by admin3
    (2, 10, 2, '2024-06-06', 'buy');    -- Taking out 10 from the cash register by admin2

-- Inserting sample data into the EtatCrevette table
INSERT INTO EtatCrevette (idAdmin, poids, idBassin, date, etat) VALUES
    (1, 50.5, 1, '2024-06-01', 1),  -- Commanded 50.5 kg in Bassin 1 on 2024-06-01
    (1, 45.3, 2, '2024-06-02', 2),  -- In preparation 45.3 kg in Bassin 2 on 2024-06-02
    (2, 55.2, 3, '2024-06-03', 3),  -- Ready 55.2 kg in Bassin 3 on 2024-06-03
    (3, 40.0, 4, '2024-06-04', 4),  -- Delivered 40.0 kg in Bassin 4 on 2024-06-04
    (3, 30.5, 5, '2024-06-05', 5),  -- Paid 30.5 kg in Bassin 5 on 2024-06-05
    (2, 60.0, 1, '2024-06-06', 1),  -- Commanded 60.0 kg in Bassin 1 on 2024-06-06
    (1, 20.5, 2, '2024-06-07', 2),  -- In preparation 20.5 kg in Bassin 2 on 2024-06-07
    (3, 35.3, 3, '2024-06-08', 3),  -- Ready 35.3 kg in Bassin 3 on 2024-06-08
    (2, 25.0, 4, '2024-06-09', 4),  -- Delivered 25.0 kg in Bassin 4 on 2024-06-09
    (1, 15.5, 5, '2024-06-10', 5);  -- Paid 15.5 kg in Bassin 5 on 2024-06-10

-- Insert seasons covering the whole year
INSERT INTO Saison (dateDebut, dateFin) VALUES
    ('2024-01-01', '2024-04-30'),
    ('2024-05-01', '2024-08-31'),
    ('2024-09-01', '2024-12-31');

-- Insert data for 10 bassins with varying statue values for each season
INSERT INTO SaisonBassin (idBassin, idSaison, statue) VALUES
    (1, 1, 1),
    (1, 2, -1),
    (1, 3, 1),
    (2, 1, 1),
    (2, 2, 1),
    (2, 3, -1),
    (3, 1, -1),
    (3, 2, 1),
    (3, 3, 1),
    (4, 1, 1),
    (4, 2, -1),
    (4, 3, 1),
    (5, 1, 1),
    (5, 2, 1),
    (5, 3, -1),
    (6, 1, -1),
    (6, 2, 1),
    (6, 3, 1),
    (7, 1, 1),
    (7, 2, -1),
    (7, 3, 1),
    (8, 1, 1),
    (8, 2, 1),
    (8, 3, -1),
    (9, 1, -1),
    (9, 2, 1),
    (9, 3, 1),
    (10, 1, 1),
    (10, 2, -1),
    (10, 3, 1);

-- Insert data into the Cours table
INSERT INTO Cours (idType, region, prixUnitaire) VALUES
    -- Region 1
    (1, 1, 100),
    -- Region 2
    (1, 2, 110),
    -- Region 3
    (1, 3, 105),
    -- Region 4
    (1, 4, 115),
    -- Region 5
    (1, 5, 120),
    -- Region 6
    (1, 6, 105),
    -- Region 7
    (1, 7, 110),
    -- Region 8
    (1, 8, 100),
    -- Region 9
    (1, 9, 115),
    -- Region 10
    (1, 10, 120),
    -- Region 11
    (1, 11, 110),
    -- Region 12
    (1, 12, 105),
    -- Region 13
    (1, 13, 100),
    -- Region 14
    (1, 14, 120),
    -- Region 15
    (1, 15, 115),
    -- Region 16
    (1, 16, 105),
    -- Region 17
    (1, 17, 110),
    -- Region 18
    (1, 18, 100),
    -- Region 19
    (1, 19, 110),
    -- Region 20
    (1, 20, 105),
    -- Region 21
    (1, 21, 120),
    -- Region 22
    (1, 22, 115);

-- Insert data into the Frais table
INSERT INTO Frais (type, region, frais) VALUES
    -- Region 1
    (1, 1, 10),
    -- Region 2
    (1, 2, 15),
    -- Region 3
    (1, 3, 12),
    -- Region 4
    (1, 4, 18),
    -- Region 5
    (1, 5, 20),
    -- Region 6
    (1, 6, 15),
    -- Region 7
    (1, 7, 16),
    -- Region 8
    (1, 8, 11),
    -- Region 9
    (1, 9, 19),
    -- Region 10
    (1, 10, 22),
    -- Region 11
    (1, 11, 17),
    -- Region 12
    (1, 12, 14),
    -- Region 13
    (1, 13, 9),
    -- Region 14
    (1, 14, 23),
    -- Region 15
    (1, 15, 21),
    -- Region 16
    (1, 16, 13),
    -- Region 17
    (1, 17, 16),
    -- Region 18
    (1, 18, 10),
    -- Region 19
    (1, 19, 17),
    -- Region 20
    (1, 20, 14),
    -- Region 21
    (1, 21, 24),
    -- Region 22
    (1, 22, 20);