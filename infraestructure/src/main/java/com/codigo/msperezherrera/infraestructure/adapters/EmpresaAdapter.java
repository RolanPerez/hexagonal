package com.codigo.msperezherrera.infraestructure.adapters;

import com.codigo.msperezherrera.domain.aggregate.constants.Constant;
import com.codigo.msperezherrera.domain.aggregate.dto.EmpresaDto;
import com.codigo.msperezherrera.domain.aggregate.dto.SunatDto;
import com.codigo.msperezherrera.domain.aggregate.request.EmpresaRequest;
import com.codigo.msperezherrera.domain.ports.out.EmpresaServiceOut;
import com.codigo.msperezherrera.infraestructure.client.ClienteSunat;
import com.codigo.msperezherrera.infraestructure.dao.EmpresaRepository;
import com.codigo.msperezherrera.infraestructure.entity.EmpresaEntity;
import com.codigo.msperezherrera.infraestructure.mapper.EmpresaMapper;
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
public class EmpresaAdapter implements EmpresaServiceOut {

    private final EmpresaRepository empresaRepository;
    private final ClienteSunat clienteSunat;
    private final RedisService redisService;

    @Value("${token.reniec}")
    private String tokenReniec;

    @Override
    public EmpresaDto crearEmpresaOut(EmpresaRequest empresaRequest) {
        EmpresaEntity empresaEntity = getEntity(empresaRequest, false, null);
        return EmpresaMapper.fromEntity(empresaRepository.save(empresaEntity));
    }

    private EmpresaEntity getEntity(EmpresaRequest empresaRequest, boolean actualizar, Long id) {

        SunatDto sunatDto = getExecSunat(empresaRequest.getNumDoc());
        EmpresaEntity entity = new EmpresaEntity();

        entity.setRazonSocial(sunatDto.getRazonSocial());
        entity.setTipoDocumento(empresaRequest.getTipoDoc());
        entity.setNumeroDocumento(sunatDto.getNumeroDocumento());
        entity.setCondicion(sunatDto.getCondicion());
        entity.setDireccion(sunatDto.getDireccion());
        entity.setDistrito(sunatDto.getDistrito());
        entity.setProvincia(sunatDto.getProvincia());
        entity.setDepartamento(sunatDto.getDepartamento());
        entity.setEsAgenteRetencion(sunatDto.getEsAgenteRetencion());

        if (actualizar) { // Si actualizo

            Optional<EmpresaEntity> datoExtraido = empresaRepository.findById(id);
            if (datoExtraido.isPresent()) {
                entity.setId(datoExtraido.get().getId());
                entity.setEstado(datoExtraido.get().getEstado());
                entity.setUsuaCrea(datoExtraido.get().getUsuaCrea());
                entity.setDateCreate(datoExtraido.get().getDateCreate());
                entity.setUsuaDelet(datoExtraido.get().getUsuaDelet());
                entity.setDateDelet(datoExtraido.get().getDateDelet());

            } else {
                throw new RuntimeException();
            }

            // entity.setId(id);
            entity.setUsuaModif(Constant.USU_ADMIN);
            entity.setDateModif(getTimestamp());

        } else { // Si creo
            entity.setEstado(Constant.STATUS_ACTIVE);
            entity.setUsuaCrea(Constant.USU_ADMIN);
            entity.setDateCreate(getTimestamp());
        }

        return entity;
    }

    private SunatDto getExecSunat(String numDoc) {
        String authorization = "Bearer " + tokenReniec;
        return clienteSunat.getInfoSunat(numDoc, authorization);
    }

    private Timestamp getTimestamp() {
        long currenTIme = System.currentTimeMillis();
        return new Timestamp(currenTIme);
    }

    @Override
    public Optional<EmpresaDto> buscarXIdOut(Long id) {
        String redisInfo = redisService.getFromRedis(Constant.REDIS_KEY_OBTENER_EMPRESA + id);
        if (redisInfo != null) {
            EmpresaDto empresaDto = Util.convertirDesdeString(redisInfo, EmpresaDto.class);
            return Optional.of(empresaDto);
        } else {
            EmpresaDto empresaDto = EmpresaMapper.fromEntity(empresaRepository.findById(id).get());
            String dataForRedis = Util.convertirAString(empresaDto);
            redisService.saveInRedis(Constant.REDIS_KEY_OBTENER_EMPRESA + id, dataForRedis, 2);
            return Optional.of(empresaDto);
        }
    }

    @Override
    public List<EmpresaDto> obtenerTodosOut() {
        List<EmpresaDto> listaDto = new ArrayList<>();
        List<EmpresaEntity> entidades = empresaRepository.findAll();
        for (EmpresaEntity dato : entidades) {
            listaDto.add(EmpresaMapper.fromEntity(dato));
        }
        return listaDto;
    }

    @Override
    public EmpresaDto actualizarOut(Long id, EmpresaRequest empresaRequest) {

        Optional<EmpresaEntity> datoExtraido = empresaRepository.findById(id);
        if (datoExtraido.isPresent()) {
            EmpresaEntity empresaEntity = getEntity(empresaRequest, true, id);
            return EmpresaMapper.fromEntity(empresaRepository.save(empresaEntity));
        } else {
            throw new RuntimeException();
        }

    }

    @Override
    public EmpresaDto deleteOut(Long id) {
        Optional<EmpresaEntity> datoExtraido = empresaRepository.findById(id);
        if (datoExtraido.isPresent()) {
            datoExtraido.get().setEstado(Constant.STATUS_INACTIVE);
            datoExtraido.get().setUsuaDelet(Constant.USU_ADMIN);
            datoExtraido.get().setDateDelet(getTimestamp());
            return EmpresaMapper.fromEntity(empresaRepository.save(datoExtraido.get()));
        } else {
            throw new RuntimeException();
        }
    }

}
