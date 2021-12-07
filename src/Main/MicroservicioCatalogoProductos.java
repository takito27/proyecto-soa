package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MicroservicioCatalogoProductos {

    protected static Scanner sc = new Scanner(System.in);

    protected static String BD = "jdbc:postgresql://localhost:5433/Productos_SOA";
    protected static String usuario = "postgres";
    protected static String contra = "1234";

    protected static Statement statement = null;
    protected static boolean connectionFlag = false;
    protected static Connection connection;

    protected static String id_ultimo = "";

    protected static boolean menuFlag = true;
    protected static int menu = 0;

    public static void main(String[] args) {
        connectionFlag = conectar();
        if (connectionFlag) {
            do {
                try {
                    System.out.println("Seleccione la operacion deseada");
                    System.out.println("1- Alta");
                    System.out.println("2- Baja");
                    System.out.println("3- Consulta");
                    System.out.println("4- Salir\n");
                    menu = sc.nextInt();
                    switch (menu) {
                        case 1:
                            boolean flagI = true;
                            do {
                                System.out.println("\n----ALTAS DE PRODUCTOS----");
                                if (insertar() != 1) {
                                    flagI = false;
                                }
                            } while (flagI);
                            break;
                        case 2:
                            boolean flagD = true;
                            do {
                                System.out.println("\n----BAJAS DE PRODUCTOS----");
                                if (borrar() != 1) {
                                    flagD = false;
                                }
                            } while (flagD);
                            break;
                        case 3:
                            consulta();
                            break;
                        case 4:
                            System.out.println("***Saliendo y cerrando conexion***");
                            connection.close();
                            menuFlag = false;
                            break;
                        default:
                            System.err.println("Seleccione una operacion valida\n");
                            break;
                    }
                } catch (SQLException ex) {
                    System.err.println("ERROR: " + ex + "\n");
                }
            } while (menuFlag);
        }
    }

    public static boolean conectar() {
        try {
            connection = (Connection) DriverManager.getConnection(BD, usuario, contra);
            System.out.println("CONEXION A BASE DE DATOS EXITOSA\n");
            return true;
        } catch (Exception e) {
            System.err.println("ERROR: " + e + "\n");
            return false;
        }
    }

    public static int insertar() {
        int op = 0;
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT MAX(id_producto) FROM " + "\"" + "Productos" + "\"");
            while (rs.next()) {
                id_ultimo = rs.getString(1);
            }
            int id = Integer.parseInt(id_ultimo) + 1;
            System.out.println("- Inserte nombre del producto");
            sc.nextLine();
            String nombre = sc.nextLine();
            System.out.println("- Inserte el precio del producto");
            String precio = sc.nextLine();
            System.out.println("- Inserte descripcion del producto");
            String descripcion = sc.nextLine();
            String sql = "INSERT INTO" + "\"" + "Productos" + "\""
                    + "(id_producto, nombre_producto, precio_producto, descripcion_producto) "
                    + "VALUES (" + id + ", '" + nombre + "'," + Float.parseFloat(precio) + ",'" + descripcion + "')";
            statement.executeUpdate(sql);
            statement.close();
            System.out.println("\n----ALTA DE PRODUCTO EXITOSA----\n");
            System.out.println("---¿Desea insertar otro producto?---");
            System.out.println("1- SI");
            System.out.println("2- NO\n");
            op = sc.nextInt();
            System.out.println("");
        } catch (SQLException ex) {
            System.err.println("ERROR: " + ex + "\n");
        }
        return op;
    }

    public static int borrar() {
        int ret = 0;
        try {
            System.out.println("Seleccione el metodo de borrar:");
            System.out.println("1- ID");
            System.out.println("2- Nombre\n");
            int op = sc.nextInt();
            String sql = "";
            switch (op) {
                case 1:
                    System.out.println("\n- Introduzca el id del producto: ");
                    int idBorrar = sc.nextInt();
                    statement = connection.createStatement();
                    sql = "DELETE FROM public." + "\"" + "Productos" + "\""
                            + "WHERE id_producto = " + idBorrar;
                    statement.executeUpdate(sql);
                    statement.close();
                    break;
                case 2:
                    System.out.println("\n- Introduzca el nombre del producto: ");
                    sc.nextLine();
                    String nameBorrar = sc.nextLine();
                    statement = connection.createStatement();
                    sql = "DELETE FROM public." + "\"" + "Productos" + "\""
                            + "WHERE nombre_producto = '" + nameBorrar + "'";
                    statement.executeUpdate(sql);
                    statement.close();
                    break;
                default:
                    System.err.println("Seleccione una operacion valida\n");
                    break;
            }
            System.out.println("\n----BAJA DE PRODUCTO EXITOSA----\n");
            System.out.println("---¿Desea borrar otro producto?---");
            System.out.println("1- SI");
            System.out.println("2- NO\n");
            ret = sc.nextInt();
            System.out.println("");
        } catch (SQLException ex) {
            System.err.println("ERROR: " + ex + "\n");
        }
        return ret;
    }

    public static void consulta() {
        try {
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM public." + "\"" + "Productos" + "\"");
            System.out.println("");
            while (rs.next()) {
                System.out.print("ID: " + rs.getString(1));
                System.out.print(" || Nombre: " + rs.getString(2));
                System.out.print(" || Precio: " + rs.getString(3));
                System.out.print(" || Descripcion: " + rs.getString(4) + "\n");
            }
            statement.close();
            System.out.println("\n----CONSULTA DE PRODUCTO EXITOSA----\n");
            System.out.println("");
        } catch (SQLException ex) {
            System.err.println("ERROR: " + ex + "\n");
        }
    }
}
