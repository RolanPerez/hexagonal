package com.codigo.msperezherrera.infraestructure.adapters;

import com.codigo.msperezherrera.domain.aggregate.constants.Constant;
import com.codigo.msperezherrera.domain.aggregate.dto.PersonaDto;
import com.codigo.msperezherrera.domain.aggregate.dto.ReniecDto;
import com.codigo.msperezherrera.domain.aggregate.request.PersonaRequest;
import com.codigo.msperezherrera.domain.ports.out.PersonaServiceOut;
import com.codigo.msperezherrera.infraestructure.client.ClienteReniec;
import com.codigo.msperezherrera.infraestructure.dao.PersonaRepository;
import com.codigo.msperezherrera.infraestructure.entity.PersonaEntity;
import com.codigo.msperezherrera.infraestructure.mapper.PersonaMapper;
import com.codigo.msperezherrera.infraestructure.redisUtil.RedisService;
import com.codigo.msperezherrera.infraestructure.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonaAdapter implements PersonaServiceOut {

    private final PersonaRepository personaRepository;
    private final ClienteReniec clientReniec;
    private final RedisService redisService;

    @Value("${token.reniec}")
    private String tokenReniec;

    @Override
    public PersonaDto crearPersonaOut(PersonaRequest personaRequest) {
        PersonaEntity personaEntity = getEntity(personaRequest,false, null);
        return PersonaMapper.fromEntity(personaRepository.save(personaEntity));
    }

    private PersonaEntity getEntity(PersonaRequest personaRequest, boolean actualizar, Long id){

        ReniecDto reniecDto = getExecReniec(personaRequest.getNumDoc());
        PersonaEntity entity = new PersonaEntity();
        entity.setNombre(reniecDto.getNombres());
        entity.setApellido(reniecDto.getApellidoPaterno() + reniecDto.getApellidoMaterno());
        entity.setTipoDocumento(personaRequest.getTipoDoc());
        entity.setNumeroDocumento(reniecDto.getNumeroDocumento());
        entity.setEmail(personaRequest.getEmail());
        entity.setTelefono(personaRequest.getTelefono());
        entity.setDireccion(personaRequest.getDireccion());

        if(actualizar){ // Si actualizo

            Optional<PersonaEntity> datoExtraido = personaRepository.findById(id);
            if(datoExtraido.isPresent()){
                entity.setId(datoExtraido.get().getId());
                entity.setEstado(datoExtraido.get().getEstado());
                entity.setUsuaCrea(datoExtraido.get().getUsuaCrea());
                entity.setDateCreate(datoExtraido.get().getDateCreate());
                entity.setUsuaDelet(datoExtraido.get().getUsuaDelet());
                entity.setDateDelet(datoExtraido.get().getDateDelet());

            }else {
                throw new RuntimeException();
            }

            // entity.setId(id);
            entity.setUsuaModif(Constant.USU_ADMIN);
            entity.setDateModif(getTimestamp());

        }else{ // Si creo
            entity.setEstado(Constant.STATUS_ACTIVE);
            entity.setUsuaCrea(Constant.USU_ADMIN);
            entity.setDateCreate(getTimestamp());
        }

        return entity;
    }

    private ReniecDto getExecReniec(String numDoc){
        String authorization = "Bearer "+tokenReniec;
        return clientReniec.getInfoReniec(numDoc,authorization);
    }
    private Timestamp getTimestamp(){
        long currenTIme = System.currentTimeMillis();
        return new Timestamp(currenTIme);
    }

    @Override
    public Optional<PersonaDto> buscarXIdOut(Long id) {
        String redisInfo = redisService.getFromRedis(Constant.REDIS_KEY_OBTENER_PERSONA+id);
        if(redisInfo!= null){
            PersonaDto personaDto = Util.convertirDesdeString(redisInfo,PersonaDto.class);
            return Optional.of(personaDto);
        }else{
            PersonaDto personaDto = PersonaMapper.fromEntity(personaRepository.findById(id).get());
            String dataForRedis = Util.convertirAString(personaDto);
            redisService.saveInRedis(Constant.REDIS_KEY_OBTENER_PERSONA+id,dataForRedis,2);
            return Optional.of(personaDto);
        }
    }

    @Override
    public List<PersonaDto> obtenerTodosOut() {
        List<PersonaDto> listaDto = new ArrayList<>();
        List<PersonaEntity> entidades = personaRepository.findAll();
        for (PersonaEntity dato :entidades){
            listaDto.add(PersonaMapper.fromEntity(dato));
        }
        return listaDto;
    }

    @Override
    public PersonaDto actualizarOut(Long id, PersonaRequest personaRequest) {

        Optional<PersonaEntity> datoExtraido = personaRepository.findById(id);
        if(datoExtraido.isPresent()){
            PersonaEntity personaEntity = getEntity(personaRequest,true, id);
            return PersonaMapper.fromEntity(personaRepository.save(personaEntity));
        }else {
            throw new RuntimeException();
        }

    }

    @Override
    public PersonaDto deleteOut(Long id) {
        Optional<PersonaEntity> datoExtraido = personaRepository.findById(id);
        if(datoExtraido.isPresent()){
            datoExtraido.get().setEstado(Constant.STATUS_INACTIVE);
            datoExtraido.get().setUsuaDelet(Constant.USU_ADMIN);
            datoExtraido.get().setDateDelet(getTimestamp());
            return PersonaMapper.fromEntity(personaRepository.save(datoExtraido.get()));
        }else {
            throw new RuntimeException();
        }
    }
}
