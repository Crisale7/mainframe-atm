package bo.edu.ucb.sis213;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ATM {
    public int usuarioId;
    public double saldo;
    public int pinActual;

    private DatabaseConnection databaseConnection;

    public ATM(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public boolean validarPIN(int pin) {
        String query = "SELECT id, saldo, pin FROM usuarios WHERE pin = " + pin;
        ResultSet resultSet = databaseConnection.executeQuery(query);

        try {
            if (resultSet.next()) {
                usuarioId = resultSet.getInt("id");
                saldo = resultSet.getDouble("saldo");
                pinActual = resultSet.getInt("pin");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void consultarSaldo() {
        System.out.println("Su saldo actual es: $" + saldo);
    }

    public void realizarDeposito(double amount) {
        if (amount <= 0) {
            System.out.println("Cantidad no válida.");
        } else {
            saldo += amount;
            actualizarSaldoEnBD(amount, "deposito");

            System.out.println("Depósito realizado con éxito. Su nuevo saldo es: $" + saldo);
        }
    }

    public void realizarRetiro(double amount) {
        if (amount <= 0) {
            System.out.println("Cantidad no válida.");
        } else if (amount > saldo) {
            System.out.println("Saldo insuficiente.");
        } else {
            saldo -= amount;
            actualizarSaldoEnBD(amount, "retiro");

            System.out.println("Retiro realizado con éxito. Su nuevo saldo es: $" + saldo);
        }
    }

    public void cambiarPIN(int currentPin, int newPin, int confirmPin) {
        if (validarPIN(currentPin)) {
            if (newPin == confirmPin) {
                pinActual = newPin;
                actualizarPINEnBD();

                System.out.println("PIN actualizado con éxito.");
            } else {
                System.out.println("Los PINs no coinciden.");
            }
        } else {
            System.out.println("PIN incorrecto.");
        }
    }
    private void actualizarSaldoEnBD(double cantidad, String tipoOperacion) {
        // Actualizar el saldo en la base de datos en la tabla historicos
        String query = "INSERT INTO historico (usuario_id, tipo_operacion, cantidad, fecha) VALUES (" + usuarioId + ", '" + tipoOperacion + "', " + cantidad + ", NOW())";
        databaseConnection.executeUpdate(query);

        // Actualizar el saldo en la base de datos en la tabla usuarios
        query = "UPDATE usuarios SET saldo = " + saldo + " WHERE id = " + usuarioId;
        databaseConnection.executeUpdate(query);
    }

    private void actualizarPINEnBD() {
        // Actualizar el PIN en la base de datos en la tabla usuarios
        String query = "UPDATE usuarios SET pin = " + pinActual + " WHERE id = " + usuarioId;
        databaseConnection.executeUpdate(query);
    }
    }
