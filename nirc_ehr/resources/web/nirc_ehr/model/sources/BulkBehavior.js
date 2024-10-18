EHR.model.DataModelManager.registerMetadata('BulkBehavior', {
    byQuery: {
        'study.clinremarks': {
            hx: {
                formEditorConfig: {
                    xtype: 'ehr-hxtextarea'
                },
            },
            p: {
                hidden: true,
            },
            s: {
              hidden: true,
            },
            o: {
              hidden: true,
            },
            a: {
              hidden: true,
            },
            remark: {
                label: 'Remark',
                height: 120
            },
            vetreview: {
                hidden: true,
            },
            category: {
                getInitialValue: function (v, rec) {
                    return 'Behavior'
                },
                editable: false,
                hidden: true,
                columnConfig: {
                    editable: false
                }
            },
            enddate: {
                hidden: true
            },
            dateFinalized: {
                hidden: true
            },
            qcstate: {
                hidden: true
            },
        },
    }
});