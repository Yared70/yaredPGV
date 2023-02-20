import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class HiloCliente extends Thread {

    Server server;

    Socket client;

    BufferedReader bufferedReader;

    PrintWriter printWriter;

    String user;

    String nick;

    boolean logueado;

    boolean pingDisponible;

    public HiloCliente(Server server, Socket client) {
        this.server = server;
        this.client = client;

        try {

            this.bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.printWriter = new PrintWriter(client.getOutputStream(), true);

        } catch (IOException e) { }

        this.logueado = false;
        this.pingDisponible = true;
    }

    @Override
    public void run() {

        try {

            while (true) {
                String message = bufferedReader.readLine();

                if (message.startsWith("/login")) {

                    loguear(message);

                } else if (!logueado) {

                    printWriter.println("Inicia Sesión primero");

                } else {

                    if (!message.startsWith("/")) {

                        server.enviarTodos(nick + ": " + message);

                    } else if (message.startsWith("/msg")) {

                        enviarMensajePrivado(message);

                    } else if (message.startsWith("/nick")) {

                        cambiarNick(message);

                    } else if (message.startsWith("/quit")) {

                        cerrarSesion();

                    } else if (message.startsWith("/userlist")) {

                        comprobarUsuariosLogueados();

                    } else if (message.startsWith("/ping on")) {

                        pingDisponible = true;

                    } else if (message.startsWith("/ping off")) {

                        pingDisponible = false;

                    } else if (message.startsWith("/ping ")) {

                        enviarPing(message);

                    } else if (message.startsWith("/pong ")) {

                        enviarPong(message);

                    }
                }

            }

        } catch (Exception ex) { }
    }

    public void loguear(String message) {

        String[] messageSplit = message.split(" ");
        boolean loginCorrecto = server.logear(messageSplit[1], messageSplit[2], this);
        if (loginCorrecto) {

            logueado = true;
            user = messageSplit[1];
            nick = user;
            printWriter.println("Login correcto, Bienvenido");


        } else {

            printWriter.println("Usuario/contraseña incorrectas");

        }

    }

    public void enviarMensajePrivado(String message) {

        String[] messageSplit = message.split(" ");
        String destinatario = messageSplit[1];

        String mensaje = "Mensaje privado de " + nick + ": ";

        for (int i = 2; i < messageSplit.length; i++) {

            mensaje += messageSplit[i] + " ";

        }

        boolean envioCorrecto = server.enviarMensajePrivado(destinatario, mensaje, this);
        if(envioCorrecto)printWriter.println(mensaje);

        if (!envioCorrecto) {

            printWriter.println("El usuario " + destinatario + " no se ha logeado ");

        }

    }

    public void cambiarNick(String message) {

        String[] messageSplit = message.split(" ");
        String nuevoNick = messageSplit[1];

        boolean usuarioCambiadoCorrectamente = server.cambiarNick(user, nuevoNick, this);

        if (usuarioCambiadoCorrectamente) {

            server.enviarTodos("*** el usuario " + user + " es ahora conocido por " + nuevoNick);
            nick = nuevoNick;

        } else {

            printWriter.println("Ese usuario no esta disponible");

        }

    }

    public void cerrarSesion() {

        server.cerrarSesion(user, this);
        server.enviarTodos("*** el usuario " + nick + " ha abandonado el chat");
        try {
            client.close();
        } catch (IOException e) {

        }

    }

    public void comprobarUsuariosLogueados() {

        printWriter.println(server.comprobarUsuariosLogueados());

    }

    public void enviarPing(String message) {

        String[] messageSplit = message.split(" ");
        String destinatario = messageSplit[1];
        String uuid = messageSplit[2];

        if(!server.enviarPing(destinatario, uuid, nick)){

            printWriter.println("Ese usuario no existe");

        }


    }

    public void enviarPong(String message) {

        String[] messageSplit = message.split(" ");
        String destinatario = messageSplit[1];
        String uuid = messageSplit[2];

        server.enviarPong(destinatario, uuid, nick);

    }

    public boolean isPingDisponible() {
        return pingDisponible;
    }

}
