public class Bouftou {
    private int satiete;
    private int energie;
    private int humeur;

    public Bouftou() {
        this.satiete = 100;
        this.energie = 100;
        this.humeur = 100;
    }

    public void diminuerStats() {
        satiete = Math.max(0, satiete - 5);
        energie = Math.max(0, energie - 3);
        humeur = Math.max(0, humeur - 4);
    }

    public boolean estEnFuite() {
        return satiete == 0 || energie == 0 || humeur == 0;
    }

    public void manger(Objet nourriture) {
        if (nourriture.getType().equals("nourriture")) {
            satiete = Math.min(100, satiete + nourriture.getValeur());
            energie = Math.min(100, energie + nourriture.getValeur() / 2);
        }
    }

    public void dormir() {
        humeur = Math.min(100, humeur + 20);
        energie = Math.min(100, energie + 30);
    }

    public void jouer(Objet jouet) {
        if (jouet.getType().equals("jouet")) {
            humeur = Math.min(100, humeur + jouet.getValeur());
            energie = Math.max(0, energie - 10);
        }
    }

    public void afficherStats() {
        System.out.println("Satiété: " + satiete + "/100");
        System.out.println("Énergie: " + energie + "/100");
        System.out.println("Humeur: " + humeur + "/100");
    }
}