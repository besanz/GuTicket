package rmi.remote.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import data.entidades.*;
import rmi.server.exceptions.InvalidUser;

public interface IUserService extends Remote {
    User loginUser(String login, String password) throws RemoteException;
    User registerUser(String dni, String nombre, String apellidos, String email, String password) throws RemoteException, InvalidUser;

    List<Artista> obtenerArtistas() throws RemoteException;
    List<Evento> obtenerEventos() throws RemoteException;
    Evento obtenerEventoPorID(int id) throws RemoteException;
    List<Evento> obtenerEventosDestacados() throws RemoteException;
    List<Espacio> obtenerEspaciosDeEvento(int eventoId) throws RemoteException;
}