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

package comun;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaz para interactuar con el servidor de chat.
 * 
 * @author Benito Palacios Sánchez
 */
public interface Servidor_I extends Remote {
    /**
     * Registra un usuario en el servicio.
     * 
     * @param usuario Nombre de usuario.
     * @param listener Interfaz para interactuar con el cliente.
     * @throws RemoteException 
     */
    public void registra(final String usuario, final ClienteListener_I listener) throws RemoteException;
    
    /**
     * Envía un mensaje a los demás clientes.
     * 
     * @param mensaje Mensaje a enviar.
     * @param usuario Usuario que envía el mensaje. Esto presenta un fallo de
     * seguridad pues un usuario se puede hacer pasar por otro poniendo su nombre
     * aquí con lo que se conseguiría que esa persona no vea el mensaje. Una
     * solución será enviar la interfaz ClienteListener_I en lugar del nombre o
     * utilizar contraseñas.
     * @throws RemoteException 
     */
    public void envia(final String mensaje, final String usuario) throws RemoteException;
    
    /**
     * Desconecta a un usuario del sistema.
     * 
     * @param usuario Nombre de usuario a desconectar. Esto presenta un fallo de
     * seguridad pues un usuario podría desconectar a otro del sistema. Una
     * solución sería enviar ClienteListener_I en lugar del nombre, o utilizar
     * contraseñas.
     * @throws RemoteException 
     */
    public void desconecta(final String usuario) throws RemoteException;
}
