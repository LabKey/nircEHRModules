EHR.model.DataModelManager.registerMetadata('BulkClinical', {
    allQueries: {
        category: {
            defaultValue: 'Clinical',
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
            }
        },
    }
});