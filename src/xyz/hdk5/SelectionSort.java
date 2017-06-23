package xyz.hdk5;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;


public class SelectionSort extends JFrame {
    private ArrayList<Integer> array = null;
    private int sortedIndex = -1;
    private int minIndex = -1;
    private int currIndex = -1;

    private JLabel numLabel;
    private JPanel drawPanel;
    private JSpinner numOfElements;
    private JButton randButton;
    private JButton stepButton;
    private JButton animationButton;

    private boolean animation = false;

    public SelectionSort() {
        super("Selection Sort Algorithm Visualisation");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 3, 3, 3);

        numLabel = new JLabel("Number of elements: ");
        c.gridx = 0;
        c.gridy = 0;
        add(numLabel, c);


        numOfElements = new JSpinner(
                new SpinnerNumberModel(25, 2, Integer.MAX_VALUE, 1)
        );
        c.gridx = 1;
        c.gridy = 0;
        add(numOfElements, c);

        randButton = new JButton("Randomize");
        randButton.addActionListener(e -> {
            try {
                numOfElements.commitEdit();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            randomize((Integer) numOfElements.getValue());
            drawPanel.repaint();
        });

        c.gridx = 2;
        c.gridy = 0;
        add(randButton, c);

        stepButton = new JButton("Step");
        stepButton.addActionListener(e -> {
            Collections.sort(array);
            drawPanel.repaint();
        });
        c.gridx = 3;
        c.gridy = 0;
        add(stepButton, c);

        animationButton = new JButton("Start/stop animation");
        animationButton.addActionListener(e -> {

        });
        c.gridx = 4;
        c.gridy = 0;
        add(animationButton, c);

        drawPanel = new DrawArrayPanel();
        drawPanel.setPreferredSize(new Dimension(800, 300));
        drawPanel.setVisible(true);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 6;
        add(drawPanel, c);

        pack();
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        SelectionSort mainWindow = new SelectionSort();
    }

    private void randomize(int size) {
        array = new ArrayList<Integer>();
        for (int i = 1; i <= size; i++) {
            array.add(i);
        }
        Collections.shuffle(array);
    }

    private class DrawArrayPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            super.paintComponent(g2);
            g2.setColor(Color.white);
            g2.fillRect(0, 0, getWidth(), getHeight());

            if (array != null) {
                double barWidth = getWidth() / (double) array.size();
                for (int i = 0; i < array.size(); ++i) {
                    if (i < sortedIndex) {
                        g2.setColor(Color.cyan);
                    } else if (i == currIndex) {
                        g2.setColor(Color.pink);
                    } else if (i == minIndex) {
                        g2.setColor(Color.red);
                    } else {
                        g2.setColor(Color.blue);
                    }
                    double barHeight = (getHeight() * (array.get(i) / (double) array.size()));
                    Shape r = new Rectangle2D.Double(i * barWidth, getHeight() - barHeight, barWidth, barHeight);
                    g2.fill(r);
                    g2.draw(r);
                }
            }
        }
    }

}
