package tienda_db;
import java.util.Scanner;
import java.sql.*;

public class Tienda_DB
{
    Scanner lector = new Scanner(System.in);
    
    public int menu()
    {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n            TiendaSoft V 2.0");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("Seleccione la opción:\n ");
        System.out.println("[1] Agregar producto");
        System.out.println("[2] Buscar producto");
        System.out.println("[3] Eliminar producto");
        System.out.println("[4] Mostrar inventario");
        System.out.println("[5] Realizar venta");
        System.out.println("[6] Mostrar ganancias totales");
        System.out.println("[7] Salir");        
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        return lector.nextInt();        
    }   
    
    public String validar(String pregunta)
    {
        String str = "";
        while(str.equals(""))
        {
            System.out.println(pregunta);
            str = lector.nextLine();
            if(!str.matches("^[A-Za-z ]*$")) //Si no es caracter, "[0-9]*" para número
                str = "";
            
        }
        return str;
    }
            
    public static void main(String[] args)
    {
        Scanner lector = new Scanner(System.in);
        boolean in=true,hay=false;
        String url = "jdbc:mysql://localhost/tienda";
        String user = "root";
        String password = "kalandro0414";
        String objetivo, nombre="", cantidad="", valor="", cant_ven="";
        double cantidadd=0, cant_vend=0, valord=0;
        int ID=0;
        
        Tienda_DB tiendav2 = new Tienda_DB();
        
        try{     
            System.out.println("Conectando con base de datos...");
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url,user,password);//objeto tipo connection jbdc = java database connection
            System.out.println("Conexión exitosa");
            Statement estado = con.createStatement();
            ResultSet resultado;
            
             while(in)
             {        
                switch(tiendav2.menu())
                {
                    case 1: //Agregar
                        System.out.println("~~~~~~~~~~~~~ Agregando nuevo producto ~~~~~~~~~~~~~");
                        System.out.print("Ingrese el nombre del producto: ");
                        nombre = lector.nextLine();                               
                        resultado = estado.executeQuery("SELECT * FROM `productos` WHERE `nombre` LIKE '"+nombre+"'");                        
                        while (resultado.next())
                        {
                            System.out.println("Este producto ya existe");
                            System.out.println("Producto:  "+resultado.getString("nombre")+"\t\tcantidad:  "
                            +resultado.getString("cantidad")+"\t\tvalor:  "+ resultado.getString("valor"));
                            ID = resultado.getInt("ID");
                            hay = true;
                        }
                        if(hay)
                        {
                            System.out.print("¿Desea reemplazarlo? (s/n): ");
                            switch(lector.nextLine())
                            {
                                case "s":
                                    System.out.print("Ingrese la cantidad: ");
                                    cantidad = lector.nextLine();
                                    System.out.print("Ingrese el valor unitario: ");
                                    valor = lector.nextLine();      
                                    estado.executeUpdate("UPDATE `productos` SET `nombre` = '"+nombre+"',`cantidad` = '"+cantidad+"', `valor` = '"+valor+"' WHERE `productos`.`ID` = "+ID+""); 
                                    System.out.println("Producto reeemplazado");
                                    break;
                                
                                case "n":
                                    break;
                                    
                                default:
                                    System.out.println("Ha ingresado una opción incorrecta");                         
                            }   
                            hay = false;
                        }  
                        else                       
                        {
                            System.out.print("Ingrese la cantidad: ");
                            cantidad = lector.nextLine();
                            System.out.print("Ingrese el valor unitario: ");
                            valor = lector.nextLine(); 
                            estado.executeUpdate("INSERT INTO `productos` VALUES (NULL, '"+nombre+"','"+cantidad+"','"+valor+"')");
                            System.out.println("Producto agregado");
                        }
                        
                       break;
                    
                    case 2: //Buscar
                        System.out.println("~~~~~~~~~~~~~ Búsqueda de producto ~~~~~~~~~~~~~");
                        System.out.println("Escriba el nombre del producto que busca: ");                        
                        resultado = estado.executeQuery("SELECT * FROM `productos` WHERE `nombre` LIKE '"+lector.nextLine()+"'");
                        
                        while (resultado.next())
                        {
                            System.out.println("Producto encontrado:  "+resultado.getString("nombre")+"\t\tcantidad:  "
                                    +resultado.getString("cantidad")+"\t\tvalor:  "+ resultado.getString("valor"));
                            hay = true;
                        }
                        if(!hay)
                            System.out.println("El producto buscado no existe"); 
                        else
                            hay = false;
                        break;
                                          
                    case 3: //Eliminar
                        System.out.println("~~~~~~~~~~~~~ Eliminación de producto ~~~~~~~~~~~~~");
                        System.out.println("Escriba el nombre del producto que desea eliminar");
                        objetivo = lector.nextLine();
                        resultado = estado.executeQuery("SELECT * FROM `productos` WHERE `nombre` LIKE '"+objetivo+"'");
                        while (resultado.next())
                        {
                            System.out.println("Producto encontrado:  "+resultado.getString("nombre")+"\t\tcantidad:  "
                                    +resultado.getString("cantidad")+"\t\tvalor unitario:  "+ resultado.getString("valor"));
                            hay = true;                                                                                                                                                                   
                        }
                        if(hay)
                        {
                            System.out.print("¿Desea eliminarlo? (s/n): ");
                            switch(lector.nextLine())
                            {
                                case "s":
                                    estado.executeUpdate("DELETE FROM `productos` WHERE `nombre` LIKE '"+objetivo+"'");
                                    System.out.println("Producto eliminado");
                                    break;
                                
                                case "n":
                                    break;
                                    
                                default:
                                    System.out.println("Ha ingresado una opción incorrecta");                         
                            }   
                            hay = false;
                        }                             
                        else
                            System.out.println("El producto buscado no existe");
                                                                               
                        break;

                    case 4: //Inventario 
                        System.out.println("~~~~~~~~~~~~~ Inventario de productos ~~~~~~~~~~~~~");                                             
                        resultado = estado.executeQuery("SELECT * FROM `productos`");
                        while(resultado.next())
                        {
                            System.out.println("Producto:  "+resultado.getString("nombre")+"\t\tcantidad:  "
                                    +resultado.getString("cantidad")+"\t\tvalor unitario:  "+ resultado.getString("valor"));
                            hay = true;
                        }
                        if(!hay)
                            System.out.println("No hay productos en inventario"); 
                        else
                            hay = false;
                        break;

                    case 5: //Venta
                        System.out.println("~~~~~~~~~~~~~ Venta de productos ~~~~~~~~~~~~~");                                             
                        System.out.print("Escriba el nombre del producto que desea vender: ");
                        resultado = estado.executeQuery("SELECT * FROM `productos` WHERE `nombre` LIKE '"+lector.nextLine()+"'");
                        
                         while (resultado.next())
                        {
                            nombre = resultado.getString("nombre");
                            cantidad = resultado.getString("cantidad");                            
                            valor = resultado.getString("valor");    
                            ID = resultado.getInt("ID");
                            System.out.println("Producto a vender:  "+nombre+"\t\tcantidad:  "+cantidad+"\t\tvalor:  "+valor);
                            hay = true;
                            cantidadd = Double.parseDouble(cantidad);
                        }
                        if(!hay)
                            System.out.println("El producto buscado no existe"); 
                        else
                        {
                            System.out.print("Ingrese la cantidad que desea vender: ");
                            cant_ven = lector.nextLine();
                            cant_vend = Double.parseDouble(cant_ven);
                            valord = Double.parseDouble(valor);
                                                        
                            if(cant_vend>cantidadd)
                                System.out.println("No hay unidades suficientes para la venta");
                            else
                            {
                                cantidadd = cantidadd-cant_vend;
                                cantidad = Double.toString(cantidadd);
                                estado.executeUpdate("UPDATE `productos` SET `cantidad` = '"+cantidad+"' WHERE `productos`.`ID` = "+ID+""); 
                                
                                //Actualizando registro de ventas
                               /* Statement estado2 = con.createStatement();
                                ResultSet resultado2;
                                resultado2 = estado2.executeQuery("SELECT * FROM `ventas` WHERE `producto` LIKE '"+nombre+"'");                        
                                while (resultado2.next())
                                {
                                    ID = resultado2.getInt("ID");
                                    cantidad = resultado2.getString("cant_vendidos");
                                    cantidadd = Double.parseDouble(cantidad);
                                    hay = true;
                                }
                                
                                if(!hay)
                                {
                                    valor = Double.toString(cant_vend*valord);
                                    estado2.executeUpdate("INSERT INTO `ventas` VALUES (NULL, '"+nombre+"','"+cant_ven+"','"+valor+"')");
                                }
                                else
                                {
                                    hay = false;
                                    cantidad = Double.toString(cant_vend+cantidadd);
                                    valor = Double.toString((cant_vend+cantidadd)*valord);
                                    estado2.executeUpdate("UPDATE `ventas` SET `cant_vendidos` = '"+cantidad+"',`valor_total` = '"+valor+"'  WHERE `productos`.`ID` = "+ID+""); 
                                    
                                }*/
                                System.out.println("Vendido");
                            }
                        }
                        break;
                      
                    case 6: //Ganancias
                        System.out.println("~~~~~~~~~~~~~~~~~ Ganancias ~~~~~~~~~~~~~~~~~");                                             
                        System.out.println("Not yet");
                       
                        break;

                    case 7:
                        System.out.println("¡Hasta luego!");
                        in = false;
                        break;

                    default:
                        System.out.println("Ha ingresado un valor incorrecto");                      
                }
            }
            
       
            
            
            
            
            
       }catch(SQLException ex)
                {
                System.out.println("Error de mysql");
                }
        catch(Exception e)
        {
            System.out.println("Se ha detectado un error de tipo: "+e.getMessage());
        }           
    }
    
}
