package instance.model;

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
}