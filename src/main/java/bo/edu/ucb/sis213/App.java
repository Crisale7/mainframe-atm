package bo.edu.ucb.sis213;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class App {
    public static void main(String[] args) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        ATM atm = new ATM(databaseConnection);

        Scanner scanner = new Scanner(System.in);
        int intentos = 3;

        System.out.println("Bienvenido al Cajero Automático.");

        while (intentos > 0) {
            System.out.print("Ingrese su PIN de 4 dígitos: ");
            int pinIngresado = scanner.nextInt();
            if (atm.validarPIN(pinIngresado)) {
                mostrarMenu(atm);
                break;
            } else {
                intentos--;
                if (intentos > 0) {
                    System.out.println("PIN incorrecto. Le quedan " + intentos + " intentos.");
                } else {
                    System.out.println("PIN incorrecto. Ha excedido el número de intentos.");
                    System.exit(0);
                }
            }
        }
    }

    public static void mostrarMenu(ATM atm) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Consultar saldo.");
            System.out.println("2. Realizar un depósito.");
            System.out.println("3. Realizar un retiro.");
            System.out.println("4. Cambiar PIN.");
            System.out.println("5. Salir.");
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    atm.consultarSaldo();
                    break;
                case 2:
                    System.out.print("Ingrese la cantidad a depositar: $");
                    double deposito = scanner.nextDouble();
                    atm.realizarDeposito(deposito);
                    break;
                case 3:
                    System.out.print("Ingrese el monto a retirar: $");
                    double retiro = scanner.nextDouble();
                    atm.realizarRetiro(retiro);
                    break;
                case 4:
                    System.out.print("Ingrese su PIN actual: ");
                    int currentPin = scanner.nextInt();
                    if (currentPin != atm.pinActual) {
                        System.out.println("PIN incorrecto.");
                        break;
                    }
                    else {
                        System.out.print("Ingrese su nuevo PIN: ");
                        int newPin = scanner.nextInt();
                        System.out.print("Confirme su nuevo PIN: ");
                        int confirmPin = scanner.nextInt();
                        atm.cambiarPIN(currentPin, newPin, confirmPin);
                        break;
                    }
                case 5:
                    System.out.println("Gracias por usar el cajero. ¡Hasta luego!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }
}