
EHR.DataEntryUtils.registerDataEntryFormButton('NIRC_TREATMENT_SUBMIT', {
    text: 'Submit Final',
    name: 'submit',
    requiredQC: 'Completed',
    targetQC: 'Completed',
    errorThreshold: 'INFO',
    successURL: LABKEY.ActionURL.getParameter('srcURL') || LABKEY.ActionURL.getParameter('returnUrl') || LABKEY.ActionURL.getParameter('returnURL') || LABKEY.ActionURL.buildURL('ehr', 'enterData.view'),
    disabled: true,
    itemId: 'submitBtn',
    handler: function(btn){
        var panel = btn.up('ehr-dataentrypanel');
        Ext4.Msg.confirm('Finalize Form', 'You are about to finalize this form.  Do you want to do this?', function(v){
            if(v == 'yes')
                this.onSubmit(btn);
        }, this);
    },
    listeners: {
        afterRender: function(btn){
            const treatmentid = LABKEY.ActionURL.getParameter('treatmentid');
            const scheduledDate = LABKEY.ActionURL.getParameter('scheduledDate');
            const obsTask = LABKEY.ActionURL.getParameter('obsTask');
            const id = LABKEY.ActionURL.getParameter('id');
            const observations = LABKEY.ActionURL.getParameter('observations');
            // const orderIds = LABKEY.ActionURL.getParameter('orderIds');

            if (treatmentid) {

                this.addEvents('animalchange');
                this.enableBubble('animalchange');

                function onSuccess(results) {
                    if (results.rows.length === 0) {
                        console.error('No treatment order found for treatmentid ' + treatmentid);
                        return;
                    }

                    const drugGrid = this.up('ehr-dataentrypanel').query('grid').find(e => e.title === "Medications/Treatments Given");
                    if (!drugGrid) {
                        console.error('Medication/Treatments grid not found');
                        return;
                    }

                    const row = results.rows[0];
                    const record = {
                        Id: row.Id.value,
                        category: row.category.value,
                        code: row.code.value,
                        route: row.route.value,
                        concentration: row.concentration.value,
                        conc_units: row.conc_units.value,
                        amount: row.amount.value,
                        amount_units: row.amount_units.value,
                        dosage: row.dosage.value,
                        dosage_units: row.dosage_units.value,
                        volume: row.volume.value,
                        vol_units: row.vol_units.value,
                        performedby: row.performedby.value,
                        orderedby: row.orderedby.value,
                        treatmentid: row.objectid.value
                    };

                    if (scheduledDate) {
                        record.scheduledDate = scheduledDate;
                    }

                    drugGrid.store.add(record);

                    this.fireEvent('animalchange', row.Id.value);
                    drugGrid.fireEvent('panelDataChange');
                }

                LABKEY.Query.selectRows({
                    requiredVersion: 9.1,
                    schemaName: 'study',
                    queryName: 'treatment_order',
                    columns: 'Id,meaning,code,qualifier,route,concentration,conc_units,amount,amount_units,dosage,dosage_units,volume,vol_units,remark,category,performedby,objectid,orderedby',
                    filterArray: [LABKEY.Filter.create('objectid', treatmentid, LABKEY.Filter.Types.EQUAL)],
                    scope: this,
                    success: onSuccess,
                    failure: LDK.Utils.getErrorCallback()
                });

            }

            if (id && observations) {
                this.addEvents('animalchange');
                this.enableBubble('animalchange');

                function onObsSuccess(results) {
                    if (results.rows.length === 0) {
                        console.error('No observations found for taskid ' + obsTask + ' and id ' + id);
                        return;
                    }

                    const obsGrid = this.up('ehr-dataentrypanel').query('grid').find(e => e.title === "Observations");
                    if (!obsGrid) {
                        console.error('Observations grid not found');
                        return;
                    }

                    const categories = [];
                    for (let i = 0; i < results.rows.length; i++) {
                        let row = results.rows[i];

                        if (!categories.includes(row.category.value)) {
                            let record = {
                                Id: row.Id.value,
                                category: row.category.value,
                                area: row.area.value,
                                orderId: row.objectid.value,
                                caseid: row.caseid.value
                            };

                            if (scheduledDate) {
                                record.scheduledDate = scheduledDate;
                            }

                            obsGrid.store.add(record);
                            categories.push(row.category.value);
                        }
                    }

                    this.fireEvent('animalchange', results.rows[0].Id.value);
                    obsGrid.fireEvent('panelDataChange');
                }

                LABKEY.Query.selectRows({
                    requiredVersion: 9.1,
                    schemaName: 'study',
                    queryName: 'observation_order',
                    columns: 'Id,category,area,objectid,caseid',
                    filterArray: [LABKEY.Filter.create('taskid', obsTask, LABKEY.Filter.Types.EQUALS_ONE_OF),
                        LABKEY.Filter.create('id', id, LABKEY.Filter.Types.EQUAL),
                        LABKEY.Filter.create('category', observations, LABKEY.Filter.Types.EQUALS_ONE_OF)],
                    scope: this,
                    success: onObsSuccess,
                    failure: LDK.Utils.getErrorCallback()
                });
            }

        }
    },
    disableOn: 'WARN'
});