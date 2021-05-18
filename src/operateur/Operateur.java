package operateur;

import solution.Tournee;

public abstract class Operateur {
    protected Tournee tournee;
    protected int deltaCout;

    public Operateur(){deltaCout = Integer.MAX_VALUE;}

    public Operateur(Tournee tournee){
        this();
        this.tournee = tournee;
    }

    /**
     * détermine si le mouvement est réalisable (ie. si deltaCout != Integer.MAX_VALUE)
     * @return boolean si le mouvement est réalisable ou non
     */
    public boolean isMouvementRealisable(){
        return deltaCout!=Integer.MAX_VALUE;
    }

    /**
     * détermine si le mouvement est améliorant (ie. si deltaCout < 0)
     * @return boolean si le mouvement est améliorant ou non
     */
    public boolean isMouvementAmeliorant(){
        return (deltaCout<0);
    }

    /**
     * détermine si le mouvement est meilleur qu'un autre opérateur
     * @param op opérateur à comparer
     * @return true si l'opérateur this est meilleur que celui en paramètre
     */
    public boolean isMeilleur(Operateur op){
        return (this.deltaCout<op.getDeltaCout());
    }

    /**
     * Réalise le mouvement si il est réalisable
     * @return true si le mouvement a été réalisé
     */
    public boolean doMouvementIfRealisable(){
        if (isMouvementRealisable()) {
            return doMouvement();
        }
        return false;
    }

    protected abstract int evalDeltaCout();
    protected abstract boolean doMouvement();

    public int getDeltaCout() {
        return deltaCout;
    }

    public Tournee getTournee() {
        return tournee;
    }

}
