/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.function.Function;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author pichon
 */
public class Plot extends Pane {
        public Plot(
                Function<Double, Double> f,
                double xMin, double xMax, double xInc,
                Axes axes
        ) {
            Path path = new Path();
            path.setStroke(Color.ORANGE.deriveColor(0, 1, 1, 0.6));
            path.setStrokeWidth(2);

            path.setClip(
                    new Rectangle(
                            0, 0, 
                            axes.getPrefWidth(), 
                            axes.getPrefHeight()
                    )
            );

            double x = xMin;
            double y = f.apply(x);

            path.getElements().add(
                    new MoveTo(
                            mapX(x, axes), mapY(y, axes)
                    )
            );

            x += xInc;
            while (x < xMax) {
                y = f.apply(x);

                path.getElements().add(
                        new LineTo(
                                mapX(x, axes), mapY(y, axes)
                        )
                );

                x += xInc;
            }

            super.setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
            super.setPrefSize(axes.getPrefWidth(), axes.getPrefHeight());
            super.setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

            super.getChildren().setAll(axes, path);
        }
        
        public Plot(
                Function<Double, Double> f,
                double xMin, double xMax, double xInc,
                Axes axes, Double anchor
        ) {
            Path path = new Path();
            path.setStroke(Color.ORANGE.deriveColor(0, 1, 1, 0.6));
            path.setStrokeWidth(anchor);

            path.setClip(
                    new Rectangle(
                            0, 0, 
                            axes.getPrefWidth(), 
                            axes.getPrefHeight()
                    )
            );

            double x = xMin;
            double y = f.apply(x);

            path.getElements().add(
                    new MoveTo(
                            mapX(x, axes), mapY(y, axes)
                    )
            );

            x += xInc;
            while (x < xMax) {
                y = f.apply(x);

                path.getElements().add(
                        new LineTo(
                                mapX(x, axes), mapY(y, axes)
                        )
                );

                x += xInc;
            }

            super.setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
            super.setPrefSize(axes.getPrefWidth(), axes.getPrefHeight());
            super.setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

            super.getChildren().setAll(axes, path);
        }
        
        public Plot(
                Function<Double, Double> f,
                double xMin, double xMax, double xInc,
                Axes axes, Double anchor, Color colorPoint
        ) {
            Path path = new Path();
            path.setStroke(colorPoint);
            path.setStrokeWidth(anchor);

            path.setClip(
                    new Rectangle(
                            0, 0, 
                            axes.getPrefWidth(), 
                            axes.getPrefHeight()
                    )
            );

            double x = xMin;
            double y = f.apply(x);

            path.getElements().add(
                    new MoveTo(
                            mapX(x, axes), mapY(y, axes)
                    )
            );

            x += xInc;
            while (x < xMax) {
                y = f.apply(x);

                path.getElements().add(
                        new LineTo(
                                mapX(x, axes), mapY(y, axes)
                        )
                );

                x += xInc;
            }

            super.setMinSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);
            super.setPrefSize(axes.getPrefWidth(), axes.getPrefHeight());
            super.setMaxSize(Pane.USE_PREF_SIZE, Pane.USE_PREF_SIZE);

            super.getChildren().setAll(axes, path);
        }

        private double mapX(double x, Axes axes) {
            double tx = axes.getPrefWidth() / 2;
            double sx = axes.getPrefWidth() / 
               (axes.getXAxis().getUpperBound() - 
                axes.getXAxis().getLowerBound());

            return x * sx + tx;
        }

        private double mapY(double y, Axes axes) {
            double ty = axes.getPrefHeight() / 2;
            double sy = axes.getPrefHeight() / 
                (axes.getYAxis().getUpperBound() - 
                 axes.getYAxis().getLowerBound());

            return -y * sy + ty;
        }
    }
