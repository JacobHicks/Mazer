package com.jacobhicks;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Main {
    static Color[] colors;
    static JFrame display;
    static Graphics g;
    private static HashSet<Node> hm;
    public static void main(String[] args) {
        display = new JFrame("Mazer");
        display.setBounds(0, 0, 800, 625);
        display.setLayout(null);
        display.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        display.setVisible(true);
        g = display.getGraphics();
        colors = new Color[4000];
        for(int i = 0; i < 4000; i++) {
            colors[i] = new Color(i / 16, 0, 255 - i / 16);
        }
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int watchdog = 0;
        while(true) {
            watchdog++;
            g.setColor(Color.BLACK);
            hm = new HashSet<>();
            Node[][] board = new Node[150][150];
            for (int r = 0; r < board.length; r++) {
                for (int i = 0; i < board[r].length; i++) {
                    board[r][i] = new Node(i, r);
                    g.fillRect(i * (800 / board[r].length), r * (600 / board.length) + 22, (800 / board.length), (600 / board[r].length));
                }
            }
            generateMaze(board);
        /*for(int y = 0; y < board.length; y++) {
            for(int x = 0; x < board[y].length; x++) {
                g.setColor(board[y][x].isWall() ? Color.BLACK : Color.WHITE);
                g.fillRect(x * (800 / board[y].length), y * (600 / board.length) + 22, (800 / board.length), (600 / board[y].length));
            }
        }*/
            int y;
            int x;
            do {
                y = (int) (Math.random() * board.length);
                x = (int) (Math.random() * board[y].length);
            } while (board[y][x].iswall);
            board[y][x].setEnd(true);
            g.setColor(Color.GREEN);
            g.fillRect(x * (800 / board[y].length), y * (600 / board.length) + 22, (800 / board.length), (600 / board[y].length));
            g.setColor(Color.RED);
            g.fillRect(0, 22, (800 / board.length), (600 / board[y].length));
            if(watchdog % 2 == 0) {
                dfs(board, 0, 0, 0, true);
            }
            else {
                bfs(board);
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void bfs(Node[][] gb) {
        Queue<int[]> nodes = new ArrayDeque<>();
        Queue<Integer> layers = new ArrayDeque<>();
        nodes.offer(new int[]{0,0});
        layers.offer(0);
        while(!nodes.isEmpty()) {
            int dist = layers.poll();
            int[] pos = nodes.poll();
            if(!(pos[0] < 0 || pos[1] < 0 || pos[0] >= gb.length || pos[1] >= gb[pos[0]].length || gb[pos[0]][pos[1]].isWall() || !gb[pos[0]][pos[1]].isVisited())) {
                gb[pos[0]][pos[1]].setVisited(false);
                g.setColor(Color.RED);
                g.fillRect(pos[1] * (800 / gb[pos[0]].length), pos[0] * (600 / gb.length) + 22, (800 / gb.length), (600 / gb[pos[0]].length));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(gb[pos[0]][pos[1]].isEnd()) {
                    g.setColor(Color.GREEN);
                    g.fillRect(pos[1] * (800 / gb[pos[0]].length), pos[0] * (600 / gb.length) + 22, (800 / gb.length), (600 / gb[pos[0]].length));
                    dfs(gb, 0, 0, 0, false);
                    break;
                }
                else {
                    g.setColor(colors[dist % colors.length]);
                    g.fillRect(pos[1] * (800 / gb[pos[0]].length), pos[0] * (600 / gb.length) + 22, (800 / gb.length), (600 / gb[pos[0]].length));
                }
                nodes.offer(new int[]{pos[0] + 1, pos[1]});
                layers.offer(dist + 1);
                nodes.offer(new int[]{pos[0] - 1, pos[1]});
                layers.offer(dist + 1);
                nodes.offer(new int[]{pos[0], pos[1] + 1});
                layers.offer(dist + 1);
                nodes.offer(new int[]{pos[0], pos[1] - 1});
                layers.offer(dist + 1);
            }
        }
    }

    private static boolean dfs(Node[][] gb, int y, int x, int dist, boolean wait) {
        if(y < 0 || x < 0 || y >= gb.length || x >= gb[y].length || gb[y][x].isWall() || ((wait && !gb[y][x].isVisited()) || (!wait && gb[y][x].isVisited()))) {
            return false;
        }
        else if(gb[y][x].isEnd()) {
            return true;
        }
        if (wait) {
            gb[y][x].setVisited(false);
        }
        else {
            gb[y][x].setVisited(true);
        }
        g.setColor(Color.RED);
        g.fillRect(x * (800 / gb[y].length), y * (600 / gb.length) + 22, (800 / gb.length), (600 / gb[y].length));
        if(wait) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(dfs(gb, y + 1, x, dist + 1, wait) || dfs(gb, y - 1, x, dist + 1, wait) || dfs(gb, y, x + 1, dist + 1, wait) || dfs(gb, y, x - 1, dist + 1, wait)) {
            return true;
        }
        else {
            g.setColor(colors[dist % colors.length]);
            g.fillRect(x * (800 / gb[y].length), y * (600 / gb.length) + 22, (800 / gb.length), (600 / gb[y].length));
            return false;
        }
    }

    private static void generateMaze(Node[][] board) {
        ArrayList<Node> walls = new ArrayList<>();
        int x = 0; int y = 0;
        walls.add(board[0][0]);
        do {
            int rand = (int)(Math.random() * 4);
            Node tmp = null;
            if(rand == 1 && walls.size() >= 4 && !walls.get(walls.size() - 4).isVisited()) {
                tmp = walls.remove(walls.size() - 4);
            }
            else if(rand == 1 && walls.size() >= 4) {
                walls.remove(walls.size() - 4);
                continue;
            }
            if(rand == 2 && walls.size() >= 3 && !walls.get(walls.size() - 3).isVisited()) {
                tmp = walls.remove(walls.size() - 3);
            }
            else if(rand == 2 && walls.size() >= 3)  {
                walls.remove(walls.size() - 3);
                continue;
            }
            if(rand == 3 && walls.size() >= 2 && !walls.get(walls.size() - 2).isVisited()) {
                tmp = walls.remove(walls.size() - 2);
            }
            else if(rand == 3 && walls.size() >= 2) {
                walls.remove(walls.size() - 2);
                continue;
            }
            if(rand == 0 && !walls.get(walls.size() - 1).isVisited()){
                tmp = walls.remove(walls.size() - 1);
            }
            else if(rand == 0) {
                walls.remove(walls.size() - 1);
                continue;
            }
            if(tmp == null) continue;
            x = tmp.x;
            y = tmp.y;
            board[y][x].setWall(false);
            try {
                Thread.sleep(0, 0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            g.setColor(Color.WHITE);
            g.fillRect(x * (800 / board[y].length), y * (600 / board.length) + 22, (800 / board.length), (600 / board[y].length));
            //g.setColor(Color.BLACK);
            //g.fillRect(x * (800 / board[y].length), y * (800 / board.length), (800 / board.length), (800 / board[y].length));
            if(y < board.length - 1 && !board[y + 1][x].isVisited() && !walls.contains(board[y + 1][x])) {
                walls.add(board[y + 1][x]);
            }
            else if(y < board.length - 1){
                board[y + 1][x].setVisited(true);
            }
            if(y > 0 && !board[y - 1][x].isVisited() && !walls.contains(board[y - 1][x])) {
                walls.add(board[y - 1][x]);
            }
            else if(y > 0){
                board[y - 1][x].setVisited(true);
            }
            if(x < board[0].length - 1 && !board[y][x + 1].isVisited() && !walls.contains(board[y][x + 1])) {
                walls.add(board[y][x + 1]);
            }
            else if(x < board[0].length - 1){
                board[y][x + 1].setVisited(true);
            }
            if(x > 0 && !board[y][x - 1].isVisited() && !walls.contains(board[y][x - 1])) {
                walls.add(board[y][x - 1]);
            }
            else if(x > 0){
                board[y][x - 1].setVisited(true);
            }
            board[y][x].setVisited(true);
        } while(walls.size() > 0);
    }
}