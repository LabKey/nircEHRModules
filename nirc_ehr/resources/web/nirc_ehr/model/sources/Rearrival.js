
EHR.model.DataModelManager.registerMetadata('Rearrival', {

    byQuery: {
        'study.arrival': {
            rearrival: {
                getInitialValue: function (v, rec) {
                    return true
                },
                editable: false,
                hidden: true,
                columnConfig: {
                    editable: false
                }
            },
            performedby: {
                hidden: true,
                showInGrid: false
            },
            sourceFacility: {
                allowBlank: false,
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            },
            acquisitionType: {
                allowBlank: false,
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            },
            arrivalType: {
                allowBlank: false,
                columnConfig: {
                    width: 200
                }
            },
            'cage': {
                allowBlank: true,
                hidden: true,
                showInGrid: false
            },
            project: {
                allowBlank: true,
                hidden: true,
                showInGrid: false
            },
            arrivalProtocol: {
                allowBlank: true,
                hidden: true,
                showInGrid: false
            },
        }
    }
});