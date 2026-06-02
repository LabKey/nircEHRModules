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
package org.labkey.nirc_ehr.dataentry.form;

import org.labkey.api.ehr.dataentry.DataEntryFormContext;
import org.labkey.api.ehr.dataentry.FormSection;
import org.labkey.api.ehr.dataentry.forms.BirthFormType;
import org.labkey.api.ehr.dataentry.forms.LockAnimalsFormSection;
import org.labkey.api.module.Module;
import org.labkey.api.view.template.ClientDependency;
import org.labkey.nirc_ehr.dataentry.section.NIRCAnimalDetailsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCBirthFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCBirthInstructionsFormSection;
import org.labkey.nirc_ehr.dataentry.section.NIRCTaskFormSection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NIRCBirthFormType extends BirthFormType
{
    public NIRCBirthFormType (DataEntryFormContext ctx, Module owner)
    {
        super(ctx, owner, Arrays.asList(
                new LockAnimalsFormSection(),
                new NIRCBirthInstructionsFormSection(),
                new NIRCTaskFormSection(),
                new NIRCAnimalDetailsFormSection(),
                new NIRCBirthFormSection()
        ));

        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/plugin/RowEditor.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/NIRCDefault.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/Birth.js"));
        addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/window/AddAnimalsWindow.js"));

        for (FormSection s : getFormSections())
        {
            s.addConfigSource("Birth");
        }
    }

    @Override
    protected List<String> getButtonConfigs()
    {
        List<String> defaultButtons = new ArrayList<>();
        defaultButtons.add("SAVEDRAFT");
        defaultButtons.add("BIRTHARRIVALREVIEW");
        defaultButtons.add("BIRTHARRIVALFINAL");

        return defaultButtons;
    }

    @Override
    protected List<String> getMoreActionButtonConfigs()
    {
        List<String> configs = super.getMoreActionButtonConfigs();
        configs.remove("REVIEW");
        configs.remove("DISCARD");
        return configs;
    }
}
