package com.jacobhicks;

public class Node {
    int x;
    int y;
    boolean visited;
    boolean iswall;
    boolean end;
    public Node(int x, int y) {
        iswall = true;
        visited = false;
        this.x = x;
        this.y = y;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isWall() {
        return iswall;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setWall(boolean iswall) {
        this.iswall = iswall;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
}
