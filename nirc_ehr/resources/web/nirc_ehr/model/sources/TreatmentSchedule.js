EHR.model.DataModelManager.registerMetadata('TreatmentSchedule', {
    allQueries: {
        category: {
            defaultValue: 'Clinical',
        },
        project: {
            hidden: true,
            allowBlank: true
        },
        code: {
            allowBlank: false,
            inheritFromParent: false,
            editorConfig: {
                xtype: 'labkey-combo',
                displayField: 'meaning',
                valueField: 'code',
                anyMatch: true,
                queryMode: 'local',
                store: {
                    type: 'labkey-store',
                    schemaName: 'ehr_lookups',
                    queryName: 'snomed',
                    columns: 'code,meaning',
                    sort: 'sort_order',
                    autoLoad: true,
                    getRecordForCode: function(code){
                        debugger
                        var recIdx = this.findExact('code', code);
                        if (recIdx !== -1){
                            return this.getAt(recIdx);
                        }
                    }
                }
            }
        },
        volume: {
            inheritFromParent: false,
            shownInGrid: true,
            compositeField: 'Volume',
            xtype: 'nirc_ehr-drugvolumefield',
            noSaveInTemplateByDefault: true,
            editorConfig: {
                decimalPrecision: 3
            },
            header: 'Vol',
            columnConfig: {
                width: 90
            }
        },
        type: {
            hidden: true
        },
        attachmentFile: {
            hidden: true
        }
    },
    byQuery: {
        'study.drug': {
            date: {
                header: 'Date/Time',
            }
        },
        'study.treatment_order': {
            enddate: {
                allowBlank: true,
            }
        }
    }
});