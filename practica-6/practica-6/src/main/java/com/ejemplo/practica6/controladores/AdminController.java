package com.ejemplo.practica6.controladores;

import com.ejemplo.practica6.model.Mock;
import com.ejemplo.practica6.model.seguridad.Usuario;
import com.ejemplo.practica6.servicios.MockServices;
import com.ejemplo.practica6.servicios.seguridad.SeguridadServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private SeguridadServices seguridadServices;

    @Autowired
    private MockServices mockServices;

    @Autowired
    private Environment environment;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value="/")
    public ModelAndView indexAdmin(Authentication authentication) throws UnknownHostException {

        List<Usuario> usuarios = seguridadServices.findAll();

        HashMap<String, Object> params = new HashMap<>();
        params.put("usuarios", usuarios);
        params.put("logged", seguridadServices.findByUsername(authentication.getName()));

        return new ModelAndView("verUsuarios", params);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/usuario")
    public ModelAndView crearUsuarioGet(Authentication authentication, @RequestParam Optional<Integer> id)
    {
        HashMap<String, Object> params = new HashMap<>();
        params.put("logged", seguridadServices.findByUsername(authentication.getName()));
        if(id.isEmpty()){
            return new ModelAndView("crearUsuario", params);
        }else{
            Usuario usuario = seguridadServices.findUsuarioById(id.get());
            if(usuario != null){
                params.put("datos", usuario);
                String rol = usuario.getRoles().iterator().next().getRole();
                params.put("rol", rol);
                if(rol.equalsIgnoreCase("ROLE_ADMIN")){
                    params.put("rol2", "ROLE_USER");
                }else{
                    params.put("rol2", "ROLE_ADMIN");
                }
                return new ModelAndView("crearUsuario", params);
            }
        }
        return new ModelAndView("redirect:/admin/", params);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/usuario")
    public ModelAndView crearUsuarioPost(Authentication authentication, @ModelAttribute Usuario usuario, @RequestParam Optional<Integer> id, RedirectAttributes redirectAttributes)
    {
//        System.out.println(usuario);
        boolean f=seguridadServices.existsUsuarioByUsername(usuario.getUsername());

        if (id.isEmpty()){
            if(f)
            {
                redirectAttributes.addFlashAttribute("msg", "Nombre de usuario ya existe :(");
            }else{
                usuario.setRoles(new HashSet<>(Arrays.asList(seguridadServices.rolFindById(usuario.getRol()))));
                usuario.setActivo(true);
                Usuario user = seguridadServices.crearUsuario(usuario);
                if(user != null){
                    redirectAttributes.addFlashAttribute("msgSuccess", "Usuario creado exitosamente :)");
                }else{
                    redirectAttributes.addFlashAttribute("msg", "No se pudo crear debido un error inesperado en el servidor :(");
                }
            }
            return new ModelAndView("redirect:/admin/usuario");
        }else{
            Usuario temp = seguridadServices.findUsuarioById(id.get());
            if(temp != null) {
                boolean tempBool = (temp.getId() == seguridadServices.findByUsername(authentication.getName()).getId());
                if (f && !temp.getUsername().equalsIgnoreCase(usuario.getUsername())) {
                    redirectAttributes.addFlashAttribute("msg", "Nombre de usuario ya existe :(");
                } else {
                    temp.setNombre(usuario.getNombre());
                    temp.setApellido(usuario.getApellido());
                    temp.setUsername(usuario.getUsername());
                    temp.setRol(usuario.getRol());
                    temp.setRoles(new HashSet<>(Arrays.asList(seguridadServices.rolFindById(usuario.getRol()))));
                    Usuario user;
                    if (temp.getPassword().equalsIgnoreCase(usuario.getPassword())) {
                        user = seguridadServices.crearUsuarioSinCambioClave(temp);
                    } else {
                        temp.setPassword(usuario.getPassword());
                        user = seguridadServices.crearUsuario(temp);
                    }
                    if (user != null) {
                        redirectAttributes.addFlashAttribute("msgSuccess", "Usuario editado exitosamente :)");
                    } else {
                        redirectAttributes.addFlashAttribute("msg", "No se pudo editar debido un error inesperado en el servidor :(");
                    }
                }
                if (tempBool) {
                    return new ModelAndView("redirect:/logout");
                }
            }
            return new ModelAndView("redirect:/admin/usuario?id="+id.get());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/borrarUsuario/{id}")
    public ModelAndView borrar(Authentication authentication, @PathVariable int id, RedirectAttributes redirectAttributes){
        Usuario temp = seguridadServices.findUsuarioById(id);
        if(temp != null){
            if(temp.isActivo()) {
                if (temp.getId() == seguridadServices.findByUsername(authentication.getName()).getId()) {
                    redirectAttributes.addFlashAttribute("msg", "No puede desactivar el usuario registrado :(");
                } else {
                    temp.setActivo(false);
                    Usuario user = seguridadServices.deleteById(temp);
                    if (user != null) {
                        redirectAttributes.addFlashAttribute("msgSuccess", "Usuario desactivado correctamente :)");
                    } else {
                        redirectAttributes.addFlashAttribute("msg", "No se pudo desactivar debido a un error inesperado en el servidor :(");
                    }
                }
            }else{
                temp.setActivo(true);
                Usuario user = seguridadServices.deleteById(temp);
                if (user != null) {
                    redirectAttributes.addFlashAttribute("msgSuccess", "Usuario activado correctamente :)");
                } else {
                    redirectAttributes.addFlashAttribute("msg", "No se pudo activar debido a un error inesperado en el servidor :(");
                }
            }
        }
        return new ModelAndView("redirect:/admin/");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/mocks")
    public ModelAndView mocks(Authentication authentication, @RequestParam Optional<Integer> id){
        Usuario usuario = seguridadServices.findByUsername(authentication.getName());
        HashMap<String, Object> params = new HashMap<>();
        params.put("logged",usuario);
        params.put("port", environment.getProperty("local.server.port"));
        params.put("hostname", InetAddress.getLoopbackAddress().getHostName());
        params.put("token", usuario.getToken());

        if (id.isEmpty()) {
            List<Mock> mocks = mockServices.findAllByUsuario(usuario);
            params.put("mocks", mocks);
            params.put("titulo", "Mis Mocks");
        } else {
            Usuario usuarioMocks = seguridadServices.findUsuarioById(id.get());
            if (usuarioMocks != null) {
                List<Mock> mocks = mockServices.findAllByUsuario(usuarioMocks);
                params.put("mocks", mocks);
                if (id.get() != usuario.getId())
                    params.put("titulo", "Mocks de " + usuarioMocks.getUsername());
                else
                    params.put("titulo", "Mis Mocks");
            } else {
                return new ModelAndView("redirect:/admin/mocks", params);
            }
        }

        return new ModelAndView("adminMocks", params);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/mock")
    public ModelAndView crearMockGet(Authentication authentication, @RequestParam Optional<Integer> id)
    {
        HashMap<String, Object> params = new HashMap<>();
        Usuario logged = seguridadServices.findByUsername(authentication.getName());
        params.put("logged", logged);
        params.put("port", environment.getProperty("local.server.port"));
        params.put("hostname", InetAddress.getLoopbackAddress().getHostName());
        if(id.isEmpty()){
            return new ModelAndView("adminCrearMock", params);
        }else{
            Mock mock = mockServices.findById(id.get());
            if(mock != null){
                if(!mock.getUsuario().getUsername().equals(logged.getUsername()))
                    params.put("userMock",mock.getUsuario());
                params.put("datos", mock);
                return new ModelAndView("adminCrearMock", params);
            }
        }
        return new ModelAndView("redirect:/admin/", params);
    }

    @PreAuthorize("hasRole('ADMIN')")
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
                mock.setUsuario(loggedUser);
                LocalDateTime lt = LocalDateTime.now();
                mock.setFechaExpiracion(lt.plusHours(Integer.parseInt(mock.getExpira())));
                Mock newMock = mockServices.crearMock(mock);
                if(newMock != null){
                    redirectAttributes.addFlashAttribute("msgSuccess", String.format("http://%s:%s/mock/%s",InetAddress.getLoopbackAddress().getHostName(), environment.getProperty("local.server.port"), newMock.getRuta()));
                }else{
                    redirectAttributes.addFlashAttribute("msg", "No se pudo crear debido un error inesperado en el servidor :(");
                }
            }
            return new ModelAndView("redirect:/admin/mock");
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
                        redirectAttributes.addFlashAttribute("msg", "No se pudo editar debido un error inesperado en el servidor :(");
                    }
                }else{
                    redirectAttributes.addFlashAttribute("msg", "No se pudo editar un mock que no sea suyo :(");
                }
            }
            return new ModelAndView("redirect:/admin/mock?id="+id.get());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/borrarMock/{id}")
    public ModelAndView borrarMock(Authentication authentication, @PathVariable int id, RedirectAttributes redirectAttributes){
        Mock temp = mockServices.findById(id);
        String logged = authentication.getName();
        if(temp != null){
            if (!temp.getUsuario().getUsername().equalsIgnoreCase(logged)){
                redirectAttributes.addFlashAttribute("msg", "No puede eliminar un Mock que no se suyo :(");
            }else{
                redirectAttributes.addFlashAttribute("msgSuccess", "Mock eliminado correctamente :(");
                mockServices.deleteById(id);
            }
        }
        return new ModelAndView("redirect:/admin/mocks");
    }
}