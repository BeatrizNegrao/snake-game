package AC2;

public class nodeSnake {
    int x; // representa a coordenada x
    int y; // representa a coordenada y
    nodeSnake next; // Próximo segmento
    
    //Construtor 
    public nodeSnake(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //Getters e setters 
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public nodeSnake getNext() {
        return next;
    }

    public void setNext(nodeSnake next) {
        this.next = next;
    }
}