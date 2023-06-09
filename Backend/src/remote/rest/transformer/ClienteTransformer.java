package remote.rest.transformer;

import data.entidades.Cliente;
import remote.rest.dto.ClienteDTO;

import java.util.List;
import java.util.ArrayList;

public class ClienteTransformer {
    private static ClienteTransformer instance;

    private ClienteTransformer() {}

    public static synchronized ClienteTransformer getInstance() {
        if (instance == null) {
            instance = new ClienteTransformer();
        }
        return instance;
    }

    public List<Cliente> transform(List<ClienteDTO> clienteDTOs) {
        List<Cliente> clientes = new ArrayList<>();
        for (ClienteDTO clienteDTO : clienteDTOs) {
            Cliente cliente = clienteDTO.getCliente();
            clientes.add(cliente);
        }
        return clientes;
    }

    public Cliente transform(ClienteDTO clienteDTO) {
        return clienteDTO.getCliente();
    }
}
