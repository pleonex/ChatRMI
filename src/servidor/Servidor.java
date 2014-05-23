/*
 * Copyright (C) 2014 Benito Palacios Sánchez
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package servidor;

import comun.ClienteListener_I;
import comun.Servidor_I;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase con el funcionamiento de un servidor de chat.
 * 
 * @author Benito Palacios Sánchez
 */
public class Servidor extends UnicastRemoteObject implements Servidor_I {
    /** Nombre con el que se registrará este servidor en el registro. */
    private static final String SERVIDOR_KEYWORD = "servidor";
    
    /** Mapa entre nombre de usuario y clase para interactuar con el cliente. */
    private final Map<String, ClienteListener_I> listeners;
    
    /**
     * Crea una nueva instancia del servidor.
     * 
     * @throws RemoteException 
     */
    public Servidor() throws RemoteException {
        super();
        this.listeners = new HashMap<>();
    }
    
    /**
     * Inicia el servidor.
     * 
     * @param args Nombre del host y puerto del registro.
     */
    public static void main(String args[]) {
         if (args.length != 2) {
             System.out.println("USO: java servidor/Servidor servidor puerto");
             return;
         }
         
         String host = args[0];
         int puerto = Integer.parseInt(args[1]);
                 
        // Instala el gestor de seguridad
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
               
        try {
            // Creo el objeto servidor
            Servidor server = new Servidor();
              
            // Obtiene el registro de la dirección dada.
            Registry registro = LocateRegistry.getRegistry(host, puerto);
            System.out.println("[INICIO] Registro obtenido con éxito");
            
            // Mete el servidor en el registro
            registro.rebind(SERVIDOR_KEYWORD, server);
            System.out.println("[INICIO] Clase subida con éxito");
        } catch (RemoteException ex) {
            System.out.println("[INICIO] ERROR: " + ex.getMessage());
        }
    }
    
    @Override
    public void registra(final String usuario, final ClienteListener_I listener)
            throws RemoteException {
        System.out.println("[REGISTRO] " + usuario);
        this.listeners.put(usuario, listener);
    }
    
    @Override
    public void envia(final String mensaje, final String usuario) 
            throws RemoteException {
        System.out.println("[ENVIANDO] " + usuario);
        
        // Le envío el mensaje a todos los clientes excepto al que envía.
        for (String listUser : this.listeners.keySet()) {
            if (!listUser.equals(usuario))
                this.listeners.get(listUser).recibe(mensaje, usuario);
        }
    }
    
    @Override
    public void desconecta(final String usuario) throws RemoteException {
        System.out.println("[DESCONECTANDO] " + usuario);
        if (this.listeners.containsKey(usuario))
            this.listeners.remove(usuario);
    }
}
