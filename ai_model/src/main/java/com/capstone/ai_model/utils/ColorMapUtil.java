package com.capstone.ai_model.utils;

import java.awt.Color;

public class ColorMapUtil {

    public static Color jet(double value) {
        double fourValue = 4 * value;

        int r = (int) (255 * Math.min(Math.max(Math.min(fourValue - 1.5, -fourValue + 4.5), 1), 0));
        int g = (int) (255 * Math.min(Math.max(Math.min(fourValue - 0.5, -fourValue + 3.5), 1), 0));
        int b = (int) (255 * Math.min(Math.max(Math.min(fourValue + 0.5, -fourValue + 2.5), 1), 0));

        return new Color(r, g, b);
    }
}

