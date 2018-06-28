/**
 * The MTS support cds project contains client related utilities, data transfer objects and remote EJB interfaces for communication with the CDS Framework Middle Tier Service.
 *
 * Copyright (C) 2016 New York City Department of Health and Mental Hygiene, Bureau of Immunization
 * Contributions by HLN Consulting, LLC
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version. You should have received a copy of the GNU Lesser
 * General Public License along with this program. If not, see <http://www.gnu.org/licenses/> for more
 * details.
 *
 * The above-named contributors (HLN Consulting, LLC) are also licensed by the New York City
 * Department of Health and Mental Hygiene, Bureau of Immunization to have (without restriction,
 * limitation, and warranty) complete irrevocable access and rights to this project.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; THE
 *
 * SOFTWARE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING,
 * BUT NOT LIMITED TO, WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE COPYRIGHT HOLDERS, IF ANY, OR DEVELOPERS BE LIABLE FOR
 * ANY CLAIM, DAMAGES, OR OTHER LIABILITY OF ANY KIND, ARISING FROM, OUT OF, OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * For more information about this software, see https://www.hln.com/services/open-source/ or send
 * correspondence to ice@hln.com.
 */
package org.cdsframework.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.cdsframework.annotation.Column;
import org.cdsframework.annotation.Entity;
import org.cdsframework.annotation.GeneratedValue;
import org.cdsframework.annotation.Id;
import org.cdsframework.annotation.JndiReference;
import org.cdsframework.annotation.Permission;
import org.cdsframework.annotation.ReferenceDTO;
import org.cdsframework.annotation.Table;
import org.cdsframework.aspect.annotations.PropertyListener;
import org.cdsframework.base.BaseDTO;
import org.cdsframework.enumeration.GenerationSource;

/**
 *
 * @author HLN Consulting, LLC
 */
@Entity
@Table(databaseId = "CDS", name = "opencds_concept_rel")
@JndiReference(root = "mts-ejb-cds")
@Permission(name = "Concept/Code Relationship")
public class OpenCdsConceptRelDTO extends BaseDTO {

    public enum MappingType {

        CODE("Code"),
        CODE_SYSTEM("Code System"),
        VALUE_SET("Value Set");

        private final String label;

        private MappingType(String label) {
            this.label = label;
        }

        /**
         * Get the value of label
         *
         * @return the value of label
         */
        public String getLabel() {
            return label;
        }

    }

    public interface ByOpenCdsConceptId {
    }

    public interface ByCodeId {
    }

    public interface ByOpenCdsConceptIdCodeIdConceptDeterminationMethodId {
    }

    public interface CountByConceptCodeIdCodeSystemId {
    }
    private static final long serialVersionUID = -5142363253535643619L;
    @GeneratedValue(source = GenerationSource.AUTO)
    @Id
    private String relationshipId;
    @GeneratedValue(source = GenerationSource.FOREIGN_CONSTRAINT, sourceClass = OpenCdsConceptDTO.class)
    private String conceptCodeId;
    @Column(name = "cds_code_id")
    @ReferenceDTO(isNotFoundAllowed = false)
    private CdsCodeDTO cdsCodeDTO;
    @Column(name = "determination_method")
    @ReferenceDTO(isNotFoundAllowed = false)
    @NotNull
    private ConceptDeterminationMethodDTO conceptDeterminationMethodDTO;
    @Column(name = "code_system_id")
    @ReferenceDTO(isNotFoundAllowed = false)
    private CdsCodeSystemDTO cdsCodeSystemDTO;
    @Column(name = "value_set_id")
    @ReferenceDTO(isNotFoundAllowed = false)
    private ValueSetDTO valueSetDTO;
    @Size(max = 512)
    private String specificationNotes;
    @NotNull
    private MappingType mappingType;

    /**
     * Get the value of valueSetDTO
     *
     * @return the value of valueSetDTO
     */
    public ValueSetDTO getValueSetDTO() {
        return valueSetDTO;
    }

    /**
     * Set the value of valueSetDTO
     *
     * @param valueSetDTO new value of valueSetDTO
     */
    @PropertyListener
    public void setValueSetDTO(ValueSetDTO valueSetDTO) {
        this.valueSetDTO = valueSetDTO;
    }

    /**
     * Get the value of cdsCodeSystemDTO
     *
     * @return the value of cdsCodeSystemDTO
     */
    public CdsCodeSystemDTO getCdsCodeSystemDTO() {
        return cdsCodeSystemDTO;
    }

    /**
     * Set the value of cdsCodeSystemDTO
     *
     * @param cdsCodeSystemDTO new value of cdsCodeSystemDTO
     */
    @PropertyListener
    public void setCdsCodeSystemDTO(CdsCodeSystemDTO cdsCodeSystemDTO) {
        this.cdsCodeSystemDTO = cdsCodeSystemDTO;
    }

    /**
     * Get the value of specificationNotes
     *
     * @return the value of specificationNotes
     */
    public String getSpecificationNotes() {
        return specificationNotes;
    }

    /**
     * Set the value of specificationNotes
     *
     * @param specificationNotes new value of specificationNotes
     */
    @PropertyListener
    public void setSpecificationNotes(String specificationNotes) {
        this.specificationNotes = specificationNotes;
    }

    /**
     * Get the value of conceptDeterminationMethodDTO
     *
     * @return the value of conceptDeterminationMethodDTO
     */
    public ConceptDeterminationMethodDTO getConceptDeterminationMethodDTO() {
        return conceptDeterminationMethodDTO;
    }

    /**
     * Set the value of conceptDeterminationMethodDTO
     *
     * @param conceptDeterminationMethodDTO new value of conceptDeterminationMethodDTO
     */
    @PropertyListener
    public void setConceptDeterminationMethodDTO(ConceptDeterminationMethodDTO conceptDeterminationMethodDTO) {
        this.conceptDeterminationMethodDTO = conceptDeterminationMethodDTO;
    }

    /**
     * Get the value of cdsCodeDTO
     *
     * @return the value of cdsCodeDTO
     */
    public CdsCodeDTO getCdsCodeDTO() {
        return cdsCodeDTO;
    }

    /**
     * Set the value of cdsCodeDTO
     *
     * @param cdsCodeDTO new value of cdsCodeDTO
     */
    @PropertyListener
    public void setCdsCodeDTO(CdsCodeDTO cdsCodeDTO) {
        this.cdsCodeDTO = cdsCodeDTO;
    }

    /**
     * Get the value of conceptCodeId
     *
     * @return the value of conceptCodeId
     */
    public String getConceptCodeId() {
        return conceptCodeId;
    }

    /**
     * Set the value of conceptCodeId
     *
     * @param conceptCodeId new value of conceptCodeId
     */
    @PropertyListener
    public void setConceptCodeId(String conceptCodeId) {
        this.conceptCodeId = conceptCodeId;
    }

    /**
     * Get the value of relationshipId
     *
     * @return the value of relationshipId
     */
    public String getRelationshipId() {
        return relationshipId;
    }

    /**
     * Set the value of relationshipId
     *
     * @param relationshipId new value of relationshipId
     */
    @PropertyListener
    public void setRelationshipId(String relationshipId) {
        this.relationshipId = relationshipId;
    }

    /**
     * Get the value of mappingType
     *
     * @return the value of mappingType
     */
    public MappingType getMappingType() {
        return mappingType;
    }

    /**
     * Set the value of mappingType
     *
     * @param mappingType new value of mappingType
     */
    @PropertyListener
    public void setMappingType(MappingType mappingType) {
        this.mappingType = mappingType;
    }
}
