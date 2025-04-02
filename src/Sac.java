import java.util.ArrayList;
import java.util.List;

public class Sac {
    private List<Objet> objets;

    public Sac() {
        objets = new ArrayList<>();
        objets.add(new Objet("Raisin", 15, "nourriture"));
        objets.add(new Objet("Pastèque", 30, "nourriture"));
        objets.add(new Objet("Balle de tennis", 15, "jouet"));
        objets.add(new Objet("Ballon de foot", 25, "jouet"));
    }

    public Objet getObjet(String nom) {
        for (Objet objet : objets) {
            if (objet.getNom().equalsIgnoreCase(nom)) {
                return objet;
            }
        }
        return null;
    }

    public void afficherContenu() {
        System.out.print("Contenu du sac : ");
        for (Objet objet : objets) {
            System.out.print(objet.getNom() + " (" + objet.getType() + "), ");
        }
        System.out.println();
    }
}