package com.example.futureplan;

import androidx.cardview.widget.CardView;

public class CardItem {

    private String text;
    public int[] colors = new int[6];

    public CardItem(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public int getColor()
    {
        colors[0] = 0xff8800aa;
        colors[1] = 0xffffcc00;
        colors[2] = 0xffff7f2b;
        colors[3] = 0xff00aa00;
        colors[4] = 0xffff2b2b;
        colors[5] = 0xffccaaff;

        int min = 0;
        int max = 5;
        int range = max - min +1;

        int value = (int) (Math.random() * range);

        System.out.println(value);
        int color = colors[value];
        return color;
    }
}
