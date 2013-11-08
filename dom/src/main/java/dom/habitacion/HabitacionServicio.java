package dom.habitacion;

import java.io.IOException;
import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.filter.Filter;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.TimeoutException;

import com.google.common.base.Objects;

import dom.asterisk.Asterisk;
import dom.enumeradores.TipoHabitacion;
import dom.habitacion.Habitacion;

@Named("Habitacion")
public class HabitacionServicio extends AbstractFactoryAndRepository{

	// {{iconName
    public String iconName() {
        return "habitacion";
    }
    // }}
    	
	@Named("Crear")
	@MemberOrder(sequence = "1")
	public Habitacion nuevaHabitacion(	
			@RegEx(validation="[\\w\\s]+")
			@Named("Nombre") final String nombre,
			@Named("Tipo de Habitación") final TipoHabitacion tipoHabitacion,
			@Named("Interno") final int interno) {
		return nuevoHabitacion(nombre, tipoHabitacion, interno);
	}
	
	@Hidden
	public Habitacion nuevoHabitacion(
			final String nombre,		
			final TipoHabitacion tipoHabitacion,
			final int interno) {
		final Habitacion habitacion = newTransientInstance(Habitacion.class);		
		habitacion.setNombre(nombre);
		habitacion.setTipoHabitacion(tipoHabitacion);
		habitacion.setUsuario(usuarioActual());
		habitacion.setInterno(interno);
		
		persist(habitacion);
		
		return habitacion;
	}
	
	@Named("Listar")
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "2")
    public List<Habitacion> Listahabitacion() {
        final String usuario = usuarioActual();
        final List<Habitacion> listaHabitacion = allMatches(Habitacion.class, Habitacion.creadoPor(usuario));
        return listaHabitacion;
    }
	/*
     * Método para llenar el DropDownList de habitacion, con la posibilidad de que te autocompleta las coincidencias al ir tipeando
     */
    @Hidden
    public List<Habitacion> completaHabitaciones(final String nombre) {
        return allMatches(Habitacion.class, new Filter<Habitacion>() {
        	@Override
            public boolean accept(final Habitacion h) {
                return creadoPorActualUsuario(h) && h.getNombre().contains(nombre) && h.isEstado();
            }
        });
    }

	
    protected boolean creadoPorActualUsuario(final Habitacion h) {
        return Objects.equal(h.getUsuario(), usuarioActual());
    }
    protected String usuarioActual() {
        return getContainer().getUser().getName();
    }

	
	
}
