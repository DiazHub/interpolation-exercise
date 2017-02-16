/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appliedmed.exercises;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author zmichaels
 */
public class Prediction {

    private static class Point {

        final double x;
        final double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * Interpolates between the 2D points stored in the list
     *
     * @param points list of points
     * @return interpolated list of points
     */
    private static List<Point> interpolate(List<Point> points) {
        /*
         * TODO: implement this.
         * 40% of the List's points were deleted (set to null). Interpolate 
         * those points. You may either do this in-place or calculate a new List.
         */
    	List<Point> returnList = new ArrayList<>();
        for (Point p : points) {
        	// 40% out of the points == null
            // we need to interpolate 40% missing points back from 60% available points
            // 40% of 100% is 66.7% of the 60% available ones
            if (Math.random() < 0.667 && p != null) {
            	// Map to hold nearest neighbor
            	Map<String, Double> closestPoint = new HashMap<String, Double>();
            	double minimumDistance = 999999999;
            	// find the closest point ( point with min distance) to our randomly selected point
            	for (Point d : points) {
            		if( d != null) {
            			// Distance between two points
	            		double valueToSqrt = Math.pow((d.x - p.x),2) + Math.pow((d.y - p.y),2);
	            		double currentDistance = Math.sqrt(valueToSqrt);
	            		// Check if our distance is the new minimum
	            		// && currentDistance != 0 avoids comparing itself!
	            		if(currentDistance < minimumDistance && currentDistance != 0){
	            			// if new minimum, update min dist, and closest point mapping
	            			minimumDistance = currentDistance;
	            			closestPoint.put("closestX", d.x);
	            			closestPoint.put("closestY", d.y);
	            		}
            		}
            	} // end loop to find closest point
            	//interpolate a value between the random point and the closest point to it
            	//midpoint formula
            	double xValue = (p.x + closestPoint.get("closestX"))/2;
            	double yValue = (p.y + closestPoint.get("closestY"))/2;

            	returnList.add(new Point(xValue, yValue));
            }      
        }
        return returnList;
    }

    public static void main(String[] args) throws Exception {
        final Path pExpected = Paths.get("expected.csv");
        final List<Point> expected;

        // load the expected values
        try (BufferedReader in = Files.newBufferedReader(pExpected)) {
            expected = in.lines()
                    .skip(1)
                    .map(str -> str.split(","))
                    .map(p -> new Point(Double.parseDouble(p[0]), Double.parseDouble(p[1])))
                    .collect(Collectors.toList());
        }

        // delete 40% of the values
        final List<Point> actual = new ArrayList<>(expected);

        for (int i = 0; i < actual.size(); i++) {
            if (Math.random() < 0.4) {
                actual.set(i, null);
            }
        }

        // create the interpolated list 
        final List<Point> interpolated = interpolate(actual);

        final JFrame window = new JFrame("Interpolation");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(640, 480);
        window.setVisible(true);
        window.setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                final int width = this.getWidth();
                final int height = this.getHeight();

                g.clearRect(0, 0, width, height);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, width, height);

                final BufferedImage surface = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);

                // draw the expected values as cyan
                for (Point p : expected) {
                    final int x = (int) p.x;
                    final int y = (int) p.y;

                    surface.setRGB(x, y, 0xFF00FFFF);
                }

                // draw the interpolated values as yellow
                for (Point p : interpolated) {
                    if (p != null) {
                        final int x = (int) p.x;
                        final int y = (int) p.y;

                        surface.setRGB(x, y, 0xFFFFFF00);
                    }
                }

                g.drawImage(surface, 0, 0, width, height, 0, 0, 256, 256, null);
            }
        });
    }
}
