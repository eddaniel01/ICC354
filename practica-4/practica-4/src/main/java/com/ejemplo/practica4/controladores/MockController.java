package com.ejemplo.practica4.controladores;

import com.ejemplo.practica4.model.Mock;
import com.ejemplo.practica4.servicios.MockServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/mock")
public class MockController {

    @Autowired
    private MockServices mockServices;

    @RequestMapping(value="/{ruta}")
    public ResponseEntity<String> indexUser(@PathVariable String ruta) throws JsonProcessingException {
        Mock mock = mockServices.findByRuta(ruta);
        if(mock!=null){
            HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(mock.getCodigo().substring(0, 3)));
            MultiValueMap<String, String> headers = new HttpHeaders();
            headers.add("Content-Type", mock.getContentType());
            if(mock.getHeaders() != null && !Objects.equals(mock.getHeaders(), "")){
                ObjectMapper mapper = new ObjectMapper();
                List<String> keys = new ArrayList<>();
                JsonNode jsonNode = mapper.readTree(mock.getHeaders());
                Iterator<String> iterator = jsonNode.fieldNames();
                iterator.forEachRemaining(keys::add);
                JsonParser springParser = JsonParserFactory.getJsonParser();
                Map< String, Object > map = springParser.parseMap(mock.getHeaders());
                for (String header: keys){
                    headers.add(header, String.valueOf(map.get(header)));
                }
            }
            if (mock.getDemora() > 0){
                try {
                    Thread.sleep(mock.getDemora()* 1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return new ResponseEntity<>(mock.getCuerpo(), headers, httpStatus);
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(404));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @RequestMapping(value="/jwt/{ruta}")
    public ResponseEntity<String> mockJwt(@PathVariable String ruta) throws JsonProcessingException {
        Mock mock = mockServices.findByRuta(ruta);
        if(mock!=null){
            HttpStatus httpStatus = HttpStatus.valueOf(Integer.parseInt(mock.getCodigo().substring(0, 3)));
            MultiValueMap<String, String> headers = new HttpHeaders();
            headers.add("Content-Type", mock.getContentType());
            if(mock.getHeaders() != null && !Objects.equals(mock.getHeaders(), "")){
                ObjectMapper mapper = new ObjectMapper();
                List<String> keys = new ArrayList<>();
                JsonNode jsonNode = mapper.readTree(mock.getHeaders());
                Iterator<String> iterator = jsonNode.fieldNames();
                iterator.forEachRemaining(keys::add);
                JsonParser springParser = JsonParserFactory.getJsonParser();
                Map < String, Object > map = springParser.parseMap(mock.getHeaders());
                for (String header: keys){
                    headers.add(header, String.valueOf(map.get(header)));
                }
            }
            if (mock.getDemora() > 0){
                try {
                    Thread.sleep(mock.getDemora());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return new ResponseEntity<>(mock.getCuerpo(), headers, httpStatus);
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(404));
    }

}
