import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StorageGUI extends JPanel {
    private MyGUI myGui;

    private JPanel squaresPanel;
    private JScrollPane scrollPane;

    public StorageGUI(MyGUI myGui) {
        this.myGui = myGui;

        setLayout(new BorderLayout());

        // title
        JLabel titleTxt = new JLabel("Storage Units Browser", SwingConstants.CENTER);
        titleTxt.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titleTxt, BorderLayout.NORTH);

        // 2 buttons for the 2 search types
        JPanel topBtnPanel = new JPanel();
        JButton viewAllBtn = new JButton("View All Units");
        JButton viewAvailBtn = new JButton("View Available Units");

        topBtnPanel.add(viewAllBtn);
        topBtnPanel.add(viewAvailBtn);

        add(topBtnPanel, BorderLayout.PAGE_START);

        // it lets us scroll when there are more units than the screen fits
        squaresPanel = new JPanel();
        squaresPanel.setLayout(new GridLayout(0, 4, 10, 10));
        squaresPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        scrollPane = new JScrollPane(squaresPanel);
        add(scrollPane, BorderLayout.CENTER);

        // a back button that sends the user back to the welcom screen
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> myGui.showMain("Welcome Screen"));
        add(backBtn, BorderLayout.SOUTH);

        // directs the button to what it will execute
        viewAllBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllUnits();
            }
        });

        viewAvailBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAvailableUnits();
            }
        });
    }

    // this is what the view all units button uses
    private void showAllUnits() {
        squaresPanel.removeAll(); // clear the boxes if it was displaying something before

        // Get all storage IDs from where the boy Will has the mysql database lol
        List<Integer> allIds = mySQL.getStorageID();

        for (Integer id : allIds) {
            boolean isReserved = mySQL.isUnitReserved(id);
            squaresPanel.add(createStorageSquare(id, isReserved));
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    // this is what the show all units button uses
    private void showAvailableUnits() {
        squaresPanel.removeAll(); // Clear

        List<Integer> allIds = mySQL.getStorageID();
        for (Integer id : allIds) {
            boolean isReserved = mySQL.isUnitReserved(id);
            if (!isReserved) {
                squaresPanel.add(createStorageSquare(id, false));
            }
        }
        squaresPanel.revalidate();
        squaresPanel.repaint();
    }

    // I had an idea to sort of signal the person away from a unit box if its reserved, so this part
    // basically it makes a red slash over it if its reserved
    private JPanel createStorageSquare(int storageID, boolean reserved) {
        JPanel unitPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // If reserved, draw a red slash
                if (reserved) {
                    g.setColor(Color.RED);
                    g.drawLine(0, 0, getWidth(), getHeight());
                }
            }
        };
        // it makes a sqaure we adjust this if we want it smaller tho
        unitPanel.setPreferredSize(new Dimension(120, 120));
        unitPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        unitPanel.setLayout(new GridBagLayout()); // so we can center the labels

        // ID label on the square idk if we'll add images later but itll be cooler
        JLabel idLabel = new JLabel("ID: " + storageID);
        unitPanel.add(idLabel);

        // If reserved, also add a "Reserved" label in red
        if (reserved) {
            JLabel reservedLabel = new JLabel("Reserved");
            reservedLabel.setForeground(Color.RED);
            unitPanel.add(reservedLabel);
        }

        return unitPanel;
    }
}
