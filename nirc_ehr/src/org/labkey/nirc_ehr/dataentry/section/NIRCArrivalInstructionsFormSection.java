/*
 * Copyright (c) 2024-2026 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.ehr.dataentry.AbstractFormSection;
import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormElement;
import org.labkey.api.view.template.ClientDependency;

import java.util.Collections;
import java.util.List;

public class NIRCArrivalInstructionsFormSection extends AbstractFormSection
{
    public NIRCArrivalInstructionsFormSection()
    {
        super("ArrivalInstructions", "Instructions", "nirc_ehr-arrivalinstructionspanel");

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/InstructionsPanel.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/panel/ArrivalInstructionsPanel.js"));
    }

    @Override
    protected List<FormElement> getFormElements(DataEntryFormContext ctx)
    {
        return Collections.emptyList();
    }
}