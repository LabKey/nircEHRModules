EHR.model.DataModelManager.registerMetadata('BulkClinical', {
    allQueries: {
        category: {
            defaultValue: 'Clinical',
            hidden: true
        },
        project: {
            hidden: true,
            allowBlank: true
        },
    },
    byQuery: {
        'study.clinremarks': {
            p: {
                columnConfig: {
                    width: 250
                }
            },
            vetreview: {
                columnConfig: {
                    width: 250
                }
            },
            vetreviewdate: {
                columnConfig: {
                    width: 200
                }
            },
            dateFinalized: {
                columnConfig: {
                    width: 200
                }
            },
            hx: {
                hidden: true
            },
            remark: {
                allowBlank: true
            },
        },
        'study.clinical_observations': {
            category: {
                hidden: false
            },
            remark: {
                allowBlank: true
            },
        },
        'study.blood': {
            units: {
                hidden: true
            },
        },
        'study.vitals': {
            units: {
                hidden: true
            },
        }
    }
});