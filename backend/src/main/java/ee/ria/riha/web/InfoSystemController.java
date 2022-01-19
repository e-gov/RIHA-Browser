package ee.ria.riha.web;

import ee.ria.riha.conf.FeedbackServiceConnectionProperties;
import ee.ria.riha.domain.model.InfoSystem;
import ee.ria.riha.domain.model.RelationType;
import ee.ria.riha.logging.auditlog.AuditEvent;
import ee.ria.riha.logging.auditlog.AuditLogger;
import ee.ria.riha.logging.auditlog.AuditType;
import ee.ria.riha.service.FileService;
import ee.ria.riha.service.InfoSystemDataObjectService;
import ee.ria.riha.service.InfoSystemService;
import ee.ria.riha.service.RelationService;
import ee.ria.riha.service.auth.PreAuthorizeInfoSystemOwner;
import ee.ria.riha.service.auth.PrincipalHasRoleProducer;
import ee.ria.riha.storage.util.*;
import ee.ria.riha.web.model.InfoSystemDataObjectModel;
import ee.ria.riha.web.model.InfoSystemModel;
import ee.ria.riha.web.model.RelationModel;
import ee.ria.riha.web.model.StandardRealisationCreationModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static ee.ria.riha.conf.ApplicationProperties.API_V1_PREFIX;
import static java.util.stream.Collectors.toList;


@RequiredArgsConstructor
@RestController
@RequestMapping(API_V1_PREFIX + "/systems")

@Api("Information systems")
public class InfoSystemController {

    @Autowired
    private InfoSystemService infoSystemService;

    @Autowired
    private InfoSystemDataObjectService infoSystemDataObjectService;

    @Autowired
    private InfoSystemModelMapper infoSystemModelMapper;

    @Autowired
    private InfoSystemDataObjectMapper infoSystemDataObjectMapper;

    @Autowired
    private RelationService relationService;

    @Autowired
    private FileService fileService;

    private final FeedbackServiceConnectionProperties feedbackServiceConnectionProperties;

    private final AuditLogger auditLogger;

    @GetMapping
    @ApiOperation("List all existing information systems")
    @ApiPageableAndFilterableParams
    public ResponseEntity<PagedResponse<InfoSystemModel>> list(Pageable pageable, Filterable filterable) {
        return ResponseEntity.ok(
                createPagedModel(
                        infoSystemService.list(pageable, filterable),
                        infoSystemModelMapper));
    }
    @GetMapping("/autocomplete")
    @ApiOperation("List all existing information systems for autocomplete")
    public ResponseEntity<PagedResponse<InfoSystemModel>> autocomplete(@RequestParam("searchTerm") String searchTerm) {

        String paramToRestEndpoint;
        if (StringUtils.isNumeric(searchTerm)) {
            paramToRestEndpoint = "'" + searchTerm + "'";
        } else {
            paramToRestEndpoint = searchTerm;
        }

        PageRequest pageable = new PageRequest(0, 10);
        FilterRequest shortNameExact = new FilterRequest("short_name,ilike," + paramToRestEndpoint, "desc", "id");
        FilterRequest fuzzyNameExact = new FilterRequest("short_name,ilike,%" + searchTerm + "%", "desc", "id");
        FilterRequest nameExact = new FilterRequest("name,ilike," + paramToRestEndpoint, "desc", "id");
        FilterRequest nameFuzzy = new FilterRequest("name,ilike,%" + searchTerm + "%", "desc", "id");

        List<InfoSystem> foundResults = new ArrayList<>();
        for (FilterRequest filterRequest : Arrays.asList(shortNameExact, fuzzyNameExact, nameExact, nameFuzzy)) {

            searchInfoSystemsByFilter(pageable, filterRequest)
                    .forEach(infoSystem ->  {
                            if (!foundResults.contains(infoSystem)) {
                                foundResults.add(infoSystem);
                            }
            });

            if (foundResults.size() >= pageable.getPageSize()) {
                return getResponseEntity(pageable, foundResults.subList(0, pageable.getPageSize()));
            }
        }

        return getResponseEntity(pageable, foundResults);
    }

    private ResponseEntity getResponseEntity(PageRequest pageable, List<InfoSystem> response) {
        return ResponseEntity.ok(
                new PagedResponse(
                        pageable,
                        response.size(),
                        response.stream()
                                .map(infoSystemModelMapper::map)
                                .collect(toList())));
    }

    private List<InfoSystem> searchInfoSystemsByFilter(PageRequest pageable, FilterRequest filterRequest) {
        PagedResponse<InfoSystem> exactMatchesShortNames = infoSystemService.list(pageable, filterRequest);
        return exactMatchesShortNames.getContent() == null
                ? Collections.emptyList()
                : exactMatchesShortNames.getContent();
    }

    private Filterable createExactMatchFilterFromILikeFilter(Filterable filterable) {
        return new FilterRequest(
                filterable.getFilter().replaceAll("%", ""),
                filterable.getSort(),
                filterable.getFields());
    }

    @GetMapping(path = "/data-objects")
    @ApiOperation("List all existing information systems data objects")
    @ApiPageableAndFilterableParams
    public ResponseEntity<PagedResponse<InfoSystemDataObjectModel>> listDataObjects(Pageable pageable, Filterable filterable) {
        return ResponseEntity.ok(
                createPagedModel(
                        infoSystemDataObjectService.list(pageable, filterable),
                        infoSystemDataObjectMapper));
    }

    private  <E,R> PagedResponse<R> createPagedModel(PagedResponse<E> list, ModelMapper<E,R> mapper) {
        return new PagedResponse<>(new PageRequest(list.getPage(), list.getSize()),
                                   list.getTotalElements(),
                                   list.getContent().stream()
                                           .map(mapper::map)
                                           .collect(toList()));
    }

    @PostMapping
    @ApiOperation("Create new information system")
    @PrincipalHasRoleProducer
    public ResponseEntity<InfoSystemModel> create(@RequestBody InfoSystemModel model, HttpServletRequest request) {
        InfoSystem infoSystem = infoSystemService.create(new InfoSystem(model.getJson()));
        auditLogger.log(AuditEvent.CREATE, AuditType.INFOSYSTEM, request, model);
        return ResponseEntity.ok(infoSystemModelMapper.map(infoSystem));
    }

    @GetMapping("/{reference}")
    @ApiOperation("Get existing information system")
    public ResponseEntity<InfoSystemModel> get(@PathVariable("reference") String reference) {
        InfoSystem infoSystem = infoSystemService.get(reference);
        return ResponseEntity.ok(infoSystemModelMapper.map(infoSystem));
    }

    @PutMapping("/{reference}")
    @PreAuthorizeInfoSystemOwner
    @ApiOperation("Update existing information system")
    public ResponseEntity<InfoSystemModel> update(@PathVariable("reference") String reference,
                                                  @RequestBody InfoSystemModel model, HttpServletRequest request) {
        InfoSystem infoSystem = infoSystemService.update(reference, new InfoSystem(model.getJson()));
        auditLogger.log(AuditEvent.CREATE, AuditType.INFOSYSTEM, request, model);
        return ResponseEntity.ok(infoSystemModelMapper.map(infoSystem));
    }

    @PostMapping("/{reference}/create-standard-realisation-system")
    @ApiOperation("Create new realisation for standard information system")
    @PrincipalHasRoleProducer
    public ResponseEntity<InfoSystemModel> createStandardInformationSystem(@PathVariable("reference") String reference,
                                                                           @RequestBody StandardRealisationCreationModel standardRealisationCreationModel) {
        InfoSystem existingInfoSystem = infoSystemService.get(reference);

        if (existingInfoSystem == null) {
            return ResponseEntity.badRequest().build();
        }

        InfoSystem newlyCreatedInfoSystem = existingInfoSystem.copy();
        newlyCreatedInfoSystem.setStandardInformationSystemUndefined();
        newlyCreatedInfoSystem.setShortName(standardRealisationCreationModel.getShortName());
        newlyCreatedInfoSystem.setDifferences(standardRealisationCreationModel.getDifferences());
        newlyCreatedInfoSystem.setFullName(standardRealisationCreationModel.getName());
        newlyCreatedInfoSystem.setPurpose(standardRealisationCreationModel.getPurpose());

        newlyCreatedInfoSystem.clearContacts();
        newlyCreatedInfoSystem.clearSecuritySection();

        newlyCreatedInfoSystem.removeTopic("standardlahendus");

        newlyCreatedInfoSystem = infoSystemService.create(newlyCreatedInfoSystem);

        relationService.createRelation(
                standardRealisationCreationModel.getShortName(),
                RelationModel.builder()
                        .infoSystemShortName(reference)
                        .type(RelationType.USED_SYSTEM)
                        .build()
        );

        //for all existing files, should create new entry into file_resource table with new info system UUID
        fileService.updateFilesUuid(newlyCreatedInfoSystem, existingInfoSystem.getUuid());
        infoSystemService.update(newlyCreatedInfoSystem.getUuid().toString(), newlyCreatedInfoSystem);

        return ResponseEntity.ok(infoSystemModelMapper.map(newlyCreatedInfoSystem));
    }

    @GetMapping("/mail-sender-test")
    public ResponseEntity<String> mailSenderTest(){
        Properties prop=new Properties();
        prop.put("mail.smtp.host", "smtp.riaint.ee");
        prop.put("mail.smtp.port", "25");
        prop.put("mail.smtp.starttls.enable","true");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/localcerts/riha-browser.pem");
        System.setProperty("javax.net.ssl.keyStorePassword", Arrays.toString(feedbackServiceConnectionProperties.getKeyStoreKeyPassword()));

        Session session = Session.getDefaultInstance(prop);

        session.setDebug(true);

        try {
            String htmlBody = "<strong>This is an HTML Message</strong>";
            String textBody = "This is a Text Message.";
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("riha-dev@riha.ee"));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("makar.shokarev@wisercat.net"));
            message.setSubject("Testing Subject");
            message.setText(htmlBody);
            message.setContent(textBody, "text/html");
            Transport.send(message);

            System.out.println("Done");

            return ResponseEntity.ok("message send");

        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.ok(e.toString());
        }
    }


}
