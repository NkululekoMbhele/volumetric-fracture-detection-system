public class Point {
    private int x;
    private int y;

    //Constructors
    public Point(){
        this.x = 0;
        this.y = 0;
    }
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }
    //Mutator methods
    public void setX(int x){ this.x = x; }
    public void setY(int y){ this.y = y; }
    //Accessor methods
    public int getX(){ return x; }
    public int getY(){ return y; }
}
