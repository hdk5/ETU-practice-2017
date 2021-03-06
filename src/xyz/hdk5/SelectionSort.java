package xyz.hdk5;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class SelectionSort extends JFrame {
    //    private final Sorter sorter = new Sorter();
    private ArrayList<Integer> array = null;

    //Элементы графического интерфейса
    private final JLabel numLabel;
    private final JPanel drawPanel;
    private final JLabel manualInputLabel;
    private final JSpinner numOfElements;
    private final JButton randButton;
    private final JButton stepButton;
    private final JButton animationButton;
    private final JLabel animationSpeedLabel;
    private final JSpinner animationSpeedTextField;
    private final JLabel explanationLabel;
    private final JButton manualCommitButton;
    private final JTextField manualInputField;
    private final JLabel colorLegend;

    public SelectionSort() {
        super("Selection Sort Algorithm Visualisation");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(3, 3, 3, 3);

        colorLegend = new JLabel("Cyan - sorted, pink - current, red - current minimal");
        c.gridx = 5;
        c.gridy = 0;
        add(colorLegend, c);

        //Строка кол-ва элементов
        numLabel = new JLabel("Number of elements: ");
        c.gridx = 0;
        c.gridy = 0;
        add(numLabel, c);

        //Количество элементов (прокрутка)
        numOfElements = new JSpinner(
                new SpinnerNumberModel(25, 2, Integer.MAX_VALUE, 1)
        );
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 2;
        add(numOfElements, c);
        c.gridwidth = 1;

        //Кнопка рандомизации элементов(генерации)
        randButton = new JButton("Randomize");
        randButton.addActionListener(e -> {
            try {
                numOfElements.commitEdit();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

            array = new ArrayList<>();
            for (int i = 1; i <= (Integer) numOfElements.getValue(); i++) {
                array.add(i);
            }
            Collections.shuffle(array);
            reset();
        });
        c.gridx = 4;
        c.gridy = 0;
        add(randButton, c);


        manualInputLabel = new JLabel("Manual array input: ");
        c.gridx = 0;
        c.gridy = 1;
        add(manualInputLabel, c);

        manualInputField = new JTextField();
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 2;
        add(manualInputField, c);
        c.gridwidth = 1;

        //Кнопка ввода кастомного массива
        manualCommitButton = new JButton("Commit");
        manualCommitButton.addActionListener(e -> {
            try {
                ArrayList<String> strList = new ArrayList<>(
                        Arrays.asList(manualInputField.getText().split(" "))
                );
                ArrayList<Integer> newArray = new ArrayList<>();
                for (String str : strList) {
                    Integer value = Integer.parseInt(str.trim());
                    if (value < 1) {
                        throw new Throwable() {
                        };
                    }
                    newArray.add(value);
                }
                array = newArray;
                reset();
            } catch (Throwable ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Can't parse input string",
                        "Parse error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        c.gridx = 4;
        c.gridy = 1;
        add(manualCommitButton, c);

        //Кнопка выполнения шага сортировки
        stepButton = new JButton("Step");
        stepButton.addActionListener(e -> step());

        c.gridx = 0;
        c.gridy = 3;
        add(stepButton, c);

        //Speed
        animationSpeedLabel = new JLabel("Animation speed: ");
        c.gridx = 0;
        c.gridy = 4;
        add(animationSpeedLabel, c);

        animationSpeedTextField = new JSpinner(
                new SpinnerNumberModel(250, 10, 1000, 10)
        );
        c.gridx = 1;
        c.gridy = 4;
        add(animationSpeedTextField, c);

        drawPanel = new DrawArrayPanel();
        drawPanel.setPreferredSize(new Dimension(800, 300));
        drawPanel.setVisible(true);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 6;
        add(drawPanel, c);
        c.gridwidth = 1;

        //Кнопка отключения показа анимации
        animationButton = new JButton("Start/stop animation");
        animationButton.addActionListener(e -> switchAnimation());
        c.gridx = 1;
        c.gridy = 3;
        add(animationButton, c);

        explanationLabel = new JLabel("Define array to begin.");
        c.gridx = 2;
        c.gridy = 3;
        c.gridwidth = 6;
        add(explanationLabel, c);
        c.gridwidth = 1;

        pack();
        setVisible(true);
        setResizable(false);
    }

    public static void main(String[] args) {
        SelectionSort mainWindow = new SelectionSort();
    }


    //Визуализация массива в виде столбчатой диаграммы
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
                Integer arrayMax = Collections.max(array);
                double barWidth = getWidth() / (double) array.size();
                double barMaxHeight = getHeight();
                if (array.size() <= 50) {
                    barMaxHeight -= 30;
                }
                FontMetrics fm = g2.getFontMetrics();
                float fy = (float) ((getHeight() + barMaxHeight) / 2.);
                fy += fm.getAscent() / 2.;

                for (int i = 0; i < array.size(); ++i) {
                    if (i <= sortedIndex) {
                        g2.setColor(Color.cyan);
                    } else if (i == currIndex) {
                        g2.setColor(Color.pink);
                    } else if (i == minIndex) {
                        g2.setColor(Color.red);
                    } else {
                        g2.setColor(Color.blue);
                    }
                    double barHeight = (barMaxHeight * (array.get(i) / (double) arrayMax));
                    Shape r = new Rectangle2D.Double(i * barWidth, barMaxHeight - barHeight, barWidth, barHeight);
                    g2.fill(r);
                    g2.draw(r);

                    if (array.size() <= 50) {
                        g2.setColor(Color.black);
                        String iStr = array.get(i).toString();
                        float fx = (float) (((i + 1) * barWidth + i * barWidth) / 2.);
                        fx -= fm.stringWidth(iStr) / 2.;
                        g2.drawString(iStr, fx, fy);
                    }
                }
            }
        }
    }


    private enum State {
        OuterLoop, Completed, InnerLoop
    }

//    private class Sorter {

    private int sortedIndex = -1;
    private int minIndex = -1;
    private int currIndex = -1;
    private boolean animation = false;


    private State state = State.OuterLoop;

    private void reset() {
        explanationLabel.setText("Click \"Step\" or enable animation to begin visualisation.");
        animation = false;
        animationThread = null;
        state = State.OuterLoop;
        sortedIndex = -1;
        minIndex = -1;
        currIndex = -1;
        drawPanel.repaint();
    }

    private Thread animationThread = null;

    private void switchAnimation() {
        animation = !animation;
        if (animation && (animationThread == null || animationThread.getState() == Thread.State.TERMINATED)) {
            animationThread = new Thread(() -> {
                while (animation) {
                    step();
                    try {
//                            Thread.sleep(array.size() < 250 ? 250 : 2);
//                            Thread.sleep(250);
                        Thread.sleep((Integer) animationSpeedTextField.getValue());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            animationThread.start();
        }
    }

    private void step() {
        if (array == null) {
            return;
        }
        switch (state) {
            case OuterLoop:
                if (sortedIndex == array.size() - 1) {
                    state = State.Completed;
                    drawPanel.repaint();
                    explanationLabel.setText("Sort completed");
//                        step();
                    return;
                }
                minIndex = sortedIndex + 1;
                currIndex = minIndex;
                state = State.InnerLoop;
                explanationLabel.setText(String.format("Began looking for element for position %d", minIndex));
                drawPanel.repaint();
                return;
            case InnerLoop:
                ++currIndex;
                if (currIndex == array.size()) {
                    explanationLabel.setText(String.format("Looking for minimal element completed, swapping %d and %d", minIndex, sortedIndex + 1));
                    int tmp = array.get(sortedIndex + 1);
                    array.set(sortedIndex + 1, array.get(minIndex));
                    array.set(minIndex, tmp);
                    state = State.OuterLoop;
                    ++sortedIndex;
//                        step();
                    currIndex = minIndex = -1;
                } else if (array.get(currIndex) < array.get(minIndex)) {
                    minIndex = currIndex;
                    explanationLabel.setText(String.format("Found new minimal element on position %d", minIndex));
                } else {
                    explanationLabel.setText(String.format("Checked position %d, minimal element is still on position %d", currIndex, minIndex));
                }
                drawPanel.repaint();
                return;
            case Completed:
                return;
        }
    }
//    }
}
