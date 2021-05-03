package instance.reseau;

import java.util.Objects;

public class Route {
    private Point startPoint;
    private Point endPoint;
    private int distance;

    public Route(Point startPoint, Point endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;

        this.distance = distCalculation(this.startPoint,this.endPoint);
    }

    private int distCalculation(Point start, Point end){
        int distX = start.getX() - end.getX();
        int distY = start.getY() - end.getY();

        return (int)Math.ceil(Math.sqrt(Math.pow(distX, 2) + Math.pow(distY, 2)));
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Point getEndPoint() {
        return endPoint;
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Route{" +
                "startPoint=" + startPoint +
                ", endPoint=" + endPoint +
                ", cost=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route = (Route) o;
        return startPoint.equals(route.startPoint) && endPoint.equals(route.endPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPoint, endPoint);
    }

    public static void main(String[] args) {
        Point d1 = new Point(1, 0, 0);
        Point d2 = new Point(2, 10, 30);
        Point d3 = new Point(3, 10, 30);
        Route route = new Route(d1, d2);

        System.out.println(route.getDistance());

        d1.addRoute(d2);
        System.out.println(d1.getDistTo(d2));
        System.out.println(d1.getDistTo(d3));
    }
}
