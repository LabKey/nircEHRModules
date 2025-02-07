package org.labkey.nirc_ehr.dataentry.section;

import org.labkey.api.data.TableInfo;
import org.labkey.api.query.FieldKey;
import org.labkey.api.view.template.ClientDependency;

import java.util.List;

public class NIRCProcedureOrderFormSection extends BaseFormSection
{
    public static final String LABEL = "Procedure Orders";

    public NIRCProcedureOrderFormSection()
    {
        super("study", "prc_order", LABEL, "ehr-gridpanel", true, true, true);
    }

    public NIRCProcedureOrderFormSection(boolean isChild, String parentQueryName)
    {
        this();
        if (isChild)
        {
            addClientDependency(ClientDependency.supplierFromPath("nirc_ehr/model/sources/ParentChild.js"));
            addConfigSource("ParentChild");

            addClientDependency(ClientDependency.supplierFromPath("ehr/data/ChildClientStore.js"));
            setClientStoreClass("EHR.data.ChildClientStore");
            addExtraProperty("parentQueryName", parentQueryName);
        }
    }

    @Override
    protected List<FieldKey> getFieldKeys(TableInfo ti)
    {
        List<FieldKey> keys = super.getFieldKeys(ti);
        keys.remove(FieldKey.fromString("procedure"));
        keys.add(2, FieldKey.fromString("procedure"));

        return keys;
    }
}
