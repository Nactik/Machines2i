package instance.reseau;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Point {
    private int id;
    private int x;
    private int y;
    private Map<Point, Route> routeCollection;

    public Point(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        routeCollection = new HashMap<>();
    }

    public boolean addRoute(Point destination){
        if (destination == null){
            return false;
        }
        Route route = new Route(this,destination);
        this.routeCollection.put(destination,route);
        return true;
    }

    public int getDistTo(Point destination){
        Route route = this.routeCollection.get(destination);
        if(route == null) return Integer.MAX_VALUE;
        else return route.getDistance();
    }

    public int getId() {
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
        return id == point.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Point {" +
                "id: " + id +
                ", x: " + x +
                ", y: " + y +
                "}";
    }
}
