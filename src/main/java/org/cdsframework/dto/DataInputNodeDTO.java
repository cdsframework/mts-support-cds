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
import org.cdsframework.annotation.Entity;
import org.cdsframework.annotation.GeneratedValue;
import org.cdsframework.annotation.Id;
import org.cdsframework.annotation.JndiReference;
import org.cdsframework.annotation.OrderBy;
import org.cdsframework.annotation.Permission;
import org.cdsframework.annotation.Table;
import org.cdsframework.aspect.annotations.PropertyListener;
import org.cdsframework.base.BaseDTO;
import org.cdsframework.enumeration.GenerationSource;
import org.cdsframework.util.comparator.DataTemplateNodeRelComparator;

/**
 *
 * @author HLN Consulting, LLC
 */
@Entity
@OrderBy(comparator = DataTemplateNodeRelComparator.class)
@Table(databaseId = "CDS", name = "data_input_node")
@JndiReference(root = "mts-ejb-cds")
@Permission(name = "Data Template Node Relationship", isListed = false)
public class DataInputNodeDTO extends BaseDTO {

    private static final long serialVersionUID = 8260365417310511301L;

    public interface ByTemplateId {
    }

    @GeneratedValue(source = GenerationSource.AUTO)
    @Id
    private String nodeId;
    @Size(max = 2048)
    @NotNull
    private String nodePath;
    @Size(max = 512)
    @NotNull
    private String templateName;
    @Size(max = 1024)
    @NotNull
    private String templateClassName;
    @Size(max = 512)
    @NotNull
    private String attributeName;
    @Size(max = 1024)
    @NotNull
    private String attributeClassName;

    /**
     * Get the value of attributeName
     *
     * @return the value of attributeName
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * Set the value of attributeName
     *
     * @param attributeName new value of attributeName
     */
    @PropertyListener
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }


    /**
     * Get the value of templateName
     *
     * @return the value of templateName
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * Set the value of templateName
     *
     * @param templateName new value of templateName
     */
    @PropertyListener
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    /**
     * Get the value of attributeClassName
     *
     * @return the value of attributeClassName
     */
    public String getAttributeClassName() {
        return attributeClassName;
    }

    /**
     * Set the value of attributeClassName
     *
     * @param attributeClassName new value of attributeClassName
     */
    @PropertyListener
    public void setAttributeClassName(String attributeClassName) {
        this.attributeClassName = attributeClassName;
    }

    /**
     * Get the value of templateClassName
     *
     * @return the value of templateClassName
     */
    public String getTemplateClassName() {
        return templateClassName;
    }

    /**
     * Set the value of templateClassName
     *
     * @param templateClassName new value of templateClassName
     */
    @PropertyListener
    public void setTemplateClassName(String templateClassName) {
        this.templateClassName = templateClassName;
    }

    /**
     * Get the value of nodePath
     *
     * @return the value of nodePath
     */
    public String getNodePath() {
        return nodePath;
    }

    /**
     * Set the value of nodePath
     *
     * @param nodePath new value of nodePath
     */
    @PropertyListener
    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }

    /**
     * Get the value of nodeId
     *
     * @return the value of nodeId
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * Set the value of nodeId
     *
     * @param nodeId new value of nodeId
     */
    @PropertyListener
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

}
