create database crevette;
\c crevette;
create table commande(
    idCommande serial PRIMARY KEY,
    idUser int,
    idBassin int,
    poids int,
    date Date,
    type int,
    idRegion int,
    statue int,
    dateFin Date


);
INSERT INTO commande (idUser,idBassin,poids, date, type, idRegion, statue) VALUES
(1,1,5, '2024-01-15', 1, 101, 0),
(1,2,15, '2024-02-10',1, 102, 0),
(1,3,2, '2024-03-05', 1, 103, 0),
(1,4,6, '2024-04-20', 2, 104, 0),
(1,5,8, '2024-05-15', 2, 105, 0);

