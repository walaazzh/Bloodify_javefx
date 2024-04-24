package entities;

public class centredecollect {
    private int id,codePostal,capaciteMax;
    private String name,ville;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public centredecollect() {
    }

    public centredecollect(int id, int codePostal, int capaciteMax, String name, String ville) {
        this.id = id;
        this.codePostal = codePostal;
        this.capaciteMax = capaciteMax;
        this.name = name;
        this.ville = ville;
    }

    @Override
    public String toString() {
        return "CentresCollectes{" +
                "id=" + id +
                ", codePostal=" + codePostal +
                ", capaciteMax=" + capaciteMax +
                ", name='" + name + '\'' +
                ", ville='" + ville + '\'' +
                '}';
    }
}
