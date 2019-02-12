package ee.ria.riha.web;

import ee.ria.riha.domain.model.InfoSystemDataObject;
import ee.ria.riha.web.model.InfoSystemDataObjectModel;
import org.springframework.stereotype.Component;

@Component
public class InfoSystemDataObjectMapper implements ModelMapper<InfoSystemDataObject, InfoSystemDataObjectModel> {

    @Override
    public InfoSystemDataObjectModel map(InfoSystemDataObject value) {
        return new InfoSystemDataObjectModel(
                value.getInfosystem(),
                value.getDataObjectName(),
                value.getComment(),
                value.getParentObject(),
                value.getShortName(),
                value.getFileUuid(),
                value.getDiaFlag(),
                value.getAvFlag(),
                value.getIaFlag(),
                value.getPaFlag(),
                value.getPersonalData());
    }
}
