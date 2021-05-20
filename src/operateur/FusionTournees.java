package operateur;

import solution.Tournee;

public class FusionTournees extends Operateur {

    private Tournee aFusionner;

    public FusionTournees(){
        super();
        this.aFusionner = null;
    }

    public FusionTournees(Tournee tournee, Tournee aFusionner) {
        super(tournee);
        if(aFusionner != null) {
            this.aFusionner = aFusionner;
            this.dist = this.evalDeltaDist();
        }
    }

    public Tournee getaFusionner() {
        return aFusionner;
    }

    @Override
    protected int evalDeltaDist() {
        return this.tournee.deltaDistFusion(this.aFusionner);
    }

    @Override
    protected boolean doMouvement() {
        return this.tournee.doFusion(this);
    }
}