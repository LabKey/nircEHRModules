EHR.model.DataModelManager.registerMetadata('BulkClinical', {
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
            category: {
                getInitialValue: function (v, rec) {
                    return 'Clinical'
                },
                editable: false,
                hidden: true,
                columnConfig: {
                    editable: false
                }
            }
        }
    }
});