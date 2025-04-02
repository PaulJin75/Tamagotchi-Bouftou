public class Objet {
    private String nom;
    private int valeur;
    private String type;

    public Objet(String nom, int valeur, String type) {
        this.nom = nom;
        this.valeur = valeur;
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public int getValeur() {
        return valeur;
    }

    public String getType() {
        return type;
    }
}