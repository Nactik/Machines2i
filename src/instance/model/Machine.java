package instance.model;

import java.util.Objects;

public class Machine {
    private int typeId;
    private int size;
    private int penality;

    public Machine() {
        this.typeId = -1;
        this.size = -1;
        this.penality = -1;
    }

    public Machine(int typeId, int size, int penality) {
        this();
        this.typeId = typeId;
        this.size = size;
        this.penality = penality;
    }

    public int getTypeId() { return typeId; }

    public int getSize() {
        return size;
    }

    public int getPenality() { return penality; }

    @Override
    public String toString() {
        return "Machine {" +
                "typeId: " + typeId +
                ", size: " + size +
                ", penality: " + penality +
                '}';
    }
}
