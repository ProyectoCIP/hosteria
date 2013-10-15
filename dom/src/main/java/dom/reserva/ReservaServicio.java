package dom.reserva;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotInServiceMenu;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;

import com.google.common.base.Objects;

import dom.consumo.Consumo;
import dom.disponibilidad.Disponibilidad;
import dom.enumeradores.FormaPago;
import dom.huesped.Huesped;

public class ReservaServicio extends AbstractFactoryAndRepository {
	
	public void reserva(
			@Named("Huesped") Huesped huesped,
			@Optional
			@MultiLine(numberOfLines=3)
			@Named("Comentario") String comentario
			) {
		
		
		reservar(huesped,comentario);
	}
	
	private Reserva reservar(
			final Huesped huesped,
			final String comentario) {
			final Reserva reserva = newTransientInstance(Reserva.class);
			reserva.setHuesped(huesped);
			reserva.setFecha(LocalDate.now());
			reserva.setComentario(comentario);
			reserva.setUsuario(usuarioActual());
			reserva.setCantidadDias(3);
			reserva.setMontoSena(400);
			reserva.setNumero(1);
			reserva.setNombreEstado("reservada");
			reserva.setTipoSena(FormaPago.Efectivo);
			
			persistIfNotAlready(reserva);
			getContainer().informUser("Reserva realizada con éxito!");
			
			return reserva;
	}
	
	@NotInServiceMenu
	public Consumo agregarConsumo(
			final Reserva reserva,
			final String descripcion,
			final int cantidad,
			final float precio
    		) {
		
		Consumo consumo = newTransientInstance(Consumo.class);
		consumo.setDescripcion(descripcion);
		consumo.setCantidad(cantidad);
		consumo.setPrecio(precio);
		consumo.setReserva(reserva);
		//dependencia
		reserva.addToConsumo(consumo);
		
		persistIfNotAlready(consumo);
		return consumo;
	}
	
	@Hidden
	public List<HabitacionFecha> habitacionesReservadas(final String nombre) {
		return allMatches(HabitacionFecha.class,new Filter<HabitacionFecha>(){
			@Override
			public boolean accept(final HabitacionFecha habitacion) {
				// TODO Auto-generated method stub
				return Objects.equal(habitacion.getNombreHabitacion(), nombre);
			}			
		});
	}
	
	protected String usuarioActual() {
        return getContainer().getUser().getName();
    }	

}
