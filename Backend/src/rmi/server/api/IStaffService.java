package rmi.server.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import entidades.Staff;

public interface IStaffService extends Remote {
    
    /**
     * Función para crear un nuevo miembro del personal
     * @param nombre
     * @param cargo
     * @return StaffMember
     * @throws RemoteException
     */
    Staff crearStaffMember(String nombre, String cargo) throws RemoteException;

    /**
     * Función para obtener la lista de todos los miembros del personal
     * @return List<StaffMember>
     * @throws RemoteException
     */
    List<Staff> obtenerStaffMembers() throws RemoteException;

    /**
     * Función para actualizar la información de un miembro del personal
     * @param id
     * @param nombre
     * @param cargo
     * @return StaffMember
     * @throws RemoteException
     */
    Staff actualizarStaffMember(int id, String nombre, String cargo) throws RemoteException;

    /**
     * Función para eliminar a un miembro del personal
     * @param id
     * @return boolean
     * @throws RemoteException
     */
    boolean eliminarStaffMember(int id) throws RemoteException;
}