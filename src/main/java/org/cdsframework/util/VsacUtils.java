/**
 * The MTS support cds project contains client related utilities, data transfer objects and remote EJB interfaces for communication with the CDS Framework Middle Tier Service.
 *
 * Copyright (C) 2016 New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * Contributions by HLN Consulting, LLC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. You should have received a copy of the GNU Lesser
 * General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/> for more details.
 *
 * The above-named contributors (HLN Consulting, LLC) are also licensed by the
 * New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * to have (without restriction, limitation, and warranty) complete irrevocable
 * access and rights to this project.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; THE
 *
 * SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING, BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * COPYRIGHT HOLDERS, IF ANY, OR DEVELOPERS BE LIABLE FOR ANY CLAIM, DAMAGES, OR
 * OTHER LIABILITY OF ANY KIND, ARISING FROM, OUT OF, OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information about this software, see
 * https://www.hln.com/services/open-source/ or send correspondence to
 * ice@hln.com.
 */
package org.cdsframework.util;

import ihe.iti.svs._2008.RetrieveMultipleValueSetsResponse;
import ihe.iti.svs._2008.RetrieveMultipleValueSetsResponse.DescribedValueSet;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.ValueSetCdsCodeRelDTO;
import org.cdsframework.dto.ValueSetDTO;
import org.cdsframework.enumeration.ValueSetType;
import org.cdsframework.exceptions.MtsException;
import org.cdsframework.util.vsac.ProfileList;
import org.cdsframework.util.vsac.VersionList;

public class VsacUtils {

    final static LogUtils logger = LogUtils.getLogger(VsacUtils.class);

    final static String connectTimeoutProperty = "jersey.config.client.connectTimeout";
    final static String requestTimeoutProperty = "jersey.config.client.readTimeout";
    final static int CONNECT_TIMEOUT = 5000; // Connection timeout in ms
    final static int REQUEST_TIMEOUT = 30000; // Request timeout in ms

    private static URI getBaseURI(String baseUri) {
        return UriBuilder.fromUri(baseUri).build();
    }

    public static String getNewTicketGrantingTicket(String baseUri, String username, String password)
            throws MtsException {

        final String METHODNAME = "getNewTicketGrantingTicket ";
        String value = null;
        Client client = null;
        Response response = null;

        if (logger.isDebugEnabled()) {
            logger.info(METHODNAME, "base uri: ", baseUri);
        }

        try {

            // Setup the service...
            URI uri = getBaseURI(baseUri);
            client = ClientBuilder.newClient();
            client.property(connectTimeoutProperty, CONNECT_TIMEOUT);
            client.property(requestTimeoutProperty, REQUEST_TIMEOUT);
            WebTarget target = client.target(uri);

            // Get the response...
            Form form = new Form();
            response = target.
                    path("ws").
                    path("Ticket").
                    queryParam("username", username).
                    queryParam("password", password).
                    request().
                    post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), Response.class);

            // If we have an http status of OK (200), parse the response...
            if (response.getStatus() == Status.OK.getStatusCode()) {
                value = response.readEntity(String.class);
                logger.info(METHODNAME, "Ticket Granting Ticket: ", value);
            } else {
                throw new MtsException("Error status code: " + response.getStatus() + " ; message = " + response.getStatusInfo().getReasonPhrase());
            }
        } catch (MtsException e) {
            logger.error(METHODNAME, e);
            throw new MtsException(e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }

        return value;
    }

    public static String getServiceTicket(String baseUri, String username, String password)
            throws MtsException {

        final String METHODNAME = "getServiceTicket ";
        String value = null;
        Client client = null;
        Response response = null;

        try {

            // Setup the service...
            URI uri = getBaseURI(baseUri);
            client = ClientBuilder.newClient();
            client.property(connectTimeoutProperty, CONNECT_TIMEOUT);
            client.property(requestTimeoutProperty, REQUEST_TIMEOUT);
            WebTarget target = client.target(uri);
            String ticketGrantingTicket = getNewTicketGrantingTicket(baseUri, username, password);

            // Get the response...
            Form form = new Form();
            form.param("service", "http://umlsks.nlm.nih.gov");
            response = target.
                    path("ws").
                    path("Ticket").
                    path(ticketGrantingTicket).
                    request().
                    post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), Response.class);

            // If we have an http status of OK (200), parse the response...
            if (response.getStatus() == Status.OK.getStatusCode()) {
                value = response.readEntity(String.class);
                logger.info(METHODNAME, "Service Ticket: ", value);
            } else {
                throw new MtsException("Error status code: " + response.getStatus() + " ; message = " + response.getStatusInfo().getReasonPhrase());
            }
        } catch (MtsException e) {
            logger.error(METHODNAME, e);
            throw new MtsException(e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }

        return value;
    }

    public static List<String> getProfileList(String baseUri, String username, String password)
            throws MtsException {

        final String METHODNAME = "getProfileList ";
        ProfileList list = null;
        Response response = null;
        Client client = null;

        try {

            // Setup the service...
            URI uri = getBaseURI(baseUri);
            client = ClientBuilder.newClient();
            client.property(connectTimeoutProperty, CONNECT_TIMEOUT);
            client.property(requestTimeoutProperty, REQUEST_TIMEOUT);
            WebTarget target = client.target(uri);
            String serviceTicket = getServiceTicket(baseUri, username, password);

            // Get the response...
            response = target.
                    queryParam("ticket", serviceTicket).
                    path("profiles").
                    request().
                    accept(MediaType.APPLICATION_XML).
                    get(Response.class);

            // If we have an http status of OK (200), parse the response...
            if (response.getStatus() == Status.OK.getStatusCode()) {
                list = response.readEntity(ProfileList.class);
                logger.info(METHODNAME, "profile list: ", (list == null ? "NULL" : list.toString()));
            }

        } catch (MtsException e) {
            logger.error(METHODNAME, e);
            throw new MtsException(e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }

        if (list != null) {
            return list.getProfiles();
        } else {
            return null;
        }
    }

    public static List<String> getVersionList(String baseUri, String username, String password, String oid)
            throws MtsException {

        final String METHODNAME = "getVersionList ";
        VersionList list = null;
        Response response = null;
        Client client = null;

        if (logger.isDebugEnabled()) {
            logger.info(METHODNAME, "BaseURI: ", baseUri);
            logger.info(METHODNAME, "oid: ", oid);
        }

        try {

            // Setup the service...
            URI uri = getBaseURI(baseUri);
            client = ClientBuilder.newClient();
            client.property(connectTimeoutProperty, CONNECT_TIMEOUT);
            client.property(requestTimeoutProperty, REQUEST_TIMEOUT);
            WebTarget target = client.target(uri);
            String serviceTicket = getServiceTicket(baseUri, username, password);

            logger.info(METHODNAME, "Service Ticket: ", serviceTicket);

            // Get the response...
            response = target.
                    queryParam("ticket", serviceTicket).
                    path("oid").
                    path(oid).
                    path("versions").
                    request().
                    accept(MediaType.APPLICATION_XML).
                    get(Response.class);

            if (logger.isDebugEnabled()) {
                if (response != null) {
                    logger.info(METHODNAME, "Response status: " + response.getStatus());
                }
            }

            // If we have an http status of OK (200), parse the response...
            if (response != null && response.getStatus() == Status.OK.getStatusCode()) {
                list = response.readEntity(VersionList.class);
                logger.info("Version List count: " + (list != null ? list.getVersions().size() : "null"));
            } else {
                if (response != null) {
                    throw new MtsException("Error status code: " + response.getStatus() + " ; message = " + response.getStatusInfo().getReasonPhrase());
                } else {
                    throw new MtsException("response was null!");
                }
            }

        } catch (MtsException e) {
            logger.error(METHODNAME, e);
            throw new MtsException(e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }

        if (list != null) {
            return list.getVersions();
        } else {
            return null;
        }
    }

    public static RetrieveMultipleValueSetsResponse getValueSetByOidAndProfile(String baseUri, String username, String password, String oid, String profile, boolean includeDraft)
            throws MtsException {

        final String METHODNAME = "getValueSetByOidAndVersion ";
        RetrieveMultipleValueSetsResponse value = null;
        Response response = null;
        Client client = null;
        String includeDraftParameter = (includeDraft == true ? "yes" : "no");

        try {

            // Setup the service...
            URI uri = getBaseURI(baseUri);
            client = ClientBuilder.newClient();
            client.property(connectTimeoutProperty, CONNECT_TIMEOUT);
            client.property(requestTimeoutProperty, REQUEST_TIMEOUT);
            WebTarget target = client.target(uri);
            String serviceTicket = getServiceTicket(baseUri, username, password);

            // Get the response...
            if (includeDraft == true) {
                response = target.
                        queryParam("id", oid).
                        queryParam("profile", profile).
                        queryParam("includeDraft", includeDraftParameter).
                        queryParam("ticket", serviceTicket).
                        path("svs").
                        path("RetrieveMultipleValueSets").
                        request().
                        accept(MediaType.APPLICATION_XML).
                        get(Response.class);
            } else {
                response = target.
                        queryParam("id", oid).
                        queryParam("profile", profile).
                        queryParam("ticket", serviceTicket).
                        path("svs").
                        path("RetrieveMultipleValueSets").
                        request().
                        accept(MediaType.APPLICATION_XML).
                        get(Response.class);
            }

            // If we have an http status of OK (200), parse the response...
            if (response.getStatus() == Status.OK.getStatusCode()) {
                value = response.readEntity(RetrieveMultipleValueSetsResponse.class);
            } else {
                throw new MtsException("Error status code: " + response.getStatus() + " ; message = " + response.getStatusInfo().getReasonPhrase());
            }

        } catch (MtsException e) {
            logger.error(METHODNAME, e);
            throw new MtsException(e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }

        return value;
    }

    public static RetrieveMultipleValueSetsResponse getValueSetByOidAndVersion(String baseUri, String username, String password, String oid, String version)
            throws MtsException {

        final String METHODNAME = "getValueSetByOidAndVersion ";
        RetrieveMultipleValueSetsResponse value = null;
        Response response = null;
        Client client = null;

        try {

            // Setup the service...
            URI uri = getBaseURI(baseUri);
            client = ClientBuilder.newClient();
            client.property(connectTimeoutProperty, CONNECT_TIMEOUT);
            client.property(requestTimeoutProperty, REQUEST_TIMEOUT);
            WebTarget target = client.target(uri);
            String serviceTicket = getServiceTicket(baseUri, username, password);

            // Get the response...
            response = target.
                    queryParam("id", oid).
                    queryParam("version", version).
                    queryParam("ticket", serviceTicket).
                    path("svs").
                    path("RetrieveMultipleValueSets").
                    request().
                    accept(MediaType.APPLICATION_XML).
                    get(Response.class);

            // If we have an http status of OK (200), parse the response...
            if (response.getStatus() == Status.OK.getStatusCode()) {
                value = response.readEntity(RetrieveMultipleValueSetsResponse.class);
            } else {
                throw new MtsException("Error status code: " + response.getStatus() + " ; message = " + response.getStatusInfo().getReasonPhrase());
            }

        } catch (MtsException e) {
            logger.error(METHODNAME, e);
            throw new MtsException(e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            if (client != null) {
                client.close();
            }
        }

        return value;
    }

    /**
     * Retrieve a value set DTO from a response from the VSAC service.
     *
     * @param vsacData
     * @param profile
     * @param version
     * @return
     * @throws MtsException
     */
    public static ValueSetDTO getValueSetFromVsacData(RetrieveMultipleValueSetsResponse vsacData, String profile, String version)
            throws MtsException {

        final String METHODNAME = "getValueSetFromVsacData ";
        ValueSetDTO valueSetDTO = null;

        logger.info(METHODNAME, "about to translate vsac data into value set");

        // clean up profile
        if (profile != null) {
            try {
                profile = URLDecoder.decode(profile, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error(METHODNAME, e);
            }
        }

        // clean up version
        if (version != null) {
            try {
                version = URLDecoder.decode(version, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error(METHODNAME, e);
            }
        }

        logger.info(METHODNAME, "profile: ", profile);
        logger.info(METHODNAME, "version: ", version);

        if (vsacData == null) {
            logger.info(METHODNAME, "cannot translate - vsac data is null");
        } else {
            try {
                logger.info(METHODNAME, "about to process code system: ", vsacData.getDescribedValueSet().getID());

                // Get the value set from the supplied vsac data.
                DescribedValueSet vsacValueSet = vsacData.getDescribedValueSet();
                logger.info(METHODNAME, "about to process display name: ", vsacValueSet.getDisplayName());
                logger.info(METHODNAME, "about to process purpose: ", vsacValueSet.getPurpose());
                logger.info(METHODNAME, "about to process version: ", vsacValueSet.getVersion());
                logger.info(METHODNAME, "about to process status: ", vsacValueSet.getStatus());
                logger.info(METHODNAME, "about to process source: ", vsacValueSet.getSource());

                // Translate the supplied data from vsac to a value set object. Despite the name of the 
                // generated class (RetrieveMultipleValueSetsResponse), the data returned from the vsac
                // service only contains one value set.
                // Translate the value set data from vsac...
                valueSetDTO = new ValueSetDTO();
                valueSetDTO.setCode(vsacValueSet.getID());
                valueSetDTO.setName(vsacValueSet.getDisplayName());
                valueSetDTO.setOid(vsacValueSet.getID());
                valueSetDTO.setDescription(vsacValueSet.getPurpose());
                if ("Draft".equalsIgnoreCase(version) || StringUtils.isEmpty(version)) {
                    if (!StringUtils.isEmpty(profile)) {
                        valueSetDTO.setVersion(profile);
                        valueSetDTO.setVersionStatus("Draft");
                    }
                } else {
                    valueSetDTO.setVersion(vsacValueSet.getVersion());
                    valueSetDTO.setVersionStatus("Published");
                }
                valueSetDTO.setVersionDescription(vsacValueSet.getDisplayName());
                valueSetDTO.setValueSetType(ValueSetType.STATIC);
                //valueSetDTO.setValueSetType(vsacValueSet.getBinding());
                valueSetDTO.setSource(vsacValueSet.getSource());

                // Add the code (concept) data from the vsac data set...
                if (vsacValueSet.getConceptList().getConcepts() != null) {
                    for (RetrieveMultipleValueSetsResponse.DescribedValueSet.ConceptList.Concept concept : vsacValueSet.getConceptList().getConcepts()) {
                        ValueSetCdsCodeRelDTO valueSetCdsCodeRelDTO = new ValueSetCdsCodeRelDTO();
                        CdsCodeDTO cdsCodeDTO = new CdsCodeDTO();
                        valueSetCdsCodeRelDTO.setCdsCodeDTO(cdsCodeDTO);
                        valueSetDTO.addOrUpdateChildDTO(valueSetCdsCodeRelDTO);
                        cdsCodeDTO.setCode(concept.getCode());
                        cdsCodeDTO.setDisplayName(concept.getDisplayName());
                        cdsCodeDTO.setCodeSystem(concept.getCodeSystem());
                        cdsCodeDTO.setCodeSystemName(concept.getCodeSystemName());
                        logger.info(METHODNAME, "Adding concept code from vsac: ", concept.getCode());
                        logger.info(METHODNAME, "Adding concept display name from vsac: ", concept.getDisplayName());
                        logger.info(METHODNAME, "Adding concept code system from vsac: ", concept.getCodeSystem());
                        logger.info(METHODNAME, "Adding concept code system name from vsac: ", concept.getCodeSystemName());
                    }
                }

            } catch (Exception e) {
                logger.error(METHODNAME, e);
                throw new MtsException(e.getMessage());
            }
        }

        return valueSetDTO;
    }

    /**
     * Return existing ValueSet from a list of candidates.
     *
     * @param results
     * @param version
     * @param type
     * @return
     */
    public static ValueSetDTO getExistingValueSetFromList(List<ValueSetDTO> results, String version, String type) {
        final String METHODNAME = "getExistingValueSetFromList ";
        ValueSetDTO existingValueSet = null;
        logger.info(METHODNAME, "results=", results);
        logger.info(METHODNAME, "version=", version);
        logger.info(METHODNAME, "type=", type);

        // scenario #3
        for (ValueSetDTO resultItem : results) {
            if (("ACTIVE".equalsIgnoreCase(resultItem.getVersionStatus()) || "PUBLISHED".equalsIgnoreCase(resultItem.getVersionStatus()))
                    && "PUBLISHED".equalsIgnoreCase(type)
                    && version.equalsIgnoreCase(resultItem.getVersion())) {
                logger.info(METHODNAME, "found production and version match. scenario #3");
                existingValueSet = resultItem;
                break;
            }
        }

        // scenario #2
        if (existingValueSet == null) {
            for (ValueSetDTO resultItem : results) {
                if ((("ACTIVE".equalsIgnoreCase(resultItem.getVersionStatus()) && "N/A".equalsIgnoreCase(resultItem.getVersion()))
                        || "DRAFT".equalsIgnoreCase(resultItem.getVersionStatus()))
                        && !"DRAFT".equalsIgnoreCase(type)) {
                    logger.info(METHODNAME, "found draft match for published. scenario #2");
                    existingValueSet = resultItem;
                    break;
                }
            }
        }

        // scenario #1
        if (existingValueSet == null) {
            for (ValueSetDTO resultItem : results) {
                if ((("ACTIVE".equalsIgnoreCase(resultItem.getVersionStatus()) && "N/A".equalsIgnoreCase(resultItem.getVersion()))
                        || "DRAFT".equalsIgnoreCase(resultItem.getVersionStatus()))
                        && "DRAFT".equalsIgnoreCase(type)) {
                    logger.info(METHODNAME, "found draft match for draft. scenario #1");
                    existingValueSet = resultItem;
                    break;
                }
            }
        }

        if (existingValueSet != null) {
            logger.info(METHODNAME, "existingValueSet.getName()=", existingValueSet.getName());
            logger.info(METHODNAME, "existingValueSet.getOid()=", existingValueSet.getOid());
            logger.info(METHODNAME, "existingValueSet.getCode()=", existingValueSet.getCode());
            logger.info(METHODNAME, "existingValueSet.getDescription()=", existingValueSet.getDescription());
            logger.info(METHODNAME, "existingValueSet.getVersion()=", existingValueSet.getVersion());
            logger.info(METHODNAME, "existingValueSet.getVersionStatus()=", existingValueSet.getVersionStatus());
            logger.info(METHODNAME, "existingValueSet.getVersionDescription()=", existingValueSet.getVersionDescription());
        }
        return existingValueSet;
    }
}
