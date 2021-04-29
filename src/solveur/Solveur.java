package solveur;

import instance.Instance;
import solution.Solution;

public interface Solveur {
    public String getNom();

    public Solution solve(Instance instance);
}
