package dev.rafaelreis.cap3;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Capitulo3_1 {
    public static void main(String[] args) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<=1000; i++) {
                    System.out.println(i);
                }
            }
        };
        new Thread(runnable).run();

        Runnable runnable2 = () -> {
            for(int i=0; i<=1000; i++) {
                System.out.println(i);
            }
        };
        new Thread(runnable2).run();

        new Thread(() -> {
            for(int i=0; i<1000; i++) {
                System.out.println(i);
            }
        }).start();

        /* 3.1 Outro exemplo: listeners */
        Button button = new Button();
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Evento do click acionado");
            }
        });

        button.addActionListener((event) -> {
            System.out.println("eventoo do Click acionado");
        });

        button.addActionListener(event -> System.out.println("Evento do click acionado"));
    
    }
}
