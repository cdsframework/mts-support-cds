package org.cdsframework.util;

import java.io.StringReader;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.cdsframework.client.MtsMGRClient;
import org.cdsframework.dto.ConceptDeterminationMethodDTO;
import org.cdsframework.dto.PropertyBagDTO;
import org.cdsframework.dto.SessionDTO;
import org.cdsframework.dto.SystemPropertyDTO;
import org.cdsframework.enumeration.DeploymentEnvironment;
import org.cdsframework.exceptions.AuthenticationException;
import org.cdsframework.exceptions.AuthorizationException;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.exceptions.NotFoundException;
import org.cdsframework.exceptions.ValidationException;
import org.opencds.config.schema.ConceptDeterminationMethods;

/**
 *
 * @author sdn
 */
public class ConceptUtils {

    private static final LogUtils logger = LogUtils.getLogger(ConceptUtils.class);

    public static String getDefaultCdmCode(SessionDTO sessionDTO) throws MtsException, ValidationException,
            NotFoundException, AuthenticationException, AuthorizationException {

        final String METHODNAME = "getDefaultCdmCode ";
        PropertyBagDTO systemPropertyBagDTO = new PropertyBagDTO();
        systemPropertyBagDTO.setQueryClass("ByNameScope");
        SystemPropertyDTO systemPropertyDTO = new SystemPropertyDTO();
        systemPropertyDTO.setScope("cds");
        systemPropertyDTO.setName("DEFAULT_CDM");

        SystemPropertyDTO codeProperty = MtsMGRClient.getGeneralMGR().findByQuery(systemPropertyDTO, sessionDTO,
                systemPropertyBagDTO);

        if (codeProperty == null) {
            throw new MtsException("codeProperty is null!");
        }
        logger.debug(METHODNAME, "codeProperty.getValue()=", codeProperty.getValue());

        return codeProperty.getValue();
    }

    public static String getDefaultCdmCodeSystem(SessionDTO sessionDTO) throws MtsException, ValidationException,
            NotFoundException, AuthenticationException, AuthorizationException {

        final String METHODNAME = "getDefaultCdmCodeSystem ";
        PropertyBagDTO systemPropertyBagDTO = new PropertyBagDTO();
        systemPropertyBagDTO.setQueryClass("ByNameScope");
        SystemPropertyDTO systemPropertyDTO = new SystemPropertyDTO();
        systemPropertyDTO.setScope("cds");
        systemPropertyDTO.setName("DEFAULT_CDM_CODE_SYSTEM");

        SystemPropertyDTO codeSystemProperty = MtsMGRClient.getGeneralMGR().findByQuery(systemPropertyDTO, sessionDTO,
                systemPropertyBagDTO);

        if (codeSystemProperty == null) {
            throw new MtsException("codeSystemProperty is null!");
        }
        logger.debug(METHODNAME, "codeSystemProperty.getValue()=", codeSystemProperty.getValue());

        return codeSystemProperty.getValue();
    }

    public static String determineAccept(String accept) {
        final String METHODNAME = "determineAccept ";
        String result = MediaType.APPLICATION_JSON;
        if (!StringUtils.isEmpty(accept) && !accept.equalsIgnoreCase(MediaType.APPLICATION_JSON)) {
            if (accept.equalsIgnoreCase(MediaType.APPLICATION_XML)) {
                result = MediaType.APPLICATION_XML;
            } else {
                logger.error(METHODNAME, "unsupported accept=", accept);
            }
        }
        logger.info(METHODNAME, "accept=", result);
        return result;
    }

    public static DeploymentEnvironment checkConceptInput(String environment, String codeSystem, String code,
            String sessionId, String method) {
        final String METHODNAME = "checkConceptInput ";
        logger.info(METHODNAME, "environment=", environment);
        logger.info(METHODNAME, "codeSystem=", codeSystem);
        logger.info(METHODNAME, "code=", code);
        logger.info(METHODNAME, "sessionId=", sessionId);
        logger.info(METHODNAME, "method=", method);
        DeploymentEnvironment deploymentEnvironment;
        if (StringUtils.isEmpty(environment)) {
            throw new IllegalStateException("environment is empty");
        }
        if (StringUtils.isEmpty(codeSystem)) {
            throw new IllegalStateException("codeSystem is empty");
        }
        if (StringUtils.isEmpty(code)) {
            throw new IllegalStateException("code is empty");
        }
        if (StringUtils.isEmpty(sessionId)) {
            throw new IllegalStateException("sessionId is empty");
        }
        deploymentEnvironment = DeploymentEnvironment.valueOf(environment.toUpperCase());

        if (null == deploymentEnvironment) {
            logger.error(METHODNAME, "deploymentEnvironment was null!");
            throw new IllegalStateException("deploymentEnvironment was null!");
        }
        logger.info(METHODNAME, "deploymentEnvironment=", deploymentEnvironment);
        return deploymentEnvironment;
    }

    public static Map<String, byte[]> getExportData(String codeSystem, String code,
            DeploymentEnvironment environment, SessionDTO sessionDTO) throws MtsException,
            ValidationException, NotFoundException, AuthenticationException, AuthorizationException {
        ConceptDeterminationMethodDTO conceptDeterminationMethodDTO = new ConceptDeterminationMethodDTO();
        conceptDeterminationMethodDTO.setCode(code);
        conceptDeterminationMethodDTO.getQueryMap().put("codeSystem", codeSystem);
        conceptDeterminationMethodDTO.getQueryMap().put("environment", environment);

        PropertyBagDTO propertyBagDTO = new PropertyBagDTO();
        propertyBagDTO.setQueryClass("OpenCdsExport");

        return MtsMGRClient.getGeneralMGR().exportData(conceptDeterminationMethodDTO, sessionDTO, propertyBagDTO);
    }

    public static ConceptDeterminationMethods getConceptDeterminationMethods(String input) throws JAXBException {
        // final String METHODNAME = "getConceptDeterminationMethods ";
        JAXBContext jaxbContext = JAXBContext.newInstance(ConceptDeterminationMethods.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        StringReader reader = new StringReader(input);
        return (ConceptDeterminationMethods) unmarshaller.unmarshal(reader);
    }

    public static Response preDeployCdm(final String accept, final String environment, final String codeSystem,
            final String code, final String sessionId) {

        final String METHODNAME = "preDeployCdm ";

        String requestAccept = determineAccept(accept);

        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setSessionId(sessionId);

        try {
            DeploymentEnvironment deploymentEnvironment = checkConceptInput(environment, codeSystem, code, sessionId,
                    METHODNAME);
            Map<String, byte[]> exportData = getExportData(codeSystem, code, deploymentEnvironment, sessionDTO);

            String xml = new String(exportData.get("cdm.xml"));
            switch (requestAccept) {
                case MediaType.APPLICATION_JSON:
                    return Response.ok(getConceptDeterminationMethods(xml), MediaType.APPLICATION_JSON).build();
                case MediaType.APPLICATION_XML:
                    return Response.ok(xml, MediaType.APPLICATION_XML).build();
                default:
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity(String.format("Unknown response type: %s", requestAccept))
                            .type(MediaType.APPLICATION_JSON).build();
            }
        } catch (MtsException | ValidationException | NotFoundException | AuthenticationException
                | AuthorizationException | JAXBException | RuntimeException e) {
            logger.error(e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage()).type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}
