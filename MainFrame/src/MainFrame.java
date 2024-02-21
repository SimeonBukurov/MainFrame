import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainFrame extends Frame {
    private LinkedList<ShapeWithColors> shapes = new LinkedList<>();
    private Choice shapeChoice;
    private TextField fillRedTextField, fillGreenTextField, fillBlueTextField;
    private TextField outlineRedTextField, outlineGreenTextField, outlineBlueTextField;
    private TextField widthTextField, heightTextField;
    private DrawingPanel drawingPanel;

    private int startX, startY; // coordinates for the starting point of the shape
    private Color fillColor;
    private Color outlineColor;
    private ShapeType currentShapeType = ShapeType.RECTANGLE; // default shape type

    // Enum to represent different types of shapes
    private enum ShapeType {
        OVAL,
        LINE,
        RECTANGLE
    }

    public MainFrame() {
        setTitle("Drawing Application");
        setSize(800, 600);
        setBackground(Color.DARK_GRAY);

        shapeChoice = new Choice();
        shapeChoice.add("Oval");
        shapeChoice.add("Line");
        shapeChoice.add("Rectangle");
        shapeChoice.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                switch (shapeChoice.getSelectedItem()) {
                    case "Oval":
                        currentShapeType = ShapeType.OVAL;
                        break;
                    case "Line":
                        currentShapeType = ShapeType.LINE;
                        break;
                    case "Rectangle":
                        currentShapeType = ShapeType.RECTANGLE;
                        break;
                }
            }
        });

        fillRedTextField = new TextField("0", 3);
        fillGreenTextField = new TextField("0", 3);
        fillBlueTextField = new TextField("0", 3);

        outlineRedTextField = new TextField("0", 3);
        outlineGreenTextField = new TextField("0", 3);
        outlineBlueTextField = new TextField("0", 3);

        widthTextField = new TextField("0", 3);
        heightTextField = new TextField("0", 3);

        drawingPanel = new DrawingPanel();
        drawingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
                // set the fillColor and outlineColor based on the text fields
                int fillRed = Integer.parseInt(fillRedTextField.getText());
                int fillGreen = Integer.parseInt(fillGreenTextField.getText());
                int fillBlue = Integer.parseInt(fillBlueTextField.getText());

                int outlineRed = Integer.parseInt(outlineRedTextField.getText());
                int outlineGreen = Integer.parseInt(outlineGreenTextField.getText());
                int outlineBlue = Integer.parseInt(outlineBlueTextField.getText());

                fillColor = new Color(fillRed, fillGreen, fillBlue);
                outlineColor = new Color(outlineRed, outlineGreen, outlineBlue);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                int endX = e.getX();
                int endY = e.getY();
                createShape(endX, endY);
                drawingPanel.repaint();
            }
        });

        Button clearButton = new Button("Clear Shapes");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearShapes();
            }
        });

        Button printButton = new Button("Print to Console");
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printToConsole();
            }
        });

        Panel topPanel = new Panel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        topPanel.setBackground(Color.lightGray);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        topPanel.add(new Label("Choose Shape:"), gbc);

        gbc.gridx = 4;
        gbc.gridy = 0;
        topPanel.add(shapeChoice, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        topPanel.add(new Label("Fill Color (RGB):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        topPanel.add(new Label("Red:"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        topPanel.add(fillRedTextField, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        topPanel.add(new Label("Green:"), gbc);

        gbc.gridx = 4;
        gbc.gridy = 1;
        topPanel.add(fillGreenTextField, gbc);

        gbc.gridx = 5;
        gbc.gridy = 1;
        topPanel.add(new Label("Blue:"), gbc);

        gbc.gridx = 6;
        gbc.gridy = 1;
        topPanel.add(fillBlueTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        topPanel.add(new Label("Outline Color (RGB):"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        topPanel.add(new Label("Red:"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        topPanel.add(outlineRedTextField, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        topPanel.add(new Label("Green:"), gbc);

        gbc.gridx = 4;
        gbc.gridy = 2;
        topPanel.add(outlineGreenTextField, gbc);

        gbc.gridx = 5;
        gbc.gridy = 2;
        topPanel.add(new Label("Blue:"), gbc);

        gbc.gridx = 6;
        gbc.gridy = 2;
        topPanel.add(outlineBlueTextField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        topPanel.add(new Label("Width:"), gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        topPanel.add(widthTextField, gbc);

        gbc.gridx = 5;
        gbc.gridy = 3;
        topPanel.add(new Label("Height:"), gbc);

        gbc.gridx = 6;
        gbc.gridy = 3;
        topPanel.add(heightTextField, gbc);

        gbc.gridx = 7;
        gbc.gridy = 1;
        topPanel.add(clearButton, gbc);

        gbc.gridx = 7;
        gbc.gridy = 2;
        topPanel.add(printButton, gbc);

        setLayout(new BorderLayout());
        add(BorderLayout.NORTH, topPanel);
        add(BorderLayout.CENTER, drawingPanel);

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    private void clearShapes() {
        shapes.clear();
        drawingPanel.repaint();
    }

    private void printToConsole() {
        System.out.println("Shapes in the Drawing Panel:");
        for (ShapeWithColors shape : shapes) {
            System.out.println(shape.toString());
        }
    }

    private void createShape(int endX, int endY) {
        switch (currentShapeType) {
            case OVAL:
                int width = Integer.parseInt(widthTextField.getText());
                int height = Integer.parseInt(heightTextField.getText());
                // create a new oval with the specified colors, dimensions, and location
                shapes.add(new Oval(fillColor, outlineColor, endX - width / 2, endY - height / 2, width, height));
                break;
            case LINE:
                // create a new line with the specified colors and start/end coordinates
                shapes.add(new Line(outlineColor, startX, startY, endX, endY));
                break;
            case RECTANGLE:
                int width2 = Integer.parseInt(widthTextField.getText());
                int height2 = Integer.parseInt(heightTextField.getText());
                // create a new rectangle with the specified colors, dimensions, and location
                shapes.add(new Rectangle(fillColor, outlineColor, endX - width2 / 2, endY - height2 / 2, width2, height2));
                break;
        }
    }

    private class DrawingPanel extends Panel {

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            for (ShapeWithColors shape : shapes) {
                shape.draw(g);
            }
        }
    }

    private interface ShapeWithColors {
        void draw(Graphics g);
    }

    private class Oval implements ShapeWithColors {
        private Color fillColor;
        private Color outlineColor;
        private int topLeftX;
        private int topLeftY;
        private int width;
        private int height;

        @Override
        public String toString() {
            return "Oval";
        }

        public Oval(Color fillColor, Color outlineColor, int topLeftX, int topLeftY, int width, int height) {
            this.fillColor = fillColor;
            this.outlineColor = outlineColor;
            this.topLeftX = topLeftX;
            this.topLeftY = topLeftY;
            this.width = width;
            this.height = height;
        }

        @Override
        public void draw(Graphics g) {
            g.setColor(fillColor);
            g.fillOval(topLeftX, topLeftY, width, height);

            g.setColor(outlineColor);
            g.drawOval(topLeftX, topLeftY, width, height);
        }
    }

    private class Line implements ShapeWithColors {
        private Color outlineColor;
        private int startX;
        private int startY;
        private int endX;
        private int endY;

        @Override
        public String toString() {
            return "Line";
        }

        public Line(Color outlineColor, int startX, int startY, int endX, int endY) {
            this.outlineColor = outlineColor;
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }

        @Override
        public void draw(Graphics g) {
            g.setColor(outlineColor);
            g.drawLine(startX, startY, endX, endY);
        }
    }

    private class Rectangle implements ShapeWithColors {
        private Color fillColor;
        private Color outlineColor;
        private int topLeftX;
        private int topLeftY;
        private int width;
        private int height;

        @Override
        public String toString() {
            return "Rectangle";
        }

        public Rectangle(Color fillColor, Color outlineColor, int topLeftX, int topLeftY, int width, int height) {
            this.fillColor = fillColor;
            this.outlineColor = outlineColor;
            this.topLeftX = topLeftX;
            this.topLeftY = topLeftY;
            this.width = width;
            this.height = height;
        }

        @Override
        public void draw(Graphics g) {
            g.setColor(fillColor);
            g.fillRect(topLeftX, topLeftY, width, height);

            g.setColor(outlineColor);
            g.drawRect(topLeftX, topLeftY, width, height);
        }
    }

}
