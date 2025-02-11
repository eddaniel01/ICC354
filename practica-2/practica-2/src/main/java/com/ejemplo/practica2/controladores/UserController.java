package com.ejemplo.practica2.controladores;

import com.ejemplo.practica2.model.Mock;
import com.ejemplo.practica2.model.seguridad.Usuario;
import com.ejemplo.practica2.servicios.MockServices;
import com.ejemplo.practica2.servicios.seguridad.SeguridadServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private SeguridadServices seguridadServices;

    @Autowired
    private MockServices mockServices;

    @Autowired
    private Environment environment;

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value="/")
    public ModelAndView indexUser(Authentication authentication) {

        Usuario logged = seguridadServices.findByUsername(authentication.getName());
        List<Mock> mocks = mockServices.findAllByUsuario(logged);

        HashMap<String, Object> params = new HashMap<>();
        params.put("mocks", mocks);
        params.put("logged", logged);
        params.put("port", environment.getProperty("local.server.port"));
        params.put("hostname", InetAddress.getLoopbackAddress().getHostName());
        params.put("token", logged.getToken());

        return new ModelAndView("userMocks", params);
    }


    @PreAuthorize("hasRole('USER')")
    @GetMapping("/mock")
    public ModelAndView crearMockGet(Authentication authentication, @RequestParam Optional<Integer> id)
    {
        HashMap<String, Object> params = new HashMap<>();
        Usuario logged = seguridadServices.findByUsername(authentication.getName());
        params.put("logged", logged);
        params.put("port", environment.getProperty("local.server.port"));
        params.put("hostname", InetAddress.getLoopbackAddress().getHostName());
        if(id.isEmpty()){
            return new ModelAndView("userCrearMocks", params);
        }else{
            Mock mock = mockServices.findById(id.get());
            if(mock != null){
                if(mock.getUsuario().getUsername().equals(logged.getUsername())){
                    params.put("datos", mock);
                    return new ModelAndView("userCrearMocks", params);
                }
            }
        }
        return new ModelAndView("redirect:/user/", params);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/mock")
    public ModelAndView crearMockPost(Authentication authentication, @ModelAttribute Mock mock, @RequestParam Optional<Integer> id, RedirectAttributes redirectAttributes)
    {
        boolean f=mockServices.existsByRuta(mock.getRuta());
        Usuario loggedUser = seguridadServices.findByUsername(authentication.getName());

        if (id.isEmpty()){
            if(f)
            {
                redirectAttributes.addFlashAttribute("msg", "Ruta ya existe :(");
            }else{
                LocalDateTime lt = LocalDateTime.now();
                mock.setFechaExpiracion(lt.plusHours(Integer.parseInt(mock.getExpira())));
                mock.setUsuario(loggedUser);
                Mock newMock = mockServices.crearMock(mock);
                if(newMock != null){
                    redirectAttributes.addFlashAttribute("msgSuccess", String.format("http://%s:%s/mock/%s",InetAddress.getLoopbackAddress().getHostName(), environment.getProperty("local.server.port"), newMock.getRuta()));
                }else{
                    redirectAttributes.addFlashAttribute("msg", "No se pudo crear debido un error inesperado en el servidor :(");
                }
            }
            return new ModelAndView("redirect:/user/mock");
        }else{
            Mock temp = mockServices.findById(id.get());
            if(temp != null) {
                if (f && !temp.getRuta().equalsIgnoreCase(mock.getRuta())) {
                    redirectAttributes.addFlashAttribute("msg", "Nombre de ruta ya existe :(");
                } else if (authentication.getName().equalsIgnoreCase(temp.getUsuario().getUsername())){
                    LocalDateTime lt = LocalDateTime.now();
                    temp.setFechaExpiracion(lt.plusHours(Integer.parseInt(mock.getExpira())));
                    temp.setNombre(mock.getNombre());
                    temp.setDescripcion(mock.getDescripcion());
                    temp.setRuta(mock.getRuta());
                    temp.setCodigo(mock.getCodigo());
                    temp.setCuerpo(mock.getCuerpo());
                    temp.setContentType(mock.getContentType());
                    temp.setHeaders(mock.getHeaders());
                    temp.setExpira(mock.getExpira());
                    temp.setDemora(mock.getDemora());

                    Mock newMock = mockServices.crearMock(temp);
                    if (newMock != null) {
                        redirectAttributes.addFlashAttribute("msgSuccess", String.format("http://%s:%s/mock/%s",InetAddress.getLoopbackAddress().getHostName(), environment.getProperty("local.server.port"), newMock.getRuta()));
                    } else {
                        redirectAttributes.addFlashAttribute("msg", "No se pudo editar debido a un error inesperado en el servidor :(");
                    }
                }else{
                    return new ModelAndView("redirect:/user/");
                }
            }
            return new ModelAndView("redirect:/user/mock?id="+id.get());
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(value = "/borrarMock/{id}")
    public ModelAndView borrarMock(Authentication authentication, @PathVariable int id, RedirectAttributes redirectAttributes){
        Mock temp = mockServices.findById(id);
        String logged = authentication.getName();
        if(temp != null){
            if (!temp.getUsuario().getUsername().equalsIgnoreCase(logged)){
                return new ModelAndView("redirect:/user/");
            }else{
                redirectAttributes.addFlashAttribute("msgSuccess", "Mock eliminado correctamente :(");
                mockServices.deleteById(id);
            }
        }
        return new ModelAndView("redirect:/user/");
    }
}

