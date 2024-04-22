EHR.model.DataModelManager.registerMetadata('Arrival', {
    allQueries: {
        'endDate': {
            hidden: true
        }
    },
    byQuery: {
        'study.arrival': {
            'cage': {
                columnConfig: {
                    fixed: true,
                    width: 200
                },
            },
            'Id/demographics/species': {
                columnConfig: {
                    fixed: true,
                    width: 250
                }
            },
            project: {
                xtype: 'combo',
                lookup: {
                    schemaName: 'ehr',
                    queryName: 'project',
                    keyColumn: 'project',
                    columns: 'project,name'
                }
            },
            arrivalProtocol: {
                columnConfig: {
                    width: 200
                }
            },
            performedby: {
                hidden: true,
                showInGrid: false
            },
            sourceFacility: {
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            },
        }
    }
});