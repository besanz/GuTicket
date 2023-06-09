package gui;

import data.entidades.User;
import controller.StaffController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;

public class StaffControlUsuarios extends JFrame {
    private StaffController staffController;
    private JTable usersTable;
    private DefaultTableModel usersTableModel;

    public StaffControlUsuarios(StaffController staffController) {
        if (staffController == null) {
            throw new IllegalArgumentException("El objeto StaffController no puede ser nulo");
        }

        this.staffController = staffController;
        initComponents();
        getAllUsers();
    }


    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Admin - Control de Usarios");
        setPreferredSize(new Dimension(800, 600));
        getContentPane().setBackground(new Color(54, 57, 63));
        setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\ALUMNO\\Desktop\\GuTicket\\FrontendUser\\src\\gui\\element\\gu.png"));


        // Crear panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(54, 57, 63));
        GridBagConstraints c = new GridBagConstraints();

        // Crear tabla de usuarios
        usersTableModel = new DefaultTableModel(new String[]{"DNI", "Nombre", "Apellidos", "Email"}, 0);
        usersTable = new JTable(usersTableModel);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Crear renderizador de encabezados personalizado
        JTableHeader header = usersTable.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        label.setForeground(Color.WHITE); // Color del texto
        label.setBackground(new Color(220, 53, 69)); // Color de fondo
        label.setFont(label.getFont().deriveFont(Font.BOLD)); // Negrita
        return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(scrollPane, c);
        // Crear boton de eliminacion de usuarios
        JButton deleteButton = new JButton("Eliminar");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = usersTable.getSelectedRow();
                if (selectedRow != -1) {
                    String dni = (String) usersTable.getValueAt(selectedRow, 0);
                    int result = JOptionPane.showConfirmDialog(null,
                            "Esta seguro de que desea eliminar al usuario con DNI " + dni + "?",
                            "Eliminar usuario",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (result == JOptionPane.YES_OPTION) {
                        deleteUser(dni);
                        usersTableModel.removeRow(selectedRow);
                    }
                }
            }
        });
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(deleteButton, c);

        // Agregar panel principal al contenido del JFrame
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void getAllUsers() {
        List<User> users = staffController.getAllUsers();
        if (users != null) {
            for (User user : users) {
                usersTableModel.addRow(new Object[]{user.getDni(), user.getNombre(), user.getApellidos(), user.getEmail()});
            }
        } else {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios");
        }
    }

    private void deleteUser(String dni) {
        staffController.deleteUser(dni);
        JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente");
    }
}
