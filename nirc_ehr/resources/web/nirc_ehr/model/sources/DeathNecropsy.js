EHR.model.DataModelManager.registerMetadata('DeathNecropsy', {
    byQuery: {
        'study.deaths': {
            performedBy: {
                label: 'Performed By'
            },
            date: {
                label: 'Death Date'
            }
        },
        'study.necropsy': {
            date: {
                label: 'Exam Date'
            },
            grossAbnormalities: {
                xtype: 'ehr-remarkfield',
                height: 100,
                editorConfig: {
                    resizeDirections: 's'
                },
                columnConfig: {
                    width: 200
                }
            },
            diagnosis: {
                xtype: 'ehr-remarkfield',
                label: 'Diagnosis',
                resizeDirections: 's',
                height: 100,
                columnConfig: {
                    width: 200
                },
            },
            relevantHistory: {
                xtype: 'ehr-remarkfield',
                label: 'Relevant History',
                resizeDirections: 's',
                height: 100,
                columnConfig: {
                    width: 200,
                }
            },
        },
        'study.grossPathology': {
            remark: {
                hidden: true,
                showInGrid: false
            },
            project: {
                hidden: true,
                showInGrid: false
            },
            systemExamined: {
                columnConfig: {
                    width: 200
                }
            },
            performedBy: {
                hidden: true,
                showInGrid: false
            },
            appearance: {
                defaultValue: 'Normal',
                lookup: {
                    schemaName: 'ehr_lookups',
                    queryName: 'necropsy_organ_appearance',
                    keyColumn: 'title',
                    displayColumn: 'title',
                    defaultValue: 'Normal'
                }
            }
        },
        'study.tissueDisposition': {
            project: {
                hidden: true,
                showInGrid: false
            },
            performedBy: {
                hidden: true,
                showInGrid: false
            },
            necropsyDispositionCode: {
                columnConfig: {
                    fixed: true,
                    width: 200
                },
            }
        }
    }
});