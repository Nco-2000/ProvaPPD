package src;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PhilosophersManagerGUI {

    final PhilosophersManager manager;
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private DefaultTableModel forksTableModel;

    public PhilosophersManagerGUI(PhilosophersManager manager) {
        this.manager = manager;
        initializeGUI();
        startAutoRefresh();
    }

    private void initializeGUI() {
        frame = new JFrame("Philosophers Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel philosophersPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{
                "ID", "Name", "State", "Left Fork", "Right Fork", "Meals", "Thoughts"
        }, 0);
        table = new JTable(tableModel);
        JScrollPane philosophersScrollPane = new JScrollPane(table);
        philosophersPanel.add(philosophersScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Philosophers", philosophersPanel);

        JPanel forksPanel = new JPanel(new BorderLayout());
        this.forksTableModel = new DefaultTableModel(new String[]{"ID", "Name", "User"}, 0);
        JTable forksTable = new JTable(forksTableModel);
        JScrollPane forksScrollPane = new JScrollPane(forksTable);
        forksPanel.add(forksScrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Forks", forksPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);

        forksTableModel.setRowCount(0);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        List<Philosopher> philosophers = manager.getAtcivePhilosophers();

        for (Philosopher philosopher : philosophers) {
            Fork leftFork = philosopher.getLeftFork();
            Fork rightFork = philosopher.getRightFork();

            tableModel.addRow(new Object[]{
                    philosopher.getId(),
                    philosopher.getName(),
                    philosopher.getState(),
                    leftFork != null ? leftFork.getName() : "None",
                    rightFork != null ? rightFork.getName() : "None",
                    philosopher.getNumberOfMeals(),
                    philosopher.getNumberOfThoughts()
            });
        }

        forksTableModel.setRowCount(0);
        List<Fork> forks = manager.getForks();
        for (Fork fork : forks) {
            forksTableModel.addRow(new Object[]{
                    fork.getId(),
                    fork.getName(),
                    fork.getUserName()
            });
        }

        
    }

    private void startAutoRefresh() {
        int refreshInterval = 100; 

        Timer timer = new Timer(refreshInterval, e -> {refreshTable();});
        timer.start();
    }

    public void closeGUI() {
        try {
            if (frame != null) {
                frame.dispose();
            }
        } catch (Exception e) {
        }
    }
}
