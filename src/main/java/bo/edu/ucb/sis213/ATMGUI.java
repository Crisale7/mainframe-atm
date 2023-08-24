package bo.edu.ucb.sis213;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

public class ATMGUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private JLabel pinLabel;
    private JPasswordField pinField;
    private JButton enterButton;
    private JButton exitButton;

    private DatabaseConnection databaseConnection;
    private ATM atm;

    public ATMGUI() {
        super("ATM");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        databaseConnection = new DatabaseConnection();
        atm = new ATM(databaseConnection);

        Font font = new Font("Arial", Font.BOLD, 18);

        pinLabel = new JLabel("Ingrese su PIN: ", SwingConstants.CENTER);
        pinLabel.setFont(font);
        pinLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(pinLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(centerPanel, BorderLayout.CENTER);

        pinField = new JPasswordField(4);
        centerPanel.add(pinField);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 2));
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(southPanel, BorderLayout.SOUTH);

        enterButton = new JButton("Ingresar");
        enterButton.setFont(font);
        enterButton.addActionListener(this);
        southPanel.add(enterButton);

        exitButton = new JButton("Salir");
        exitButton.setFont(font);
        exitButton.addActionListener(this);
        southPanel.add(exitButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enterButton) {
            String pinText = new String(pinField.getPassword());
            int pin = Integer.parseInt(pinText);

            if (atm.validarPIN(pin)) {
                MainMenuGUI mainMenuGUI = new MainMenuGUI(atm);
                mainMenuGUI.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "PIN incorrecto. Por favor intente de nuevo.");
            }
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        ATMGUI atmGUI = new ATMGUI();
        atmGUI.setVisible(true);
    }
}

class MainMenuGUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private JLabel menuLabel;
    private JButton checkBalanceButton;
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton changePinButton;
    private JButton exitButton;

    private ATM atm;

    public MainMenuGUI(ATM atm) {
        super("ATM Menu Principal");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        this.atm = atm;

        Font font = new Font("Arial", Font.BOLD, 18);

        menuLabel = new JLabel("Menu Principal", SwingConstants.CENTER);
        menuLabel.setFont(font);
        menuLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(menuLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 1));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(centerPanel, BorderLayout.CENTER);

        checkBalanceButton = new JButton("Ver Saldo");
        checkBalanceButton.setFont(font);
        checkBalanceButton.addActionListener(this);
        centerPanel.add(checkBalanceButton);

        depositButton = new JButton("Depositar");
        depositButton.setFont(font);
        depositButton.addActionListener(this);
        centerPanel.add(depositButton);

        withdrawButton = new JButton("Retirar");
        withdrawButton.setFont(font);
        withdrawButton.addActionListener(this);
        centerPanel.add(withdrawButton);

        changePinButton = new JButton("Cambiar PIN");
        changePinButton = new JButton("Cambiar PIN");
        changePinButton.setFont(font);
        changePinButton.addActionListener(this);
        centerPanel.add(changePinButton);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 1));
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(southPanel, BorderLayout.SOUTH);

        exitButton = new JButton("Salir");
        exitButton.setFont(font);
        exitButton.addActionListener(this);
        southPanel.add(exitButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == checkBalanceButton) {
            atm.consultarSaldo();
            JOptionPane.showMessageDialog(this, "Tu balance actual es: $" + atm.saldo);
        } else if (e.getSource() == depositButton) {
            String input = JOptionPane.showInputDialog(this, "Ingrese la cantidad a depositar: $");
            double amount = Double.parseDouble(input);

            atm.realizarDeposito(amount);
            JOptionPane.showMessageDialog(this, "Deposito exitoso. Su nuevo balance es: $" + atm.saldo);

        } else if (e.getSource() == withdrawButton) {
            String input = JOptionPane.showInputDialog(this, "Ingrese el monto a retirar: $");
            double amount = Double.parseDouble(input);

            atm.realizarRetiro(amount);
            JOptionPane.showMessageDialog(this, "Retiro exitoso. Su nuevo balance es: $" + atm.saldo);
        } else if (e.getSource() == changePinButton) {
            String input = JOptionPane.showInputDialog(this, "Ingrese su PIN actual: ");
            int currentPin = Integer.parseInt(input);

            if (currentPin == atm.pinActual) {
                input = JOptionPane.showInputDialog(this, "Ingrese su nuevo PIN: ");
                int newPin = Integer.parseInt(input);
                input = JOptionPane.showInputDialog(this, "Confirme su nuevo PIN: ");
                int confirmPin = Integer.parseInt(input);

                if (newPin == confirmPin) {
                    atm.cambiarPIN(currentPin, newPin, confirmPin);
                    JOptionPane.showMessageDialog(this, "PIN actualizado exitosamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "PINs no coinciden.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "PIN incorrecto.");
            }
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }
}