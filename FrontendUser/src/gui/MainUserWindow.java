package gui;

import data.entidades.Evento;
import data.entidades.User;

import controller.UserController;
import java.rmi.RemoteException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;
import java.util.List;
import java.text.SimpleDateFormat;

public class MainUserWindow extends JFrame {
    private final User user;
    private UserController userController;

    public MainUserWindow(User user, UserController userController) {
        this.user = user;
        this.userController = userController;
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                userController.logoutUser();
            }
        });
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("GuTicket - Ventana Principal");
        setSize(800, 600);
        getContentPane().setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(new Color(54, 57, 63));
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ALUMNO\\Desktop\\GuTicket\\FrontendUser\\src\\gui\\element\\gu.png"));

        // Update the welcomeLabel's margin
        JLabel welcomeLabel = new JLabel("Bienvenido a GuTicket, " + user.getNombre());
        welcomeLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 24));
        welcomeLabel.setForeground(new Color(114, 137, 218));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 40, 0));
        getContentPane().add(welcomeLabel, BorderLayout.NORTH);

        // Crea un panel contenedor para los eventos
        JPanel eventosPanel = new JPanel();
        eventosPanel.setLayout(new GridLayout(0, 2, 20, 20));
        eventosPanel.setBackground(new Color(54, 57, 63));
        eventosPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            List<Evento> eventos = userController.getRemoteFacade().obtenerEventos();
            for (Evento evento : eventos) {
                eventosPanel.add(createEventoPanel(evento));
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar eventos");
            return;
        }

        // Agrega el panel de eventos a un JScrollPane
        JScrollPane scrollPane = new JScrollPane(eventosPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        
        setLocationRelativeTo(null);
    }

    private JPanel createEventoPanel(Evento evento) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        JPanel eventoPanel = new JPanel();
        eventoPanel.setLayout(new BorderLayout());
        eventoPanel.setBackground(new Color(52, 54, 59));
        eventoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(114, 137, 218), 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Panel de informacion del evento
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        infoPanel.setBackground(new Color(52, 54, 59));
        eventoPanel.add(infoPanel, BorderLayout.CENTER);

        // Muestra el titulo del evento
        JLabel titleLabel = new JLabel(evento.getTitulo());
        titleLabel.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 18));
        titleLabel.setForeground(new Color(114, 137, 218));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        infoPanel.add(titleLabel, gbc);

        // Muestra la fecha del evento
        if (evento.getFecha() != null) {
            JLabel fechaLabel = new JLabel("Dia: " + sdf.format(evento.getFecha()));
            fechaLabel.setFont(new Font("Yu Gothic UI Semibold", Font.PLAIN, 14));
            fechaLabel.setForeground(new Color(114, 137, 218));

            gbc.gridy = 1;
            infoPanel.add(fechaLabel, gbc);
        }

        eventoPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                
                EventDetail eventDetailWindow = new EventDetail(evento, userController.getRemoteFacade(), user, userController);
                eventDetailWindow.setVisible(true);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                eventoPanel.setBackground(Color.WHITE);
                eventoPanel.setForeground(Color.BLUE);
                infoPanel.setBackground(Color.WHITE);
                infoPanel.setForeground(Color.BLUE);
                titleLabel.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 28));
                
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                eventoPanel.setBackground(new Color(52, 54, 59));
                eventoPanel.setForeground(new Color(114, 137, 218));
                infoPanel.setBackground(new Color(52, 54, 59));
                infoPanel.setForeground(new Color(114, 137, 218));
                titleLabel.setFont(new Font("Yu Gothic UI Semibold", Font.BOLD, 18));
                
            }
        });
        

        return eventoPanel;
    }
}