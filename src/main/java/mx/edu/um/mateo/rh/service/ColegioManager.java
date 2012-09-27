
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.rh.model.Colegio;

public interface ColegioManager{
    /**
     * Retrieves all of the colegios
     */
public Map<String, Object> getColegios(final Colegio colegio) ;
    /**
     * Gets colegio's information based on id.
     * @param id the colegio's id
     * @return colegio populated colegio object
     */
    public Colegio getColegio(final Long id);

    /**
     * Saves a colegio's information
     * @param colegio the object to be saved
     */
    public void saveColegio(Colegio colegio);

    /**
     * Removes a colegio from the database by id
     * @param id the colegio's id
     */
    public void removeColegio(final Long id);
}

