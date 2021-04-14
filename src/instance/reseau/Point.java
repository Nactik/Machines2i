package instance.reseau;

import java.util.Objects;

public class Point {
    private Integer id;
    private int x;
    private int y;

    public Point(Integer id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Integer getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return id.equals(point.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Point{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
