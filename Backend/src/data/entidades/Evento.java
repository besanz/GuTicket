package data.entidades;

import java.util.Date;
import java.util.List;

import java.io.Serializable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
public class Evento implements Serializable {

    @Getter @Setter private int id;

    @Getter @Setter private String titulo;

    @Getter @Setter private String descripcion;

    @Getter @Setter private Date fecha;

    @Getter @Setter private String createdAt;

    @Getter @Setter private String updatedAt;

    @Getter @Setter private String publishedAt;

    @Getter @Setter private int aforo;

    @Getter @Setter private List<Precio> precios;

}
