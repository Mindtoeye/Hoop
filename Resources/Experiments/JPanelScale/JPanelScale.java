 
import javax.swing.*;
import java.util.Random;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImageFilter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
 
public class JPanelScale 
{  
    private static final JComponent createRandomPanel(String name) {
        JPanel panel = new JPanel();
        panel.setName(name);
        panel.setPreferredSize(new Dimension(640, 440));
        Random random = new Random();
        int numOfComponents = random.nextInt(9) + 1;
        for (int i = 0; i < numOfComponents; i++) {
            switch(random.nextInt(5)) {
                case 0: panel.add(new JLabel("label"));
                        break;
                case 1: panel.add(new JTextField("textField"));
                        break;
                case 2: panel.add(new JButton("button"));
                        break;
                case 3: panel.add(new JCheckBox("checkbox"));
                        break;
                case 4: String[] data = {"list", "value 1", "value 2"};
                        panel.add(new JList(data));
                        break;
            }
        }
        return panel;
    }
 
    private static class FrontPanel extends JPanel {
 
        private JComponent[] subComponents;
        private JComponent frontPanel;
        private JPanel cardPanel;
        private CardLayout cardLayout;
        private JButton backButton;
 
        public FrontPanel(JComponent[] subComponents) {
            this.subComponents = subComponents;
 
            frontPanel = createFrontPanel();
 
            cardPanel = new JPanel();
            cardLayout = new CardLayout();
 
            cardPanel.setLayout(cardLayout);
            cardPanel.add(frontPanel, frontPanel.getName());
            for (int i = 0; i < subComponents.length; i++) {
                cardPanel.add(subComponents[i], subComponents[i].getName());
            }
 
            cardLayout.first(cardPanel);
 
            setLayout(new BorderLayout());
            add(cardPanel, BorderLayout.CENTER);
 
            JPanel buttonPanel = new JPanel();
            backButton = new JButton("<=");
            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cardLayout.first(cardPanel);
                    backButton.setEnabled(false);
                }
            });
            backButton.setEnabled(false);
            buttonPanel.add(backButton);
            add(buttonPanel, BorderLayout.SOUTH);
        }
 
        private JComponent createFrontPanel() {
            JPanel p = new JPanel();
            p.setLayout(new FlowLayout());
            p.setName("frontPanel");
 
            for (int i = 0; i < subComponents.length; i++) {
                JButton selectPanel = new JButton(new PanelSelectAction(subComponents[i]));
                p.add(selectPanel);
            }
 
            return p;
        }
 
        private class PanelSelectAction extends AbstractAction {
 
            private JComponent c;
 
            public PanelSelectAction(JComponent c) {
                super(c.getName(), new ScaledPanelIcon(c));
                this.c = c;
            }
 
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(cardPanel, c.getName());
                backButton.setEnabled(true);
            }
        }
 
        private static class ScaledPanelIcon implements Icon {
 
            private int width;
            private int height;
 
            private Image image;
 
            public ScaledPanelIcon(JComponent c) {
                this(c, 100, 100);
            }
 
            public ScaledPanelIcon(JComponent c, int width, int height) {
                this.width = width;
                this.height = height;
 
                BufferedImage tempImage = new BufferedImage(c.getPreferredSize().width, c.getPreferredSize().height, BufferedImage.TYPE_INT_ARGB);
                System.out.println("image w = " + tempImage.getWidth() + " h = " + tempImage.getHeight());
                Graphics g = tempImage.getGraphics();
                JWindow tempParent = new JWindow();
                tempParent.getContentPane().add(c);
                tempParent.pack();
                try{
                    c.paint(g);
                }
                finally {
                    tempParent.remove(c);
                    tempParent.dispose();
                    g.dispose();
                }
 
                image = tempImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            }
 
            public int getIconHeight() {
                return height;
            }
 
            public int getIconWidth() {
                return width;
            }
 
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.drawImage(image, x, y, width, height, c);
            }
        }
    }
    
    public static void main(String[] args) 
    {
        Random random = new Random();
        JComponent[] panelArray = new JComponent[random.nextInt(9) + 1];
        for (int i = 0; i < panelArray.length; i++) {
            panelArray[i] = createRandomPanel("Panel " + (i + 1));
        }
 
        FrontPanel frontPanel = new FrontPanel(panelArray);
 
        JFrame f = new JFrame("FrontPanelTest");
        f.getContentPane().add(frontPanel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(640, 480);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }    
}
