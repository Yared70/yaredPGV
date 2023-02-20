import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static java.lang.Thread.sleep;

class Server {

    List<HiloCliente> clienteList;
    HashMap<String, HiloCliente> usuariosLogeados;

    String dirIp;
    Integer port;

    public Server(String dirIp, Integer port) {
        this.clienteList = new ArrayList<>();
        this.usuariosLogeados = new HashMap<>();
        this.dirIp = dirIp;
        this.port = port;
    }

    public void ejecutar() {

        ServerSocket serverSocket = null;
        try {

            serverSocket = new ServerSocket(port);
            System.out.println("Servidor iniciado en el puerto 6667");

            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Nueva conexion");
                HiloCliente cliente = new HiloCliente(this, client);
                clienteList.add(cliente);
                cliente.start();
            }

        } catch (IOException e) { }



    }

    public void enviarTodos(String mensaje) {

        usuariosLogeados.forEach((user, hilo) -> {

            hilo.printWriter.println(mensaje);

        });

    }

    public boolean enviarMensajePrivado(String destinatario, String mensaje, HiloCliente hilo) {

        if (usuariosLogeados.containsKey(destinatario)) {

            HiloCliente hiloDestinatario = usuariosLogeados.get(destinatario);

            hiloDestinatario.printWriter.println(mensaje);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String dateFormat = sdf.format(date);

            String nickDestinatario = hiloDestinatario.nick;
            String usuarioEmisor = hilo.user;
            String nickEmisor = hilo.nick;

            String respuesta = dateFormat + " msg origen (" + usuarioEmisor + ", " + nickEmisor + ") destinatario " +
                    "(" + destinatario + ", " + nickDestinatario + ") " + mensaje;
            grabarLog(respuesta);

            return true;

        } else {

            return false;

        }

    }

    public boolean logear(String usuario, String password, HiloCliente hilo) {

        if(BDUsuarios.verificarPass(usuario, password)){

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String dateFormat = sdf.format(date);

            InetAddress ip = hilo.client.getLocalAddress();
            String dirIp = ip.toString().replace("/", "");
            usuariosLogeados.put(usuario, hilo);

            String usuariosLogueados = comprobarUsuariosLogueados();

            usuariosLogeados.forEach( (userConectado, hiloUser) -> hiloUser.printWriter.println(usuariosLogueados) );

            String mensajeLog = (dateFormat + " " + usuario + " conectado desde " + dirIp);

            grabarLog(mensajeLog);

            return true;

        }else{
            return false;
        }

    }

    public boolean cambiarNick(String usuarioActual, String usuarioNuevo, HiloCliente hiloCliente) {

        boolean comprobarUsuarioOcupado = BDUsuarios.comprobarUsuario(usuarioNuevo);
        if (!comprobarUsuarioOcupado) {
            usuariosLogeados.remove(usuarioActual);
            usuariosLogeados.put(usuarioNuevo, hiloCliente);

            return true;

        } else {

            return false;

        }

    }

    public boolean cerrarSesion(String user, HiloCliente hiloCliente) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateFormat = sdf.format(date);

        InetAddress ip = hiloCliente.client.getLocalAddress();
        String dirIp = ip.toString().replace("/", "");

        String mensajeLog = (dateFormat + " " + user + " se ha desconectado desde " + dirIp);

        grabarLog(mensajeLog);

        clienteList.remove(hiloCliente);
        usuariosLogeados.remove(user);
        return true;

    }

    public String comprobarUsuariosLogueados() {

        StringBuilder respuesta = new StringBuilder("Usuarios conectados: ");

        usuariosLogeados.keySet().forEach(usuarioConectado -> respuesta.append(usuarioConectado + " "));

        return respuesta.toString();

    }

    public boolean enviarPing(String destinatario, String uuid, String emisor) {

        if(!usuariosLogeados.containsKey(destinatario)) {
             return false;
        }

            if (usuariosLogeados.get(destinatario).isPingDisponible()) {

                String message = "/ping " + emisor + " " + uuid;
                usuariosLogeados.get(destinatario).printWriter.println(message);
                return true;

            } else {

                usuariosLogeados.get(emisor).printWriter.println("*** el usuario " + destinatario + " tiene el ping deshabilitado");
                return true;

            }


    }

    public void enviarPong(String destinatario, String uuid, String emisor) {

        if (usuariosLogeados.get(destinatario).isPingDisponible()) {

            String message = "/pong " + emisor + " " + uuid;
            usuariosLogeados.get(destinatario).printWriter.println(message);

        } else {

            usuariosLogeados.get(emisor).printWriter.println("*** el usuario " + destinatario + " tiene el ping deshabilitado");

        }

    }

    public void grabarLog(String mensaje){

        File archivo = new File("/tmp/chatLog.txt");

        try ( BufferedWriter bw = Files.newBufferedWriter(archivo.toPath(),
                StandardOpenOption.APPEND,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE

        )) {

            bw.write(mensaje + "\n");

        } catch (IOException ex) {
            ex.printStackTrace();
        }



    }

}
