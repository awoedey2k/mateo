/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.webflow.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeProveedorDetalleManager;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeProveedorManager;
import mx.edu.um.mateo.contabilidad.facturas.service.ProveedorFacturasManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.general.utils.AutorizacionCCPlInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.core.collection.ParameterMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 *
 * @author develop
 */
@Component
public class InformeProveedorAction extends MultiAction {

    protected final transient Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private InformeProveedorManager instance;
    @Autowired
    protected Ambiente ambiente;
    @Autowired
    private InformeProveedorDetalleManager detalleManager;
    @Autowired
    private ProveedorFacturasManager proveedorFacturasManagerfacturasManager;

    public Event creaInforme(RequestContext context) {

        Usuario usuario = ambiente.obtieneUsuario();
        InformeProveedor informeProveedor = (InformeProveedor) context.getFlowScope().get("informeProveedor");
        log.debug("informeEmpleadoAction{}", informeProveedor);
        informeProveedor.setEmpleado(usuario);
        instance.crea(informeProveedor, usuario);
        context.getFlowScope().put("informeEmpleadoId", informeProveedor.getId());
        return success();
    }

    public Event crearDetalle(RequestContext context) throws AutorizacionCCPlInvalidoException, IOException {
//        String name = file.getName();
//        log.debug("nombre archivo{}", name);
        List<MultipartFile> files = new ArrayList<>();
        List<String> fileNames = new ArrayList<String>();
        Usuario usuario = ambiente.obtieneUsuario();
        InformeProveedorDetalle informeProveedorDetalle = (InformeProveedorDetalle) context.getFlowScope().get("informeProveedorDetalle");
        informeProveedorDetalle.setUsuarioAlta(usuario);
        String frc = informeProveedorDetalle.getRFCProveedor();
        ProveedorFacturas proveedorFacturas = proveedorFacturasManagerfacturasManager.obtiene(frc);
        log.debug("proveedor{}", proveedorFacturas.toString());
        informeProveedorDetalle.setProveedorFacturas(proveedorFacturas);
        informeProveedorDetalle.setNombreProveedor(proveedorFacturas.getNombre());
        informeProveedorDetalle.setFechaCaptura(new Date());
        informeProveedorDetalle.setStatus("A");
        files.add(informeProveedorDetalle.getFile());
        files.add(informeProveedorDetalle.getFile2());

        Long id = (Long) context.getFlowScope().get("informeEmpleadoId");
        InformeProveedor informeProveedor = instance.obtiene(id);
        informeProveedorDetalle.setInformeProveedor(informeProveedor);
        log.debug("informeProveedorDetalleAction{}", informeProveedorDetalle);

        Calendar calendar = GregorianCalendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DATE);
        String nombre = context.getExternalContext().getCurrentUser().getName();
        if (null != files && files.size() > 0) {
            for (MultipartFile multipartFile : files) {
                String fileName = multipartFile.getOriginalFilename();
                fileNames.add(fileName);
                String uploadDir = "/home/facturas/" + año + "/" + mes + "/" + dia + "/" + nombre + "/" + multipartFile.getOriginalFilename();
                File dirPath = new File(uploadDir);
                if (!dirPath.exists()) {
                    dirPath.mkdirs();
                }
                multipartFile.transferTo(new File("/home/facturas/" + año + "/" + mes + "/" + dia + "/" + nombre + "/" + multipartFile.getOriginalFilename()));
                if (multipartFile.getOriginalFilename().contains(".pdf")) {
                    informeProveedorDetalle.setPathPDF("/home/facturas/" + año + "/" + mes + "/" + dia + "/" + nombre + "/" + multipartFile.getOriginalFilename());
                    informeProveedorDetalle.setNombrePDF(multipartFile.getOriginalFilename());
                }
                if (multipartFile.getOriginalFilename().contains(".xml")) {
                    informeProveedorDetalle.setPathXMl("/home/facturas/" + año + "/" + mes + "/" + dia + "/" + nombre + "/" + multipartFile.getOriginalFilename());
                    informeProveedorDetalle.setNombreXMl(multipartFile.getOriginalFilename());
                }
            }
        }
        detalleManager.crea(informeProveedorDetalle, usuario);
        return success();

    }
}
