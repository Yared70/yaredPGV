/*** @author Javier Criado, 02/01/2023:18:58:28  ***/
public class BDUsuarios {
    private static java.util.Map<String,String> bdUsuarios=new java.util.TreeMap<>();
    static {
        bdUsuarios.put("Pepe", "pepe23");
        bdUsuarios.put("Pepa", "pepa23");
        bdUsuarios.put("Luis","luis23");
        bdUsuarios.put("Luisa","luisa23");
        bdUsuarios.put("Juan","juan23");
        bdUsuarios.put("Juana","juana23");
        bdUsuarios.put("Anton","anton23");
        bdUsuarios.put("Antonia","antonia23");
        bdUsuarios.put("Mario","mario23");
        bdUsuarios.put("María","maría23");
        bdUsuarios.put("Paco","paco23");
        bdUsuarios.put("Paqui","paqui23");
    }
    public static void listaUsuarios() {
        for(String usuario:bdUsuarios.keySet()) {
            System.out.println(usuario);
        }
    }

    public static boolean comprobarUsuario(String user){

        return bdUsuarios.containsKey(user);

    }

    public static void cambiarUsuario(String userActual, String nuevoUser){

        String password = bdUsuarios.get(userActual);
        bdUsuarios.remove(userActual);
        bdUsuarios.put(nuevoUser, password);

    }

    public static boolean verificarPass(String usuario,String pass) {
        return pass.equals(bdUsuarios.get(usuario));
    }
}
