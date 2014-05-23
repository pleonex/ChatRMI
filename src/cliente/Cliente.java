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

package cliente;

import comun.ClienteListener_I;
import comun.Servidor_I;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Cliente de chat.
 * 
 * @author Benito Palacios Sánchez
 */
public class Cliente extends UnicastRemoteObject implements ClienteListener_I {
    /** Nombre con el que se ha registrado el servidor en el registro. */
    private static final String SERVIDOR_KEYWORD = "servidor";
    
    /** Nombre del usuario. */
    private final String usuario;
    
    /** Interfaz para interactuar con el servidor. */
    private Servidor_I servidor;
    
    /** Callback a donde se enviará los mensajes recibidos. */
    private final ClienteListener_I listener;
    
    /**
     * Crea una nueva instancia de la clase.
     * 
     * @param host Host del registro.
     * @param port Puerto del registro.
     * @param usuario Nombre de usuario.
     * @param listener Callback a donde se enviarán los mensajes recibidos.
     * @throws RemoteException 
     */
    public Cliente(final String host, final int port, final String usuario,
            final ClienteListener_I listener) throws RemoteException {
        this.listener = listener;
        this.usuario  = usuario;
        this.servidor = Connect(host, port, usuario, this);
    }
    
    /**
     * Comprueba si el cliente está conectado con el servidor.
     * 
     * @return Indica si está conectado con el servidor o no.
     */
    public boolean estaConectado() {
        return this.servidor != null;
    }
    
    /**
     * Conecta con el servidor.
     * 
     * @param host Host del registro.
     * @param port Puerto del registro.
     * @param usuario Nombre de usuario.
     * @param listener Callback a donde se enviarán los mensajes.
     * @return Devuelve la interfaz para interactuar con el servidor.
     */
    private static Servidor_I Connect(final String host, final int port, 
            final String usuario, final ClienteListener_I listener) {
        // Instalo el gestor de seguridad
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());
        
        Servidor_I servidor = null; 
        try {
            // Obtiene el registro
            Registry registro = LocateRegistry.getRegistry(host, port);
            System.out.println("[CONECTA] Registro obtenido con éxito");
            
            // Obtiene el objeto del servidor
            servidor = (Servidor_I)registro.lookup(SERVIDOR_KEYWORD);
            System.out.println("[CONECTA] Servidor obtenido con éxito");

            // Llama al método y me registro en el servidor
            servidor.registra(usuario, listener);
            System.out.println("[CONECTA] Usuario registrado");
        } catch (RemoteException | NotBoundException ex) {
            System.err.println("[CONECTA] ERROR: " + ex.getMessage());
        }
        
        return servidor;
    }
    
    @Override
    public void recibe(final String mensaje, final String usuario)
            throws RemoteException {
        System.out.println("[RECIBIDO]" + usuario);
        if (this.listener != null)
            this.listener.recibe(mensaje, usuario);
    }
    
    /**
     * Desconecta al usuario del servicio.
     */
    public void desconecta() {
        try {
            System.out.println("[DESCONECTA]");
            if (this.estaConectado()) {
                this.servidor.desconecta(this.usuario);
                this.servidor = null;
            }
        } catch (RemoteException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    /**
     * Envía un mensaje a los demás usuario del chat.
     * 
     * @param mensaje Mensaje a enviar.
     */
    public void envia(final String mensaje) {
        try {
            System.out.println("[ENVÍA]");
            if (this.estaConectado())
                this.servidor.envia(mensaje, this.usuario);
        } catch (RemoteException ex) {
            System.err.println(ex.getMessage());
        }
    }
}
