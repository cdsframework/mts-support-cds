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
package org.cdsframework.base;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.cdsframework.annotation.Column;
import org.cdsframework.annotation.ReferenceDTO;
import org.cdsframework.aspect.annotations.PropertyListener;
import org.cdsframework.dto.CdsCodeDTO;
import org.cdsframework.dto.OpenCdsConceptDTO;
import org.cdsframework.interfaces.PredicatePartConceptInterface;

/**
 *
 * @author HLN Consulting, LLC
 */
 @JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
public abstract class BasePredicatePartConceptDTO extends BaseDTO implements PredicatePartConceptInterface {

    private static final long serialVersionUID = 3607341003048445831L;

    @ReferenceDTO
    @Column(name = "code_id")
    private CdsCodeDTO cdsCodeDTO;
    @ReferenceDTO
    @Column(name = "concept_id")
    private OpenCdsConceptDTO openCdsConceptDTO;
    
    @Override
    public CdsCodeDTO getCdsCodeDTO() {
        return cdsCodeDTO;
    }

    @PropertyListener
    public void setCdsCodeDTO(CdsCodeDTO cdsCodeDTO) {
        this.cdsCodeDTO = cdsCodeDTO;
    }

    @Override
    public OpenCdsConceptDTO getOpenCdsConceptDTO() {
        return openCdsConceptDTO;
    }

    @PropertyListener
    public void setOpenCdsConceptDTO(OpenCdsConceptDTO openCdsConceptDTO) {
        this.openCdsConceptDTO = openCdsConceptDTO;
    }
}
