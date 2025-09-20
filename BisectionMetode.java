
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class BisectionMetode extends JFrame {

    private JTextField aField;
    private JTextField bField;
    private JTextField tolField;
    private JTextField maxIterField;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private JLabel resultLabel;

    public BisectionMetode() {
        setTitle("Metode Bisection untuk Pencarian Akar");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel aLabel = new JLabel("Masukkan Nilai a:");
        aLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        aField = new JTextField();
        aField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(aLabel);
        inputPanel.add(aField);

        JLabel bLabel = new JLabel("Masukkan Nilai b:");
        bLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        bField = new JTextField();
        bField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(bLabel);
        inputPanel.add(bField);

        JLabel tolLabel = new JLabel("Masukkan Toleransi (ε):");
        tolLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        tolField = new JTextField();
        tolField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(tolLabel);
        inputPanel.add(tolField);

        JLabel maxIterLabel = new JLabel("Masukkan Iterasi Maksimum:");
        maxIterLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        maxIterField = new JTextField();
        maxIterField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputPanel.add(maxIterLabel);
        inputPanel.add(maxIterField);

        JButton findRootButton = new JButton("Cari Akar");
        findRootButton.setFont(new Font("Arial", Font.BOLD, 14));
        findRootButton.setBackground(new Color(70, 130, 180));
        findRootButton.setForeground(Color.WHITE);
        findRootButton.setFocusPainted(false);
        findRootButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        findRootButton.addActionListener(new FindRootAction());
        inputPanel.add(findRootButton);

        JButton saveTxtButton = new JButton("Simpan ke Txt");
        saveTxtButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveTxtButton.setBackground(new Color(34, 139, 34));
        saveTxtButton.setForeground(Color.WHITE);
        saveTxtButton.setFocusPainted(false);
        saveTxtButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        saveTxtButton.addActionListener(new SaveTxtAction());
        inputPanel.add(saveTxtButton);

        add(inputPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Iterasi");
        tableModel.addColumn("a");
        tableModel.addColumn("b");
        tableModel.addColumn("x");
        tableModel.addColumn("F(a)");
        tableModel.addColumn("F(b)");

        resultTable = new JTable(tableModel);
        resultTable.setFont(new Font("Courier New", Font.PLAIN, 12));
        resultTable.setRowHeight(20);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        resultPanel.add(new JScrollPane(resultTable), BorderLayout.CENTER);
        add(resultPanel, BorderLayout.CENTER);

        resultLabel = new JLabel("");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setForeground(Color.RED);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(resultLabel, BorderLayout.SOUTH);
    }

    private class FindRootAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                double a = Double.parseDouble(aField.getText());
                double b = Double.parseDouble(bField.getText());
                double tol = Double.parseDouble(tolField.getText());
                int maxIter = Integer.parseInt(maxIterField.getText());

                double fa = f(a);
                double fb = f(b);

                if (fa * fb > 0) {
                    resultLabel
                            .setText("Interval yang dipilih tidak valid, silakan masukkan nilai a dan b yang berbeda.");
                    tableModel.setRowCount(0); // Clear table
                    return;
                }

                tableModel.setRowCount(0); // Clear table
                int iterasi = 0;
                double xr = 0;

                while (iterasi < maxIter) {
                    xr = (a + b) / 2;
                    double fxr = f(xr);

                    tableModel.addRow(new Object[] { iterasi + 1, String.format("%.6f", a), String.format("%.6f", b),
                            String.format("%.6f", xr), String.format("%.6f", fa), String.format("%.6f", fb) });

                    if (Math.abs(fxr) < tol || (b - a) / 2 < tol || iterasi >= 10) {
                        break;
                    }

                    iterasi++;

                    if (fa * fxr < 0) {
                        b = xr;
                        fb = fxr;
                    } else {
                        a = xr;
                        fa = fxr;
                    }
                }

                resultLabel.setText(String.format("Akar ditemukan di xr = %.6f setelah %d iterasi", xr, iterasi));

            } catch (NumberFormatException ex) {
                resultLabel.setText("Masukkan nilai numerik yang valid.");
                tableModel.setRowCount(0); // Clear table
            }
        }
    }

    private class SaveTxtAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("Hasil_Bisection.txt"))) {
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    bw.write(tableModel.getColumnName(i) + "\t");
                }
                bw.newLine();

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        bw.write(tableModel.getValueAt(i, j).toString() + "\t");
                    }
                    bw.newLine();
                }

                JOptionPane.showMessageDialog(null, "Hasil disimpan (Hasil_Bisection.txt)");

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private double f(double x) {
        return Math.exp(-x) - x;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BisectionMetode().setVisible(true);
        });
    }
}

