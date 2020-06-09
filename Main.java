import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;

public class Main extends Application {
    private final int width = 60;
    private final int height = 60;
    private int size = 10;
    private Cell[] cells = new Cell[width * height];
    private Pane pane = new Pane();
    private Scene scene = new Scene(pane, width * size, height * size);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        for (int i = 0; i < width * height; i++) {
            cells[i] = new Cell(i);
        }
        primaryStage.setScene(scene);
        primaryStage.setTitle("bfs算法可视化");
        primaryStage.show();
        Thread bfs = new Thread(this::bfsGo);
        bfs.start();
    }


    private void bfsGo() {
        int[] to = new int[width * height];//从起点到一个已知路径上的最后一个顶点
        LinkedList<Integer> queue = new LinkedList<>();
        int source = (int) (Math.random() * width * height);
        int target = (int) (Math.random() * width * height);
        System.out.println(source + " " + target);
        queue.add(source);
        cells[source].visited = true;
        while (!queue.isEmpty()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int current = queue.remove();
            for (Integer i : getNeighbors(current)) {
                if (!cells[i].visited) {
                    queue.offer(i);
                    //一些队列有大小限制，因此如果想在一个满的队列中加入一个新项，多出的项就会被拒绝。
                    //这时offer方法就可以起作用了。它不是对调用 add() 方法抛出一个 unchecked 异常，而只是得到由 offer() 返回的 false。
                    cells[i].visited = true;
                    cells[i].setFill(Color.WHITE);
                    to[i] = current;
                }
            }

        }


        for (int i = target; i != source; i = to[i]) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cells[i].setFill(Color.RED);

        }
        cells[source].rectangle.setFill(Color.RED);
    }


    private ArrayList<Integer> getNeighbors(int x) {
        ArrayList<Integer> neighbors = new ArrayList<>();
        if (x / width != 0) {
            neighbors.add(x - width);
        }
        if (x / width != height - 1) {
            neighbors.add(x + width);
        }
        if (x % width != 0) {
            neighbors.add(x - 1);
        }
        if (x % width != width - 1) {
            neighbors.add(x + 1);
        }
        return neighbors;
    }


    class Cell {
        int x;
        int y;
        boolean visited;
        Rectangle rectangle;

        Cell(int i) {
            x = i % width;
            y = i / width;
            rectangle = new Rectangle(x * size, y * size, size, size);
            rectangle.setStroke(Color.WHITE);
            pane.getChildren().add(rectangle);
        }

        void setFill(Color c) {
            Platform.runLater(() -> rectangle.setFill(c));
        }
    }
}
