import java.util.Scanner;

public class Jeu {
    private Bouftou bouftou;
    private Sac sac;
    private Scanner scanner;

    public Jeu() {
        bouftou = new Bouftou();
        sac = new Sac();
        scanner = new Scanner(System.in);
    }

    public void lancer() {
        System.out.println("Bienvenue dans Tamagotchi Bouftou Virtuel !");
        boolean jeuEnCours = true;

        while (jeuEnCours) {
            bouftou.diminuerStats();
            bouftou.afficherStats();

            if (bouftou.estEnFuite()) {
                System.out.println("Votre Bouftou s'est enfui ! Réinitialisation...");
                bouftou = new Bouftou();
                continue;
            }

            System.out.println("Que voulez-vous faire ? (manger/dormir/jouer/quitter)");
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

                case "quitter":
                    jeuEnCours = false;
                    System.out.println("Au revoir !");
                    break;

                default:
                    System.out.println("Commande invalide.");
            }
        }
        scanner.close();
    }
}