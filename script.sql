CREATE TABLE Bouftou (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(50) NOT NULL,
    satiete INT NOT NULL CHECK (satiete >= 0 AND satiete <= 100),
    energie INT NOT NULL CHECK (energie >= 0 AND energie <= 100),
    humeur INT NOT NULL CHECK (humeur >= 0 AND humeur <= 100)
);

INSERT INTO Bouftou (nom, satiete, energie, humeur) VALUES
    ('Bouftou de Paul', 50, 50, 50),
    ('Bouftou de John', 50, 50, 50);