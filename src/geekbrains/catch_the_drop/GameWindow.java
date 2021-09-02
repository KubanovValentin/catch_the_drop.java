package geekbrains.catch_the_drop;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {
    private static GameWindow game_window; //  и тепрерь создать обьект на который и будет ссылаться эта переменная
    private static long last_frame_time;
    //и теперь у нас есть 3 переменных и теперь надо туда загрузить картинки
    private static  Image background;
    private static  Image game_over;
    private static  Image drop;
    private static float drop_left = 200; //координаты капли x
    private static float drop_top = -100; //координаты капли y
    private static float drop_v = 200;
    private static int score;


    public static void main(String[] args) throws IOException {
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));
        // создаем обьект
        game_window = new GameWindow();
        //как создали обьект производим его настроку
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// это то что при закрытии окна закрываеться и программа
        //теперь настроим точку(с координатами) по которой будет открываться наше окно
        game_window.setLocation(200, 100);
        //теперь настраиваем- ширину и высоту окна
        game_window.setSize(906, 476);
        //теперь запрещаем изменения размеров окна
        game_window.setResizable(false);
        last_frame_time = System.nanoTime();
        GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float drop_right = drop_left + drop.getWidth(null);
                float drop_bottom = drop_top + drop.getHeight(null);
                boolean is_drop = x >= drop_left && x <= drop_right && y >= drop_top && y <= drop_bottom;
                if (is_drop) {
                    drop_top = -100;
                    drop_left = (int) (Math.random() * (game_field.getWidth() - drop.getWidth(null)));
                    drop_v = drop_v + 20;
                    score++;
                    game_window.setTitle("Score: " + score);
                }
            }
        });
        game_window.add(game_field);
        //теперь надо сделать окно видимым т.к оно по умолчанию не видимое
        game_window.setVisible(true);
        //запускаем код и видим что открываеться нам окно
        //теперь нам осталось подготомить окно таким образом чтобы мы могли в нем рисовать
    }
    //обьявим новый метод
    private static void onRepaint(Graphics g) {
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time) * 0.000000001f;
        last_frame_time = current_time;
        drop_top = drop_top + drop_v * delta_time;
//        drop_left = drop_left + drop_v * delta_time;
        g.drawImage(background, 0,0, null);
        g.drawImage(drop, (int) drop_left, (int) drop_top, null);
        if (drop_top > game_window.getHeight())  g.drawImage(game_over,280,120, null);
//        g.fillOval(10, 10,200, 100);
//        g.drawLine(200, 200, 400, 400);

    }
    private static class GameField extends  JPanel {
        @Override
        protected  void paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
