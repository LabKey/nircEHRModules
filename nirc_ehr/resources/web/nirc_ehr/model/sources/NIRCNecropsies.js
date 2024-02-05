EHR.model.DataModelManager.registerMetadata('NIRCNecropsies', {
    byQuery: {
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
                allowBlank: false,
                resizeDirections: 's',
                height: 100,
                columnConfig: {
                    width: 200
                },
            },
            relevantHistory: {
                xtype: 'ehr-remarkfield',
                label: 'Relevant History',
                allowBlank: false,
                resizeDirections: 's',
                height: 100,
                columnConfig: {
                    width: 200,
                }
            },
        },
        'study.deaths': {
            performedBy: {
                label: 'Performed By'
            },
            date: {
                label: 'Death Date'
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
        },
        'study.tissueDisposition': {
            remark: {
                hidden: true,
                showInGrid: false
            },
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