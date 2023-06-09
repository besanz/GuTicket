package gui;

import data.entidades.Evento;
import data.entidades.Espacio;
import data.entidades.Precio;
import data.entidades.User;

import remote.IFacadeUser;
import remote.api.paypal.PaypalService;
import java.io.IOException;
import java.rmi.RemoteException;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import controller.UserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicketDetail extends JFrame {
    private final Evento evento;
    private final Espacio espacio;
    private final Precio precio;
    private final User user;
    private final UserController userController;
    private IFacadeUser remoteFacade;
    private PaypalService paypalService;


    public TicketDetail(Evento evento, Espacio espacio, Precio precio, User user, UserController userController) {
        this.evento = evento;
        this.espacio = espacio;
        this.precio = precio;
        this.user = user;
        this.userController = userController;
        this.remoteFacade = userController.getRemoteFacade();
        this.paypalService = new PaypalService();
        initComponents();
    }

    private void initComponents() {
        setTitle("GuTicket - Compra");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ALUMNO\\Desktop\\GuTicket\\FrontendUser\\src\\gui\\element\\gu.png"));

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setLayout(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(4, 1));
        infoPanel.setBackground(new Color(230, 230, 230));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel eventLabel = new JLabel("Evento: " + evento.getTitulo());
        eventLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JLabel dateLabel = new JLabel("Fecha: " + evento.getFecha());
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel venueLabel = new JLabel("Lugar: " + espacio.getNombre());
        venueLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel priceLabel = new JLabel("Precio: $" + precio.getValor());
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        infoPanel.add(eventLabel);
        infoPanel.add(dateLabel);
        infoPanel.add(venueLabel);
        infoPanel.add(priceLabel);

        JButton buyButton = new JButton("Comprar Ahora");
        buyButton.setBackground(new Color(114, 137, 218));
        buyButton.setForeground(Color.WHITE);
        buyButton.setFont(new Font("Arial", Font.BOLD, 16));
        buyButton.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == buyButton) {
                    // Mostrar el cuadro de dialogo de confirmacion
                    int result = JOptionPane.showConfirmDialog(TicketDetail.this,
                            "Estas seguro de que quieres comprar ahora?", "Confirmacion",
                            JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        // Accion cuando se hace clic en "Si"
                        userController.buyTicket(evento, espacio, precio, user);
                        try {
                            Precio updatedPrecio = remoteFacade.getPrecioByID(precio.getId());
                            remoteFacade.updateTickets(updatedPrecio);
                        } catch (IOException ex) {
                            System.out.println("Error al actualizar los tickets: " + ex.getMessage());
                        }
                    } else if (result == JOptionPane.NO_OPTION) {
                        // Accion cuando se hace clic en "No"
                        System.out.println("Compra cancelada");
                    }
                }
            }

        });

        JButton paypalButton = new JButton("Pagar con PayPal");
        paypalButton.setBackground(new Color(105, 155, 178));
        paypalButton.setForeground(Color.WHITE);
        paypalButton.setFont(new Font("Arial", Font.BOLD, 16));
        paypalButton.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        paypalButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (paypalService != null && precio != null) {
                        //Actuvar pasarela PayPal
                        //paypalService.createPayment(String.valueOf(precio.getValor())); 
                        System.out.println("Pago iniciado con PayPal");
                } else {
                    System.out.println("PaypalService or precio is null");
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buttonPanel.add(buyButton);
        buttonPanel.add(paypalButton);

        mainPanel.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
    }
}
