---liste equipement 
select * from Equipement;

---stock equipement no need
CREATE or replace VIEW StockEquipementVue AS
SELECT 
    e.idEquipement,
    e.Nom,
    SUM(CASE WHEN s.action = 1 THEN s.quantite ELSE -s.quantite END) AS quantite
FROM 
    StockEquipement s
JOIN 
    Equipement e ON s.equipement = e.idEquipement
GROUP BY 
    e.idEquipement, e.Nom
ORDER BY
    SUM(CASE WHEN s.action = 1 THEN s.quantite ELSE -s.quantite END) DESC;


--stock equipement date
CREATE or replace VIEW StockEquipementVueDate AS
SELECT 
    e.idEquipement,
    e.Nom,
    COALESCE(SUM(CASE WHEN s.action = 1 THEN s.quantite ELSE -s.quantite END), 0) AS quantite
FROM 
    Equipement e
LEFT JOIN 
    StockEquipement s ON e.idEquipement = s.equipement
WHERE
    s.date <= '2024-06-01' -- Use the desired date here
GROUP BY 
    e.idEquipement, e.Nom
ORDER BY 
    quantite DESC;

--crud equipement
INSERT INTO Equipement (Nom, prixUnitaire) VALUES ('EquipmentName', 100);
SELECT * FROM Equipement WHERE idEquipement = 1;
UPDATE Equipement SET Nom = 'UpdatedName', prixUnitaire = 150 WHERE idEquipement = 1;
DELETE FROM Equipement WHERE idEquipement = 1;


--moove equipement
INSERT INTO StockEquipement (equipement, quantite, date, action) VALUES
(1, 10, '2024-06-01', 1);


---liste crevette en stock
CREATE OR REPLACE VIEW StockCrevetteVueDate AS
SELECT
    b.idBassin,
    COALESCE(SUM(CASE WHEN sc.action = 1 THEN sc.poids ELSE -sc.poids END), 0) AS quantite,
    'kg' AS unite
FROM
    Bassin b
LEFT JOIN
    StockCrevette sc ON b.idBassin = sc.idBassin
WHERE
    b.idBassin = ?  -- Replace '?' with the specific idBassin you want to filter by
    AND sc.date <= '2024-06-02'  -- Replace '2024-06-02' with the desired date
GROUP BY
    b.idBassin
ORDER BY
    b.idBassin ASC;

--insert Etat Crevette
INSERT INTO EtatCrevette (poids, idBassin, date, etat) 
VALUES (value_for_poids, value_for_idBassin, 'yyyy-mm-dd', value_for_etat);

--mouvement caisse et total
--rehefa mikitika vola dia alefa any am caisse fona

-- in caisse now
SELECT COALESCE(SUM(CASE WHEN action = 1 THEN montant ELSE -montant END), 0) AS total
FROM Caisse;








