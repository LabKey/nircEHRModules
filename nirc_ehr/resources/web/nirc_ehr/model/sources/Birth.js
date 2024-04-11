EHR.model.DataModelManager.registerMetadata('Birth', {
    allQueries: {

    },
    byQuery: {
        'study.birth': {
            'Id/demographics/dam': {
                xtype: 'ehr-animalfield'
            },
            'Id/demographics/sire': {
                xtype: 'ehr-animalfield'
            },
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
        'study.assignment': {
            'project': {
                xtype: 'combo',
                lookup: {
                    schemaName: 'ehr',
                    queryName: 'project',
                    keyColumn: 'project',
                    columns: 'project,name'
                }
            }
        },
        'study.protocolAssignment': {
            'protocol': {
                xtype: 'combo',
                columnConfig: {
                    fixed: true,
                    width: 150
                },
            }
        }
    }
});