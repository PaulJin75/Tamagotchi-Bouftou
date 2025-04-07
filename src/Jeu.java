import java.sql.*;
import java.util.Scanner;

public class Jeu {
    private Bouftou bouftou;
    private Sac sac;
    private Scanner scanner;
    private Connection conn;

    public Jeu() {
        sac = new Sac();
        scanner = new Scanner(System.in);
        connecterBD();
        choisirBouftou();
    }

    private void connecterBD() {
        try {
            String url = "jdbc:mysql://localhost:3306/tamagotchi_bouftou?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = "motdepassesqlpourroot";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connexion à MySQL réussie.");
        } catch (SQLException e) {
            System.out.println("Erreur connexion BD : " + e.getMessage());
        }
    }

    private void choisirBouftou() {
        try {
            System.out.println("Choisissez un Bouftou existant ou créez-en un nouveau :");
            String sql = "SELECT id, nom FROM Bouftou";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Bouftous disponibles :");
            while (rs.next()) {
                System.out.println("ID " + rs.getInt("id") + ": " + rs.getString("nom"));
            }
            System.out.println("Entrez l'ID d'un Bouftou existant (ex. 1 pour Paul) ou 0 pour en créer un nouveau :");
            int id = Integer.parseInt(scanner.nextLine());
            if (id == 0) {
                creerNouveauBouftou();
            } else {
                chargerBouftou(id);
            }
        } catch (SQLException e) {
            System.out.println("Erreur affichage Bouftous : " + e.getMessage());
            bouftou = new Bouftou();
        }
    }

    private void creerNouveauBouftou() {
        try {
            System.out.println("Entrez un nom pour votre nouveau Bouftou :");
            String nom = scanner.nextLine();
            String checkSql = "SELECT COUNT(*) FROM Bouftou WHERE nom = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, nom);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                System.out.println("Ce nom existe déjà ! Chargez-le ou choisissez un autre nom.");
                choisirBouftou();
                return;
            }
            bouftou = new Bouftou();
            String sql = "INSERT INTO Bouftou (nom, satiete, energie, humeur) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nom);
            pstmt.setInt(2, bouftou.getSatiete());
            pstmt.setInt(3, bouftou.getEnergie());
            pstmt.setInt(4, bouftou.getHumeur());
            pstmt.executeUpdate();
            System.out.println("Nouveau Bouftou créé : " + nom);
        } catch (SQLException e) {
            System.out.println("Erreur création Bouftou : " + e.getMessage());
            bouftou = new Bouftou();
        }
    }

    public void chargerBouftou(int id) {
        try {
            String sql = "SELECT * FROM Bouftou WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bouftou = new Bouftou();
                bouftou.setSatiete(rs.getInt("satiete"));
                bouftou.setEnergie(rs.getInt("energie"));
                bouftou.setHumeur(rs.getInt("humeur"));
                System.out.println("Bouftou chargé : " + rs.getString("nom"));
            } else {
                System.out.println("Bouftou non trouvé pour l'ID " + id + ". Création d'un nouveau Bouftou.");
                creerNouveauBouftou();
            }
        } catch (SQLException e) {
            System.out.println("Erreur chargement : " + e.getMessage());
            bouftou = new Bouftou();
        }
    }

    public void sauvegarderBouftou() {
        try {
            System.out.println("Entrez le nom du Bouftou à sauvegarder :");
            String nom = scanner.nextLine();
            String checkSql = "SELECT id FROM Bouftou WHERE nom = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, nom);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String sql = "UPDATE Bouftou SET satiete = ?, energie = ?, humeur = ? WHERE id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, bouftou.getSatiete());
                pstmt.setInt(2, bouftou.getEnergie());
                pstmt.setInt(3, bouftou.getHumeur());
                pstmt.setInt(4, id);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Bouftou mis à jour : " + nom);
                } else {
                    System.out.println("Erreur lors de la mise à jour.");
                }
            } else {

                String sql = "INSERT INTO Bouftou (nom, satiete, energie, humeur) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, nom);
                pstmt.setInt(2, bouftou.getSatiete());
                pstmt.setInt(3, bouftou.getEnergie());
                pstmt.setInt(4, bouftou.getHumeur());
                pstmt.executeUpdate();
                System.out.println("Nouveau Bouftou sauvegardé : " + nom);
            }
        } catch (SQLException e) {
            System.out.println("Erreur sauvegarde : " + e.getMessage());
        }
    }

    public void lancer() {
        System.out.println("Bienvenue dans Tamagotchi Bouftou Virtuel !");
        boolean jeuEnCours = true;
        while (jeuEnCours) {
            bouftou.afficherStats();
            System.out.println("Que voulez-vous faire ? (manger/dormir/jouer/sauvegarder/charger/quitter)");
            String choix = scanner.nextLine().toLowerCase();
    
            switch (choix) {
                case "manger":
                    sac.afficherContenu();
                    System.out.println("Choisissez un objet pour manger (Raisin/Pastèque) :");
                    String objetManger = scanner.nextLine();
                    Objet nourriture = sac.getObjet(objetManger);
                    if (nourriture != null && nourriture.getType().equals("nourriture")) {
                        bouftou.manger(nourriture);
                        System.out.println("Bouftou a mangé " + nourriture.getNom() + " !");
                        bouftou.afficherStats(); 
                    } else {
                        System.out.println("Objet non trouvé ou non comestible.");
                    }
                    break;
    
                case "dormir":
                    bouftou.dormir();
                    System.out.println("Bouftou a dormi !");
                    bouftou.afficherStats();
                    break;
    
                case "jouer":
                    sac.afficherContenu();
                    System.out.println("Choisissez un objet pour jouer (Balle de tennis/Ballon de foot) :");
                    String objetJouer = scanner.nextLine();
                    Objet jouet = sac.getObjet(objetJouer);
                    if (jouet != null && jouet.getType().equals("jouet")) {
                        bouftou.jouer(jouet);
                        System.out.println("Bouftou a joué avec " + jouet.getNom() + " !");
                        bouftou.afficherStats();
                    } else {
                        System.out.println("Objet non trouvé ou non jouable.");
                    }
                    break;
    
                case "sauvegarder":
                    sauvegarderBouftou();
                    break;
    
                case "charger":
                    choisirBouftou();
                    break;
    
                case "quitter":
                    jeuEnCours = false;
                    System.out.println("Au revoir !");
                    break;
    
                default:
                    System.out.println("Commande invalide.");
            }
    
            bouftou.diminuerStats(); 
            if (bouftou.estEnFuite()) {
                System.out.println("Votre Bouftou s'est enfui ! Réinitialisation...");
                bouftou = new Bouftou();
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Erreur fermeture BD : " + e.getMessage());
        }
        scanner.close();
    }
}