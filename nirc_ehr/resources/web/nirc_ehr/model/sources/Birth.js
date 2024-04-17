EHR.model.DataModelManager.registerMetadata('Birth', {
    allQueries: {
        'endDate': {
            hidden: true
        }
    },
    byQuery: {
        'study.birth': {
            'Id/demographics/species': {
                columnConfig: {
                    fixed: true,
                    width: 250
                }
            },
            'cage': {
                columnConfig: {
                    fixed: true,
                    width: 200
                },
            }
        },
        'study.protocolAssignment': {
            'protocol': {
                xtype: 'combo',
                columnConfig: {
                    fixed: true,
                    width: 150
                },
                'project': {
                    xtype: 'combo',
                    lookup: {
                        schemaName: 'ehr',
                        queryName: 'project',
                        keyColumn: 'project',
                        columns: 'project,name'
                    }
                }
            }
        }
    }
});