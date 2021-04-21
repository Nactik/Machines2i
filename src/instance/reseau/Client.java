package instance.reseau;

import java.util.Objects;

public class Client extends Point{
    private int demande;

    public Client(Integer id, int x, int y, int demande) {
        super(id, x, y);
        this.demande = demande;
    }

    public int getDemande() {
        return demande;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Client client = (Client) o;
        return this.getId() == client.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    @Override
    public String toString() {
        return "Client {" +
                "demande=" + demande + " " +
                super.toString() +
                " }";
    }
}
