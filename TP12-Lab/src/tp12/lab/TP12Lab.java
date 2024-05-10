/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tp12.lab;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author pablo
 */
public class TP12Lab {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try{
        // Establezco el driver
           Class.forName("org.mariadb.jdbc.Driver");
           String bd = "jdbc:mysql://localhost/construirsa2";
           String usuario = "root";
           String password = "";
           
        // Realizo la conexión con la base de datos 
           Connection con = DriverManager.getConnection(bd,usuario,password);
           PreparedStatement ps;
           
        // Inserto los empleados mediante un metodo
           
           insertarEmpleadoDB("111","Martinez","Gonzalo","1","1",con);
           insertarEmpleadoDB("222","Perez","Camila","1","1",con);
           insertarEmpleadoDB("333","Romano","Carla","1","0",con);
        
        // Inserto las herramientas mediante otro metodo
    
           insertarHerramientaDB("Martillo","Stanley","12","1",con);
           insertarHerramientaDB("Pinza","Stanley","10","1",con);
            

        // Listo todas las herramientas stock > 10
            int stock = 10;
        // Preparo la consulta con stock valor dinamico y se lo asigno posteriormente con setInt
            ps = con.prepareStatement("SELECT * FROM `herramienta` WHERE stock > ?");
            ps.setInt(1, stock);
        // Ejecuto la consulta con executeQuery por que es una sentencia SELECT
            ResultSet datos = ps.executeQuery();

            System.out.println("Herramientas con stock mayor a 10");
        // Mientras exista una fila siguiente
            while (datos.next()) {
                System.out.println("Nombre: " + datos.getString("nombre"));
                System.out.println("Descripcion: " + datos.getString("descripcion"));
                System.out.println("Stock: " + datos.getInt("stock"));
            }
        
        // Doy de baja el primer empleado o empleado con id 1
        ps = con.prepareStatement("UPDATE `empleado` SET `estado`= 0 WHERE id_empleado = 1");
        ps.executeUpdate(); 
           
        }catch (ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar el driver");
        } catch (SQLException ex) {
            int codigoE=ex.getErrorCode();
            if(codigoE==1062){
                  JOptionPane.showMessageDialog(null, "Entrada Duplicada");
            } else if(codigoE==1049){
            JOptionPane.showMessageDialog(null, "BD Desconocida");
            }else{
                  JOptionPane.showMessageDialog(null, "Error ");
            }
            
            ex.printStackTrace();
            System.out.println("codigo de error "+ex.getErrorCode());
        }
    }
    
    public static void insertarEmpleadoDB(String dni,String apellido,String nombre,String acceso,String estado,Connection con) throws SQLException{
        // Indico que el metodo puede tirar una excepcion SQLException y que la maneje el main
        // Establezco como va a ser la consulta con los datos recibidos por parametro
        String sql = "INSERT INTO "
                   + "empleado(dni, apellido, nombre, acceso, estado) "
                   + "VALUES ("+ dni +",'"+apellido+"','"+nombre+"',"+acceso+","+estado+")"; 
        
        // Preparo la consulta
        PreparedStatement ps = con.prepareStatement(sql);
        
        // La ejecuto con executeUpdate por que es un INSERT
        int filas = ps.executeUpdate();
        
        if (filas > 0) {
            System.out.println("Se ha agregado el empleado: " + nombre + " " + apellido);
        }
    }
    
    public static void insertarHerramientaDB(String nombre, String descripcion, String stock, String estado,Connection con) throws SQLException{ 
        // IDEM metodo insertarEmpleado
        String sql = "INSERT INTO "
                   + "herramienta(nombre, descripcion, stock, estado) "
                   + "VALUES ('"+nombre+"','"+descripcion+"',"+stock+","+estado+")"; 
        
        PreparedStatement ps = con.prepareStatement(sql);
        int filas = ps.executeUpdate();
        if (filas > 0) {
            System.out.println("Se ha agregado la herramienta: " + nombre);
        }
    }
 
}
