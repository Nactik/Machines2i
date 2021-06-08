
package operateur;

import solution.Tournee;

public abstract class Operateur {

    protected Tournee tournee;
    protected int dist;

    public Operateur() {
        tournee = null;
        this.dist = Integer.MAX_VALUE;
    }

    public Operateur(Tournee tournee) {
        this();
        if(tournee != null) this.tournee = tournee;
    }

    public boolean isMouvementRealisable(){
        return this.dist != Integer.MAX_VALUE;
    }

    public boolean isMeilleur(Operateur op){
        return this.dist < op.getDeltaDist();
    }

    protected abstract int evalDeltaDist();

    protected abstract boolean doMouvement();

    public boolean doMouvementIfRealisable(){
        if(!isMouvementRealisable()) return false;
        return doMouvement();
    }

    public boolean isMouvementAmeliorant(){
        return this.dist < 0;
    }

    public int getDeltaDist(){
        return dist;
    }

    @Override
    public String toString() {
        return "Operateur { " +
                "tournee= " + tournee +
                ", cout= " + dist +
                " }";
    }
}